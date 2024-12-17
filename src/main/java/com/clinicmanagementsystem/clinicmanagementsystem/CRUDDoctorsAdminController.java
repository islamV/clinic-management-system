package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.sql.*;
import java.util.Optional;

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
                int id = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String speciality = resultSet.getString("specialty");
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
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField specialtyField = new JTextField();

        JCheckBox maleCheckBox = new JCheckBox("Male");
        JCheckBox femaleCheckBox = new JCheckBox("Female");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleCheckBox);
        genderGroup.add(femaleCheckBox);

        Object[] fields = {
                "Doctor Name:", nameField,
                "Email Address:", emailField,
                "Phone Number:", phoneField,
                "Age:", ageField,
                "Gender:", maleCheckBox, femaleCheckBox,
                "Specialty:", specialtyField
        };
        int option = JOptionPane.showConfirmDialog(null, fields, "Add New Doctor", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String ageInput = ageField.getText();
            String gender = maleCheckBox.isSelected() ? "Male" : (femaleCheckBox.isSelected() ? "Female" : "");
            String specialty = specialtyField.getText();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || ageInput.isEmpty() || gender.isEmpty() || specialty.isEmpty()) {
                showErrorDialog("Input Error", "Please fill in all the fields.");
                return;
            }

            try {
                int age = Integer.parseInt(ageInput);

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
                        showSuccessDialog("Doctor Added", "The new doctor has been successfully added.");
                    } else {
                        showErrorDialog("Database Error", "Failed to retrieve user ID for the new doctor.");
                    }
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Input Error", "Age must be a valid number.");
            } catch (Exception e) {
                e.printStackTrace();
                showErrorDialog("Error", "An unexpected error occurred.");
            }
        } else {
            System.out.println("User cancelled the operation.");
        }
        initialize();
    }


    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

        JTextField nameField = new JTextField(selectedDoctor.getName());
        JTextField emailField = new JTextField(selectedDoctor.getEmail());
        JTextField phoneField = new JTextField(selectedDoctor.getPhone());
        JTextField ageField = new JTextField(String.valueOf(selectedDoctor.getAge()));
        JTextField specialtyField = new JTextField(selectedDoctor.getSpeciality());

        JCheckBox maleCheckBox = new JCheckBox("Male", selectedDoctor.getGender().equals("Male"));
        JCheckBox femaleCheckBox = new JCheckBox("Female", selectedDoctor.getGender().equals("Female"));

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleCheckBox);
        genderGroup.add(femaleCheckBox);

        Object[] fields = {
                "Doctor Name:", nameField,
                "Email Address:", emailField,
                "Phone Number:", phoneField,
                "Age:", ageField,
                "Gender:", maleCheckBox, femaleCheckBox,
                "Specialty:", specialtyField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Edit Doctor", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText();
            String newEmail = emailField.getText();
            String newPhone = phoneField.getText();
            String ageInput = ageField.getText();
            String gender = maleCheckBox.isSelected() ? "Male" : (femaleCheckBox.isSelected() ? "Female" : "");
            String newSpecialty = specialtyField.getText();

            if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || ageInput.isEmpty() || gender.isEmpty() || newSpecialty.isEmpty()) {
                showErrorDialog("Input Error", "Please fill in all the fields.");
                return;
            }

            try {
                int newAge = Integer.parseInt(ageInput);
                updateDoctorAndUser(selectedDoctor.getId(), newName, newAge, newEmail, newPhone, gender, newSpecialty);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Age must be a valid number.");
            } catch (Exception e) {
                e.printStackTrace();
                showErrorDialog("Error", "An unexpected error occurred.");
            }
        } else {
            System.out.println("User cancelled the operation.");
        }
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
