package com.clinicmanagementsystem.clinicmanagementsystem;

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
public class LoginPageController implements Initializable {

    
    @FXML
    private Hyperlink dont_hyperlink;

    @FXML
    private TextField email_txtfld;

    @FXML
    private Button login_btn;

    @FXML
    private PasswordField password_txtfld;

    private Connection con;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public void login() throws SQLException {
        String sql = "Select * FROM users where email_address = ? and password = ?";

        con = DatabaseConnection.getConnection();

        try{
            prepare = con.prepareStatement(sql);
            prepare.setString(1, email_txtfld.getText());
            prepare.setString(2, password_txtfld.getText());
            result = prepare.executeQuery();

            Alert alert;
            if (email_txtfld.getText().isEmpty() || password_txtfld.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the fields");
                alert.showAndWait();
            } else {
                if (result.next()) {
                    //if correct inputs
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful Login");
                    alert.setHeaderText(null);
                    alert.setContentText("Logged in successfully!");
                    alert.showAndWait();

                    String role = result.getString("role");

                    switch (role) {
                        case "Admin":
                            //switch
                            break;
                        case "Doctor":
                            //switch
                            break;
                        case "Patient":
                            //switch
                            break;
                        default:
                            System.out.println("An error has occurred");
                            break;
                    }

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong email or password!");
                    alert.showAndWait();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchToRegisterPage() throws SQLException, ClassNotFoundException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("D:\\Programming\\clinic-management-system\\src\\main\\java\\com\\clinicmanagementsystem\\clinicmanagementsystem\\RegistrationPage.fxml"));
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
        email_txtfld.clear();
        password_txtfld.clear();
    }    
    
}
