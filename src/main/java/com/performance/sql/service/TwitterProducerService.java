package com.performance.sql.service;

import com.google.common.collect.Lists;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

        // hosebirdClient
        pollMessageFromQueue(hosebirdClient, msgQueue) ;
    }

    private void pollMessageFromQueue(Client hosebirdClient, BlockingQueue<String> msgQueue) {
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
