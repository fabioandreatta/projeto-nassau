package com.nassau.br.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a table in HBase of project Nassau
 * @author fsantos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NassauHTable {
	/**
	 * The table name
	 * @return
	 */
	public String name();
	
	/**
	 * The column families of the table
	 * @return
	 */
	public String[] families();
}
