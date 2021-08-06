package com.serverless.tests.functions.genero;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.genero.GravarGenero;
import com.serverless.services.GeneroService;

@ExtendWith(MockitoExtension.class)
public class GravarGeneroTest {

    @Mock
    private GeneroService generoService;

    @Mock
    private Logger log;

    @Mock
    private Context context;

    @InjectMocks
    private GravarGenero gravarGenero;
    
    private Map<String, Object> input;
    
    @BeforeEach
    public void setup() {
    	this.input = new HashMap<String, Object>();
    	this.gravarGenero = new GravarGenero();
    	this.gravarGenero.setGeneroService(this.generoService);
    }

    @Test
    public void testHandlerRequest() {
        Mockito.doNothing().when(generoService).cadastrarNovoGeneroDeLivro(Mockito.anyString());
        this.input.put("body", "{\"nome\": \"Comédia\"}");
        ApiGatewayResponse responseExpect = constuirRespostaComSucesso();
        ApiGatewayResponse responseActual = gravarGenero.handleRequest(this.input, context);

        verificarAcertos(responseExpect, responseActual);
    }
    
    private void verificarAcertos(ApiGatewayResponse responseExpect, ApiGatewayResponse responseActual) {
        Assertions.assertEquals(responseExpect.getStatusCode(), responseActual.getStatusCode());
        Assertions.assertEquals(responseExpect.getBody(), responseActual.getBody());
        Assertions.assertEquals(responseExpect.getHeaders(), responseActual.getHeaders());
    }
    
    private ApiGatewayResponse constuirRespostaComSucesso() {
    	return ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Gravação efetuada com sucesso!", this.input))
                .build();
    }
}
