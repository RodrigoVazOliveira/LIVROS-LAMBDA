package com.serverless.tests.functions.livro;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.livro.GravarLivro;
import com.serverless.models.Genero;
import com.serverless.models.Livro;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.LivroService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class GravarLivroTest {

    @Mock
    private LivroService livroService;

    @Mock
    private Context context;

    @InjectMocks
    private GravarLivro gravarLivro;

    private Map<String, Object> input;

    private Livro livro;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.input = new HashMap<>();
        this.livro = new Livro();
        this.livro.setTitulo("Harry Potter e a Pedra Filosofal");
        this.livro.setAnoLancamento(Year.parse("2001"));

        TipoDeLivro tipoDeLivro = new TipoDeLivro();
        tipoDeLivro.setNome("Capa dura");

        Genero genero = new Genero();
        genero.setNome("Ação");

        this.livro.setTipoDeLivro(new ArrayList<>(tipoDeLivro));
        this.livro.setGenero(genero);

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testarHandleRequest() {
        doNothing().when(livroService).gravarNovoLivro(any(Livro.class));
        this.input.put("body", converterObjetoParaJson());
        ApiGatewayResponse responseExpect = gerarResposta();
        ApiGatewayResponse responseActual = this.gravarLivro.handleRequest(this.input, this.context);

        Assertions.assertEquals(responseExpect.getBody(), responseActual.getBody());
        Assertions.assertEquals(responseExpect.getStatusCode(), responseActual.getStatusCode());
        Assertions.assertEquals(responseExpect.getHeaders(), responseActual.getHeaders());
    }

    public String converterObjetoParaJson() {
        String objetoJson = null;
        try {
            objetoJson = objectMapper.writeValueAsString(this.livro);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return objetoJson;
    }

    public ApiGatewayResponse gerarResposta() {
        return ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setObjectBody(new Response("Livro gravado com sucesso!", this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
}
