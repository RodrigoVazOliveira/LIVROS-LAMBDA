package com.serverless.tests.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TipoDeLivroServiceTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private TipoDeLivroService tipoDeLivroService;

    @Test
    public void testarCadastroDeTipoDeLivroComSucesso() {
        doNothing().when(this.dynamoDBMapper).save(any());
        TipoDeLivro tipoDeLivro = new TipoDeLivro();
        tipoDeLivro.setNome("Capa dura");
        this.tipoDeLivroService.cadastrarNovoTipoDeLivro(tipoDeLivro);
        verify(this.dynamoDBMapper).save(any());
    }
}