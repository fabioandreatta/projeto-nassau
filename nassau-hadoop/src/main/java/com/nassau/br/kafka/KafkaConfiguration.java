package com.nassau.br.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.nassau.br.SerializedDFe;

/**
 * Configurações do Kafka
 * @author fabio
 */
@Configuration
@PropertySource("classpath:kafka.properties")
public class KafkaConfiguration {
	/**
	 * Bootstrap configuration
	 */
	@Value("${kafka.servers.bootstrap}")
	private String bootstrapServers;
	
	/*
	 * Producer
	 */
	/**
	 * Configurações do Kafka
	 * @return
	 */
	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 		bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 	StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SerializedDFeSerializer.class);
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 			5000);
		return props;
	}
	
	/**
	 * Producer Factory
	 * @return
	 */
	@Bean
	public ProducerFactory<String, SerializedDFe> producerFactory() {
		return new DefaultKafkaProducerFactory<String, SerializedDFe>(producerConfigs());
	}
	
	/**
	 * KafkaTemplate
	 * @return
	 */
	@Bean
	public KafkaTemplate<String, SerializedDFe> kafkaTemplate() {
		return new KafkaTemplate<String, SerializedDFe>(producerFactory());
	}
	
	/*
	 * Consumer
	 */
	// TODO
}
