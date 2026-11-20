package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.JwtHelper;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class TemaControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private TemaRepository temaRepository;

	private static final String BASE_URL = "/temas";
	private static final String USUARIO = "tema.teste@email.com";
	private static final String SENHA = "senhaTeste123";

	private String token;

	@BeforeAll
	void start() {
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Teste Tema", USUARIO, SENHA));
		token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);
	}

	@Test
	@DisplayName("✔ 01 - Deve cadastrar um novo tema")
	void deveCadastrarTema() {

		// Given
		HttpEntity<Tema> requisicao = JwtHelper.criarRequisicaoComToken(TestBuilder.criarTema(null, "Java"), token);

		// When
		ResponseEntity<Tema> resposta = testRestTemplate.exchange(BASE_URL, HttpMethod.POST, requisicao, Tema.class);

		// Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals("Java", resposta.getBody().getDescricao());
	}

	@Test
	@DisplayName("✔ 02 - Deve atualizar um tema existente com status 200")
	void deveAtualizarTema() {

		// Given
		Tema salvo = temaRepository.save(TestBuilder.criarTema(null, "Spring"));
		HttpEntity<Tema> requisicao = JwtHelper.criarRequisicaoComToken(
				TestBuilder.criarTema(salvo.getId(), "Spring Boot"), token);

		// When
		ResponseEntity<Tema> resposta = testRestTemplate.exchange(BASE_URL, HttpMethod.PUT, requisicao, Tema.class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals("Spring Boot", resposta.getBody().getDescricao());
	}

	@Test
	@DisplayName("✔ 03 - Não deve atualizar um tema sem id")
	void naoDeveAtualizarTemaSemId() {

		// Given
		HttpEntity<Tema> requisicao = JwtHelper.criarRequisicaoComToken(TestBuilder.criarTema(null, "Sem id"), token);

		// When
		ResponseEntity<String> resposta = testRestTemplate.exchange(BASE_URL, HttpMethod.PUT, requisicao, String.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	@Test
	@DisplayName("✔ 04 - Deve retornar 404 ao buscar um tema inexistente")
	void deveRetornar404ParaTemaInexistente() {

		// Given
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);

		// When
		ResponseEntity<String> resposta = testRestTemplate.exchange(
				BASE_URL + "/999999", HttpMethod.GET, requisicao, String.class);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
	}

	@Test
	@DisplayName("✔ 05 - Não deve permitir acesso sem token")
	void naoDevePermitirAcessoSemToken() {

		// When
		ResponseEntity<String> resposta = testRestTemplate.getForEntity(BASE_URL, String.class);

		// Then
		assertEquals(HttpStatus.UNAUTHORIZED, resposta.getStatusCode());
	}

}
