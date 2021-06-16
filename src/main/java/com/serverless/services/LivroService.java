package com.serverless.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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

    public Livro buscarLivroPeloId(String id) {
        LOG.info("Efetuando busca no dynamoDB");
        List<Livro> livros = this.dynamoDBMapper.query(Livro.class, criarQuery(id))
                .stream().collect(Collectors.toList());
        verificarBuscarPorId(livros);

        return livros.get(0);
    }

    private DynamoDBQueryExpression<Livro> criarQuery(String id) {
        LOG.info("Criar query para fazer a busca");
        DynamoDBQueryExpression<Livro> query = new DynamoDBQueryExpression<>();
        query.setHashKeyValues(criarObjetoLivro(id));
        return query;
    }

    private void verificarBuscarPorId(List<Livro> livros) {
        LOG.info("Verificar se não encontrou um livro");
        if (livros.size() == 0) {
            throw new RuntimeException("Não foi possivel localizar um livro com o id informado!");
        }
    }

    private Livro criarObjetoLivro(String id) {
        LOG.info("Criando livro para buscar com o id informado!");
        Livro livro = new Livro();
        livro.setId(id);
        return livro;
    }

    public void atualizarLivro(Livro livro) {
        LOG.info("Verificando s eo livro informado existe");
        Livro livroAtual = buscarLivroPeloId(livro.getId());
        LOG.info("Atualizando dados");
        atualizarNovosDadosNoLivroAtual(livroAtual, livro);
        LOG.info("Gravando os dados atualizados");
        this.dynamoDBMapper.save(livroAtual);
    }

    private void atualizarNovosDadosNoLivroAtual(Livro livroAtual, Livro livro) {
        livroAtual.setTitulo(livro.getTitulo());
        livroAtual.setAnoLancamento(livro.getAnoLancamento());
        livroAtual.setGenero(livro.getGenero());
        livroAtual.setTipoDeLivro(livro.getTipoDeLivro());
    }

	public void deletaLivro(Livro livro) {
		Livro deleteLivro = buscarLivroPeloId(livro.getId());
		this.dynamoDBMapper.delete(deleteLivro);
	}
}
