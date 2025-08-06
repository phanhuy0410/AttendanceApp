import java.sql.SQLException;
import java.util.List;
import com.jfoenix.controls.JFXButton;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoryController {
    @FXML
    private JFXButton clearButton;
    @FXML
    private TableView<Attendance> historyTable;
    @FXML
    private TableColumn<Attendance, String> nameColumn;
    @FXML
    private TableColumn<Attendance, String> mssvColumn;
    @FXML
    private TableColumn<Attendance, String> courseColumn;
    @FXML
    private TableColumn<Attendance, String> courseNameColumn;
    @FXML
    private TableColumn<Attendance, String> groupColumn;
    @FXML
    private TableColumn<Attendance, String> timeColumn;

    private final ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();
    private String mssv;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        mssvColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("courseGroup"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        historyTable.setItems(attendanceList);
    }

    public void loadHistory(String mssv) {
        this.mssv = mssv;
        refreshTable();
    }

    @FXML
    private void handleClearTable() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle(null);
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có muốn xoá toàn bộ lịch sử điểm danh?");
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try {
                if (mssv != null) {
                    DBHelper.clearAttendanceHistory(mssv);
                    attendanceList.clear();
                    historyTable.refresh();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Lỗi");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Không tìm thấy mã số sinh viên.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshTable() {
        try {
            attendanceList.clear();
            if (mssv != null) {
                List<Attendance> history = DBHelper.getAttendanceHistory(mssv);
                attendanceList.addAll(history);
                historyTable.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleNewAttendance(String full_name, String mssv, String tenHP, String maHP, int nhom) {
        try {
            DBHelper.saveAttendance(full_name, mssv, tenHP, maHP, nhom);
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
