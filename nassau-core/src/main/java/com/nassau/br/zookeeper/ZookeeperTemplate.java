package com.nassau.br.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import com.nassau.br.module.Module;

/**
 * Interface entre o Nassau e o Zookeeper
 * @author fsantos
 */
public class ZookeeperTemplate implements IZkChildListener {
	/*
	 * Constants
	 */
	private static String NASSAU_ROOT = "/nassau";
	
	/**
	 * O conjunto de modulos registrados
	 */
	private Set<Module> modules = new CopyOnWriteArraySet<Module>();
	
	/**
	 * Zookeeper object
	 */
	private ZkClient zookeeper;
	
	/**
	 * Construtor
	 * @param quorum
	 */
	public ZookeeperTemplate(String quorum) {
		super();
		start(quorum);
	}
	
	/**
	 * Starts the client
	 */
	private void start(String quorum) {
		// Cria a conexao com o ZK 
		zookeeper = new ZkClient(new ZkConnection(quorum));
		
		// Check if the root znode exists
		if (!zookeeper.exists(NASSAU_ROOT)) zookeeper.createPersistent(NASSAU_ROOT);
		List<String> children = zookeeper.subscribeChildChanges(NASSAU_ROOT, this);
		updateRegisteredModules(children);
	}
	
	/**
	 * Monitora qualquer mudanca na lista de znodes monitorados
	 */
	@Override
	public void handleChildChange(String path, List<String> children) throws Exception {
		updateRegisteredModules(children);
	}
	
	/**
	 * Atualiza a lista de modulos
	 * @param children
	 */
	private void updateRegisteredModules(List<String> children) {
		// Verifica adicoes de modulos
		for (String child : children) {
			Module module = zookeeper.readData(NASSAU_ROOT + "/" + child);
			if (!modules.contains(child)) modules.add(module.setZNode(child));
		}
		
		// Verifica remocoes de modulos
		for (Module module : modules) {
			if (!children.contains(module.getZNode())) modules.remove(module);
		}
	}
	
	/**
	 * Checks if a znode exists under nassau root
	 * @param znode
	 * @return
	 */
	public boolean exists(String znode) {
		return zookeeper.exists(NASSAU_ROOT + znode);
	}
	
	/**
	 * Register a new nassau znode
	 * @param path
	 * @param data
	 */
	public void register(Module param) {
		Module module = new Module(param);
		String path = module.getZNode();
		if (!exists(path)) zookeeper.createPersistent(NASSAU_ROOT + path, module);
		modules.add(module);
	}
	
	/**
	 * Unregister a nassau znode
	 * @param path
	 */
	public void unregister(Module module) {
		String path = module.getZNode();
		if (exists(path)) zookeeper.delete(NASSAU_ROOT + path);
		modules.remove(path);
	}

	/**
	 * Gets a list of registered modules in nassau
	 * @return
	 */
	public List<Module> getRegisteredModules() {
		List<Module> list = new ArrayList<Module>();
		for (Module module : modules) {
			list.add(module);
		}
		return list;
	}
}
