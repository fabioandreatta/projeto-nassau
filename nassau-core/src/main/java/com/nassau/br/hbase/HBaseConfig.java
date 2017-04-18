package com.nassau.br.hbase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.nassau.br.exceptions.NassauException;

@Configuration
@PropertySource("classpath:hbase.properties")
@ComponentScan({"com.nassau.br"})
public class HBaseConfig {
	/**
	 * Constants
	 */
	public static final String HBASE_CONFIGURATION_TIMEOUT					= "timeout";
	public static final String HBASE_CONFIGURATION_ZOOKEEPER_ZNODE_PARENT	= "zookeeper.znode.parent";
	public static final String HBASE_CONFIGURATION_ZOOKEEPER_QUORUM			= "hbase.zookeeper.quorum";
	public static final String HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT 	= "hbase.zookeeper.property.clientPort";

	/**
	 * Bootstrap configuration
	 */
	@Value("${timeout:5000}")
	private Integer timeout;
	
	/**
	 * Bootstrap configuration
	 */
	@Value("${zookeeper.znode.parent:}")
	private String zparent;
	
	/**
	 * Bootstrap configuration
	 */
	@Value("${hbase.zookeeper.quorum:}")
	private String quorum;
	
	/**
	 * Bootstrap configuration
	 */
	@Value("${hbase.zookeeper.property.clientPort:2181}")
	private String clientPort;
	
	/**
	 * Configurações do HBase
	 * @return
	 * @throws NassauException 
	 */
	@Bean
	public HBaseTemplate hbaseTemplate() throws NassauException {
		return new HBaseTemplate(timeout, zparent, quorum, clientPort);
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
