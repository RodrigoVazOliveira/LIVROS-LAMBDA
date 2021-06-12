package com.serverless.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.services.TipoDeLivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class DeleteTipoDeLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private TipoDeLivroService tipoDeLivroService;
    private static final Logger LOG = LogManager.getLogger(DeleteTipoDeLivro.class);

    public DeleteTipoDeLivro() {
        this.tipoDeLivroService = new TipoDeLivroService();
    }

    public DeleteTipoDeLivro(TipoDeLivroService tipoDeLivroService) {
        this.tipoDeLivroService = tipoDeLivroService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Iniciando o lambda e tratando os dados enviados");
        String id = input.get("id").toString();
        LOG.info("Iniciando serviço do tipo do livro para deletar o tipo de livro no ndynamoDB");
        try {
            this.tipoDeLivroService.deleteTipoDeLivro(id);
            return ApiGatewayResponse.builder()
                    .setStatusCode(204)
                    .setObjectBody(new Response("tipo de livro deletado!", input))
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        } catch (RuntimeException e) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(400)
                    .setObjectBody(new Response(e.getMessage(), input))
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        }
    }
}
