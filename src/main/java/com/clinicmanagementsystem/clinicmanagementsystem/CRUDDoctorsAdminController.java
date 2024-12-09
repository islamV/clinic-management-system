package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.*;

public class CRUDDoctorsAdminController {

    @FXML
    public Label lblAdminUsername;
    @FXML
    public Button btnManageAppointments;
    @FXML
    public Button btnManageDoctors;
    @FXML
    public Button btnLogout;
    @FXML
    public VBox crudContainer;
    @FXML
    public TextField txtSearchDoctor;
    @FXML
    public TableView<Doctor> tableDoctors;
    @FXML
    public Button btnSearchDoctor;
    @FXML
    public TableColumn<Doctor, Integer> colDoctorID;
    @FXML
    public TableColumn<Doctor, String> colDoctorName;
    @FXML
    public TableColumn<Doctor, Integer> colDoctorAge;
    @FXML
    public TableColumn<Doctor, String> colDoctorSpeciality;
    @FXML
    public Button btnAddDoctor;
    @FXML
    public Button btnEditDoctor;
    @FXML
    public Button btnDeleteDoctor;

    private ObservableList<Doctor> doctorList;

    public void initialize() {
        doctorList = FXCollections.observableArrayList();

        colDoctorID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDoctorAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colDoctorSpeciality.setCellValueFactory(new PropertyValueFactory<>("speciality"));

        loadDoctorsFromDatabase();
    }

