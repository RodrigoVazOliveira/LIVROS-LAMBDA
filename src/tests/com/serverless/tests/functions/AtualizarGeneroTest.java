package com.serverless.tests.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.genero.AtualizarGenero;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith({MockitoExtension.class})
public class AtualizarGeneroTest {

    @Mock
    private GeneroService generoService;

    @Mock
    private Logger log;

    @Mock
    private Context context;

    @InjectMocks
    private AtualizarGenero atualizarGenero;

    @BeforeEach
    public void setup() {
        this.atualizarGenero = new AtualizarGenero(this.generoService);
    }

    @Test
    public void testarAtualizacaoDeGeneroComSucesso() {
        Mockito.lenient().doNothing().when(this.generoService).atualizarGenero(Mockito.any());

        Map<String, Object> input = new HashMap<>();
        input.put("Id", "2");
        input.put("nome", "Ficção cientifica");

        ApiGatewayResponse responseEsperada =  ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Genero com id 2 atualizado com sucesso!", input))
                .build();

        ApiGatewayResponse respostaTest = this.atualizarGenero.handleRequest(input, this.context);

        Assert.assertEquals(responseEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assert.assertEquals(responseEsperada.getBody(), respostaTest.getBody());
        Assert.assertEquals(responseEsperada.getHeaders(), respostaTest.getHeaders());
    }

    @Test
    public void testarAtualizacaoDeGeneroComError() {
        Mockito.doThrow(new RuntimeException("Não foi localziado nenhum genero com id 2"))
                .when(this.generoService).atualizarGenero(Mockito.any());

        Map<String, Object> input = new HashMap<>();
        input.put("Id", "2");
        input.put("nome", "Ficção cientifica");

        ApiGatewayResponse responseEsperada =  ApiGatewayResponse.builder()
                .setStatusCode(400)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Não foi localziado nenhum genero com id 2", input))
                .build();

        ApiGatewayResponse respostaTest = this.atualizarGenero.handleRequest(input, this.context);

        Assert.assertEquals(responseEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assert.assertEquals(responseEsperada.getBody(), respostaTest.getBody());
        Assert.assertEquals(responseEsperada.getHeaders(), respostaTest.getHeaders());
    }
}
