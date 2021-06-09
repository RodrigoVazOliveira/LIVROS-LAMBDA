package com.serverless.tests.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.genero.ObterGenero;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void testarObterTodosGenerosComSucesso()  {
        Iterable<Genero> generos = Mockito.mock(Iterable.class);
        Mockito.lenient().when(this.generoService.obterTodosGeneros()).thenReturn(generos);

        ObjectMapper objectMapper = new ObjectMapper();
        String mensagem = null;
        try {
            mensagem = objectMapper.writeValueAsString(generos);
        } catch (JsonProcessingException e) {
            mensagem = e.getMessage();
        }
        Map<String, Object> input = new HashMap<>();

        ApiGatewayResponse respostaEsperada =  ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response(mensagem, input))
                .build();

        ApiGatewayResponse respostaTest = this.obterGenero.handleRequest(input, this.context);

        Assert.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
        Assert.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assert.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
    }
}
