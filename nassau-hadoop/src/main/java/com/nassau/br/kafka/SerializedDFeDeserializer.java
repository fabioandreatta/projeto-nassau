package com.nassau.br.kafka;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.nassau.br.SerializedDFe;
import com.nassau.br.exceptions.NassauException;
import com.nassau.br.hbase.HBaseSingleton;
import com.nassau.br.hbase.HBaseTemplate;
import com.nassau.br.hbase.mappers.SerializedDFeRowMapper;

/**
 * Essa classe deserializa um DFe para desenfileirá-lo do Kafka.
 * A estratégia é carregar o DFe do HBase eem um objeto SerializedDFe.
 * 
 * @author fabio
 */
public class SerializedDFeDeserializer implements Deserializer<SerializedDFe>  {

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
	public SerializedDFe deserialize(String topic, byte[] data) {
		try {
			HBaseTemplate hbase = HBaseSingleton.hbase();
			String id = new String(data);
			return hbase.get("incoming-dfe", id, new SerializedDFeRowMapper());
		} catch (NassauException e) {
			// TODO log
		}
		return null;
	}

}
