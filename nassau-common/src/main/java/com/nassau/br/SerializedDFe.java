package com.nassau.br;

import java.util.UUID;

/**
 * Um documento fiscal eletrônico serializado pode ser qualquer DF-e.
 * 
 * @author fabio
 */
public class SerializedDFe extends DFe {
	private static final long serialVersionUID = -4386655332794879952L;

	/**
	 * Identificador universal único de um documento fiscal eletrônico serializado.
	 */
	private String uuid;
	
	/**
	 * O DF-e serializado
	 */
	private String serialized;

	/**
	 * Construtor padrão
	 */
	public SerializedDFe() {
		super();
		uuid = UUID.randomUUID().toString().toUpperCase();
	}
	
	/**
	 * Construtor de cópia
	 * @param dfe
	 */
	public SerializedDFe(SerializedDFe dfe) {
		super(dfe);
		uuid 		= dfe.uuid;
		serialized 	= dfe.serialized;
	}
	
	/**
	 * Construtor de objeto único
	 * @param serialized
	 */
	public SerializedDFe(String serialized) {
		this();
		this.serialized = serialized;
	}
	
	/**
	 * Construtor de objeto único
	 * @param serialized
	 */
	public SerializedDFe(String uuid, String serialized) {
		super();
		this.uuid 		= uuid;
		this.serialized = serialized;
	}

	/**
	 * O identificador único de um DFe é o seu uuid.
	 */
	@Override
	public String id() {
		return uuid;
	}
	
	/**
	 * Retorna os valores serializados do DFe
	 * @return
	 */
	public String getSerializedData() {
		return serialized;
	}
}
