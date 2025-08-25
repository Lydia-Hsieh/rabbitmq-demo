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

## Create Producer, Consumer, Queue & Test Durable Queue Through RabbitMQ Management UI
https://chatgpt.com/s/t_68ac297b33608191aec61bd261f406be

## Test Persistent Message Through RabbitMQ Management UI
https://chatgpt.com/s/t_68ac2b0bd7888191adf507db09b7f04d

## Durable Queue V.S. Persistent Message Exam
### **題目 1**

* **條件**：Non-Durable Queue + Persistent Message
* **結果**：Queue 不會存在，所以訊息也不會存在。
  ✅ 你答對了。Persistent 只保證「訊息會寫進磁碟」，但如果 **Queue 定義本身不見了**，訊息也沒地方存放 → 直接沒了。

---

### **題目 2**

* **條件**：Durable Queue + Non-Persistent Message
* **結果**：Queue 會在，但訊息會消失。
  ✅ 你答對了。Queue 是 durable 所以還在；但訊息是 Non-Persistent，重啟時因為沒寫磁碟，會直接丟失。

---

### **題目 3**

* **條件**：Durable Queue + Persistent Message
* **結果**：Queue 會在，訊息也會在。
  ✅ 你答對了。這就是所謂「完整可靠」的組合 → Queue 存在、訊息也保證存活。

---

### **題目 4（陷阱題）**

* **條件**：Non-Durable Queue + Non-Persistent Message
* **直覺答案**：Queue 不會存在，訊息自然也不會存在。
* **陷阱點**：很多人以為 **Non-Persistent 訊息就算 Queue 還在，也能被抓一次再消失**。
  但這題重啟後 Queue 本身已經不見了，所以訊息根本沒機會「暫時存在」→ 直接全滅。
  ✅ 你答對了！陷阱只是想混淆「Queue 層級」跟「Message 層級」的持久化。其實只要 Queue 沒了，Message 不管 persistent 與否都沒救。

---

## 🎯 Day 1 總結

* **Queue Durable?** → 決定「重啟後 Queue 還在不在」。
* **Message Persistent?** → 決定「重啟後 Queue 裡的訊息還在不在」。
* **兩者必須搭配**，才有完整持久化。