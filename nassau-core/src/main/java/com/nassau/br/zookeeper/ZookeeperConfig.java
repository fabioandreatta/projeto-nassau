package com.nassau.br.zookeeper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:zookeeper.properties") 
@ComponentScan({"com.nassau.br"})
public class ZookeeperConfig {
	/**
	 * Bootstrap configuration
	 */
	@Value("${zookeeper.quorum:}")
	private String zkQuorum;
	
	@Bean
	public ZookeeperTemplate zookeeper() {
		return new ZookeeperTemplate(zkQuorum);
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
