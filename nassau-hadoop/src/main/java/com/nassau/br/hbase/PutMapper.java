package com.nassau.br.hbase;

import org.apache.hadoop.hbase.client.Put;

/**
 * Maps an object into a HBase Put
 * @author fabio
 *
 * @param <T>
 */
public interface PutMapper {
	/**
	 * Map method
	 * @param object
	 * @param put
	 * @throws Throwable
	 */
	public <T> Put map(T object) throws Throwable;
}
