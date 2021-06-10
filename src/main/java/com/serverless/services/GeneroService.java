package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.Genero;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return this.dynamoDBMapper.scan(Genero.class, new DynamoDBScanExpression()).stream().collect(Collectors.toList());
    }

    public Genero buscarGeneroPorId(String id) {
        gerarMapper();
        Map<String, AttributeValue> valorABuscar = new HashMap<>();
        valorABuscar.put("valorUm", new AttributeValue().withS(id));
        valorABuscar.put("safd", new AttributeValue().withM())

        DynamoDBQueryExpression<Genero> query = new DynamoDBQueryExpression<>();
        query.withConditionalOperator("Id = :valorUm");
        query.withExpressionAttributeValues(valorABuscar);

        List<Genero> resultado = this.dynamoDBMapper.query(Genero.class, query).stream().collect(Collectors.toList());

        if (resultado == null || resultado.size() == 0) {
            throw new RuntimeException("Não foi localziado nenhum genero com id " + id);
        }

        return resultado.get(0);
    }

    public void atualizarGenero(Genero generoAtualizar) {
        gerarMapper();
        Genero genero = this.buscarGeneroPorId(generoAtualizar.getId());
        genero.setNome(generoAtualizar.getNome());

        this.dynamoDBMapper.save(genero);

        LocalDateTime da;
        da.compareTo()
    }

    public void deletarGenero(String id) {
        gerarMapper();
        Genero genero = this.buscarGeneroPorId(id);
        this.dynamoDBMapper.delete(genero);
    }
}
