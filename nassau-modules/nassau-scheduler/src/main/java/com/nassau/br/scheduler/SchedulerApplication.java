package com.nassau.br.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.nassau.br"})
public class SchedulerApplication {
	/**
	 * Scheduler
	 */
	private static DFeScheduler scheduler;
	
	/**
	 * Scheduler thread
	 */
	private static Thread thread;
	
	/**
	 * Autowire the scheduler
	 * @param scheduler
	 */
	@Autowired
	private void setScheduler(DFeScheduler scheduler) {
		SchedulerApplication.scheduler = scheduler;
	}
	
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class);
		
		// Start the scheduler
		thread = new Thread(scheduler);
		thread.start();
		
		// Add a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        try {
		        	scheduler.stop();
					thread.join();
				} catch (InterruptedException e) {
					// TODO log
				}
		    }
		});
	}
}
