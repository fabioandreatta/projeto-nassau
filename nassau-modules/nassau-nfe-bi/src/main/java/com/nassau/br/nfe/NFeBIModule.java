package com.nassau.br.nfe;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.SerializedDFe;
import com.nassau.br.kafka.SerializedDFeConsumer;
import com.nassau.br.kafka.SerializedDFeMessageListener;
import com.nassau.br.module.ModuleImplementation;

@Component
public class NFeBIModule extends ModuleImplementation {
	private static final long serialVersionUID = -642578741433039627L;
	
	/**
	 * Consumer
	 */
	@Autowired
	private SerializedDFeConsumer consumer;
	
	/**
	 * Construtor
	 */
	public NFeBIModule() {
		super("/nfe", "nfe.bi");
	}
	
	/**
	 * Executa o processamento
	 */
	@Override
	public void process() {
		// Subscribe to the incomming topic
		consumer.addMessageListener(new SerializedDFeMessageListener() {
			@Override
			public void onMessage(String topic, SerializedDFe dfe) throws Exception {
				if (dfe == null) return;
				
				// Dispatch the message to all registered modules
				System.out.println(String.format("Topic (%s), Id (%s), Data(%s)", topic, dfe.id(), dfe.getSerializedData()));
			}
		});
		consumer.subscribe(Arrays.asList(getKafkaTopic())); 
	}
}
