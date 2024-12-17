/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.clinicmanagementsystem.clinicmanagementsystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.*;
import java.util.Date;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eng. Mohamed
 */
public class RegistrationPageController implements Initializable {

    @FXML
    private TextField age_txtfld;

    @FXML
    private TextField email_txtfld;

    @FXML
    private ComboBox<String> gender_combobox;

    @FXML
    private TextField name_txtfld;

    @FXML
    private PasswordField password_txtfld;

    @FXML
    private TextField phone_txtfld;

    @FXML
    private Button register_btn;

    @FXML
    private PasswordField repassword_txtfld;

    @FXML
    private Hyperlink already_hyperlink;

    private Connection con;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public void register() throws SQLException {
        String sql = "INSERT INTO users (name, email_address, phone_number, gender, age, role, password) VALUES (?,?,?,?,?,?,?)";

        con = DatabaseConnection.getConnection();

        try {
            prepare = con.prepareStatement(sql);
            prepare.setString(1, name_txtfld.getText());
            prepare.setString(2, email_txtfld.getText());
            prepare.setString(3, phone_txtfld.getText());
            String selectedGender = (String) gender_combobox.getValue();
            prepare.setString(4, selectedGender);
            prepare.setString(5, age_txtfld.getText());
            prepare.setString(6, "Patient");
            prepare.setString(7, password_txtfld.getText());

            Alert alert;
            if (name_txtfld.getText().isEmpty() || email_txtfld.getText().isEmpty() || phone_txtfld.getText().isEmpty() ||
                    age_txtfld.getText().isEmpty() || repassword_txtfld.getText().isEmpty() || selectedGender == null || selectedGender.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required.");
                alert.showAndWait();
            } else if (password_txtfld.getText().length() < 5) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Password must be at least 5 characters long. Please enter a valid password.");
                alert.showAndWait();

            } else if (!password_txtfld.getText().equals(repassword_txtfld.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match.");
                alert.showAndWait();
            }
            else {
                prepare.execute();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Successfully registered.");
                alert.showAndWait();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clinicmanagementsystem/clinicmanagementsystem/FXML/LoginPage.fxml"));
                Parent root = loader.load();
                Stage currentStage = (Stage) email_txtfld.getScene().getWindow();
                currentStage.setScene(new Scene(root));
                currentStage.show();

            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void switchToLoginPage() throws SQLException, ClassNotFoundException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clinicmanagementsystem/clinicmanagementsystem/FXML/LoginPage.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) email_txtfld.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gender_combobox.getItems().addAll("Male", "Female");
    }

}
