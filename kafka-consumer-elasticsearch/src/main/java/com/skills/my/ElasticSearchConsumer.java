package com.skills.my;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ElasticSearchConsumer {
    private static String hostname = "http://127.0.0.1:9200";
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchConsumer.class);
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = createClient();
        String jsonString = "{\"foo\": \"bar\"}";
        IndexRequest request = new IndexRequest("twitter",
                "tweets").source(jsonString, XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        String id = response.getId();
        logger.info(id);
        client.close();
    }

    public static RestHighLevelClient createClient() {
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(hostname)));
    }
}
