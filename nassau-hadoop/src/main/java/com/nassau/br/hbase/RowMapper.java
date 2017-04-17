package com.nassau.br.hbase;

import org.apache.hadoop.hbase.client.Result;

import com.nassau.br.Identifiable;

/**
 * Maps a result into an object
 * @author fabio
 */
public interface RowMapper {
	/**
	 * Maps method
	 * @param result
	 * @return
	 * @throws Throwable
	 */
	public <T extends Identifiable> T map(Result result) throws Throwable;
}
