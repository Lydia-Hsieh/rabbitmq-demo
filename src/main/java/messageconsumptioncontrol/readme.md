# 02. Message Consumption Control

## Auto Ack & Manual Ack
maual ack
```java
DeliverCallback deliverCallback = (consumerTag, delivery) -> {
    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

    System.out.println(" [x] Received '" + message + "'");
    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
};

boolean autoAck = false;
channel.basicConsume(HelloQueue.QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
```

## Requeue after consumption failed
### resolution of consumption failed
1. basicAck
2. basicNack
3. basicReject
### basicAck
- 用途：告訴 RabbitMQ「這條訊息處理成功，可以刪掉」
- 結果：訊息從 queue 移除，不會回來。

### basicNack
- 用途：告訴 RabbitMQ「這條訊息處理失敗」
- 參數：有一個 `requeue flag`
  - true → RabbitMQ 把訊息丟回 queue（重試機制）
  - false → 訊息丟掉（如果有 DLQ，會進 DLQ）
- 支援批次：可以一次 nack 多條訊息

### basicReject
- 用途：也是告訴 RabbitMQ「這條訊息處理失敗」
- 差別：只能針對「單一訊息」操作，不能批次。
- 參數：同樣有 requeue=true/false。 

>一般開發：建議用 basicNack，因為它彈性比較大（可以批次）
