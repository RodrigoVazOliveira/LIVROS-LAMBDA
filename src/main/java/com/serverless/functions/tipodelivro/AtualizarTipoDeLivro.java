package com.serverless.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class AtualizarTipoDeLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private TipoDeLivroService tipoDeLivroService;
    private final static Logger LOG = LogManager.getLogger(AtualizarTipoDeLivro.class);

    public AtualizarTipoDeLivro() {
        this.tipoDeLivroService = new TipoDeLivroService();
    }

    public AtualizarTipoDeLivro(TipoDeLivroService tipoDeLivroService) {
        this.tipoDeLivroService = tipoDeLivroService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Inciiando o funcionamento do lambda para atualizar");
        TipoDeLivro tipoDeLivro = new TipoDeLivro();
        tipoDeLivro.setId(input.get("id").toString());
        tipoDeLivro.setNome(input.get("nome").toString());

        try {
            LOG.info("Inciando da atualização do tipo de livro!");
            this.tipoDeLivroService.atualizarTipoDeLivro(tipoDeLivro);
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(new Response("Dados atualizado com sucesso! " + tipoDeLivro,
                            input))
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        } catch (RuntimeException e) {
            LOG.info("erro na atualização do tipo de livro!");
            return ApiGatewayResponse.builder()
                    .setStatusCode(400)
                    .setObjectBody(new Response(e.getMessage(), input))
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        }
    }
}
