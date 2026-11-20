package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.JwtHelper;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class PostagemControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private TemaRepository temaRepository;

	private static final String BASE_URL = "/postagens";
	private static final String USUARIO = "postagem.teste@email.com";
	private static final String SENHA = "senhaTeste123";

	private String token;

	@BeforeAll
	void start() {
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Teste Postagem", USUARIO, SENHA));
		token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);
	}

	@Test
	@DisplayName("✔ 01 - Não deve cadastrar postagem com tema inexistente")
	void naoDeveCadastrarComTemaInexistente() {

		// Given
		Postagem postagem = TestBuilder.criarPostagem("Título válido", "Texto com mais de dez caracteres",
				TestBuilder.criarTema(999999L, "Inexistente"));
		HttpEntity<Postagem> requisicao = JwtHelper.criarRequisicaoComToken(postagem, token);

		// When
		ResponseEntity<String> resposta = testRestTemplate.exchange(BASE_URL, HttpMethod.POST, requisicao, String.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	@Test
	@DisplayName("✔ 02 - Não deve cadastrar postagem com título inválido")
	void naoDeveCadastrarComTituloInvalido() {

		// Given
		Tema tema = temaRepository.save(TestBuilder.criarTema(null, "Testes"));
		Postagem postagem = TestBuilder.criarPostagem("Oi", "Texto com mais de dez caracteres", tema);
		HttpEntity<Postagem> requisicao = JwtHelper.criarRequisicaoComToken(postagem, token);

		// When
		ResponseEntity<String> resposta = testRestTemplate.exchange(BASE_URL, HttpMethod.POST, requisicao, String.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	@Test
	@DisplayName("✔ 03 - Não deve atualizar postagem sem id")
	void naoDeveAtualizarSemId() {

		// Given
		Tema tema = temaRepository.save(TestBuilder.criarTema(null, "Atualização"));
		Postagem postagem = TestBuilder.criarPostagem("Título válido", "Texto com mais de dez caracteres", tema);
		HttpEntity<Postagem> requisicao = JwtHelper.criarRequisicaoComToken(postagem, token);

		// When
		ResponseEntity<String> resposta = testRestTemplate.exchange(BASE_URL, HttpMethod.PUT, requisicao, String.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

}
