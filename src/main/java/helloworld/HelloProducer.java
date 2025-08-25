package helloworld;

import com.rabbitmq.client.Channel;

public class HelloProducer {

    public static void main(String[] args) {
        String message = "Hello...World!";
        try  (Channel channel = HelloQueue.getHelloQueueChannel()) {
            channel.basicPublish("", HelloQueue.QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '"+ message + "'");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
