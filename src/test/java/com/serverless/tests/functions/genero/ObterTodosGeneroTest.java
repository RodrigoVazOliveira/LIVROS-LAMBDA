package com.serverless.tests.functions.genero;

import static org.mockito.Mockito.lenient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.genero.ObterGenero;
import com.serverless.helper.ObjectMapperProxy;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;

@ExtendWith(MockitoExtension.class)
public class ObterTodosGeneroTest {

    @Mock
    private GeneroService generoService;

    @Mock
    private Logger Log;

    @Mock
    private Context context;

    @InjectMocks
    private ObterGenero obterGenero;
    
    @Mock
    private Iterable<Genero> generos;
    
    private Map<String, Object> input;
    
    private String mensagemRetorno;
    
    private final ObjectMapper objectMapper = ObjectMapperProxy.getObjectMapper();
    
    @BeforeEach
    public void setup() {
    	this.input = new HashMap<String, Object>();
    	this.mensagemRetorno = null;
    }

    @Test
    public void testarObterTodosGenerosComSucesso()  {
        lenient().when(this.generoService.obterTodosGeneros()).thenReturn(this.generos);
    	converterListaDeGenerosParaJson();
        ApiGatewayResponse responseExpect = construirRespostaComSucesso();
        ApiGatewayResponse responseActual = this.obterGenero.handleRequest(input, this.context);

    	verificarAcertos(responseExpect, responseActual);
    }
    
    private void converterListaDeGenerosParaJson() {
    	try {
			this.mensagemRetorno = this.objectMapper.writeValueAsString(this.generos);
		} catch (JsonProcessingException e) {
			this.mensagemRetorno = e.getMessage();
		}
    }
    
    private void verificarAcertos(ApiGatewayResponse responseExpect, ApiGatewayResponse responseActual) {
        Assertions.assertEquals(responseExpect.getHeaders(), responseActual.getHeaders());
        Assertions.assertEquals(responseExpect.getStatusCode(), responseActual.getStatusCode());
        Assertions.assertEquals(responseExpect.getBody(), responseActual.getBody());
    }
    
    private ApiGatewayResponse construirRespostaComSucesso() {
    	return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response(this.mensagemRetorno, this.input))
                .build();
    }
}
