package com.serverless.tests.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TipoDeLivroServiceTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private TipoDeLivroService tipoDeLivroService;

    private PaginatedQueryList<TipoDeLivro> tipoDeLivros;

    private Iterable<TipoDeLivro> tipoDeLivrosTest;

    @BeforeEach
    public void setup() {
        List<TipoDeLivro> listaTipoDeLivros = new ArrayList<>();
        TipoDeLivro objeto = new TipoDeLivro();
        objeto.setNome("Capa dura");
        objeto.setId("1");
        listaTipoDeLivros.add(objeto);
        this.tipoDeLivrosTest = listaTipoDeLivros;
        this.tipoDeLivros = mock(PaginatedQueryList.class,
                withSettings().defaultAnswer(new ForwardsInvocations(listaTipoDeLivros)));
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
        when(this.dynamoDBMapper.query(eq(TipoDeLivro.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(this.tipoDeLivros);
        Iterable<TipoDeLivro> tentativaTest = this.tipoDeLivroService.obterTodosTipoDeLivro();
        Assertions.assertEquals(this.tipoDeLivrosTest, tentativaTest);
    }
}