package com.nassau.br.scheduler;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.SerializedDFe;
import com.nassau.br.kafka.SerializedDFeConsumer;
import com.nassau.br.kafka.SerializedDFeMessageListener;
import com.nassau.br.kafka.SerializedDFeProducer;
import com.nassau.br.module.Module;
import com.nassau.br.zookeeper.ZookeeperTemplate;

@Component
public class DFeScheduler implements Serializable, Runnable {
	private static final long serialVersionUID = -3051518856304492224L;

	/**
	 * Consumer
	 */
	@Autowired
	private SerializedDFeConsumer consumer;
	
	/**
	 * Producer
	 */
	@Autowired
	private SerializedDFeProducer producer;
	
	/**
	 * Zookeeper
	 */
	@Autowired
	private ZookeeperTemplate zookeeper;
	
	/**
	 * Running flag
	 */
	private Boolean running = false;
	
	/**
	 * Executes the scheduler
	 */
	@Override
	public void run() {
		// Subscribe to the incomming topic
		consumer.addMessageListener(new SerializedDFeMessageListener() {
			@Override
			public void onMessage(String topic, SerializedDFe dfe) throws Exception {
				if (zookeeper == null) return;
				
				// Dispatch the message to all registered modules
				for (Module module : zookeeper.getRegisteredModules()) {
					producer.send(module.getKafkaTopic(), dfe);
				}
			}
		});
		consumer.subscribe(Arrays.asList("incoming.dfe")); 
		
		// Holds until get stopped
		running = true;
		while(running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO log
			}
		}
	}
	
	/**
	 * Stops the scheduler
	 */
	public void stop() {
		synchronized (running) {
			running = false;
		}
	}
}
