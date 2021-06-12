package com.serverless.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class CadastrarTipoDeLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(CadastrarTipoDeLivro.class);
    private TipoDeLivroService tipoDeLivroService;
    private TipoDeLivro tipoDeLivro;

    public CadastrarTipoDeLivro() {
        this.tipoDeLivroService = new TipoDeLivroService();
    }

    public CadastrarTipoDeLivro(TipoDeLivroService tipoDeLivroService) {
        this.tipoDeLivroService = tipoDeLivroService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Inicio de gravação dos dados do tipo de livro");
        LOG.info("Resgatando dados enviados");

        getData(input);

        LOG.info("Gravando os dados no dynamoDB");
        this.tipoDeLivroService.cadastrarNovoTipoDeLivro(this.tipoDeLivro);

        return ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(
                        new Response("O tipo de livro " + tipoDeLivro + " foi gravado com sucesso!"
                                , input))
                .setStatusCode(201)
                .build();
    }

    private void getData(Map<String, Object> input) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.tipoDeLivro = new TipoDeLivro();
        try {
            JsonNode body = objectMapper.readTree( (String) input.get("body") );
            this.tipoDeLivro.setNome(body.get("nome").asText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
