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
    private ComboBox<?> gender_combobox;

    @FXML
    private TextField name_txtfld;

    @FXML
    private PasswordField password_txtfld;

    @FXML
    private TextField phone_txtfld;

    @FXML
    private Button register_btn;

    @FXML
    private PasswordField repasswrod_txtfld;
    
    @FXML
    private Hyperlink already_hyperlink;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
