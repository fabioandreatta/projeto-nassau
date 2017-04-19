package com.nassau.br.module;

import java.io.Serializable;

/**
 * A classe base de todos os modulos do projeto nassau.
 * 
 * @author fsantos
 */
public class Module implements Serializable {
	private static final long serialVersionUID = -7529833984935119735L;

	/**
	 * O Topico do Kafka por onde os DF-es chegarao ate o modulo
	 */
	private String kafkaTopic;
	
	/**
	 * O caminho do Zookeeper deste modulo 
	 */
	private String zNode;
	
	/**
	 * Construtor
	 * @param kafkaTopic
	 * @param zNode
	 */
	public Module() {
		super();
	}
	
	/**
	 * Construtor
	 * @param kafkaTopic
	 * @param zNode
	 */
	public Module(String zNode, String kafkaTopic) {
		super();
		this.kafkaTopic = kafkaTopic;
		this.zNode 		= zNode;
	}
	
	/**
	 * Construtor de cópia
	 * @param module
	 */
	public Module(Module module) {
		super();
		this.kafkaTopic = module.kafkaTopic;
		this.zNode		= module.zNode;
	}

	public String getKafkaTopic() {
		return kafkaTopic;
	}
	
	public Module setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
		return this;
	}

	public String getZNode() {
		return zNode;
	}
	
	public Module setZNode(String znode) {
		this.zNode = znode;
		return this;
	}
	
	@Override
    public int hashCode() {
        return zNode.hashCode();
    }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Module) {
			Module module = (Module) object;
			return zNode.equals(module.zNode);
		}
		return false;
	}
}
