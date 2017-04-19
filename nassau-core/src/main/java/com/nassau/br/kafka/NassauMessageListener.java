package com.nassau.br.kafka;

import com.nassau.br.DFe;

public interface NassauMessageListener<T extends DFe> {
	public void onMessage(String topic, T dfe) throws Exception;
}
