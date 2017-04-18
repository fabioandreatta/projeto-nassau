package com.nassau.br.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

import com.nassau.br.exceptions.NassauException;

/**
 * HBaseHelper implements a singleton because connection is a high-weight operation, and we want to avoid such operations.
 *  
 * @author fabio.andreatta
 */
public class HBaseTemplate {
	/**
	 * Configurations
	 */
	private Configuration configuration;
	
	/**
	 * Connection to HBase
	 */
	private Connection connection;
	
	/**
	 * Singleton methods
	 * @throws GBSClusterException 
	 */
	public HBaseTemplate(Integer timeout, String zparent, String quorum, String port) throws NassauException {
		super();
		configure(timeout, zparent, quorum, port);
	}
	
	/**
	 * Connects to HBase
	 * @param quorum
	 * @param port
	 * @throws GBSClusterException
	 */
	private void configure(Integer timeout, String zparent, String quorum, String port) throws NassauException {
		configuration = HBaseConfiguration.create();
		
		configuration.setInt(HBaseConfig.HBASE_CONFIGURATION_TIMEOUT, 				timeout);
		configuration.set(HBaseConfig.HBASE_CONFIGURATION_ZOOKEEPER_ZNODE_PARENT, 	zparent);
		configuration.set(HBaseConfig.HBASE_CONFIGURATION_ZOOKEEPER_QUORUM, 		quorum);
		configuration.set(HBaseConfig.HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT, 	port);
		
		try {
			HBaseAdmin.checkHBaseAvailable(configuration);
		} catch (Exception e) {
			throw new NassauException(); // TODO Improve this
		}
		
		try {
			connection = ConnectionFactory.createConnection(configuration);
		} catch (Exception e) {
			throw new NassauException(); // TODO Improve this
		}
	}
	
	/**
	 * Checks if a table exists
	 * @param table
	 * @return
	 */
	public boolean tableExists(String table) throws NassauException {
		try {
        	Admin admin = connection.getAdmin();
        	return admin.tableExists(TableName.valueOf(table)); 
        } catch (IOException e) {
            throw new NassauException(); // TODO Improve this
        }
	}
	
	/**
     * Creates a table in HBase
     * @param table
     * @throws NassauException
     */
    public void createTable(String table, List<String> families) throws NassauException {
        try {
        	Admin admin = connection.getAdmin();
        	if (admin.tableExists(TableName.valueOf(table))) return; 
        	
            HTableDescriptor htable = new HTableDescriptor(TableName.valueOf(table));
            for (String family : families) 
            	htable.addFamily(new HColumnDescriptor(family));
            
            admin.createTable(htable);
        } catch (IOException e) {
            throw new NassauException(); // TODO Improve this
        }
    }
    
    /**
     * Drops a table in HBase
     * @param table
     * @throws NassauException
     */
    public void dropTable(String table) throws NassauException {
        try {
        	Admin admin = connection.getAdmin();
        	if (!admin.tableExists(TableName.valueOf(table))) return; 
            admin.disableTable(TableName.valueOf(table));
            admin.deleteTable(TableName.valueOf(table));
        } catch (IOException e) {
            throw new NassauException(); // TODO Improve this
        }
    }
    
    /**
     * Executes a task in the HBase
     * @param executor
     * @throws NassauException
     */
    public void execute(HBaseExecutor executor) throws NassauException {
    	try {
    		if (executor != null) 
    			executor.execute(connection.getAdmin());
    	} catch (IOException e) {
            throw new NassauException(); // TODO Improve this
        }
    }
    
    /**
     * Puts an object into a table
     * @param table
     * @param object
     * @param mapper
     * @throws NassauException
     */
    public <T> void put(String table, T object, PutMapper mapper) throws NassauException {
    	try {
    		Table htable = connection.getTable(TableName.valueOf(table));
    		Put entry = mapper.map(object);
    		htable.put(entry);
    		htable.close();
    	} catch (Throwable e) {
    		throw new NassauException(e); // TODO Improve this
    	}
    }
    
    /**
     * Puts a list of objects into a table (Bulk insert)
     * @param table
     * @param object
     * @param mapper
     * @throws NassauException
     */
    public <T> void put(String table, List<T> objects, PutMapper mapper) throws NassauException {
    	try {
    		List<Put> entries = new ArrayList<Put>(0);
    		Table htable = connection.getTable(TableName.valueOf(table));
    		for (T object : objects) {
    			Put entry = mapper.map(object);
    			if (entry != null)
    				entries.add(entry);
    		}
    		htable.put(entries);
    		htable.close();
    	} catch (Throwable e) {
    		throw new NassauException(); // TODO Improve this
    	}
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
    public <T> T get(String table, String row, RowMapper mapper) throws NassauException {
    	try {
    		Table htable = connection.getTable(TableName.valueOf(table));
    		Result result = htable.get(new Get(row.getBytes()));
    		T object = mapper.map(table, result);
    		htable.close();
    		return object;
    	} catch (Throwable e) {
    		throw new NassauException(); // TODO Improve this
    	}
    }
}
