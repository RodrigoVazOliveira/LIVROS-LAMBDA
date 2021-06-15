package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.Livro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class LivroService {

    private DynamoDBMapper dynamoDBMapper = DynamoDBConfiguration.build();
    private static final Logger LOG = LogManager.getLogger(LivroService.class);

    public LivroService() {
    }

    public LivroService(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void gravarNovoLivro(Livro livro) {
        this.dynamoDBMapper.save(livro);
    }

    public List<Livro> obterTodosOsLivros() throws RuntimeException {
        LOG.info("Acessando o dynamoDB");
        return this.dynamoDBMapper.scan(Livro.class, new DynamoDBScanExpression())
                .stream().collect(Collectors.toList());
    }
}
