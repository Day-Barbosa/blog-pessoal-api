package com.generation.blogpessoal.util;

import java.util.HashMap;
import java.util.Map;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;

public class TestBuilder {

	private TestBuilder() {}

	public static Usuario criarUsuario(Long id, String nome, String usuario, String senha) {
		Usuario novoUsuario = new Usuario();
		novoUsuario.setId(id);
		novoUsuario.setNome(nome);
		novoUsuario.setUsuario(usuario);
		novoUsuario.setSenha(senha);
		novoUsuario.setFoto("-");
		return novoUsuario;
	}

	/**
	 * Monta o corpo JSON de um usuário incluindo a senha.
	 * Necessário porque a senha é WRITE_ONLY: o Jackson não a serializa a partir da entidade,
	 * então os testes enviam o JSON como o front-end faz.
	 */
	public static Map<String, Object> paraJson(Usuario usuario) {
		Map<String, Object> json = new HashMap<>();
		json.put("id", usuario.getId());
		json.put("nome", usuario.getNome());
		json.put("usuario", usuario.getUsuario());
		json.put("senha", usuario.getSenha());
		json.put("foto", usuario.getFoto());
		return json;
	}

	public static UsuarioLogin criarUsuarioLogin(String usuario, String senha) {
		UsuarioLogin usuarioLogin = new UsuarioLogin();
		usuarioLogin.setUsuario(usuario);
		usuarioLogin.setSenha(senha);
		return usuarioLogin;
	}

	public static Tema criarTema(Long id, String descricao) {
		Tema tema = new Tema();
		tema.setId(id);
		tema.setDescricao(descricao);
		return tema;
	}

	public static Postagem criarPostagem(String titulo, String texto, Tema tema) {
		Postagem postagem = new Postagem();
		postagem.setTitulo(titulo);
		postagem.setTexto(texto);
		postagem.setTema(tema);
		return postagem;
	}
}
