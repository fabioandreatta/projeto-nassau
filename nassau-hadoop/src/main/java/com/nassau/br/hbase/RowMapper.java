package com.nassau.br.hbase;

import org.apache.hadoop.hbase.client.Result;

import com.nassau.br.Identifiable;

/**
 * Maps a result into an object
 * @author fabio
 *
 * @param <T>
 */
public interface RowMapper<T extends Identifiable> {
	/**
	 * Maps method
	 * @param result
	 * @return
	 * @throws Throwable
	 */
	public T map(Result result) throws Throwable;
}
