package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MakeReportController {

    @FXML
    private Button backButton;

    @FXML
    private Label makeLabel;

    @FXML
    private TextArea reportArea;

    @FXML
    private Button submitButton;

    private int appointmentID;

    MakeReportController(int appointmentIDD){
        appointmentID = appointmentIDD;
    }

    @FXML
    private void submitbutton (){
        if(reportArea.getText()==null){
            showAlert("Please enter report.");
        }else{
            String reportContent = reportArea.getText();
            int doctorID = userData.id;
            String insertQuery = "insert into reports (appointment_id,doctor_id,report_content) valuse (?,?,?)";
            try(Connection con=DatabaseConnection.getConnection();
                PreparedStatement stat =con.prepareStatement(insertQuery)){

                stat.setInt(1,appointmentID);
                stat.setInt(2,doctorID);
                stat.setString(3,reportContent);
                int rs = stat.executeUpdate();
                if(rs>0){
                    showAlert("Report submitted successfully.");
                }else{
                    showAlert("Can't submit the report.");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void backbutton (){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PreviousFrame.fxml"));// غير الاسم هنا باسم الفريم بتاعتك يا ماهر
            Parent previousSceneRoot = loader.load();
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            Scene previousScene = new Scene(previousSceneRoot);
            currentStage.setScene(previousScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}

