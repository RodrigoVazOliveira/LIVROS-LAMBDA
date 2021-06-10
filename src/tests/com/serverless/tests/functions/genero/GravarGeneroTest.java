package com.serverless.tests.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.genero.GravarGenero;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class GravarGeneroTest {

    @Mock
    private GeneroService generoService;

    @Mock
    private Logger log;

    @Mock
    private Context context;

    @InjectMocks
    private GravarGenero gravarGenero;

    @Test
    public void testHandlerRequest() {
        Mockito.doNothing().when(generoService).cadastrarNovoGeneroDeLivro(Mockito.anyString());
        Map<String, Object> input = new HashMap<>();
        input.put("nome", "Média");

        Response response = new Response("Gravação efetuada com sucesso!", input);

        ApiGatewayResponse test = ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(response)
                .build();

        ApiGatewayResponse apiGatewayResponse = gravarGenero.handleRequest(input, context);

        Assert.assertEquals(test.getStatusCode(), apiGatewayResponse.getStatusCode());
        Assert.assertEquals(test.getBody(), apiGatewayResponse.getBody());
        Assert.assertEquals(test.getHeaders(), apiGatewayResponse.getHeaders());
    }
}
