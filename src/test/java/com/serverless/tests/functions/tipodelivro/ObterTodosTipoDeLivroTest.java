package com.serverless.tests.functions.tipodelivro;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.tipodelivro.ObterTodosTipoDeLivro;
import com.serverless.helper.ObjectMapperProxy;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;

@ExtendWith(MockitoExtension.class)
public class ObterTodosTipoDeLivroTest {

    @Mock
    private TipoDeLivroService tipoDeLivroService;

    @Mock
    private Context context;

    @InjectMocks
    private ObterTodosTipoDeLivro obterTodosTipoDeLivro;

    private Iterable<TipoDeLivro> tipoDeLivros;

    private Map<String, Object> input;

    private final ObjectMapper objectMapper = ObjectMapperProxy.getObjectMapper();
    
    private String mensagemRetorno;

    @BeforeEach
    public void setup() {
        this.input = new HashMap<>();

        List<TipoDeLivro> tipoDeLivrosList = new ArrayList<>();
        tipoDeLivrosList.add(new TipoDeLivro());

        this.tipoDeLivros = tipoDeLivrosList;
        
        this.mensagemRetorno = null;
    }

    @Test
    public void testarhandleRequest() {
        when(this.tipoDeLivroService.obterTodosTipoDeLivro()).thenReturn(this.tipoDeLivros);
        converterListaDeTipoDeLivroParaJson();
        ApiGatewayResponse responseExpect = construirRespostaComSucesso();
        ApiGatewayResponse responseActual = this.obterTodosTipoDeLivro.handleRequest(this.input, this.context);

        verificarAcertos(responseExpect, responseActual);
    }
    
    private void converterListaDeTipoDeLivroParaJson() {
    	try {
			this.mensagemRetorno = this.objectMapper.writeValueAsString(this.tipoDeLivros);
		} catch (JsonProcessingException e) {
			this.mensagemRetorno = e.getMessage();
		}
    }
    
    private ApiGatewayResponse construirRespostaComSucesso() {
    	return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(new Response(this.mensagemRetorno, this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
    
    private void verificarAcertos(ApiGatewayResponse responseExpect, ApiGatewayResponse responseActual) {
        assertThat(responseActual.getStatusCode(), is(responseExpect.getStatusCode()));
        assertThat(responseActual.getBody(), is(responseExpect.getBody()));
        assertThat(responseActual.getHeaders(), is(responseExpect.getHeaders()));
    }
}
