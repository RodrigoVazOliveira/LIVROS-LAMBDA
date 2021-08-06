package com.serverless.tests.functions.livro;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.livro.ObterTodosOsLivros;
import com.serverless.models.Livro;
import com.serverless.services.LivroService;

@ExtendWith(MockitoExtension.class)
public class ObterTodosOsLivrosTest {

    @Mock
    private LivroService  livroService;

    @Mock
    private Context context;

    private Map<String, Object> input;

    private List<Livro> livros;

    private ObjectMapper objectMapper;

    private ObterTodosOsLivros obterTodosOsLivros;

    @BeforeEach
    public void setup() {
        this.input = new HashMap<>();
        this.livros = new ArrayList<>();
        Livro livro = new Livro();

        this.livros.add(livro);

        this.objectMapper = new ObjectMapper();
        
        this.obterTodosOsLivros = new ObterTodosOsLivros();
        this.obterTodosOsLivros.setLivroService(this.livroService);
    }

    @Test
    public void testarObterTodosOsLivrosComSucesso() {
        when(livroService.obterTodosOsLivros()).thenReturn(this.livros);
        ApiGatewayResponse responseExpect = responseWithSuccess();
        ApiGatewayResponse responseActual = obterTodosOsLivros.handleRequest(this.input, this.context);

        verifyTests(responseExpect, responseActual);
    }

    @Test
    public void testarObterTodosOsLivrosComErro() {
        doThrow(new RuntimeException("Ocorreu um erro")).when(this.livroService).obterTodosOsLivros();
        ApiGatewayResponse responseExpect = responseWithError();
        ApiGatewayResponse responseActual = obterTodosOsLivros.handleRequest(this.input, this.context);

        verifyTests(responseExpect, responseActual);
    }


    public void verifyTests(ApiGatewayResponse responseExpect, ApiGatewayResponse responseActual) {
        Assertions.assertEquals(responseExpect.getBody(), responseActual.getBody());
        Assertions.assertEquals(responseExpect.getStatusCode(), responseActual.getStatusCode());
        Assertions.assertEquals(responseExpect.getHeaders(), responseActual.getHeaders());
    }

    public ApiGatewayResponse responseWithSuccess() {
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(new Response(convertListLivrosToJson(), input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless")).build();
    }

    public ApiGatewayResponse responseWithError() {
        return ApiGatewayResponse.builder()
                .setObjectBody(new Response("Ocorreu um erro", this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setStatusCode(500).build();
    }

    public String convertListLivrosToJson() {
        String message = "";
        try {
            message = this.objectMapper.writeValueAsString(this.livros);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return message;
    }
}
