package com.serverless.tests.services;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneroServiceTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private DynamoDBConfiguration dynamoDBConfiguration;

    @Mock
    private PaginatedScanList<Genero> mockListaDeGenero;

    @InjectMocks
    private GeneroService generoService;

    @BeforeEach
    public void setup() {
        lenient().when(dynamoDBConfiguration.getDynamoDBMapper()).thenReturn(dynamoDBMapper);
    }

    @Test
    public void testCadastrarNovoGenero() {
        lenient().doNothing().when(this.dynamoDBMapper).save(any());
        this.generoService.cadastrarNovoGeneroDeLivro("comedia");
        verify(dynamoDBMapper).save(any());
    }


    @Test
    public void testarObterTodosGeneros() {
        lenient().when(this.dynamoDBMapper.scan(eq(Genero.class), any(DynamoDBScanExpression.class))).thenReturn(this.mockListaDeGenero);
        List<Genero> generos = (List<Genero>) generoService.obterTodosGeneros();
        Assert.assertEquals(this.mockListaDeGenero.size(), generos.size());
    }
}