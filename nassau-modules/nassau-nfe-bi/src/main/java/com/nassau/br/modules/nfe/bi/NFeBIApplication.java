package com.nassau.br.modules.nfe.bi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.nassau.br.hbase.HBaseTemplate;

@SpringBootApplication
@ComponentScan({"com.nassau.br"})
public class NFeBIApplication {
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(NFeBIApplication.class);
		// HBaseTemplate hbase = context.getBean(HBaseTemplate.class);
		
	}
}
