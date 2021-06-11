package com.serverless.tests.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.tipodelivro.ObterTodosTipoDeLivro;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObterTodosTipoDeLivroTest {

    @Mock
    private TipoDeLivroService tipoDeLivroService;

    @Mock
    private Context context;

    @InjectMocks
    private ObterTodosTipoDeLivro        obterTodosTipoDeLivro;

    @Mock
    private Iterable<TipoDeLivro> tipoDeLivros;

    private Map<String, Object> input;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.input = new HashMap<>();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testarhandleRequest() {
        when(this.tipoDeLivroService.obterTodosTipoDeLivro()).thenReturn(this.tipoDeLivros);

        try {
            ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(new Response(this.objectMapper.writeValueAsString(this.tipoDeLivros), input))
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
            ApiGatewayResponse respostaTest = this.obterTodosTipoDeLivro.handleRequest(this.input, this.context);

            Assert.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
            Assert.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
            Assert.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
