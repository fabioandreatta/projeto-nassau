package com.nassau.br.hbase.mappers;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.nassau.br.SerializedDFe;
import com.nassau.br.hbase.RowMapper;

/**
 * Maps a result from HBase in a SerializedDFe
 * @author fabio
 *
 */
public class SerializedDFeRowMapper implements RowMapper<SerializedDFe> {
	@Override
	public SerializedDFe map(Result result) throws Throwable {
		String id 			= Bytes.toString(result.getRow());
		String serialized 	= Bytes.toString(result.getValue("serialized".getBytes(), "dfe".getBytes()));
		return new SerializedDFe(id, serialized);
	}
}
