package com.example.student_management.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.QueryApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${influxdb.url}")
    private String influxDBUrl;

    @Value("${influxdb.token}")
    private String token;

    @Value("${influxdb.org}")
    private String org;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(influxDBUrl, token.toCharArray(), org, bucket);
    }

    @Bean
    public WriteApi writeApi(InfluxDBClient influxDBClient) {
        return influxDBClient.getWriteApi();
    }

    @Bean
    public QueryApi queryApi(InfluxDBClient influxDBClient) {
        return influxDBClient.getQueryApi();
    }
}