    private void loadDoctorsFromDatabase() {
        String sql = "SELECT \n" +
                "    u.user_id,\n" +
                "    u.name,\n" +
                "    u.age,\n" +
                "    u.gender,\n" +
                "    u.phone_number,\n" +
                "    u.email_address,\n" +
        "    d.specialty\n" +
                "FROM \n" +
                "    doctors d\n" +
                "JOIN \n" +
                "    users u ON d.user_id = u.user_id;";



        DatabaseConnection databaseConnection = new DatabaseConnection();
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            doctorList.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String speciality = resultSet.getString("speciality");
                String gender = resultSet.getString("gender");
                String phoneNumber = resultSet.getString("phone_number");
                String email = resultSet.getString("email_address");
                doctorList.add(new Doctor(id, name, age, speciality , gender , phoneNumber , email));
            }
            tableDoctors.setItems(doctorList);

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error loading doctors", "An error occurred while retrieving doctor data.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void ADD(ActionEvent event) {
        try {
            String name = promptForInput("Doctor Name", "Enter the doctor's name:");
            if (name == null) return;

            String email = promptForInput("Email Address", "Enter the doctor's email:");
            if (email == null) return;

            String phone = promptForInput("Phone Number", "Enter the doctor's phone number:");
            if (phone == null) return;

            String ageInput = promptForInput("Age", "Enter the doctor's age:");
            if (ageInput == null) return;
            int age = Integer.parseInt(ageInput);

            String gender = promptForChoice("Gender", "Select the doctor's gender:", "Male", "Female");
            if (gender == null) return;

            String specialty = promptForInput("Specialty", "Enter the doctor's specialty:");
            if (specialty == null) return;

            DatabaseConnection conn = new DatabaseConnection();
            try (Connection connection = conn.getConnection();
                 PreparedStatement userStatement = connection.prepareStatement(
                         "INSERT INTO users (name, email_address, phone_number, gender, age, role, password) VALUES (?, ?, ?, ?, ?, 'Doctor', '12345678')",
                         Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement doctorStatement = connection.prepareStatement(
                         "INSERT INTO doctors (user_id, specialty) VALUES (?, ?)")) {

                userStatement.setString(1, name);
                userStatement.setString(2, email);
                userStatement.setString(3, phone);
                userStatement.setString(4, gender);
                userStatement.setInt(5, age);

                userStatement.executeUpdate();

                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    doctorStatement.setInt(1, userId);
                    doctorStatement.setString(2, specialty);

                    doctorStatement.executeUpdate();
                } else {
                    showErrorDialog("Database Error", "Failed to retrieve user ID for the new doctor.");
                    return;
                }

                showSuccessDialog("Doctor Added", "The new doctor has been successfully added.");
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Input Error", "Age must be a valid number.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An unexpected error occurred.");
        }
    }


    private String promptForInput(String title, String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        return dialog.showAndWait().orElse(null);
    }

    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String promptForChoice(String title, String message, String... options) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options[0], options);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        return dialog.showAndWait().orElse(null);
    }

    public void Delete(ActionEvent event) {
        Doctor selectedDoctor = (Doctor) tableDoctors.getSelectionModel().getSelectedItem();

        if (selectedDoctor == null) {
            showErrorDialog("No Selection", "Please select a doctor from the table to delete.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Confirmation");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to delete doctor: " + selectedDoctor.getName() + "?");
        if (confirmDialog.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        DatabaseConnection conn = new DatabaseConnection();
        try (Connection connection = conn.getConnection();
             PreparedStatement deleteDoctorStmt = connection.prepareStatement("DELETE FROM doctors WHERE user_id = ?");
             PreparedStatement deleteUserStmt = connection.prepareStatement("DELETE FROM users WHERE user_id = ?")) {

            int userId = selectedDoctor.getId();

            deleteDoctorStmt.setInt(1, userId);
            int doctorRowsAffected = deleteDoctorStmt.executeUpdate();

            if (doctorRowsAffected > 0) {
                deleteUserStmt.setInt(1, userId);
                int userRowsAffected = deleteUserStmt.executeUpdate();

                if (userRowsAffected > 0) {
                    tableDoctors.getItems().remove(selectedDoctor);
                    showSuccessDialog("Doctor Deleted", "The doctor has been successfully deleted.");
                } else {
                    showErrorDialog("Deletion Failed", "Unable to delete");
                }
            } else {
                showErrorDialog("Deletion Failed", "Unable to delete");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Database Error", "An error occurred while deleting the doctor.");
        }
    }

    public void Edit(ActionEvent event) {
        Doctor selectedDoctor = (Doctor) tableDoctors.getSelectionModel().getSelectedItem();

        if (selectedDoctor == null) {
            showErrorDialog("No Selection", "Please select a doctor from the table to edit.");
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog(selectedDoctor.getName());
        nameDialog.setTitle("Edit Doctor");
        nameDialog.setHeaderText("Edit Doctor's Name");
        nameDialog.setContentText("Name:");
        String newName = nameDialog.showAndWait().orElse(null);

        if (newName == null || newName.trim().isEmpty()) {
            showErrorDialog("Invalid Input", "Name cannot be empty.");
            return;
        }

        TextInputDialog ageDialog = new TextInputDialog(String.valueOf(selectedDoctor.getAge()));
        ageDialog.setTitle("Edit Doctor");
        ageDialog.setHeaderText("Edit Doctor's Age");
        ageDialog.setContentText("Age:");
        String newAgeStr = ageDialog.showAndWait().orElse(null);

        if (newAgeStr == null || newAgeStr.trim().isEmpty()) {
            showErrorDialog("Invalid Input", "Age cannot be empty.");
            return;
        }
        
        int newAge;
        try {
            newAge = Integer.parseInt(newAgeStr);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Input", "Please enter a valid age.");
            return;
        }

        TextInputDialog emailDialog = new TextInputDialog(selectedDoctor.getEmail());
        emailDialog.setTitle("Edit Doctor");
        emailDialog.setHeaderText("Edit Doctor's Email");
        emailDialog.setContentText("Email:");
        String newEmail = emailDialog.showAndWait().orElse(null);

        if (newEmail == null || newEmail.trim().isEmpty()) {
            showErrorDialog("Invalid Input", "Email cannot be empty.");
            return;
        }

        TextInputDialog phoneDialog = new TextInputDialog(selectedDoctor.getPhone());
        phoneDialog.setTitle("Edit Doctor");
        phoneDialog.setHeaderText("Edit Doctor's Phone Number");
        phoneDialog.setContentText("Phone:");
        String newPhone = phoneDialog.showAndWait().orElse(null);

        if (newPhone == null || newPhone.trim().isEmpty()) {
            showErrorDialog("Invalid Input", "Phone number cannot be empty.");
            return;
        }

        ChoiceDialog<String> genderDialog = new ChoiceDialog<>(selectedDoctor.getGender(), "Male", "Female");
        genderDialog.setTitle("Edit Doctor");
        genderDialog.setHeaderText("Edit Doctor's Gender");
        genderDialog.setContentText("Gender:");
        String newGender = genderDialog.showAndWait().orElse(null);

        if (newGender == null) {
            showErrorDialog("Invalid Input", "Gender cannot be empty.");
            return;
        }

        TextInputDialog specialtyDialog = new TextInputDialog(selectedDoctor.getSpeciality());
        specialtyDialog.setTitle("Edit Doctor");
        specialtyDialog.setHeaderText("Edit Doctor's Specialty");
        specialtyDialog.setContentText("Specialty:");
        String newSpecialty = specialtyDialog.showAndWait().orElse(null);

        if (newSpecialty == null || newSpecialty.trim().isEmpty()) {
            showErrorDialog("Invalid Input", "Specialty cannot be empty.");
            return;
        }

        updateDoctorAndUser(selectedDoctor.getId(), newName, newAge, newEmail, newPhone, newGender, newSpecialty);
    }

    private void updateDoctorAndUser(int userId, String name, int age, String email, String phone, String gender, String specialty) {
        DatabaseConnection conn = new DatabaseConnection();
        try (Connection connection = conn.getConnection()) {

            String updateUserSql = "UPDATE users SET name = ?, age = ?, email_address = ?, phone_number = ?, gender = ? WHERE user_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setString(1, name);
                stmt.setInt(2, age);
                stmt.setString(3, email);
                stmt.setString(4, phone);
                stmt.setString(5, gender);
                stmt.setInt(6, userId);
                stmt.executeUpdate();
            }

            String updateDoctorSql = "UPDATE doctors SET specialty = ? WHERE user_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateDoctorSql)) {
                stmt.setString(1, specialty);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
            }

            showSuccessDialog("Doctor Updated", "The doctor's information has been successfully updated.");

            initialize();

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Error", "An error occurred while updating the doctor.");
        }
    }
}
