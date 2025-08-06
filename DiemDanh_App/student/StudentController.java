import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.rabbitmq.client.*;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

public class StudentController implements Initializable{

    private static final String DIRECT_EXCHANGE = "attendanceExchange";

    @FXML
    private JFXButton logout;
    @FXML
    private JFXButton historyButton;
    @FXML
    private TextField studentNameField;
    @FXML
    private TextField studentIdField;
    @FXML
    private MenuButton courseNameButton;
    @FXML
    private MenuButton courseIdButton;
    @FXML
    private MenuButton groupButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label mssvLabel;
    
    private String mssv;
    public void setStudentInfo(String mssv) {
        welcomeLabel.setText("Xin chào, " + mssv);
        studentIdField.setText(mssv);
        mssvLabel.setText(mssv);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setStudentInfo(mssv);
        for (MenuItem item : courseNameButton.getItems()) {
            final MenuItem menuItem = item;
            item.setOnAction(event -> {
                courseNameButton.setText(menuItem.getText());
            });
        }

        for (MenuItem item : courseIdButton.getItems()) {
            final MenuItem menuItem = item;
            item.setOnAction(event -> {
                courseIdButton.setText(menuItem.getText());
            });
        }

        for (MenuItem item : groupButton.getItems()) {
            final MenuItem menuItem = item;
            item.setOnAction(event -> {
                groupButton.setText(menuItem.getText());
            });
        }
    }

    @FXML
    public void handleCheckIn() {
        try {
            String full_name = studentNameField.getText();
            String mssv = studentIdField.getText();
            String tenHP = courseNameButton.getText();
            String maHP = courseIdButton.getText();
            String nhomHP = groupButton.getText();
            int nhom = Integer.parseInt(nhomHP);
            if (full_name.isEmpty() || mssv.isEmpty() || maHP.equals("Chọn") || tenHP.equals("Chọn") || nhomHP.equals("Chọn")) {
                showAlert(Alert.AlertType.WARNING, "Thiếu thông tin",
                        "Vui lòng nhập đầy đủ!");
                return;
            }
            if (DBHelper.hasAlreadyCheckedIn(mssv, maHP)) {
                showAlert(Alert.AlertType.INFORMATION, "Thông báo",
                        "Hôm nay bạn đã điểm danh cho học phần này rồi!");
            } else {
                DBHelper.saveAttendance(full_name, mssv, tenHP, maHP, nhom);
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Bạn đã điểm danh thành công!");
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                String currentTimeFormatted = now.format(formatter);
                // Gửi qua rabbitmq
                String ROUTING_KEY = "attendanceKey." + maHP;
                AttendanceRabbitMQ att = new AttendanceRabbitMQ(full_name, mssv, currentTimeFormatted, maHP, nhom);
                String message = new Gson().toJson(att);
                System.out.println("Sending message: " + message);
                try {
                    Connection conn = MQHelper.getConnection();
                    Channel channel = conn.createChannel();
                    channel.exchangeDeclare(DIRECT_EXCHANGE, "direct", true);
                    channel.basicPublish(DIRECT_EXCHANGE, ROUTING_KEY, null, message.getBytes("UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Logout() {
        try {
            Stage stage = (Stage) logout.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view_history.fxml"));
            Parent root = loader.load();
            HistoryController controller = loader.getController();
            controller.loadHistory(studentIdField.getText());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInfoClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_profile.fxml"));
            Parent root = loader.load();
            StudentInfoController controller = loader.getController();
            controller.loadStudentInfo(studentIdField.getText());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
