package helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static common.Constants.RABBIT_MQ_ADDRESS;
import static common.Constants.RABBIT_MQ_PORT;
import static common.Constants.RABBIT_MQ_TEST_PASSWORD;
import static common.Constants.RABBIT_MQ_TEST_USERNAME;
import static common.Constants.RABBIT_MQ_TEST_VHOST;

public class HelloQueue {

    public static final String QUEUE_NAME = "hello.queue";

    public static Channel getHelloQueueChannel() {
        //create connection factory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RABBIT_MQ_ADDRESS);
        connectionFactory.setPort(RABBIT_MQ_PORT);
        connectionFactory.setUsername(RABBIT_MQ_TEST_USERNAME);
        connectionFactory.setPassword(RABBIT_MQ_TEST_PASSWORD);
        connectionFactory.setVirtualHost(RABBIT_MQ_TEST_VHOST);
        //enable auto recovery and topology recovery
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setTopologyRecoveryEnabled(true);

        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            System.out.println("Successfully get channel.");

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return channel;
    }
}
