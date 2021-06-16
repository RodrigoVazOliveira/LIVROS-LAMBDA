package com.serverless.tests.functions.livro;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.functions.livro.DeleteLivro;
import com.serverless.models.Livro;
import com.serverless.services.LivroService;

@ExtendWith(MockitoExtension.class)
public class DeletaLivroTest {
	
	@Mock
	private LivroService livroService;
	
	@Mock
	private Context context;
	
	private Map<String, Object> input;
	
	private ApiGatewayResponse responseExpect;
	private ApiGatewayResponse responseActual;
	
	@InjectMocks
	private DeleteLivro deleteLivro;
	
	@BeforeEach
	public void setup() {
		this.input = new HashMap<String, Object>();
		this.input.put("body", "{\"id\": \"34233242342\"}");
	}
	
	@Test
	public void testarHandleRequestWithSuccess() {
		doNothing().when(this.livroService).deletaLivro(any(Livro.class));
		this.responseActual = this.deleteLivro.handleRequest(this.input, this.context);
		buildResponseWithSucess();
		verifyAsserts();
	}
	
	private void buildResponseWithSucess() {
		this.responseExpect = ApiGatewayResponse.builder()
				.setStatusCode(204)
				.setObjectBody(new Response("Livro deletado com sucesso!", this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
	}
	
	private void verifyAsserts() {
		Assertions.assertEquals(this.responseExpect.getStatusCode(), this.responseActual.getStatusCode());
		Assertions.assertEquals(this.responseExpect.getBody(), this.responseActual.getBody());
		Assertions.assertEquals(this.responseExpect.getHeaders(), this.responseActual.getHeaders());
	}
}
