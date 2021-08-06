package com.serverless.functions.tipodelivro.helper;

import com.serverless.services.TipoDeLivroService;

public class TipoDeLivroServiceInstance {
	
	public static TipoDeLivroService getInstance(TipoDeLivroService tipoDeLivroService) {
		if (tipoDeLivroService == null) {
			return new TipoDeLivroService();
		}
		
		return tipoDeLivroService;
	}
}
