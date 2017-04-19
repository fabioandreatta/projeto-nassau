package com.nassau.br.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.SerializedDFe;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * Kafka Multi Threaded Consumer 
 * @author fabio
 */
@Component
public class SerializedDFeConsumer implements NassauConsumer<SerializedDFe> {
	private static final long serialVersionUID = 5846450328705618323L;
	
	/**
	 * The thread class for consumers
	 * @author fabio
	 */
	public static class ConsumerThread implements Runnable {
		/**
		 * The Kafka Stream
		 */
		private KafkaStream<byte[], byte[]> stream;
		
		/**
		 * The topic the consumer is reading
		 */
		private String topic;
		
		/**
		 * Listeners
		 */
		private List<NassauMessageListener<SerializedDFe>> listeners;
		
		/**
		 * Kafka message deserializer
		 */
		private SerializedDFeDeserializer deserializer;
		
		/**
		 * Constructor
		 * @param stream
		 */
		public ConsumerThread(String topic, KafkaStream<byte[], byte[]> stream, List<NassauMessageListener<SerializedDFe>> listeners) {
			super();
			this.topic 			= topic;
			this.stream 		= stream;
			this.listeners 		= listeners;
			this.deserializer	= new SerializedDFeDeserializer();
		}

		/**
		 * Thread method
		 */
		@Override
		public void run() {
			if (stream == null) return;
			ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
	        while (iterator.hasNext()) {
	        	// Move foward
	        	MessageAndMetadata<byte[], byte[]> message = iterator.next();
	        	
	        	// Send the messages
	        	if (listeners != null) {
	        		for (NassauMessageListener<SerializedDFe> listener : listeners) {
	        			if (listener != null) {
	        				try {
	        					listener.onMessage(topic, deserializer.deserialize(topic, message.message()));
	        				} catch(Exception e) {
	        					// TODO log
	        				}
	        			}
	        		}
	        	}
	        	
	        	// TODO we must find a way to commit this message
	        }
		}
	}

	/**
	 * Executor Service
	 */
	private ExecutorService executor;
	
	/**
	 * Consumer
	 */
	@Autowired
	private ConsumerConnector consumer;
	
	/**
	 * Listeners
	 */
	private List<NassauMessageListener<SerializedDFe>> listeners;
	
	/**
	 * Constructor
	 */
	public SerializedDFeConsumer() {
		super();
	}
	
	/**
	 * Subscribe to one topic
	 */
	@Override
	public void subscribe(List<String> topics) {
		if (topics == null || topics.isEmpty()) return;
		
		// Configuring one thread per topic
		// TODO this should be configurable in the future.
		Map<String, Integer> topicCount = new HashMap<String, Integer>();
		for (String topic : topics) topicCount.put(topic, 1);
		
		// Creating the streams
		executor = Executors.newFixedThreadPool(topics.size());
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
		for (Map.Entry<String, List<KafkaStream<byte[], byte[]>>> topicStreams : consumerStreams.entrySet()) {
			for (KafkaStream<byte[], byte[]> stream : topicStreams.getValue()) {
	            executor.submit(new ConsumerThread(topicStreams.getKey(), stream, listeners));
	        }
		}
	}

	/**
	 * Message Listeners
	 */
	@Override
	public void addMessageListener(NassauMessageListener<SerializedDFe> listener) {
		if (listeners == null) listeners = new CopyOnWriteArrayList<NassauMessageListener<SerializedDFe>>();
		listeners.add(listener);
	}
}
