package com.serverless.tests.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.genero.DeleteGenero;
import com.serverless.services.GeneroService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class DeleteGeneroTest {

    @Mock
    private GeneroService generoService;

    @Mock
    private Context context;

    @InjectMocks
    private DeleteGenero deleteGenero;

    @Test
    public void testarDeleteGeneroComSucesso() {
        doNothing().when(this.generoService).deletarGenero(anyString());

        Map<String, Object> input = new HashMap<>();
        input.put("id", "4242");

        ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("O genero com id 4242 foi excluido!", input))
                .setStatusCode(204)
                .build();

        ApiGatewayResponse respostaTest = this.deleteGenero.handleRequest(input, this.context);

        Assert.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assert.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
        Assert.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
    }

    @Test
    public void testarDeleteGeneroComErro() {
        doThrow(new RuntimeException("Não foi localziado nenhum genero com id 1"))
                .when(this.generoService).deletarGenero(anyString());

        Map<String, Object> input = new HashMap<>();
        input.put("id", "1");

        ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Não foi localziado nenhum genero com id 1", input))
                .setStatusCode(400)
                .build();

        ApiGatewayResponse respostaTest = this.deleteGenero.handleRequest(input, this.context);

        Assert.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assert.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
        Assert.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
    }
}
