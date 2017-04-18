package com.nassau.br.kafka;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.SerializedDFe;

/**
 * Kafka Producer
 * @author fabio
 */
@Component
public class SerializedDFeProducer implements NassauProducer<SerializedDFe> {
	private static final long serialVersionUID = 5432507035498534140L;
	
	/**
	 * Producer Template
	 */
	@Autowired
	private Producer<String, SerializedDFe> producer;
	
	/**
	 * Envia uma mensagem para o Kafka
	 * @param topic
	 * @param message
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void send(String topic, SerializedDFe message, boolean sync) throws Exception {
		ProducerRecord<String, SerializedDFe> record = new ProducerRecord<String, SerializedDFe>(topic, message);
		Future<RecordMetadata> future = producer.send(record, new Callback() {
			@Override
			public void onCompletion(RecordMetadata record, Exception ex) {
				if (ex != null) {
					// TODO Log
				}
			}
		});
		if (sync) future.get();
	}
	
	/**
	 * Envia uma mensagem para o kafka em modo assíncrono
	 * @param topic
	 * @param message
	 * @throws Exception 
	 */
	public void send(String topic, SerializedDFe message) throws Exception {
		send(topic, message, false);
	}
}
