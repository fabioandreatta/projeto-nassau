package com.nassau.br;

/**
 * Os Documentos Fiscais Eletrônicos (DF-e) são o principal objetos de processamento
 * do Projeto Nassau. Todos esses elementos devem implementar a classe DFe.
 *  
 * @author fabio
 */
public abstract class DFe implements Identifiable {
	private static final long serialVersionUID = -1714287269368808581L;
	
	/**
	 * Default constructor
	 */
	public DFe() {
		super();
	}
	
	/**
	 * Copy constructor
	 * @param dfe
	 */
	public DFe(DFe dfe) {
		super();
	}
}
