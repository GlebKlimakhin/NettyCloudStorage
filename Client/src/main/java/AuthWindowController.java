import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthWindowController {

    private Network network;

    @FXML
    private Button signInButton;

    @FXML
    private TextField signInLogin;

    @FXML
    private PasswordField signInPassword;

    @FXML
    private Button regButton;

    @FXML
    void initialize() {

    }

    public void tryToAuth(){
        Network.sendMessage(new AuthMessage(signInLogin.getText(), signInPassword.getText()));
        signInLogin.clear();
        signInPassword.clear();
    }

    public void toSignUpWindow(){
        regButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("regInterface.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


}
