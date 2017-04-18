package com.nassau.br.hbase;

import org.apache.hadoop.hbase.client.Admin;

import com.nassau.br.exceptions.NassauException;

/**
 * Interface para execução de métodos no HBase
 * 
 * @author fabio
 */
public interface HBaseExecutor {
	/**
	 * Metodo de execução de tarefas no HBase.
	 * 
	 * @param admin
	 * @throws NassauException
	 */
	public void execute(Admin admin) throws NassauException;
}
