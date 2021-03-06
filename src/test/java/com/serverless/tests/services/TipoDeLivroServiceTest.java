package com.serverless.tests.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;

@ExtendWith(MockitoExtension.class)
public class TipoDeLivroServiceTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private TipoDeLivroService tipoDeLivroService;

    private PaginatedScanList<TipoDeLivro> tipoDeLivros;

    private PaginatedQueryList<TipoDeLivro> tipoDeLivrosQuery;

    private PaginatedQueryList<TipoDeLivro> tipoDeLivrosQueryError;

    private Iterable<TipoDeLivro> tipoDeLivrosTest;

    private TipoDeLivro tipoDeLivro;

    @BeforeEach
    public void setup() {
        List<TipoDeLivro> listaTipoDeLivros = new ArrayList<>();

        this.tipoDeLivro = new TipoDeLivro();
        this.tipoDeLivro.setId("1");
        this.tipoDeLivro.setNome("Capa dura");

        listaTipoDeLivros.add(this.tipoDeLivro);
        this.tipoDeLivrosTest = listaTipoDeLivros;

        this.tipoDeLivros = mock(PaginatedScanList.class,
                withSettings().defaultAnswer(new ForwardsInvocations(listaTipoDeLivros)));

        this.tipoDeLivrosQuery = mock(PaginatedQueryList.class,
                withSettings().defaultAnswer(new ForwardsInvocations(listaTipoDeLivros)));

        List<TipoDeLivro> listaComError = new ArrayList<>();
        mock(PaginatedScanList.class,
                withSettings().defaultAnswer(new ForwardsInvocations(listaComError)));

        this.tipoDeLivrosQueryError = mock(PaginatedQueryList.class,
                withSettings().defaultAnswer(new ForwardsInvocations(listaComError)));
    }

    @Test
    public void testarCadastroDeTipoDeLivroComSucesso() {
        doNothing().when(this.dynamoDBMapper).save(any());
        TipoDeLivro tipoDeLivro = new TipoDeLivro();
        tipoDeLivro.setNome("Capa dura");
        this.tipoDeLivroService.cadastrarNovoTipoDeLivro(tipoDeLivro);
        verify(this.dynamoDBMapper).save(any());
    }

    @Test
    public void testarObterTodosTipoDeLivroComSucesso() {
        when(this.dynamoDBMapper.scan(eq(TipoDeLivro.class), any(DynamoDBScanExpression.class)))
                .thenReturn(this.tipoDeLivros);
        Iterable<TipoDeLivro> tentativaTest = this.tipoDeLivroService.obterTodosTipoDeLivro();
        Assertions.assertEquals(this.tipoDeLivrosTest, tentativaTest);
    }

    @Test
    public void testarAtualizarTipoDeLivroComSucesso() {
        when(this.dynamoDBMapper.query(eq(TipoDeLivro.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(this.tipoDeLivrosQuery);
        doNothing().when(this.dynamoDBMapper).save(this.tipoDeLivro);
       this.tipoDeLivroService.atualizarTipoDeLivro(this.tipoDeLivro);
       verify(this.dynamoDBMapper).save(any());
    }

    @Test
    public void testarAtualizarTipoDeLivroComErro() {
        when(this.dynamoDBMapper.query(eq(TipoDeLivro.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(this.tipoDeLivrosQueryError);
        RuntimeException resposta = Assertions.assertThrows(RuntimeException.class, () -> {
            this.tipoDeLivroService.atualizarTipoDeLivro(this.tipoDeLivro);
        });
        Assertions.assertEquals("n??o existe um tipo de livro com id 1", resposta.getMessage());
    }
}