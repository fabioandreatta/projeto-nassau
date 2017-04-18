package com.nassau.br;

import java.util.UUID;

import com.nassau.br.annotations.NassauHTable;
import com.nassau.br.annotations.NassauHTableColumn;
import com.nassau.br.annotations.NassauHTableRowId;

/**
 * Um documento fiscal eletrônico serializado pode ser qualquer DF-e.
 * 
 * @author fabio
 */
@NassauHTable(
	name = "serialized-dfe",
	families = {"document"}
)
public class SerializedDFe extends DFe {
	private static final long serialVersionUID = -4386655332794879952L;

	/**
	 * Identificador universal Único de um documento fiscal eletrônico serializado.
	 */
	@NassauHTableRowId
	private String uuid;
	
	/**
	 * O DF-e serializado
	 */
	@NassauHTableColumn(family = "document", column = "dfe")
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
	 * Construtor de objeto Ãºnico
	 * @param serialized
	 */
	public SerializedDFe(String serialized) {
		this();
		this.serialized = serialized;
	}
	
	/**
	 * Construtor de objeto Ãºnico
	 * @param serialized
	 */
	public SerializedDFe(String uuid, String serialized) {
		super();
		this.uuid 		= uuid;
		this.serialized = serialized;
	}

	/**
	 * O identificador Ãºnico de um DFe Ã© o seu uuid.
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
