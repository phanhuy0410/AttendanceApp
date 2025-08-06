import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

public class MQHelper {
    public static Connection getConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        return factory.newConnection();
    }
}

