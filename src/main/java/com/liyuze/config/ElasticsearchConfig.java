package com.liyuze.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ElasticsearchConfiguration
 *
 * @author YangBin
 * @date 2018/4/20
 */
@Configuration
public class ElasticsearchConfig implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //重写指定原来配置参数
    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    public String getClusterNodes() {
        return clusterNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    private TransportClient client;

    @Override
    public void destroy() throws Exception {
        try {
            logger.info("Closing elasticSearch client");
            if (client != null) {
                client.close();
            }
        } catch (final Exception e) {
            logger.error("Error closing ElasticSearch client: ", e);
        }
    }

    @Override
    public TransportClient getObject() throws Exception {
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return TransportClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        buildClient();
    }

    private void buildClient() {
        try {
            System.setProperty("es.set.netty.runtime.available.processors", "false");
            client = new PreBuiltTransportClient(settings());
            if (!clusterNodes.isEmpty()) {
                for (String nodes : clusterNodes.split(";")) {
                    String[] inetsocket = nodes.split(":");
                    String address = inetsocket[0];
                    Integer port = Integer.valueOf(inetsocket[1]);
                    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(address), port));
                }
            }
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 初始化默认的client
     */
    private Settings settings() {
        return Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", true)
                .build();
    }

}
