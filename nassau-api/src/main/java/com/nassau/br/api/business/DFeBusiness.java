package com.nassau.br.api.business;

import java.io.Serializable;

import com.nassau.br.DFe;

public interface DFeBusiness<T extends DFe> extends Serializable {
	/**
	 * Envia um documento fiscal eletronico para processamento
	 * @param dfe
	 * @throws Exception
	 */
	public void process(T dfe) throws Exception;
}
