package com.serverless.tests.functions.tipodelivro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.tipodelivro.CadastrarTipoDeLivro;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;

@ExtendWith(MockitoExtension.class)
public class CadastrarTipoDeLivroTest {

    @Mock
    private TipoDeLivroService tipoDeLivroService;

    private Map<String, Object> input;

    @Mock
    private Context context;

    @InjectMocks
    private CadastrarTipoDeLivro cadastrarTipoDeLivro;

    @Test
    public void testarCadastroDeTipoDeLivroComSucesso() {
        Mockito.doNothing().when(this.tipoDeLivroService).cadastrarNovoTipoDeLivro(Mockito.any(TipoDeLivro.class));
        this.input = new HashMap<>();
        this.input.put("body", "{\"nome\": \"bolso\"}");
        TipoDeLivro tipoDeLivro = new TipoDeLivro();
        tipoDeLivro.setNome("bolso");
        ApiGatewayResponse respostaEsperada = ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("O tipo de livro " + tipoDeLivro + " foi gravado com sucesso!", input))
                .setStatusCode(201)
                .build();
        ApiGatewayResponse respostaTest =  this.cadastrarTipoDeLivro.handleRequest(input, this.context);

        assertThat(respostaTest.getStatusCode(), is(respostaEsperada.getStatusCode()));
        assertThat(respostaTest.getBody(), is(respostaEsperada.getBody()));
        assertThat(respostaTest.getHeaders(), is(respostaEsperada.getHeaders()));
    }
}
