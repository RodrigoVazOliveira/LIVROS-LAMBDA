package com.serverless.functions.tipodelivro;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.tipodelivro.helper.TipoDeLivroServiceInstance;
import com.serverless.helper.GetInput;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;

public class CadastrarTipoDeLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(CadastrarTipoDeLivro.class);
    private TipoDeLivroService tipoDeLivroService;
    private TipoDeLivro tipoDeLivro;
    private Map<String, Object> input;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Inicio de gravação dos dados do tipo de livro");
        LOG.info("Resgatando dados enviados");
        this.input = input;
        this.tipoDeLivroService = TipoDeLivroServiceInstance.getInstance(this.tipoDeLivroService);
        getData();
        LOG.info("Gravando os dados no dynamoDB");
        this.tipoDeLivroService.cadastrarNovoTipoDeLivro(this.tipoDeLivro);

        return gerarResposta();
    }

    private void getData() {
        JsonNode body = GetInput.getBody(this.input);
        this.tipoDeLivro = new TipoDeLivro();
        this.tipoDeLivro.setNome(body.get("nome").asText());
    }

    private ApiGatewayResponse gerarResposta() {
        return ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("O tipo de livro " + tipoDeLivro + " foi gravado com sucesso!"
                                , this.input))
                .setStatusCode(201)
                .build();
    }

	public void setTipoDeLivroService(TipoDeLivroService tipoDeLivroService) {
		this.tipoDeLivroService = tipoDeLivroService;
	}
}
