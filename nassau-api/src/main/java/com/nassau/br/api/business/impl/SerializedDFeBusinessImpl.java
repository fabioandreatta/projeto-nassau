package com.nassau.br.api.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.SerializedDFe;
import com.nassau.br.api.business.SerializedDFeBusiness;
import com.nassau.br.kafka.SerializedDFeProducer;

/**
 * Envia um documento fiscal eletrônico para processamento
 * @author fabio
 */
@Component
public class SerializedDFeBusinessImpl implements SerializedDFeBusiness {
	private static final long serialVersionUID = -579373172798589530L;

	/**
	 * The Kafka Producer
	 */
	@Autowired
	private SerializedDFeProducer producer;
	
	/**
	 * O envio para processamento de um DF-e consiste em:
	 * 	1. salva o documento fiscal eletronico no HBase;
	 * 	2. enfileira o id do DF-e no Kafka.
	 */
	@Override
	public void process(SerializedDFe dfe) throws Exception {
		producer.send("incoming.dfe", dfe);
	}
}
