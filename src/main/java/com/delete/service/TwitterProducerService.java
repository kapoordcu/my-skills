package com.delete.service;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class TwitterProducerService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterProducerService.class);
    private static final String consumerKey = "z0JwCuWLcUYkhhuBN3Mcpy4Pl";
    private static final String consumerSecret = "SmNIWRu2k9RRQp0YXuHYhqbqIxlZD6t6Dh2EcdYPx8NXSq4lbb";
    private static final String token = "793839253113274368-Hrb1a03na34NvoxZ5p3H6sVFdKJqXFL";
    private static final String secret = "f446e0vReLQVvG2oD3YaEELKyyuMJvg0oIOQwSg7tNuEz";

    public void produce() {
/** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);
        List<String> terms = Lists.newArrayList("twitter", "api", "modi", "trump", "tipico");

        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret,
                token, secret);
        Client hosebirdClient = createClient(hosebirdHosts, hosebirdEndpoint, hosebirdAuth, msgQueue);
        hosebirdClient.connect();
        KafkaProducer<String, String> producer = createKafkaProducer();
        // hosebirdClient
        pollMessageFromQueue(hosebirdClient, msgQueue, producer) ;
    }

    private KafkaProducer<String, String> createKafkaProducer() {
        String bootstrapServers = "127.0.0.1:9092";

        // KAFKA producer properties
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Idempotnet Producer
        props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        props.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        props.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");


        // High Throughput Performance (at the expense of latency and cpu usage)
        props.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024));

        // Create the  producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        return producer;
    }

    private void pollMessageFromQueue(Client hosebirdClient, BlockingQueue<String> msgQueue, KafkaProducer<String, String> producer) {
        logger.info("Start producing");
        String msg = null;
        while (!hosebirdClient.isDone()) {
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                hosebirdClient.stop();
            }
            if(msg!=null) {
                logger.info(msg);
                producer.send(new ProducerRecord<>("twittertweets", null, msg), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if(e!=null) {
                            logger.error("Something bad happened", e);
                        }
                    }
                });
            }
        }
        logger.info("Stop producing");
    }

    private Client createClient(Hosts hosebirdHosts, StatusesFilterEndpoint hosebirdEndpoint,
                              Authentication hosebirdAuth, BlockingQueue<String> msgQueue) {
        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        return builder.build();

    }


}
