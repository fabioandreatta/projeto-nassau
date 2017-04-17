package com.nassau.br.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nassau.br.SerializedDFe;
import com.nassau.br.api.business.SerializedDFeBusiness;
import com.nassau.br.api.response.Response;

@RestController
public class SerializedDFeController {
	
	@Autowired
	private SerializedDFeBusiness serializedDFeBusiness;

	// TODO We must configure the api documentation. I tried swagger, but it's not working anymore. 
    @RequestMapping(method = RequestMethod.POST, path="/process", produces = "application/json")
	public Response process(@RequestBody String serialized) {
    	try {
			serializedDFeBusiness.process(new SerializedDFe(serialized));
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(Response.Status.ERROR, e.getMessage());
		}
		return new Response(Response.Status.OK, "Documento encaminhado para processamento.");
	}
}
