package messageconsumptioncontrol.prefetch;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import helloworld.HelloQueue;

import java.nio.charset.StandardCharsets;

public class PrefetchConsumerB {

    private static int prefetchCount = 1;

    public static void main(String[] args) {
        try {
            Channel channel = HelloQueue.getHelloQueueChannel();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("[x] Consumer B received '" + msg + "', prefetch count: " + prefetchCount);
                prefetchCount++;

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            boolean autoAck = false;
            channel.basicQos(1);
            channel.basicConsume(HelloQueue.QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
