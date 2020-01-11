//package com.delete;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.TopicPartition;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Properties;
//
//public class ConsumerDemoGroupsAssignSeek {
//    public static void main(String[] args) {
//        String bootstrapServers = "127.0.0.1:9092";
//        String topic = "testme";
//
//        Logger logger = LoggerFactory.getLogger(ConsumerDemoGroupsAssignSeek.class);
//
//        // KAFKA consumer properties
//        Properties props = new Properties();
//        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        // create consumer
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//        // Assign/Seek: used to replay data or fetch a specific message
//
//        // Assign
//        TopicPartition topicPartition = new TopicPartition(topic, 0);
//        consumer.assign(Collections.singletonList(topicPartition));
//
//        // Seek
//        consumer.seek(topicPartition, 15L);
//        boolean keepReading = true;
//        Integer numMessagesToRead = 5;
//        Integer numMessagesReadSoFar = 0;
//        // poll for data
//        while (keepReading)  {
//            // consumer.poll(100); //new is kafka 2.0.0, Deprecated
//            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
//            for(ConsumerRecord record : consumerRecords) {
//                numMessagesReadSoFar +=1;
//                logger.info("Key: " + record.key() + ", value: " + record.value()
//                        + ", partition: " + record.partition()
//                        + ", offsets: " + record.offset()
//                );
//                if(numMessagesReadSoFar >= numMessagesToRead) {
//                    keepReading = false;
//                    break;
//                }
//            }
//        }
//        logger.info("Exiting application");
//    }
//}
