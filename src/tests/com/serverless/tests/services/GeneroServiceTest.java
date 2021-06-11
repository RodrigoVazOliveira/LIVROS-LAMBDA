package com.serverless.tests.services;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
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
    private PaginatedScanList<Genero> mockListaDeGenero;

    @Mock
    private PaginatedQueryList<Genero> mockListBuscaPorId;

    @InjectMocks
    private GeneroService generoService;

    private Genero testeGenero;

    @BeforeEach
    public void setup() {
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
        Assertions.assertEquals(this.mockListaDeGenero.size(), generos.size());
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

        Assertions.assertEquals(ex.getMessage(), "Não foi localziado nenhum genero com id IdTEst");
    }

    @Test
    public void testarDeletarGeneroPorIdComSucesso() {
        List<Genero> listaResultadoGenero = new ArrayList<>();
        listaResultadoGenero.add(this.testeGenero);
        lenient().when(this.mockListBuscaPorId.stream()).thenReturn(Stream.of(listaResultadoGenero.toArray(new Genero[0])));
        lenient().when(this.dynamoDBMapper.query(eq(Genero.class), any(DynamoDBQueryExpression.class))).thenReturn(this.mockListBuscaPorId);
        lenient().doNothing().when(this.dynamoDBMapper).delete(any());
        this.generoService.deletarGenero("id");
        verify(this.dynamoDBMapper).delete(any());
    }

    @Test
    public void testarDeletarGeneroPorIdComErro() {

        RuntimeException respostaEsperada = new RuntimeException("Não foi localziado nenhum genero com id 2");
        doThrow(respostaEsperada)
                .when(this.dynamoDBMapper).query(eq(Genero.class), any(DynamoDBQueryExpression.class));
        RuntimeException respostaTest = Assertions.assertThrows(RuntimeException.class, () -> {
            this.generoService.deletarGenero("2");
        });
        Assertions.assertEquals(respostaTest, respostaEsperada);
    }
}