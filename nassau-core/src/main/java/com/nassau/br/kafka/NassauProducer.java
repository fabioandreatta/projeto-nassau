package com.nassau.br.kafka;

import java.io.Serializable;

import com.nassau.br.DFe;

public interface NassauProducer<T extends DFe> extends Serializable {
	public void send(String topic, T message, boolean sync) throws Exception;
	public void send(String topic, T message) throws Exception;
}
