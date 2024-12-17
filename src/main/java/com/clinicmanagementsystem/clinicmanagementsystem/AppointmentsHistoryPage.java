package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AppointmentsHistoryPage {


    @FXML
    private TableView<Appointment> tableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> queueNumberColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    @FXML
    private TableColumn<Appointment, String> doctorNameColumn;
    @FXML
    private TableColumn<Appointment, String> scheduleDayColumn;
    @FXML
    private TableColumn<Appointment, String> createdAtColumn;

    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up the columns
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        queueNumberColumn.setCellValueFactory(new PropertyValueFactory<>("queueNumber"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        scheduleDayColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleDay"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Load data from the database
        loadAppointmentsFromDatabase();
    }

    private void loadAppointmentsFromDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = """
                SELECT\s
                   a.appointment_id,\s
                   a.queue_number,\s
                   a.status,\s
                   p.name AS patient_name,\s
                   u.name AS doctor_name\s ,
                  schedule.day As schedule_day\s ,
                a.created_at AS created_at
               FROM\s
                   appointments a
               JOIN\s
                   users p ON a.patient_id = p.user_id
               JOIN\s
                   schedules schedule ON a.schedule_id = schedule.schedule_id
        
               JOIN\s
        
                   doctors d ON a.doctor_id = d.doctor_id
               JOIN\s
                   users u ON d.user_id = u.user_id
               
               
                """;

        try (Connection connection =  databaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("appointment_id");
                int queueNumber = resultSet.getInt("queue_number");
                String status = resultSet.getString("status");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");
                String schedule_day = resultSet.getString("schedule_day");
                String created_at = resultSet.getString("created_at");


                appointmentList.add(new Appointment(appointmentId, queueNumber, status, patientName, doctorName , schedule_day , created_at));
            }

            tableView.setItems(appointmentList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/patient-home-page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
