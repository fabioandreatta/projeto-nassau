package com.nassau.br;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nassau.br.hbase.HBaseConfig;
import com.nassau.br.kafka.KafkaConfiguration;
import com.nassau.br.kafka.SerializedDFeProducer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HBaseConfig.class, KafkaConfiguration.class})
public class KafkaTest {
	@Autowired
	private SerializedDFeProducer producer;
	
	@Test
	public void testSend() throws Exception {
		SerializedDFe dfe = new SerializedDFe("Teste");
		producer.send("incoming.dfe", dfe);
		assertEquals("O item não foi enfileirado.", true, true);
	}
}
