package com.serverless.tests.functions.livro;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.helper.ObjectMapperProxy;
import com.serverless.models.Genero;
import com.serverless.models.Livro;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.LivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    }
}
