import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginStudentController {
    
    @FXML
    private TextField studentIdField;
    @FXML
    private TextField passwdField;
    @FXML
    private Button loginButton;


    @FXML
    public void Login(ActionEvent event) {
        String mssv = studentIdField.getText();
        String password = passwdField.getText();
        if (mssv.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        try (java.sql.Connection conn = DBHelper.connect()) {
            String sql = "SELECT password FROM login WHERE maso = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mssv);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (BCrypt.checkpw(password, storedHash)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("student_view.fxml"));
                    Parent root = loader.load();
                    StudentController controller = loader.getController();
                    Platform.runLater(() -> controller.setStudentInfo(mssv));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Sai mật khẩu!");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Không tìm thấy MSSV!");
            }
        } catch (Exception e) {
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
