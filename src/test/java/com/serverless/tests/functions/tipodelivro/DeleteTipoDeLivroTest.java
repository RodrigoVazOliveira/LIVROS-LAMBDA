package com.serverless.tests.functions.tipodelivro;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.tipodelivro.DeleteTipoDeLivro;
import com.serverless.services.TipoDeLivroService;

@ExtendWith(MockitoExtension.class)
public class DeleteTipoDeLivroTest {

    @Mock
    private TipoDeLivroService tipoDeLivroService;

    private Map<String, Object> input;

    @Mock
    private Context context;

    @InjectMocks
    private DeleteTipoDeLivro deleteTipoDeLivro;

    @BeforeEach
    public void setup() {
        this.input = new HashMap<>();
    }

    @Test
    public void testarDeleteTipoDeLivroComSucesso() {
        doNothing().when(this.tipoDeLivroService).deleteTipoDeLivro(anyString());
        this.input.put("body", "{\"id\": \"1\"}");
        ApiGatewayResponse responseActual = this.deleteTipoDeLivro.handleRequest(this.input, this.context);
        ApiGatewayResponse responseExpect = construirRespostaComSucesso();

    	verificarAcertos(responseExpect, responseActual);
    }

    @Test
    public void testarDeleteTipoDeLivroComErro() {
        doThrow(new RuntimeException("não existe um tipo de livro com id 1"))
                .when(this.tipoDeLivroService).deleteTipoDeLivro(anyString());
        this.input.put("body", "{\"id\": \"1\"}");
        ApiGatewayResponse responseActual = this.deleteTipoDeLivro.handleRequest(this.input, this.context);
        ApiGatewayResponse responseExpect = construirRepostaComError();

        verificarAcertos(responseExpect, responseActual);
    }
    
    private void verificarAcertos(ApiGatewayResponse responseExpect, ApiGatewayResponse responseActual) {
        Assertions.assertEquals(responseExpect.getStatusCode(), responseActual.getStatusCode());
        Assertions.assertEquals(responseExpect.getBody(), responseActual.getBody());
        Assertions.assertEquals(responseExpect.getHeaders(), responseActual.getHeaders());
    }
    
    private ApiGatewayResponse construirRespostaComSucesso() {
    	return ApiGatewayResponse.builder()
                .setObjectBody(new Response("tipo de livro deletado!", this.input))
                .setStatusCode(204)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
    
    private ApiGatewayResponse construirRepostaComError() {
    	return ApiGatewayResponse.builder()
                .setObjectBody(new Response("não existe um tipo de livro com id 1", this.input))
                .setStatusCode(400)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
}
