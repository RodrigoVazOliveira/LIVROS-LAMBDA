package com.serverless.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.services.TipoDeLivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CadastrarTipoDeLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(CadastrarTipoDeLivro.class);
    private TipoDeLivroService tipoDeLivroService;

    public CadastrarTipoDeLivro() {
        this.tipoDeLivroService = new TipoDeLivroService();
    }

    public CadastrarTipoDeLivro(TipoDeLivroService tipoDeLivroService) {
        this.tipoDeLivroService = tipoDeLivroService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        return null;
    }
}
