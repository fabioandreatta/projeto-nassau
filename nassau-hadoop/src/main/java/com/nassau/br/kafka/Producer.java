package com.nassau.br.kafka;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.nassau.br.SerializedDFe;

/**
 * Kafka Producer
 * @author fabio
 */
@Component
public class Producer {
	/**
	 * Producer Template
	 */
	@Autowired
	private KafkaTemplate<String, SerializedDFe> producer;
	
	/**
	 * Envia uma mensagem para o Kafka
	 * @param topic
	 * @param message
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void send(String topic, SerializedDFe message, boolean sync) throws Exception {
		ListenableFuture<SendResult<String, SerializedDFe>> future = producer.send(topic, message);
		
		// Need this?
		future.addCallback(new ListenableFutureCallback<SendResult<String, SerializedDFe>>() {
			@Override
			public void onSuccess(SendResult<String, SerializedDFe> result) {
				// TODO log
				// LOGGER.info("sent message='{}' with offset={}", message, result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO log
				// LOGGER.error("unable to send message='{}'", message, ex);
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
