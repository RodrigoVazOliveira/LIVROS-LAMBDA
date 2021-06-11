package com.serverless.tests.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

;

@ExtendWith(MockitoExtension.class)
public class AtualizarTipoDeLivroTest {

    @Mock
    private TipoDeLivroService tipoDeLivroService;

    @InjectMocks
    private AtualizarTipoDeLivro atualizarTipoDeLivro;

    private Map<String, Object> input;

    @Mock
    private Context context;

    private TipoDeLivro tipoDeLivro;

    public void setup() {
        this.tipoDeLivro = new TipoDeLivro();
        this.tipoDeLivro.setId("1");
        this.tipoDeLivro.setNome("Capa dura");

        this.input = new HashMap<>();
    }

    @Test
    public void testarHandlerRequest() {
        doNothing().when(this.tipoDeLivroService).atualizarTipoDeLivro(any());
        input.put("id", "1");
        input.put("nome", "Capa dura");

        ApiGatewayResponse respostaTest = this.atualizarTipoDeLivro.handleRequest(this.input, this.context);

        ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setObjectBody(new Response("Dados atualizado com sucesso! " + this.tipoDeLivro, input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();

        Assert.assertEquals(respostaEsperada.getStatusCode(), respostaTest.getStatusCode());
        Assert.assertEquals(respostaEsperada.getBody(), respostaTest.getBody());
        Assert.assertEquals(respostaEsperada.getHeaders(), respostaTest.getHeaders());
    }
}
