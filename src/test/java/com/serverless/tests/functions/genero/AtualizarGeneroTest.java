package com.serverless.tests.functions.genero;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.genero.AtualizarGenero;
import com.serverless.services.GeneroService;

@ExtendWith({MockitoExtension.class})
public class AtualizarGeneroTest {

    @Mock
    private GeneroService generoService;

    @Mock
    private Logger log;

    @Mock
    private Context context;
    
    private Map<String, Object> input;

    private AtualizarGenero atualizarGenero;
    
    @BeforeEach
    public void setup() {
    	this.input = new HashMap<String, Object>();
    	this.atualizarGenero = new AtualizarGenero();
    	this.atualizarGenero.setGeneroService(this.generoService);
    }
    

    @Test
    public void testarAtualizacaoDeGeneroComSucesso() {
        Mockito.lenient().doNothing().when(this.generoService).atualizarGenero(Mockito.any());
        this.input.put("body", "{\"id\":\"2\", \"nome\":\"Ficção Cientifica\"}");
        ApiGatewayResponse responseExpect = construirMensagemDeSucesso(); 
        ApiGatewayResponse responseActual = this.atualizarGenero.handleRequest(this.input, this.context);

        verificarAcertos(responseExpect, responseActual);
    }

    @Test
    public void testarAtualizacaoDeGeneroComError() {
        Mockito.doThrow(new RuntimeException("Não foi localziado nenhum genero com id 2"))
                .when(this.generoService).atualizarGenero(Mockito.any());
        this.input.put("body", "{\"id\":\"2\", \"nome\":\"Ficção Cientifica\"}");
        ApiGatewayResponse responseExpect =  construirMensagemDeError();
        ApiGatewayResponse responseActual = this.atualizarGenero.handleRequest(this.input, this.context);

    	verificarAcertos(responseExpect, responseActual);
    }
    
    public void verificarAcertos(ApiGatewayResponse responseExpect, ApiGatewayResponse responseActual) {
        Assertions.assertEquals(responseExpect.getStatusCode(), responseActual.getStatusCode());
        Assertions.assertEquals(responseExpect.getBody(), responseActual.getBody());
        Assertions.assertEquals(responseExpect.getHeaders(), responseActual.getHeaders());
    }
    
    private ApiGatewayResponse construirMensagemDeSucesso() {
    	return ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Genero com id 2 atualizado com sucesso!", this.input))
                .build();
    }
    
    private ApiGatewayResponse construirMensagemDeError() {
    	return ApiGatewayResponse.builder()
                .setStatusCode(400)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Não foi localziado nenhum genero com id 2", this.input))
                .build();
    }
}
