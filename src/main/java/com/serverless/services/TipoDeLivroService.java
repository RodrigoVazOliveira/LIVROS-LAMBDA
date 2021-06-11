package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.TipoDeLivro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
}
