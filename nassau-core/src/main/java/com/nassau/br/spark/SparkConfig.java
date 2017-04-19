package com.nassau.br.spark;

import org.apache.spark.SparkConf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:spark.properties") 
@ComponentScan({"com.nassau.br"})
public class SparkConfig {
	
	@Bean
	public SparkConf createSparkConf() {
		return new SparkConf()
                .setAppName("Projeto Nassau")
                .setMaster("local[*]");
	}
	
	
}
