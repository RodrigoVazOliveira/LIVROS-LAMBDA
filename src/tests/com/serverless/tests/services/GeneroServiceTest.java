package com.serverless.tests.services;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneroServiceTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private DynamoDBConfiguration dynamoDBConfiguration;

    @Mock
    private PaginatedScanList<Genero> mockListaDeGenero;

    @Mock
    private PaginatedQueryList<Genero> mockListBuscaPorId;

    @Mock
    private List<Genero> mockListRetornoBuscaPorId;

    @InjectMocks
    private GeneroService generoService;

    private Genero testeGenero;

    @BeforeEach
    public void setup() {
        lenient().when(dynamoDBConfiguration.getDynamoDBMapper()).thenReturn(dynamoDBMapper);
        this.testeGenero = new Genero();
        this.testeGenero.setNome("Nome 1");
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

    @Test
    public void testarAtualizacaoDeGenero() {
        lenient().doNothing().when(dynamoDBMapper).save(Mockito.any());
        List<Genero> listaResultadoGenero = new ArrayList<>();
        listaResultadoGenero.add(this.testeGenero);
        lenient().when(this.mockListBuscaPorId.stream()).thenReturn(Stream.of(listaResultadoGenero.toArray(new Genero[0])));
        lenient().when(this.dynamoDBMapper.query(eq(Genero.class), any(DynamoDBQueryExpression.class))).thenReturn(this.mockListBuscaPorId);

        this.testeGenero.setId("IdTEst");
        this.generoService.atualizarGenero(this.testeGenero);
        verify(dynamoDBMapper).save(any());
    }

    @Test
    public void testarAtualizacaoDeGeneroError() {
        lenient().when(this.dynamoDBMapper.query(eq(Genero.class), any(DynamoDBQueryExpression.class))).thenReturn(this.mockListBuscaPorId);
        this.testeGenero.setId("IdTEst");

        RuntimeException ex =  Assertions.assertThrows(RuntimeException.class, () -> {
            this.generoService.atualizarGenero(this.testeGenero);
        });

        Assert.assertEquals(ex.getMessage(), "Não foi localziado nenhum genero com id IdTEst");
    }
}