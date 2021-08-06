package com.serverless.tests.functions.genero;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.genero.DeleteGenero;
import com.serverless.services.GeneroService;

@ExtendWith(MockitoExtension.class)
public class DeleteGeneroTest {

    @Mock
    private GeneroService generoService;

    @Mock
    private Context context;

    private DeleteGenero deleteGenero;

    private Map<String, Object> input;
    
    @BeforeEach
    public void setup() {
    	this.input = new HashMap<String, Object>();
    	this.deleteGenero = new DeleteGenero();
    	this.deleteGenero.setGeneroService(this.generoService);
    }
    
    @Test
    public void testarDeleteGeneroComSucesso() {
        doNothing().when(this.generoService).deletarGenero(anyString());
        this.input.put("body", "{\"id\":\"4242\"}");
        ApiGatewayResponse responseExpect = construirRespostaComSucesso();
        ApiGatewayResponse responseActual = this.deleteGenero.handleRequest(this.input, this.context);

        verificarAcertos(responseExpect, responseActual);
    }

    @Test
    public void testarDeleteGeneroComErro() {
        doThrow(new RuntimeException("Não foi localziado nenhum genero com id 1"))
                .when(this.generoService).deletarGenero(anyString());
        this.input.put("body", "{\"id\":\"4242\"}");
        ApiGatewayResponse responseExpect = construirRespostaComErro();
        ApiGatewayResponse responseActual = this.deleteGenero.handleRequest(this.input, this.context);

        verificarAcertos(responseExpect, responseActual);
    }
    
    private void verificarAcertos(ApiGatewayResponse responseExpect, ApiGatewayResponse responseActual) {
        Assertions.assertEquals(responseExpect.getStatusCode(), responseActual.getStatusCode());
        Assertions.assertEquals(responseExpect.getBody(), responseActual.getBody());
        Assertions.assertEquals(responseExpect.getHeaders(), responseActual.getHeaders());
    }
    
    private ApiGatewayResponse construirRespostaComSucesso() {
    	return ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("O genero com id 4242 foi excluido!", this.input))
                .setStatusCode(204)
                .build();
    }
    
    private ApiGatewayResponse construirRespostaComErro() {
    	return ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Não foi localziado nenhum genero com id 1", this.input))
                .setStatusCode(400)
                .build();
    }
}
