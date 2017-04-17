package com.nassau.br.hbase;

import org.apache.hadoop.hbase.client.Put;

import com.nassau.br.Identifiable;

/**
 * Maps an object into a HBase Put
 * @author fabio
 *
 * @param <T>
 */
public interface PutMapper<T extends Identifiable> {
	/**
	 * Map method
	 * @param object
	 * @param put
	 * @throws Throwable
	 */
	public Put map(T object) throws Throwable;
}
