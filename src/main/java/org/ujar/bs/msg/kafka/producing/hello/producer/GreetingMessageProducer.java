package org.ujar.bs.msg.kafka.producing.hello.producer;

import static org.ujar.bs.msg.kafka.producing.hello.config.Constants.TOPIC_DEFINITION_HELLO_WORLD;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.ujar.boot.starter.kafka.config.KafkaTopicDefinitionProperties;
import org.ujar.bs.msg.kafka.producing.hello.model.Greeting;

@Component
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP2")
public class GreetingMessageProducer {
  private final KafkaTemplate<String, Greeting> kafkaTemplate;
  private final KafkaTopicDefinitionProperties topicDefinitions;

  /**
   * Send message to Kafka broker with avoiding transaction-aware configuration environment
   */
  public void send(Greeting message) {
    final var key = UUID.randomUUID().toString();
    log.info("( {} ) Send message, key: {}, value: {}", Thread.currentThread().getName(), key, message);
    kafkaTemplate.executeInTransaction(op -> op.send(
        topicDefinitions.get(TOPIC_DEFINITION_HELLO_WORLD).name(),
        key,
        message)
    );
  }
}
