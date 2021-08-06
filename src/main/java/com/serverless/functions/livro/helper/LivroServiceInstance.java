package com.serverless.functions.livro.helper;

import com.serverless.services.LivroService;

public class LivroServiceInstance {
	
	public static LivroService getInstance(LivroService livroService) {
		if (livroService == null) {
			return new LivroService();
		}
		
		return livroService;
	}
}
