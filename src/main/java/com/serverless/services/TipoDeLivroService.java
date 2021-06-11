package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.TipoDeLivro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

public class TipoDeLivroService {

    private DynamoDBMapper dynamoDBMapper = DynamoDBConfiguration.build();
    private static final Logger LOG = LogManager.getLogger(GeneroService.class);

    public TipoDeLivroService() {
    }

    public TipoDeLivroService(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void cadastrarNovoTipoDeLivro(TipoDeLivro tipoDeLivro) {
        LOG.info("service: Gravando tipo de livro no dynamoDB");
        this.dynamoDBMapper.save(tipoDeLivro);
    }

    public Iterable<TipoDeLivro> obterTodosTipoDeLivro() {
        LOG.info("Acessando dynamoDB para obter os dados do tipo de livro");
        return this.dynamoDBMapper.query(TipoDeLivro.class, new DynamoDBQueryExpression<>())
                .stream().collect(Collectors.toList());
    }
}
