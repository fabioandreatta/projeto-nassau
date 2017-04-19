package com.nassau.br.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.SerializedDFe;
import com.nassau.br.exceptions.NassauException;
import com.nassau.br.hbase.EntityManager;

/**
 * Essa classe serializa um DFe para enfileira-lo no Kafka.
 * A estrategia e salvar o DFe no HBase e enfileirar somente o codigo do documento no Kafka.
 *  
 * @author fabio
 */
@Component
public class SerializedDFeSerializer implements Serializer<SerializedDFe> {
	/**
	 * Isso aqui e uma gambiarra tremenda. Temos que achar um jeito melhor pra
	 * injetar o HBase no Serializador do Kafka.
	 */
	private static EntityManager em;
	
	@Autowired
	public void setHbase(EntityManager em) {
		SerializedDFeSerializer.em = em; 
	}
	
	@Override
	public void close() {
		// Nothing to do
	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
		// Nothing to do
	}

	@Override
	public byte[] serialize(String topic, SerializedDFe dfe) {
		try {
			em.put(dfe);
			return dfe.id().getBytes();
		} catch (NassauException e) {
			// TODO log
			e.printStackTrace();
		}
		return null;
	}
}
