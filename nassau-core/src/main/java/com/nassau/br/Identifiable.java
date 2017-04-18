package com.nassau.br;

import java.io.Serializable;

/**
 * Todo objeto do modelo deve ser identificável
 * @author fabio
 */
public interface Identifiable extends Serializable {
	/**
	 * O método id deve retornar um identificador único para cada
	 * um dos objetos DFe instânciados.
	 *  
	 * @return
	 */
	public String id();
}
