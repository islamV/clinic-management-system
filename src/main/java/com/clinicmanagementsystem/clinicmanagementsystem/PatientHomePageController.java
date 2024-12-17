package com.clinicmanagementsystem.clinicmanagementsystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientHomePageController {

    @FXML
    private Button historyAppointmentButton;

    @FXML
    private Button makeAppointmentButton;


    public void historyButton(ActionEvent event) throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/AppointmentsHistoryPage.fxml"));
        Parent patientRoot = patientLoader.load();
        Stage patientStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the stage from the event source
        patientStage.setScene(new Scene(patientRoot));
        patientStage.show();
    }
    public void makeButton(ActionEvent event) throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/MakeAppointmentPage.fxml"));
        Parent patientRoot = patientLoader.load();
        Stage patientStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the stage from the event source
        patientStage.setScene(new Scene(patientRoot));
        patientStage.show();
    }

}