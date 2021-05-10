import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private String login;

    @FXML
    private HBox authPanel;

    @FXML
    private TextField authLoginField;

    @FXML
    private PasswordField authPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private TextField regLoginField;

    @FXML
    private PasswordField regPasswordField;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private Button signUpButton;

    @FXML
    private HBox interfacePanel;

    @FXML
    private ListView<String> clientFileListView;

    @FXML
    private Button uploadButton;

    @FXML
    private Button deleteFromClientButton;

    @FXML
    private Button exitButton;

    @FXML
    private ListView<String> serverFileListView;

    @FXML
    private Button downloadButton;

    @FXML
    private Button deleteFromServerButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Network.start();
        setAuthorized(false);
        Thread clientThread = new Thread(()-> {

            try{
            while (true) {
                AbstractMessage message = Network.readObject();

                if (message instanceof RegMessage) {
                    RegMessage regMessage = (RegMessage) message;
                    if (regMessage.equals("/not_null_user_id")) {
                        Platform.runLater(() -> System.out.println("login is taken"));
                    } else {
                        String login = regMessage.getMessage().split(" ")[1];
                        Files.createDirectory(Paths.get("client" + login));
                        Platform.runLater(() -> signUpButton.setText("Registration success"));
                    }
                }

                if (message instanceof AuthMessage) {
                    AuthMessage authMessage = (AuthMessage) message;
                    if (authMessage.getMessage().startsWith("/auth_ok")) {
                        setAuthorized(true);
                        login = authMessage.getMessage().split(" ")[1];
                        System.out.printf("client " + login + " is now connected");
                        break;
                    }
                    if (authMessage.equals("/null_user_id")) {
                        Platform.runLater(() -> signUpButton.setText("Incorrect input"));
                    }
                }
            }
            Network.sendMessage(new RefreshServerFilesListMessage());
            refreshLocalFilesList();

            while (true){
                AbstractMessage message = Network.readObject();
                if(message instanceof FileMessage){
                    FileMessage fileMessage = (FileMessage) message;
                    if(! Files.exists(Paths.get("client" + login + "/" + fileMessage.getFilename()))){
                        Files.write(Paths.get("client" + login + "/" + fileMessage.getFilename()),
                                fileMessage.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();

                    }
                }

                if(message instanceof RefreshServerFilesListMessage){
                    RefreshServerFilesListMessage refreshMessage = (RefreshServerFilesListMessage) message;
                    refreshServerFileList(refreshMessage.getServerFilesList());
                }

            }
    }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    clientThread.setDaemon(true);
    clientThread.start();
    }

    private void setAuthorized(boolean isAuthorized) {
        if (!isAuthorized) {
            authPanel.setVisible(true);
            authPanel.setManaged(true);
            interfacePanel.setVisible(false);
            interfacePanel.setManaged(false);
        } else {
            authPanel.setVisible(false);
            authPanel.setManaged(false);
            interfacePanel.setVisible(true);
            interfacePanel.setManaged(true);
        }
    }

    public void tryToAuth() {
        Network.sendMessage(new AuthMessage(authLoginField.getText(), authPasswordField.getText()));
        authLoginField.clear();
        authPasswordField.clear();
    }

    public void tryToReg(){
        if(regPasswordField.getText().equals(repeatPasswordField.getText())){
            Network.sendMessage(new RegMessage(regLoginField.getText(), regPasswordField.getText()));
        }
        else{
            throw new IllegalArgumentException("Invalid input");
        }
    }

    public void closeConnection(){
        Network.sendMessage(new AuthMessage("/disconnect"));
        Main.launch();
    }

    private static void updateUI(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

    private void refreshServerFileList(ArrayList<String> fileList) {
        updateUI(() -> {
            serverFileListView.getItems().clear();
            serverFileListView.getItems().addAll(fileList);
        });
    }

    private void refreshLocalFilesList() {
        updateUI(() -> {
            clientFileListView.getItems().clear();
            try {
                Files.list(Paths.get("client" + login))
                        .map(f -> f.getFileName().toString())
                        .forEach(f -> clientFileListView.getItems().add(f));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteClientFile(){
        try {
            Files.delete(Paths.get("client" + login + "/" + clientFileListView.getSelectionModel().getSelectedItem()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(){
        Network.sendMessage(new DownloadRequestMessage(serverFileListView.getSelectionModel().getSelectedItem()));
    }

    public void uploadToServer(){
        try {
            Network.sendMessage(new FileMessage(Paths.get("client" + login + "/" +
                    clientFileListView.getSelectionModel().getSelectedItem())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteFromServer(){
        Network.sendMessage(new DeleteRequestMessage(serverFileListView.getSelectionModel().getSelectedItem()));
    }



}
