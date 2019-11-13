package com.performance.sql;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        String bootstrapServers = "127.0.0.1:9092";

        // KAFKA producer properties
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create the  producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        // Producer record
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "value kafka");
        // Send data - async
        producer.send(record);
        //flush and close
        producer.flush();
        producer.close();
        //SpringApplication.run(Application.class, args);
    }
}

