package messageconsumptioncontrol;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import helloworld.HelloQueue;

import java.nio.charset.StandardCharsets;

public class RequeueConsumer {

    public static void main(String[] args) {
        try {
            Channel channel = HelloQueue.getHelloQueueChannel();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                System.out.println(" [x] Received '" + message + "'");
                //basicReject(requeue=true) → 訊息會不斷回到 queue
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), true);
                //basicNack(requeue=false) → 訊息會被丟棄（如果設了 DLQ 就會進 DLQ）
//                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                //不呼叫 ack/nack → 當 Consumer crash後，RabbitMQ 會自動把訊息放回 queue
            };

            boolean autoAck = false;
            channel.basicConsume(HelloQueue.QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
