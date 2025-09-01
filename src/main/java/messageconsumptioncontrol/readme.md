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


## Prefetch (basicQos)
- `PrefetchProducer`: send 10 messages
- `PrefetchConsumerA` & `PrefetchConsumerB`
  - subscribe same queue at the same time
  - compare:
    - no prefetch -> random
    - set prefetch to 1 -> 公平分配 (Fair Dispatch)
>autoAck=true → broker 一旦把訊息「派發」給 consumer，就立刻當作處理完成。
>所以不管你 ConsumerA 裡 sleep 5 秒、還是 5 分鐘，RabbitMQ 都不會在意。
>> -> 要觸發 不公平分配，需要改成manual ack + basicQos(1)

- test result
```text
[x] Consumer A received '1', prefetch count: 1

---------

[x] Consumer B received '2', prefetch count: 1
[x] Consumer B received '3', prefetch count: 2
[x] Consumer B received '4', prefetch count: 3
[x] Consumer B received '5', prefetch count: 4
[x] Consumer B received '6', prefetch count: 5
[x] Consumer B received '7', prefetch count: 6
[x] Consumer B received '8', prefetch count: 7
[x] Consumer B received '9', prefetch count: 8
[x] Consumer B received '10', prefetch count: 9
```
