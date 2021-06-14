package com.serverless.helper;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

public class GetInput {

    private static final Logger LOG = LogManager.getLogger(GetInput.class);

    public static JsonNode getBody(Map<String, Object> input) {
        LOG.info("Obtendo dados enviados");
        JsonNode body = null;
        try {
            body = ObjectMapperProxy.getObjectMapper().readTree((String) input.get("body"));
            return body;
        } catch (IOException e) {
            LOG.info("ocorreu um erro ao converter os dados");
            e.printStackTrace();
        }
        return body;
    }
}
