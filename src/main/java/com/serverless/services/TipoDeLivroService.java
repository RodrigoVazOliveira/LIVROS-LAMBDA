package com.serverless.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.serverless.configurations.DynamoDBConfiguration;
import com.serverless.models.TipoDeLivro;

public class TipoDeLivroService {

    private DynamoDBMapper dynamoDBMapper;
    private static final Logger LOG = LogManager.getLogger(GeneroService.class);

    public TipoDeLivroService() {
    }

    public TipoDeLivroService(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }
    
    private void instanciarDynamoDB() {
    	if (this.dynamoDBMapper == null) {
    		this.dynamoDBMapper = DynamoDBConfiguration.build();
    	}
    }

    public void cadastrarNovoTipoDeLivro(TipoDeLivro tipoDeLivro) {
    	instanciarDynamoDB();
        LOG.info("service: Gravando tipo de livro no dynamoDB");
        this.dynamoDBMapper.save(tipoDeLivro);
    }

    public Iterable<TipoDeLivro> obterTodosTipoDeLivro() {
    	instanciarDynamoDB();
        LOG.info("Acessando dynamoDB para obter os dados do tipo de livro");
        return this.dynamoDBMapper.scan(TipoDeLivro.class, new DynamoDBScanExpression())
                .stream().collect(Collectors.toList());
    }

    private TipoDeLivro buscarTipoDeLivroPorId(String id) {
    	instanciarDynamoDB();
        LOG.info("buscar tipo de livro por id no dynamoDB");
        TipoDeLivro tipoDeLivro = new TipoDeLivro();
        tipoDeLivro.setId(id);
        DynamoDBQueryExpression<TipoDeLivro> query = gerarQueryPorId(tipoDeLivro);
        LOG.info("Criando a lista de busca!");
        List<TipoDeLivro> resultadoDaBusca = this.dynamoDBMapper.query(TipoDeLivro.class, query)
                .stream().collect(Collectors.toList());
        gerarMensagemDeErro(resultadoDaBusca, id);

        return resultadoDaBusca.get(0);
     }

     private void gerarMensagemDeErro(List<TipoDeLivro> tipoDeLivros, String id) {
         if (tipoDeLivros.size() == 0) {
             LOG.error("busca com dados incorretos");
             throw new RuntimeException("não existe um tipo de livro com id " + id);
         }
     }

     private DynamoDBQueryExpression<TipoDeLivro> gerarQueryPorId(TipoDeLivro tipoDeLivro) {
         LOG.info("Criando a query de busca!");
         DynamoDBQueryExpression<TipoDeLivro> query = new DynamoDBQueryExpression<>();
         query.withHashKeyValues(tipoDeLivro);
         return query;
     }

    public void atualizarTipoDeLivro(TipoDeLivro tipoDeLivro) {
        TipoDeLivro tipoDeLivroDb = buscarTipoDeLivroPorId(tipoDeLivro.getId());
        tipoDeLivroDb.setNome(tipoDeLivro.getNome());
        this.dynamoDBMapper.save(tipoDeLivroDb);
    }

    public void deleteTipoDeLivro(String id) {
        TipoDeLivro tipoDeLivro = buscarTipoDeLivroPorId(id);
        LOG.info("delete o tipo de livro no dynamoDB com dynamoDBMapper");
        this.dynamoDBMapper.delete(tipoDeLivro);
    }
}
