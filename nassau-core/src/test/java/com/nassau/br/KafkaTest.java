package com.nassau.br;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nassau.br.hbase.HBaseConfig;
import com.nassau.br.kafka.KafkaConfiguration;
import com.nassau.br.kafka.SerializedDFeConsumer;
import com.nassau.br.kafka.SerializedDFeMessageListener;
import com.nassau.br.kafka.SerializedDFeProducer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HBaseConfig.class, KafkaConfiguration.class})
public class KafkaTest {
	
	@Autowired
	private SerializedDFeConsumer consumer;
	
	
	@Autowired
	private SerializedDFeProducer producer;
	
	@Test
	public void testSend() throws Exception {
		SerializedDFe dfe = new SerializedDFe("Teste");
		producer.send("incoming.dfe", dfe);
		assertEquals("O item não foi enfileirado.", true, true);
	}
	
	private Integer count = 0;
	
	@Test
	public void testConsumer() throws Exception {
		count = 0;
		consumer.addMessageListener(new SerializedDFeMessageListener() {
			@Override
			public void onMessage(String topic, SerializedDFe dfe) throws Exception {
				count++;
			}
		});
		consumer.subscribe(Arrays.asList("incoming.dfe"));
		
		for (int i = 0; i < 10; i++) {
			producer.send("incoming.dfe", new SerializedDFe(String.format("DFe %d", i)));
			Thread.sleep(10000);
		}
		
		assertEquals("Nem todas as mensagens foram recebidas.", (Integer)10, count);
	}
}
