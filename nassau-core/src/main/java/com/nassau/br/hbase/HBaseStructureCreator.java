package com.nassau.br.hbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nassau.br.annotations.NassauHTable;
import com.nassau.br.exceptions.NassauException;
import com.nassau.br.reflection.PackageClassLoader;

@Configuration
public class HBaseStructureCreator {
	
	@Autowired
	private HBaseTemplate hbase;
	
	@Bean
	public HBaseStructure createStructure() {
		// TODO isso devia ser configurável :)
		List<Class<?>> classes = PackageClassLoader.find("com.nassau.br"); 
		List<Class<?>> structuralClasses = new ArrayList<Class<?>>();
		for (Class<?> clazz : classes) {
			if (clazz.isAnnotationPresent(NassauHTable.class)) {
				NassauHTable annotation = clazz.getAnnotation(NassauHTable.class);
				String table = annotation.name();
				List<String> families = Arrays.asList(annotation.families());
				
				try {
					if (!hbase.tableExists(table)) {
						hbase.createTable(table, families);
					}
					// TODO Devemos melhorar isso. Se a tabela ja existe, entao temos que ver 
					// se todas as familias estao presentes. 
				} catch (NassauException e) {
					// TODO Log
				}
				
				structuralClasses.add(clazz);
			}
		}
		return new HBaseStructure(structuralClasses);
	}
}
