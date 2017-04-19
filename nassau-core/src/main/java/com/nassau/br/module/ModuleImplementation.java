package com.nassau.br.module;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.nassau.br.zookeeper.ZookeeperTemplate;

public abstract class ModuleImplementation extends Module implements Serializable, Runnable {
	private static final long serialVersionUID = -5390983990037888805L;

	/**
	 * Zookeeper
	 */
	@Autowired
	private ZookeeperTemplate zookeeper;
	
	/**
	 * Running flag
	 */
	private Boolean running = false;
	
	/**
	 * Construtor
	 * @param kafkaTopic
	 * @param zNode
	 */
	public ModuleImplementation(String zNode, String kafkaTopic) {
		super(zNode, kafkaTopic);
	}
	
	/**
	 * Runs
	 */
	@Override
	public void run() {
		try {
			// Registrando no Zk
			zookeeper.register(this);
			
			// Executa o que precisa
			process();
		} catch(Exception e) {
			// TODO log
		}
		waitTermination();
	}
	
	public abstract void process() throws Exception;
	
	/**
	 * Waits for termination
	 */
	public void waitTermination() {
		// Holds until get stopped
		running = true;
		while(running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO log
			}
		}
	}
	
	/**
	 * Stops the scheduler
	 */
	public void stop() {
		synchronized (running) {
			running = false;
		}
	}
}
