package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetAppointmentLimitController {

    @FXML
    private TextField maxAppointmentsField;

    private int doctorId;

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    @FXML
    private void handleSetLimit(ActionEvent event) {
        String input = maxAppointmentsField.getText();

        if (input == null || input.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid number.");
            return;
        }

        try {
            int limit = Integer.parseInt(input);

            if (limit <= 0) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "The limit must be greater than zero.");
                return;
            }

            // Save the limit to the database
            saveAppointmentLimitToDatabase(limit);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment limit set to " + limit + " for Doctor ID " + doctorId + ".");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid integer.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save the limit to the database.");
            e.printStackTrace();
        }
    }

    // Method to save the appointment limit to the database
    private void saveAppointmentLimitToDatabase(int limit) throws SQLException {
        String query = "UPDATE appointments SET appointment_limit = ? WHERE doctor_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, doctorId);

            preparedStatement.executeUpdate();
        }
    }

    // Utility method to show alert dialogs
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
