package com.nassau.br.kafka;

import java.io.Serializable;
import java.util.List;

import com.nassau.br.DFe;

public interface NassauConsumer<T extends DFe> extends Serializable {
	/**
	 * Subscribe to one topic
	 * @param topics
	 */
	public void subscribe(List<String> topics);
	
	/**
	 * Adds a message listener
	 * @param listener
	 */
	public void addMessageListener(NassauMessageListener<T> listener);
}
