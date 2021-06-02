package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.Genero;

public class GeneroService {

    private DynamoDBMapper dynamoDBMapper;
    private DynamoDBConfiguration dynamoDBConfiguration;

    public void cadastrarNovoGeneroDeLivro(String nome) {
        gerarMapper();
        Genero genero = new Genero();
        genero.setNome(nome);
        this.dynamoDBMapper.save(genero);
    }

    public void gerarMapper() {
        if (this.dynamoDBConfiguration == null) {
            this.dynamoDBConfiguration = new DynamoDBConfiguration();
            this.dynamoDBConfiguration.build();
            this.dynamoDBConfiguration.buildDynamoDBMapper();
        }

        this.dynamoDBMapper = this.dynamoDBConfiguration.getDynamoDBMapper();
    }
}
