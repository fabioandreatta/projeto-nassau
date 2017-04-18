package com.nassau.br.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.SerializedDFe;
import com.nassau.br.exceptions.NassauException;
import com.nassau.br.hbase.EntityManager;

/**
 * Essa classe deserializa um DFe para desenfileirá-lo do Kafka.
 * A estratégia é carregar o DFe do HBase eem um objeto SerializedDFe.
 * 
 * @author fabio
 */
@Component
public class SerializedDFeDeserializer implements Deserializer<SerializedDFe>  {
	/**
	 * Isso aqui � uma gambiarra tremenda. Temos que achar um jeito melhor pra
	 * injetar o HBase no Serializador do Kafka.
	 */
	private static EntityManager em;
	
	@Autowired
	public void setHbase(EntityManager em) {
		SerializedDFeDeserializer.em = em; 
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
	public SerializedDFe deserialize(String topic, byte[] data) {
		try {
			SerializedDFe dfe = em.get(SerializedDFe.class, new String(data));
			return dfe;
		} catch (NassauException e) {
			// TODO Log
		}
		return null;
	}

}
