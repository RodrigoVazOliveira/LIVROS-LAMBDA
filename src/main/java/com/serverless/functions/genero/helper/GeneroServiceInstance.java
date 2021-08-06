package com.serverless.functions.genero.helper;

import com.serverless.services.GeneroService;

public class GeneroServiceInstance {
	
	public static GeneroService getInstance(GeneroService generoService) {
		if (generoService == null) {
			return new GeneroService();
		}
		
		return generoService;
	}
}
