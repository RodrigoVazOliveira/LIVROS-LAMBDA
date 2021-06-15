package com.serverless.tests.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.serverless.models.Livro;
import com.serverless.services.LivroService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private PaginatedScanList<Livro> paginatedScanListLivro;

    @InjectMocks
    private LivroService livroService;

    private Livro livro;

    @BeforeEach
    public void setup() {
        this.livro = mock(Livro.class);
    }

    @Test
    public void testarGravarNovoLivro() {
        doNothing().when(this.dynamoDBMapper).save(any(Livro.class));
        this.livroService.gravarNovoLivro(this.livro);
        verify(this.dynamoDBMapper).save(any(Livro.class));
    }

    @Test
    public void testarObterTodosOsLivros() {
        when(this.dynamoDBMapper.scan(eq(Livro.class), any(DynamoDBScanExpression.class)))
                .thenReturn(this.paginatedScanListLivro);
        List<Livro> livros = this.livroService.obterTodosOsLivros();
        Assertions.assertEquals(this.paginatedScanListLivro.size(), livros.size());
    }
}
