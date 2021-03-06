package com.nassau.br;

import java.util.Arrays;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nassau.br.hbase.HBaseConfig;
import com.nassau.br.hbase.HBaseTemplate;
import com.nassau.br.hbase.PutMapper;
import com.nassau.br.hbase.RowMapper;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HBaseConfig.class})
public class HBaseTest {
	@Autowired
	private HBaseTemplate hbase;
	
	@Test
	public void testCreateTable() throws Exception {
		String table = "dfe.tests";
		if (hbase.tableExists(table))
			hbase.dropTable(table);
		
		hbase.createTable(table, Arrays.asList(new String[] {"document"}));

		SerializedDFe dfe = new SerializedDFe("Test data");
		hbase.put(table, dfe, new PutMapper() {
			@Override
			public <T> Put map(T object) throws Throwable {
				SerializedDFe dfei = (SerializedDFe) object;
				Put entry = new Put(dfei.id().getBytes());
				entry.addColumn("document".getBytes(), "dfe".getBytes(), dfei.getSerializedData().getBytes());
				return entry;
			}
		});
		
		SerializedDFe loaded = hbase.get(table, dfe.id(), new RowMapper() {
			@SuppressWarnings("unchecked")
			@Override
			public SerializedDFe map(String table, Result result) throws Throwable {
				String id 			= Bytes.toString(result.getRow());
				String serialized 	= Bytes.toString(result.getValue("document".getBytes(), "dfe".getBytes()));
				return new SerializedDFe(id, serialized);
			}
		});
		
		hbase.dropTable(table);
		
		assertEquals("Os ids nao casam.", dfe.id(), loaded.id());
	}
}
