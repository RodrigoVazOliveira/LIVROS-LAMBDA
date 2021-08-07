package com.serverless.functions.livro;

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
import com.serverless.models.Livro;
import com.serverless.services.LivroService;

public class DeleteLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private LivroService livroService;
	private Livro livro;
	private static final Logger LOG = LogManager.getLogger(DeleteLivro.class);
	private Map<String, Object> input;
	private ApiGatewayResponse response;

	public DeleteLivro() {
		livroService = new LivroService();
	}
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Iniciando processo de deletar o livro pelo id!");
		this.input = input;
		obterDadosInformado();
		executarProcessoDeDeletarLivro();

		return this.response;
	}
	
	private void obterDadosInformado() {
		LOG.info("Obtendo dado informado!");
		JsonNode body = GetInput.getBody(this.input);
		criarLivro(body);
	}
	
	private void criarLivro(JsonNode body) {
		LOG.info("Criando objeto livro para deletar");
		this.livro = new Livro();
		this.livro.setId(body.get("id").asText());
	}
	
	private void executarProcessoDeDeletarLivro() {
		LOG.info("Executar processo para deletar o livro no dynamoDB");
		try {
			this.livroService.deletaLivro(this.livro);
			buildResponse(204, "Livro deletado com sucesso!");
		} catch (RuntimeException e) {
			LOG.error("Ocorreu um erro ao tentar deletar o livro");
			buildResponse(400, e.getMessage());
		}
	}
	
	private void buildResponse(Integer statusCode, String mensagem) {
		LOG.info("Gerando resposta.....");
		this.response = ApiGatewayResponse.builder()
				.setStatusCode(statusCode)
				.setObjectBody(new Response(mensagem, this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
	}

	public void setLivroService(LivroService livroService) {
		this.livroService = livroService;
	}
}
