package com.nassau.br.hbase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO Very important
 * Isso aqui é uma tremenda de uma gambiarra. 
 * Precisamos pensar em uma maneira de injetar os componentes direto no serializador do Kafka. 
 * Não sei se o pessoal da spring já pensou nisso. Senão, podemos ajudar.
 * 
 * @author fabio
 */
@Component
public class HBaseSingleton {
	/**
	 * Singleton
	 */
	private static HBaseTemplate hbase;
	
	/**
	 * Statig method
	 * @return
	 */
	public static HBaseTemplate hbase() {
		return hbase;
	}
	
	@Autowired
	public void setHbase(HBaseTemplate hbase) {
		HBaseSingleton.hbase = hbase; 
	}
}
