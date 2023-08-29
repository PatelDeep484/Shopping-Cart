package application.controllers;

import application.database.JdbcDA;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class RegisterationController {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField fullnameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitBtn;

    @FXML
    void cancelButtonEvent(ActionEvent event) {
    		Platform.exit();
    }

    @FXML
    void submitButtonEvent(ActionEvent event) {
    	Window owner = submitBtn.getScene().getWindow();
    	if(fullnameTextField.getText().isEmpty()) {
    		showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter the User Name");
    	}
    	String fullName = fullnameTextField.getText();
    	String emailId = emailTextField.getText();
    	String password = passwordField.getText();
    	
    	JdbcDA da = new JdbcDA();
    	da.insertRecord(fullName, emailId, password);
    }
    private static void showAlert(Alert.AlertType alertT, Window owner, String title, String message) {
    	Alert alert = new Alert(alertT);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.setContentText(message);
    	alert.initOwner(owner);
    	alert.show();
    }
}
