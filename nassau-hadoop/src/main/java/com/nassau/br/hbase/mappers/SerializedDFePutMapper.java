package com.nassau.br.hbase.mappers;

import org.apache.hadoop.hbase.client.Put;

import com.nassau.br.SerializedDFe;
import com.nassau.br.hbase.PutMapper;

/**
 * Maps a SerializedDFe to be saved on HBase
 * @author fabio
 */
public class SerializedDFePutMapper implements PutMapper<SerializedDFe> {
	@Override
	public Put map(SerializedDFe dfe) throws Throwable {
		Put entry = new Put(dfe.id().getBytes());
		String serialized = (dfe.getSerializedData() != null) ? dfe.getSerializedData() : ""; 
		entry.addColumn("serialized".getBytes(), "dfe".getBytes(), serialized.getBytes());
		return entry;
	}
}
