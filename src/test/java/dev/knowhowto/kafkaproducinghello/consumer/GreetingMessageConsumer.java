package dev.knowhowto.kafkaproducinghello.consumer;

import java.util.concurrent.CountDownLatch;

import com.iqkv.boot.kafka.exception.ConsumerRecordProcessingException;
import dev.knowhowto.kafkaproducinghello.model.Greeting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class GreetingMessageConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private String payload;

  @KafkaListener(containerFactory = "consumeGreetingKafkaListenerContainerFactory",
                 topics = "${iqkv.kafka.topics.hello-world.name}",
                 groupId = "${spring.kafka.consumer.group-id}")
  public void consume(ConsumerRecord<String, Greeting> consumerRecord) {
    try {
      log.info("( {} ) Received message, key: {}, value: {}",
          Thread.currentThread().getName(), consumerRecord.key(), consumerRecord.value());
      payload = consumerRecord.value().toString();
      latch.countDown();
    } catch (Exception e) {
      throw new ConsumerRecordProcessingException("Error processing message data.", e);
    }
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}

