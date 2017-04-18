package com.nassau.br;

/**
 * Esta classe representa uma Nota Fiscal Eletrônica (NF-e).
 * 
 * @author fabio
 */
public class NFe extends DFe {
	private static final long serialVersionUID = 81481963057434620L;

	/**
	 * A chave de acesso de uma NF-e
	 */
	public String chave;
	
	/**
	 * Construtor padrão
	 */
	public NFe() {
		super();
	}
	
	/**
	 * Construtor de cópia
	 */
	public NFe(NFe nfe) {
		super(nfe);
		chave = nfe.chave;
	}
	
	/**
	 * O identificador único de uma NF-e é a sua chave de acesso.
	 */
	@Override
	public String id() {
		return chave;
	}

}
