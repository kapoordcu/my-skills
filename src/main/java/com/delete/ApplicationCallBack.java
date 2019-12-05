package com.delete;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;


@SpringBootApplication
public class ApplicationCallBack {
    public static void main(String[] args) {
        String bootstrapServers = "127.0.0.1:9092";
        Logger logger = LoggerFactory.getLogger(ApplicationCallBack.class);

        // KAFKA producer properties
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create the  producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        // Producer record
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("testme", "value kafka " + i);
            // Send data - async
            producer.send(record, (recordMetadata, e) -> {
                // executes every time a record is sent successfully or exception is thrown
                if(e==null) {
                    // the record is sent successfully
                    logger.info("Recieved new metadata \n" +
                            "Topic:" + recordMetadata.topic() + "\n" +
                            "Partition:" + recordMetadata.partition() + "\n" +
                            "Offset:" + recordMetadata.offset() + "\n" +
                            "Timestamp:" + recordMetadata.timestamp() + "\n"
                    );

                } else {
                    logger.error("Error while producing" + e);
                }
            });
        }

        //flush and close
        producer.flush();
        producer.close();
        //SpringApplication.run(Application.class, args);
    }
}

