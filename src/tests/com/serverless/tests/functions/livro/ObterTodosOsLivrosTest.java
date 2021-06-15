package com.serverless.tests.functions.livro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.models.Livro;
import com.serverless.services.LivroService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.Context;
import java.util.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObterTodosOsLivrosTest {

    @Mock
    private LivroService  livroService;

    @Mock
    private Context context;

    private Map<String, Object> input;

    private List<Livro> livros;

    private ObjectMapper objectMapper;

    @InjectMocks
    private ObterTodosOsLivros obterTodosOsLivros;

    @BeforeEach
    public void setup() {
        this.input = new HashMap<>();
        this.livros = new ArrayList<>();
        Livro livro = new Livro();

        this.livros.add(livro);

        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testarObterTodosOsLivros() {
        when(livroService.obterTodosOsLivros()).thenReturn(this.livros);
        ApiGatewayResponse responseExpect = responseWithSuccess();
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
