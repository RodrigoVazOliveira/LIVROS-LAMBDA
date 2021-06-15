package com.serverless.tests.functions.livro;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.livro.AtualizarLivro;
import com.serverless.helper.ObjectMapperProxy;
import com.serverless.models.Genero;
import com.serverless.models.Livro;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.LivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class AtualizarLivroTest {

    @Mock
    private LivroService tiLivroService;

    @Mock
    private Context context;

    private Map<String, Object> input;

    private final ObjectMapper objectMapper = ObjectMapperProxy.getObjectMapper();

    private final static Logger LOG = LogManager.getLogger(AtualizarLivroTest.class);

    private Livro livro;

    @InjectMocks
    private AtualizarLivro atualizarLivro;

    @BeforeEach
    public void setup() {
        this.input = new HashMap<>();

        this.livro = new Livro();
        this.livro.setTitulo("Harry Potter e a Pedra Filosofal");
        this.livro.setAnoLancamento(Year.parse("2021"));

        TipoDeLivro tipoDeLivro = new TipoDeLivro();
        tipoDeLivro.setNome("Capa dura");

        Genero genero = new Genero();
        genero.setNome("Ação");

        this.livro.setTipoDeLivro(Arrays.asList(tipoDeLivro));
        this.livro.setGenero(genero);
    }

    @Test
    public void testarHandleRequestWithSuccess() {
        doNothing().when(this.tiLivroService).atualizarLivro(any(Livro.class));
        converterLivroParaJson();
        ApiGatewayResponse responseExpect = builderResponseSuccess();
        ApiGatewayResponse responseActual = this.atualizarLivro.handleRequest(this.input, this.context);

        testsCasesAssertions(responseExpect, responseActual);
    }

    public void testsCasesAssertions(ApiGatewayResponse responseExpect, ApiGatewayResponse responseActual) {
        Assertions.assertEquals(responseExpect.getBody(), responseActual.getBody());
        Assertions.assertEquals(responseExpect.getStatusCode(), responseActual.getStatusCode());
        Assertions.assertEquals(responseExpect.getHeaders(), responseActual.getHeaders());
    }

    public void converterLivroParaJson() {
        LOG.info("TEST: converntendo entrada ");
        try {
            this.input.put("body", this.objectMapper.writeValueAsString(this.livro));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public ApiGatewayResponse builderResponseSuccess() {
        LOG.info("TEST: GERANDO MENSAGEM COM SUCESSO!");
        return ApiGatewayResponse.builder()
                .setObjectBody(new Response("Livro atualizado com sucesso!", this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setStatusCode(204).build();
    }
}
