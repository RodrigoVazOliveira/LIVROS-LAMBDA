package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.TipoDeLivro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private TipoDeLivro buscarTipoDeLivroPorId(String id) {
        LOG.info("buscar tipo de livro por id no dynamoDB");
        Map<String, AttributeValue> params = new HashMap<>();
        params.put("valorUm", new AttributeValue().withS(id));

        LOG.info("Criando a query de busca!");
        DynamoDBQueryExpression<TipoDeLivro> query = new DynamoDBQueryExpression<>();
        query.withConditionalOperator("Id = :valorUm");
        query.withExpressionAttributeValues(params);

        LOG.info("Criando a lista de busca!");
        List<TipoDeLivro> resultadoDaBusca = this.dynamoDBMapper.query(TipoDeLivro.class, query)
                .stream().collect(Collectors.toList());

        if (resultadoDaBusca.size() == 0) {
            LOG.error("busca com dados incorretos");
            throw new RuntimeException("não existe um tipo de livro com id " + id);
        }

        return resultadoDaBusca.get(0);
     }

    public void atualizarTipoDeLivro(TipoDeLivro tipoDeLivro) {
        TipoDeLivro tipoDeLivroDb = buscarTipoDeLivroPorId(tipoDeLivro.getId());
        tipoDeLivroDb.setNome(tipoDeLivro.getNome());
        this.dynamoDBMapper.save(tipoDeLivroDb);
    }

    public void deleteTipoDeLivro(String id) {
        TipoDeLivro tipoDeLivro = buscarTipoDeLivroPorId(id);
        this.dynamoDBMapper.delete(tipoDeLivro);
    }
}
