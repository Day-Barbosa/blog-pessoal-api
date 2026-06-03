package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity // Define que a classe postagem vai se tornar uma tabela
@Table(name = "tb_postagens") // Define o nome da tabela seguindo o padrão predefinido
public class Postagem {

    @Id // Define a chave primária (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define que o campo é gerado automaticamente
    private Long id; // bigint

    // Indicando que o título é obrigatório, ou seja, no banco de dados seria NOT NULL
    @NotBlank(message = "O atributo título é obrigatório") // Não pode ser nulo ou vazio
    @Size(min = 5, max = 100, message = "O atributo título deve conter no mínimo 5 e no máximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "O atributo texto é obrigatório") // Não pode ser nulo ou vazio
    @Size(min = 10, max = 1000, message = "O atributo texto deve conter no mínimo 10 e no máximo 1000 caracteres")
    private String texto;

    @UpdateTimestamp // Atualiza automaticamente com a data/hora da última modificação
    private LocalDateTime data;
    
    @ManyToOne // Muitas postagens podem estar ligadas a um único tema
    @JsonIgnoreProperties("postagem") // Evita recursividade ao buscar o tema da postagem
    private Tema tema;
    
    @ManyToOne // Muitas postagens podem estar ligadas a um único usuário
    @JsonIgnoreProperties("postagem") // Evita recursividade ao buscar o usuário da postagem
    private Usuario usuario;

    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getData() {
        return data;
    }
    
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    
	public Tema getTema() {
		return tema;
	}
	
	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}