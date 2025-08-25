# Rabbit MQ Practice - 01. Hello World

## Start Rabbit MQ Docker
- container not exists

`docker run -d --hostname rabbit --name rabbitmq-test -p 5672:5672 -p 15672:15672 rabbitmq:4-management`
- container exists

`docker start rabbitmq-test`
- RabbitMQ Management UI

`http://localhost:15672` default user: guest, pwd: guest

## Connect to Rabbit MQ
### Add RabbitMQ Java Client dependencies
```groovy
plugins {
    id 'java'
}

dependencies {
    implementation 'com.rabbitmq:amqp-client:5.26.0'
}
```
### Prepare connection parameters
```java
    public static final String RABBIT_MQ_ADDRESS = "localhost";
    public static final int RABBIT_MQ_PORT = 5672;

    //default
    public static final String RABBIT_MQ_USERNAME = "guest";
    public static final String RABBIT_MQ_PASSWORD = "guest";
    public static final String RABBIT_MQ_DEFAULT_VHOST = "/";

    //for testing virtual host
    public static final String RABBIT_MQ_TEST_USERNAME = "testuser";
    public static final String RABBIT_MQ_TEST_PASSWORD = "testpass";
    public static final String RABBIT_MQ_TEST_VHOST = "/test-vhost";
```
- If testing vhost, need to add vhost and user & set up permissions on rabbit MQ UI

### Create connection factory
```java
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
```

### Create Connection & Channel
```java
Connection connection = null;
Channel channel = null;
try {
    connection = connectionFactory.newConnection();
    channel = connection.createChannel();
    System.out.println("Successfully get channel.");

    channel.queueDeclare("hello.queue", false, false, false, null);
} catch (IOException | TimeoutException e) {
    throw new RuntimeException(e);
}
```

## Producer, Consumer, Queue
see code
