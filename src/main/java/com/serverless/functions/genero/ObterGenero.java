package com.serverless.functions.genero;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.helper.ObjectMapperProxy;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;

public class ObterGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(ObterGenero.class);
    private GeneroService generoService;
    private Map<String, Object> input;
    private String resultado;

    public ObterGenero() {
    	this.generoService = new GeneroService();
    }
    
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Iniciar processo para gerar lista de generos!");
        this.input = input;
        String mensagem = converterListaDeGeneroParaJson(this.generoService.obterTodosGeneros());
        LOG.info("Gerado lista de generos!");

        return criarResposta(mensagem);
    }

    private String converterListaDeGeneroParaJson(Iterable<Genero> generos) {
        try {
            this.resultado = ObjectMapperProxy.getObjectMapper().writeValueAsString(generos);
        } catch (JsonProcessingException e) {
            this.resultado = e.getMessage();
        }

        return this.resultado;
    }

    private ApiGatewayResponse criarResposta(String mensagem) {
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response(mensagem, this.input))
                .build();
    }

	public void setGeneroService(GeneroService generoService) {
		this.generoService = generoService;
	}
}
