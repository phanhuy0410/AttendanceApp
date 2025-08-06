import com.google.gson.Gson;
import com.rabbitmq.client.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;

public class AttendanceTableController {
    private static final String DIRECT_EXCHANGE = "attendanceExchange";

    @FXML
    private TableView<AttendanceRabbitMQ> attendanceTable;

    @FXML
    private TableColumn<AttendanceRabbitMQ, String> nameColumn;

    @FXML
    private TableColumn<AttendanceRabbitMQ, String> mssvColumn;

    @FXML
    private TableColumn<AttendanceRabbitMQ, String> timeColumn;

    private final ObservableList<AttendanceRabbitMQ> attendanceList = FXCollections.observableArrayList();

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        mssvColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        attendanceTable.setItems(attendanceList);
    }

    private String courseId;
    private int group;

    public void setCourseData(String courseId, int group) {
        this.courseId = courseId;
        this.group = group;
        QueueInitializer.ensureQueueExists(courseId, group);
        startListeningForAttendance();
    }

    private final Gson gson = new Gson();

    public void startListeningForAttendance() {
        
        String QUEUE_NAME = "teacher_"+courseId+"_"+group;
        try {
            Connection conn = MQHelper.getConnection();
            Channel channel = conn.createChannel();
            channel.exchangeDeclare(DIRECT_EXCHANGE, "direct", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            String ROUTING_KEY = "attendanceKey."+courseId;
            channel.queueBind(QUEUE_NAME, DIRECT_EXCHANGE, ROUTING_KEY);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received message: " + message);
                AttendanceRabbitMQ att = gson.fromJson(message, AttendanceRabbitMQ.class);
                if (att.getMaHP().equals(courseId) && att.getNhom() == group) {
                    Platform.runLater(() -> {
                        attendanceList.add(att);
                    });
                }
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
