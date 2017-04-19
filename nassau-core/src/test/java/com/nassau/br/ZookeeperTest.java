package com.nassau.br;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nassau.br.module.Module;
import com.nassau.br.zookeeper.ZookeeperConfig;
import com.nassau.br.zookeeper.ZookeeperTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ZookeeperConfig.class})
public class ZookeeperTest {
	@Autowired
	private ZookeeperTemplate zookeeper;
	
	@Test
	public void testZookeeper() {
		Module module = new Module("/nfe", "nfe.bi");
		zookeeper.register(module);
		assertEquals("O topico nao foi criado.", zookeeper.exists(module.getZNode()), true);
		zookeeper.unregister(module);
		assertEquals("O topico nao foi apagado.", zookeeper.exists(module.getZNode()), false);
	}
}
