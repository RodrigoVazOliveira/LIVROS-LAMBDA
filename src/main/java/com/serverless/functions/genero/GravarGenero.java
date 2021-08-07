package com.serverless.functions.genero;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.helper.GetInput;
import com.serverless.services.GeneroService;

public class GravarGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private GeneroService generoService;
	private static final Logger LOG = LogManager.getLogger(GravarGenero.class);
	private String nome;

	public GravarGenero() {
		this.generoService = new GeneroService();
	}
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Gravando o novo genero no dynamoDB");
		getData(input);
		this.generoService.cadastrarNovoGeneroDeLivro(this.nome);
		LOG.info("Gravação efetuada com sucesso!");

		return ApiGatewayResponse.builder().setStatusCode(201)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.setObjectBody(new Response("Gravação efetuada com sucesso!", input)).build();
	}

	private void getData(Map<String, Object> input) {
		JsonNode body = GetInput.getBody(input);
		this.nome = body.get("nome").asText();
	}

	public void setGeneroService(GeneroService generoService) {
		this.generoService = generoService;
	}
}