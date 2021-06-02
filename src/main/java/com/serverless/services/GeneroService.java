package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.Genero;

import java.util.ArrayList;
import java.util.List;

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

    public Iterable<Genero> obterTodosGeneros() {
        gerarMapper();
        List<Genero> generos = new ArrayList<>();
        generos.addAll(this.dynamoDBMapper.scan(Genero.class, new DynamoDBScanExpression()));
        return generos;
    }
}
