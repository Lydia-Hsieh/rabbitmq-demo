package messageconsumptioncontrol;

import com.rabbitmq.client.Channel;
import helloworld.HelloQueue;

public class MessageProducer {

    public static void main(String[] args) {
        try  (Channel channel = HelloQueue.getHelloQueueChannel()) {
            channel.basicPublish("", HelloQueue.QUEUE_NAME, null, "1".getBytes());
            System.out.println(" [x] Sent '"+ "1" + "'");
            Thread.sleep(3000);

            channel.basicPublish("", HelloQueue.QUEUE_NAME, null, "2".getBytes());
            System.out.println(" [x] Sent '"+ "2" + "'");
            Thread.sleep(3000);

            channel.basicPublish("", HelloQueue.QUEUE_NAME, null, "3".getBytes());
            System.out.println(" [x] Sent '"+ "3" + "'");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
