package com.nassau.br.nfe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.nassau.br"})
public class NFeBIApplication {
	
	/**
	 * Scheduler
	 */
	private static NFeBIModule module;
	
	/**
	 * Scheduler thread
	 */
	private static Thread thread;
	
	/**
	 * Autowire the scheduler
	 * @param scheduler
	 */
	@Autowired
	private void setScheduler(NFeBIModule module) {
		NFeBIApplication.module = module;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(NFeBIApplication.class);
		
		// Start the scheduler
		thread = new Thread(module);
		thread.start();
		
		// Add a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        try {
		        	module.stop();
					thread.join();
				} catch (InterruptedException e) {
					// TODO log
				}
		    }
		});
	}
}
