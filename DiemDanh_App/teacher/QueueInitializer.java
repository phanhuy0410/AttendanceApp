import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class QueueInitializer {
    private static final String DIRECT_EXCHANGE = "attendanceExchange";

    public static void ensureQueueExists(String courseId, int group) {
        String QUEUE_NAME = "teacher_" + courseId + "_" + group;
        String ROUTING_KEY = "attendanceKey." + courseId;
        try {
            Connection conn = MQHelper.getConnection();
            Channel channel = conn.createChannel();
            channel.exchangeDeclare(DIRECT_EXCHANGE, "direct", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, DIRECT_EXCHANGE, ROUTING_KEY);
            System.out.println("Queue ensured: " + QUEUE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

