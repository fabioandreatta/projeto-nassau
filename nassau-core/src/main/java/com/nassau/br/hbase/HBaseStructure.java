package com.nassau.br.hbase;

import java.util.List;

public class HBaseStructure {
	/**
	 * The structure classes
	 */
	private List<Class<?>> structuralClasses;
	
	/**
	 * Constructor
	 * @param structuralClasses
	 */
	public HBaseStructure(List<Class<?>> structuralClasses) {
		super();
		this.structuralClasses = structuralClasses;
	}

	public List<Class<?>> getStructuralClasses() {
		return structuralClasses;
	}

	public void setStructuralClasses(List<Class<?>> structuralClasses) {
		this.structuralClasses = structuralClasses;
	}
}
