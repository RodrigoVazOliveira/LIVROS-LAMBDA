package com.serverless.tests.functions.tipodelivro;

import static org.mockito.ArgumentMatchers.any;
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
import com.serverless.functions.tipodelivro.AtualizarTipoDeLivro;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;

;

@ExtendWith(MockitoExtension.class)
public class AtualizarTipoDeLivroTest {

    @Mock
    private TipoDeLivroService tipoDeLivroService;

    private AtualizarTipoDeLivro atualizarTipoDeLivro;

    private Map<String, Object> input;

    @Mock
    private Context context;

    private TipoDeLivro tipoDeLivro;

    @BeforeEach
    public void setup() {
        this.tipoDeLivro = new TipoDeLivro();
        this.tipoDeLivro.setId("1");
        this.tipoDeLivro.setNome("Capa dura");

        this.input = new HashMap<>();
        
        this.atualizarTipoDeLivro = new AtualizarTipoDeLivro();
        this.atualizarTipoDeLivro.setTipoDeLivroService(this.tipoDeLivroService);
    }

    @Test
    public void testarHandlerRequestComSucesso() {
        doNothing().when(this.tipoDeLivroService).atualizarTipoDeLivro(any());
        this.input.put("body", "{\"id\":\"1\", \"nome\":\"Capa dura\"}");
        ApiGatewayResponse respostaTest = this.atualizarTipoDeLivro.handleRequest(this.input, this.context);
        ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setObjectBody(new Response("Dados atualizado com sucesso! " + this.tipoDeLivro, input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();

        Assertions.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assertions.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
        Assertions.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
    }

    @Test
    public void testarHandlerRequestComError() {
        doThrow(new RuntimeException("não existe um tipo de livro com id 1"))
                .when(this.tipoDeLivroService).atualizarTipoDeLivro(any());
        this.input.put("body", "{\"id\":\"1\", \"nome\":\"Capa dura\"}");
        ApiGatewayResponse respostaTest = this.atualizarTipoDeLivro.handleRequest(this.input, this.context);
        ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                .setStatusCode(400)
                .setObjectBody(new Response("não existe um tipo de livro com id 1", this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();

        Assertions.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assertions.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
        Assertions.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
    }
}
