package com.nassau.br.kafka;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.nassau.br.SerializedDFe;
import com.nassau.br.exceptions.NassauException;
import com.nassau.br.hbase.HBaseSingleton;
import com.nassau.br.hbase.HBaseTemplate;
import com.nassau.br.hbase.mappers.SerializedDFePutMapper;

/**
 * Essa classe serializa um DFe para enfileirá-lo no Kafka.
 * A estratégia é salvar o DFe no HBase e enfileirar somente o código do documento no Kafka.
 *  
 * @author fabio
 */
public class SerializedDFeSerializer implements Serializer<SerializedDFe> {
	@Override
	public void close() {
		// Nothing to do
	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
		// Check if hbase table exists
		HBaseTemplate hbase = HBaseSingleton.hbase();
		try {
			if (!hbase.tableExists("incoming-dfe")) {
				hbase.createTable("incoming-dfe", Arrays.asList(new String[] {"serialized"}));
			}
		} catch (NassauException e) {
			// TODO log
		}
	}

	@Override
	public byte[] serialize(String topic, SerializedDFe dfe) {
		try {
			HBaseTemplate hbase = HBaseSingleton.hbase();
			hbase.put("incoming-dfe", dfe, new SerializedDFePutMapper());
		} catch (NassauException e) {
			// TODO log
		}
		return dfe.id().getBytes();
	}
}
