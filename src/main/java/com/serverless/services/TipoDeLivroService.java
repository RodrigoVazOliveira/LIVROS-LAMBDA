package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.TipoDeLivro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TipoDeLivroService {

    private DynamoDBMapper dynamoDBMapper;
    private DynamoDBConfiguration dynamoDBConfiguration;
    private static final Logger LOG = LogManager.getLogger(GeneroService.class);

    public void cadastrarNovoTipoDeLivro(TipoDeLivro tipoDeLivro) {
        gerarMapper();
        this.dynamoDBMapper.save(tipoDeLivro);
    }

    private void gerarMapper() {
        LOG.info("Gerando o mapper com as configurações do ambiente");
        if (this.dynamoDBConfiguration == null) {
            this.dynamoDBConfiguration = new DynamoDBConfiguration();
            this.dynamoDBConfiguration.build();
            this.dynamoDBConfiguration.buildDynamoDBMapper();
        }

        LOG.info("Obtendo o mapper");
        this.dynamoDBMapper = this.dynamoDBConfiguration.getDynamoDBMapper();
    }
}
