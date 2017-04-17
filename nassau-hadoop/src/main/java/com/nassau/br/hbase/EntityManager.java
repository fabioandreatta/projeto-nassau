package com.nassau.br.hbase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.Identifiable;
import com.nassau.br.annotations.NassauHTable;
import com.nassau.br.annotations.NassauHTableColumn;
import com.nassau.br.annotations.NassauHTableRowId;
import com.nassau.br.exceptions.NassauException;

/**
 * HBase Entity Manager 
 * @author fsantos
 */
@Component
public class EntityManager implements PutMapper, RowMapper {
	private static class Value {
		public byte[] family;
		public byte[] column;
		public byte[] value;
		
		public Value(byte[] family, byte[] column, byte[] value) {
			super();
			this.family = family;
			this.column = column;
			this.value  = value;
		}
	}
	
	@Autowired
	private HBaseStructure structure;
	
	@Autowired
	private HBaseTemplate hbase;
	
	/**
	 * Identifica a tabela de um objeto
	 * @return
	 */
	private <T extends Identifiable> String getObjectTable(T object) {
		Class<?> clazz = object.getClass(); 
		if (clazz.isAnnotationPresent(NassauHTable.class)) {
			NassauHTable annotation = clazz.getAnnotation(NassauHTable.class);
			String table = annotation.name();
			return table;
		}
		return null;
	}
	
	/*
	 * HBase Template encapsulation methods
	 */
	/**
	 * Checks if a table exists
	 * @param table
	 * @return
	 */
	public boolean tableExists(String table) throws NassauException {
		return hbase.tableExists(table);
	}
	
	/**
     * Creates a table in HBase
     * @param table
     * @throws NassauException
     */
    public void createTable(String table, List<String> families) throws NassauException {
        hbase.createTable(table, families);
    }
    
    /**
     * Drops a table in HBase
     * @param table
     * @throws NassauException
     */
    public void dropTable(String table) throws NassauException {
        hbase.dropTable(table);
    }
    
    /**
     * Executes a task in the HBase
     * @param executor
     * @throws NassauException
     */
    public void execute(HBaseExecutor executor) throws NassauException {
    	hbase.execute(executor);
    }
    
    /**
     * Puts an object into a table
     * @param table
     * @param object
     * @param mapper
     * @throws NassauException
     */
    public <T extends Identifiable> void put(T object) throws NassauException {
    	hbase.put(getObjectTable(object), object, this);
    }
    
    /**
     * Puts a list of objects into a table (Bulk insert)
     * @param table
     * @param object
     * @param mapper
     * @throws NassauException
     */
    public <T extends Identifiable> void put(List<T> objects) throws NassauException {
    	hbase.put(getObjectTable(objects.get(0)), objects, this); // TODO pelamordeDeus, previna null pointer exceptions
    }

    /**
     * Loads one object from HBase
     * 
     * @param table
     * @param row
     * @param mapper
     * @return
     * @throws NassauException
     */
    public <T extends Identifiable> T get(String table, String row) throws NassauException {
    	return hbase.get(table, row, this);
    }
	
	/*
	 * Mappers methods
	 */
	@Override
	public <T extends Identifiable> Put map(T object) throws Throwable {
		if (structure.getStructuralClasses().contains(object.getClass())) {
			Class<?> clazz = object.getClass();
			String id = "";
			List<Value> values = new ArrayList<Value>();
			
			// Mapeia o ID e as colunas
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(NassauHTableRowId.class)) {
					id = (String) field.get(object);
				}
				
				if (field.isAnnotationPresent(NassauHTableColumn.class)) {
					NassauHTableColumn annotation = field.getAnnotation(NassauHTableColumn.class);
					String value = (String) field.get(object);
					values.add(new Value(annotation.family().getBytes(), annotation.column().getBytes(), value.getBytes()));
				}
			}
			
			// Cria o objeto Put
			Put put = new Put(id.getBytes());
			for (Value value : values) {
				put.addColumn(value.family, value.column, value.value);
			}
			return put;
		}
		return null;
	}

	@Override
	public <T extends Identifiable> T map(Result result) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
}
