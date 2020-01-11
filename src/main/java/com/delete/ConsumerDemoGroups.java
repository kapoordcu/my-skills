//package com.delete;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Properties;
//
//public class ConsumerDemoGroups {
//    public static void main(String[] args) {
//        String bootstrapServers = "127.0.0.1:9092";
//        String groupId = "my-fourth-group-1";
//        String topic = "testme";
//
//        Logger logger = LoggerFactory.getLogger(ConsumerDemoGroups.class);
//
//        // KAFKA consumer properties
//        Properties props = new Properties();
//        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        // create consumer
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//
//        // subscribe consumer to our topic
//        consumer.subscribe(Collections.singletonList(topic));
//
//        // poll for data
//        while (true)  {
//            // consumer.poll(100); //new is kafka 2.0.0, Deprecated
//            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
//            for(ConsumerRecord record : consumerRecords) {
//                logger.info("Key: " + record.key() + ", value: " + record.value()
//                        + ", partition: " + record.partition()
//                        + ", offsets: " + record.offset()
//                );
//            }
//        }
//    }
//}
