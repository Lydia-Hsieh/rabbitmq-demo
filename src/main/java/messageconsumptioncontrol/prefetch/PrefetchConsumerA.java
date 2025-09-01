package messageconsumptioncontrol.prefetch;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import helloworld.HelloQueue;

import java.nio.charset.StandardCharsets;

public class PrefetchConsumerA {

    private static int prefetchCount = 1;

    public static void main(String[] args) {
        try {
            Channel channel = HelloQueue.getHelloQueueChannel();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("[x] Consumer A received '" + msg + "', prefetch count: " + prefetchCount);
                doWork();
                prefetchCount++;

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            boolean autoAck = false;
            channel.basicQos(1); // 每次最多派 1 條訊息，等 ack 才能再派
            channel.basicConsume(HelloQueue.QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //simulate the delay when system is busy
    private static void doWork() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
