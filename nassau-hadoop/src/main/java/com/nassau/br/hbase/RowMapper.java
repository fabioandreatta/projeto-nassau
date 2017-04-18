package com.nassau.br.hbase;

import org.apache.hadoop.hbase.client.Result;

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
	public <T> T map(String table, Result result) throws Throwable;
}
