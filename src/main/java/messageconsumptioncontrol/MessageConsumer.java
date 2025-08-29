package messageconsumptioncontrol;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import helloworld.HelloQueue;

public class MessageConsumer {

    public static void main(String[] args) {
        try {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
            };

            Channel channel = HelloQueue.getHelloQueueChannel();
            boolean autoAck = true;
            channel.basicConsume(HelloQueue.QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
