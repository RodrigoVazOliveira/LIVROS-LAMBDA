package com.serverless.tests.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.tipodelivro.DeleteTipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

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

        this.input.put("id", "1");

        ApiGatewayResponse respostaTest = this.deleteTipoDeLivro.handleRequest(this.input, this.context);

        ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                .setObjectBody(new Response("tipo de livro deletado!", this.input))
                .setStatusCode(204)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();

        Assertions.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assertions.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
        Assertions.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
    }

    @Test
    public void testarDeleteTipoDeLivroComErro() {
        doThrow(new RuntimeException("não existe um tipo de livro com id 1"))
                .when(this.tipoDeLivroService).deleteTipoDeLivro(anyString());

        this.input.put("id", "1");

        ApiGatewayResponse respostaTest = this.deleteTipoDeLivro.handleRequest(this.input, this.context);

        ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                .setObjectBody(new Response("não existe um tipo de livro com id 1", this.input))
                .setStatusCode(400)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();

        Assertions.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assertions.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
        Assertions.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
    }
}
