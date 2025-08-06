import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.rabbitmq.client.*;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn.*;
import javafx.stage.Stage;

public class TeacherController implements Initializable {

    @FXML
    private TextField teacherIdField;
    @FXML
    private Label welcomeLabel;
    @FXML
    private JFXButton logout;

    public void setTeacherInfo(String msgv) {
        welcomeLabel.setText("Xin chào, " + msgv);
    }

    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, Integer> groupColumn;
    @FXML
    private TableColumn<Course, String> courseIdColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, Void> viewButton;

    @FXML
    private MenuButton yearMenu;
    @FXML
    private MenuButton semesterMenu;
    @FXML
    private TextField searchField;
    @FXML
    private MenuItem yearItem;
    @FXML
    private MenuItem semesterItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (MenuItem item : yearMenu.getItems()) {
            final MenuItem menuItem = item;
            item.setOnAction(e -> yearMenu.setText(menuItem.getText()));
        }
        for (MenuItem item : semesterMenu.getItems()) {
            final MenuItem menuItem = item;
            item.setOnAction(e -> semesterMenu.setText(menuItem.getText()));
        }
        initializeTableView();
    }

    @FXML
    public void initializeTableView() {
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        addViewButtonToTable();
    }

    @FXML
    private void Logout() {
        try {
            Stage stage = (Stage) logout.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String year = yearMenu.getText();
        String semester = semesterMenu.getText();
        String courseId = searchField.getText().trim();
        if (year.equals("Chọn") || semester.equals("Chọn") || courseId.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Thiếu thông tin",
                    "Vui lòng chọn năm học, học kỳ và nhập mã học phần.");
            return;
        }
        List<Course> results = DBHelper.searchCourses(year, semester, courseId);
        courseTable.setItems(FXCollections.observableArrayList(results));
    }

    private void addViewButtonToTable() {
        viewButton.setCellFactory(col -> new TableCell<>() {
            private final JFXButton btn = new JFXButton("Xem điểm danh");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    btn.setOnAction(e -> {
                        Course course = getTableView().getItems().get(getIndex());
                        if (course != null) {
                            openAttendanceTable(course);
                        }
                    });
                    setGraphic(btn);
                }
            }
        });
    }

    private void openAttendanceTable(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("table_attendance_view.fxml"));
            Parent root = loader.load();
            AttendanceTableController controller = loader.getController();
            controller.setCourseData(course.getCourseId(), course.getGroup());
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
