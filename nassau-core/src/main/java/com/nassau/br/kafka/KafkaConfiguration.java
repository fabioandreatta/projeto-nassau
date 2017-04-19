package com.nassau.br.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.nassau.br.SerializedDFe;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * Configuracoes do Kafka
 * @author fabio
 */
@Configuration
@PropertySource("classpath:kafka.properties") 
@ComponentScan({"com.nassau.br"})
public class KafkaConfiguration {
	/**
	 * Bootstrap configuration
	 */
	@Value("${kafka.servers.bootstrap:}")
	private String bootstrapServers; // TODO we should try to get all properties from the file
	
	@Value("${zookeeper.connect:}")
	private String zookeeperConnect; // TODO we should try to get all properties from the file
	
	/*
	 * Producer
	 */
	/**
	 * Configuracoes do Kafka
	 * @return
	 */
	@Bean
	public Properties producerConfigs() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 		bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 	StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SerializedDFeSerializer.class);
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 			5000);
		return props;
	}
	
	/**
	 * Creates the Kafka producer
	 * @return
	 */
	@Bean
	public Producer<String, SerializedDFe> producer() {
		return new KafkaProducer<String, SerializedDFe>(producerConfigs());
	}
	
	/*
	 * Consumer
	 */
	@Bean
	public Properties consumerConfigs() {
		Properties props = new Properties();
		props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 			bootstrapServers);
		props.put("zookeeper.connect", 																	zookeeperConnect);
		props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 		StringSerializer.class);
		props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 	SerializedDFeSerializer.class);
		props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, 					"nassau");
		return props;
	}
	
	@Bean
	public ConsumerConnector consumer() {
		return Consumer.createJavaConsumerConnector(new ConsumerConfig(consumerConfigs()));
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
