package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.Livro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LivroService {

    private DynamoDBMapper dynamoDBMapper = DynamoDBConfiguration.build();
    private static final Logger LOG = LogManager.getLogger(LivroService.class);
    private GeneroService generoService;
    private TipoDeLivroService tipoDeLivroService;

    public LivroService() {
    }

    public LivroService(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void gravarNovoLivro(Livro livro) {
        this.dynamoDBMapper.save(livro);
    }
}
