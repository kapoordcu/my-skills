package com.performance.sql.service;

import com.google.common.collect.Lists;
import com.performance.sql.ApplicationCallBack;
import com.performance.sql.config.TwitterConfig;
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
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class TwitterProducerService {
    private TwitterConfig config;
    private static final Logger logger = LoggerFactory.getLogger(TwitterProducerService.class);

    @Autowired
    public TwitterProducerService(TwitterConfig config) {
        this.config = config;
    }

    public void produce() {
/** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);
        List<String> terms = Lists.newArrayList("twitter", "api");

        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(config.getConsumerKey(), config.getConsumerSecret(),
                config.getToken(), config.getSecret());
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
