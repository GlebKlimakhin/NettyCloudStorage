import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainWindowController {

    private String login;

    @FXML
    private TextField downloadTextField;

    @FXML
    private Button uploadButton;

    @FXML
    private TextField uploadTextField;

    @FXML
    private Button downloadButton;

    @FXML
    private ListView <String> serverFilesListView;

    @FXML
    private ListView <String> clientFilesListView;


    void initialize() {
    }

    private void tryToDownloadFile() {
        Network.sendMessage(new DownloadRequestMessage(serverFilesListView.getSelectionModel().getSelectedItem()));
    }

    private void tryToUploadFile(){
        try {
            Network.sendMessage(new FileMessage(Paths.get("client" + login + "/"
                    + clientFilesListView.getSelectionModel().getSelectedItem())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromClient() {
        try {
            Files.delete(Paths.get("client" + login + "/" + clientFilesListView.getSelectionModel().getSelectedItem()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshLocalFilesList();
    }

    public void deleteFromServer() {
        Network.sendMessage(new DeleteRequestMessage(serverFilesListView.getSelectionModel().getSelectedItem()));
    }

    private void updateInterface(Runnable r){
        if(Platform.isFxApplicationThread()) {
            r.run();
        }
        else{
            Platform.runLater(r);
        }
    }

    private void refreshLocalFilesList() {
        updateInterface(() -> {
            try {
                clientFilesListView.getItems().clear();
                Files.list(Paths.get("client " + login))
                        .map(p -> p.getFileName().toString())
                        .forEach(p -> clientFilesListView.getItems().add(p));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void closeConnection(){
        Network.sendMessage(new AuthMessage("/disconnect"));
        Main.launch();
    }

}
