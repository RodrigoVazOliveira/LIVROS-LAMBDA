package com.serverless.tests.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GeneroServiceTest {

    private GeneroService generoService;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Before
    public void setup() {
        this.generoService = new GeneroService();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void gravarNovoGeneroTest() {
        Mockito.doNothing().when(dynamoDBMapper).save(Mockito.any());

        this.generoService.cadastrarNovoGeneroDeLivro("Comédia");

        Mockito.verify(dynamoDBMapper).save(Mockito.any());
    }
}