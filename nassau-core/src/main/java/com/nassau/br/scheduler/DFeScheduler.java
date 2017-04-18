package com.nassau.br.scheduler;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nassau.br.kafka.SerializedDFeProducer;
import com.nassau.br.zookeeper.ZookeeperTemplate;

/**
 * O Scheduler é responsável por redirecionar todos os DFe que estão chegando
 * para os modulos registrados.
 * 
 * @author fsantos
 */
@Component
public class DFeScheduler implements Serializable {
	private static final long serialVersionUID = 6494001910087796121L;

	@Autowired
	private ZookeeperTemplate zookeeper;
	
	@Autowired
	private SerializedDFeProducer producer;
}
