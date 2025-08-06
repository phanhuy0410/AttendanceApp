import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField teacherIdField;
    @FXML
    private PasswordField passwdField;
    @FXML
    private Button loginButton;
    
    @FXML
    public void Login(ActionEvent event) {
        String msgv = teacherIdField.getText();
        String password = passwdField.getText();
        if (msgv.isEmpty() || password.isEmpty()) {
            System.out.println("Vui lòng nhập đầy đủ mã số và mật khẩu.");
            return;
        }
        try (java.sql.Connection conn = DBHelper.connect()) {
            String sql = "SELECT password FROM login WHERE maso = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, msgv);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (BCrypt.checkpw(password, storedHash)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_view.fxml"));
                    Parent root = loader.load();
                    TeacherController controller = loader.getController();
                    controller.setTeacherInfo(msgv);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    System.out.println("Sai mật khẩu.");
                }
            } else {
                System.out.println("Không tìm thấy MSGV.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
