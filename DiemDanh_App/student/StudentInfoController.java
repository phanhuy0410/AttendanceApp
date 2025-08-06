import javafx.scene.control.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.rabbitmq.client.Connection;
import javafx.fxml.FXML;

public class StudentInfoController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField mssvField;
    @FXML
    private TextField classField;
    @FXML
    private TextField emailField;
    private String mssv;

    public void loadStudentInfo(String mssv) {
        this.mssv = mssv;
        Student student = DBHelper.getStudentInfo(mssv);
        if (student != null) {
            nameField.setText(student.getFullName());
            mssvField.setText(student.getMSSV());
            classField.setText(student.getClassName());
            emailField.setText(student.getEmail());
        }
    }
}


