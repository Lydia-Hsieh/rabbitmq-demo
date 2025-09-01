package messageconsumptioncontrol.prefetch;

import com.rabbitmq.client.Channel;
import helloworld.HelloQueue;

public class PrefetchProducer {

    public static void main(String[] args) {
        try  (Channel channel = HelloQueue.getHelloQueueChannel()) {
            int count = 1;
            for (int i = 0; i < 10; i++) {
                System.out.println(" [x] Sent '" + count + "'");
                channel.basicPublish("", HelloQueue.QUEUE_NAME, null, String.valueOf(count++).getBytes());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
