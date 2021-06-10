package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.Genero;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneroService {

    private DynamoDBMapper dynamoDBMapper;
    private DynamoDBConfiguration dynamoDBConfiguration;
    private static final Logger LOG = LogManager.getLogger(GeneroService.class);

    public void cadastrarNovoGeneroDeLivro(String nome) {
        gerarMapper();
        LOG.info("Gerando o uma novo objeto genero");
        Genero genero = new Genero();
        genero.setNome(nome);
        LOG.info("Persistindo o dado no dynamoDB na tabela genero");
        this.dynamoDBMapper.save(genero);
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

    public Iterable<Genero> obterTodosGeneros() {
        gerarMapper();
        LOG.info("Gerando a lista com todos generos cadastrados com dynamoDB");
        return this.dynamoDBMapper.scan(Genero.class, new DynamoDBScanExpression()).stream().collect(Collectors.toList());
    }

    public Genero buscarGeneroPorId(String id) {
        gerarMapper();
        LOG.info("Gerando o map para fazer a consulta");
        Map<String, AttributeValue> valorABuscar = new HashMap<>();
        valorABuscar.put("valorUm", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Genero> query = new DynamoDBQueryExpression<>();
        query.withConditionalOperator("Id = :valorUm");
        query.withExpressionAttributeValues(valorABuscar);

        LOG.info("buscando e gerando a lista com retorno da consulta com dynamoDB");
        List<Genero> resultado = this.dynamoDBMapper.query(Genero.class, query).stream().collect(Collectors.toList());

        if (resultado == null || resultado.size() == 0) {
            LOG.error("Nâo foi possivel encontrar dados");
            throw new RuntimeException("Não foi localziado nenhum genero com id " + id);
        }

        return resultado.get(0);
    }

    public void atualizarGenero(Genero generoAtualizar) {
        gerarMapper();
        Genero genero = this.buscarGeneroPorId(generoAtualizar.getId());
        genero.setNome(generoAtualizar.getNome());
        LOG.info("Atualizando o genero no dynamoDB");
        this.dynamoDBMapper.save(genero);
    }

    public void deletarGenero(String id) {
        gerarMapper();
        Genero genero = this.buscarGeneroPorId(id);
        LOG.info("Excluindo o genero no dynamoDB");
        this.dynamoDBMapper.delete(genero);
    }
}
