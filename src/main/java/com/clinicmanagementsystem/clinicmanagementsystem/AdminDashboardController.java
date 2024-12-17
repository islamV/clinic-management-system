package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    public Button btnManageAppointments;
    public Button btnManageDoctors;
    public Button btnLogout;
    public Label lblAdminUsername;

    public void showDoctorCrud(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource("FXML/CRUD-Doctors-Admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 690, 600);
        CRUDDoctorsAdminController controller = fxmlLoader.getController();
        controller.initialize();
        stage.setTitle("Clinic Management System");
        stage.setScene(scene);
        stage.show();
    }
}
