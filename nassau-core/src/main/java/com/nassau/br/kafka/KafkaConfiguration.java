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

/**
 * Configurações do Kafka
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
	
	/*
	 * Producer
	 */
	/**
	 * Configurações do Kafka
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
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
