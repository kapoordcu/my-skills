//package com.delete;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import java.util.Properties;
//import java.util.concurrent.ExecutionException;
//
//
//@SpringBootApplication
//public class ApplicationKeys {
//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        String bootstrapServers = "127.0.0.1:9092";
//        Logger logger = LoggerFactory.getLogger(ApplicationKeys.class);
//
//        // KAFKA producer properties
//        Properties props = new Properties();
//        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//
//        // Create the  producer
//        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
//
//        // Producer record
//        for (int i = 0; i < 10; i++) {
//            String topic = "testme";
//            String value = "value kafka " + i;
//            String key = "id_" + i;
//            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);
//            logger.info("key " + key);
//            // Same key goes to same partition
//            // Send data - async
//            producer.send(record, (recordMetadata, e) -> {
//                // executes every time a record is sent successfully or exception is thrown
//                if(e==null) {
//                    // the record is sent successfully
//                    logger.info("Recieved new metadata \n" +
//                            "Topic:" + recordMetadata.topic() + "\n" +
//                            "Partition:" + recordMetadata.partition() + "\n" +
//                            "Offset:" + recordMetadata.offset() + "\n" +
//                            "Timestamp:" + recordMetadata.timestamp() + "\n"
//                    );
//
//                } else {
//                    logger.error("Error while producing" + e);
//                }
//            }).get(); // block the send() to make it synchronous
//        }
//
//        //flush and close
//        producer.flush();
//        producer.close();
//        //SpringApplication.run(Application.class, args);
//    }
//}
//
