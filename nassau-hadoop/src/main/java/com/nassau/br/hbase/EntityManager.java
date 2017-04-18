package com.nassau.br.hbase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.annotations.NassauHTable;
import com.nassau.br.annotations.NassauHTableColumn;
import com.nassau.br.annotations.NassauHTableRowId;
import com.nassau.br.exceptions.NassauException;

/**
 * HBase Entity Manager 
 * OBS.:
 * 	Por enquanto, essa classe só serializa e deserializa objetos do tipo String.
 * 	Um trabalho futuro interessante é serializar e deserializar objetos do tipo:
 * 		Integer, Long, Double e Float.
 * 
 * @author fsantos
 */
@Component
public class EntityManager implements PutMapper, RowMapper {
	/**
	 * The value class
	 * @author fabio
	 */
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
	private String getClassTable(Class<?> clazz) {
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
    public <T> void put(T object) throws NassauException {
    	hbase.put(getClassTable(object.getClass()), object, this);
    }
    
    /**
     * Puts a list of objects into a table (Bulk insert)
     * @param table
     * @param object
     * @param mapper
     * @throws NassauException
     */
    public <T> void put(List<T> objects) throws NassauException {
    	hbase.put(getClassTable(objects.get(0).getClass()), objects, this); // TODO pelamordeDeus, previna null pointer exceptions
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
    public <T> T get(Class<?> clazz, String row) throws NassauException {
    	return hbase.get(getClassTable(clazz), row, this);
    }
	
	/*
	 * Mappers methods
	 */
	@Override
	public <T> Put map(T object) throws Throwable {
		if (structure.getStructuralClasses().contains(object.getClass())) {
			Class<?> clazz = object.getClass();
			String id = null;
			List<Value> values = new ArrayList<Value>();
			
			// Mapeia o ID e as colunas
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(NassauHTableRowId.class)) {
					id = (String) field.get(object);
				}
				
				if (field.isAnnotationPresent(NassauHTableColumn.class)) {
					NassauHTableColumn annotation = field.getAnnotation(NassauHTableColumn.class);
					String value = (String) field.get(object);
					values.add(new Value(annotation.family().getBytes(), annotation.column().getBytes(), value.getBytes()));
				}
			}
			
			// Check if there is an id field. Mandatory.
			if (id == null)
				throw new NassauException(); // TODO Improve this 
			
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
	public <T> T map(String table, Result result) throws Throwable {
		if (table != null && result != null) {
			Class<?> clazz = null;
			for (Class<?> structureClass : structure.getStructuralClasses()) {
				if (structureClass.isAnnotationPresent(NassauHTable.class) && table.equals(structureClass.getAnnotation(NassauHTable.class).name())) {
					clazz = structureClass;
					break;
				}
			}
			
			// A Class is mandatory.
			if (clazz == null)
				throw new NassauException(); // TODO Improve this
			
			// Fields
			@SuppressWarnings("unchecked")
			T object = (T) clazz.newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(NassauHTableRowId.class)) {
					field.set(object, new String(result.getRow()));
				}
				
				if (field.isAnnotationPresent(NassauHTableColumn.class)) {
					NassauHTableColumn annotation = field.getAnnotation(NassauHTableColumn.class);
					byte[] value = result.getValue(annotation.family().getBytes(), annotation.column().getBytes());
					field.set(object, new String(value));
				}
			}
			
			return object;
		}
		return null;
	}
}
