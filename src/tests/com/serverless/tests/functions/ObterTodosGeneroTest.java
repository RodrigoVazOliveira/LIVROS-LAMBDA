package com.serverless.tests.functions;

import com.serverless.functions.genero.ObterGenero;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ObterTodosGeneroTest {

    @Mock
    private GeneroService generoService;

    @Mock
    private Logger Log;

    @InjectMocks
    private ObterGenero obterGenero;

    @Test
    public void testarObterTodosGenerosComSucesso() {

    }
}
