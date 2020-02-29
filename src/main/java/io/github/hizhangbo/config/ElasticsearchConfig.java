package io.github.hizhangbo.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

/**
 * @author Bob
 * @date 2020-02-29 17:21
 */
@PropertySource("classpath:/application.properties")
@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String[] host;

    @Value("${elasticsearch.port}")
    private int[] port;

    @Value("${elasticsearch.user}")
    private String user;

    @Value("${elasticsearch.password}")
    private String password;

    private HttpHost[] cluster;

    @PostConstruct
    private void init() {
        cluster = new HttpHost[host.length];
        for (int i = 0; i < host.length; i++) {
            cluster[i] = new HttpHost(host[i], port[i]);
        }
    }

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(user, password));
        RestClientBuilder builder = RestClient.builder(cluster)
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                );
        return new RestHighLevelClient(builder);
    }
}
