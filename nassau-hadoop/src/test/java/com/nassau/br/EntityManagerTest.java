package com.nassau.br;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nassau.br.exceptions.NassauException;
import com.nassau.br.hbase.EntityManager;
import com.nassau.br.hbase.HBaseConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HBaseConfig.class})
public class EntityManagerTest {
	@Autowired
	private EntityManager em;
	
	@Test
	public void testEntityManager() throws NassauException {
		SerializedDFe dfe = new SerializedDFe("Serialized DFe Test Data");
		em.put(dfe);
		SerializedDFe loaded = em.get(SerializedDFe.class, dfe.id());
		
		assertEquals("Os ids nao casam.", dfe.id(), loaded.id());
	}
}
