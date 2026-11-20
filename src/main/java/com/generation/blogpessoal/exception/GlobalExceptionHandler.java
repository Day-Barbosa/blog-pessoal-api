package com.generation.blogpessoal.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Padroniza a resposta dos erros de validação (@Valid) em um JSON legível para o front-end:
 *
 * <pre>
 * {
 *   "status": 400,
 *   "erro": "Dados inválidos",
 *   "campos": { "titulo": "O atributo título deve conter no mínimo 5 e no máximo 100 caracteres" },
 *   "timestamp": "2026-11-20T19:30:00"
 * }
 * </pre>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidacao(MethodArgumentNotValidException ex) {

		Map<String, String> campos = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));

		Map<String, Object> corpo = new LinkedHashMap<>();
		corpo.put("status", HttpStatus.BAD_REQUEST.value());
		corpo.put("erro", "Dados inválidos");
		corpo.put("campos", campos);
		corpo.put("timestamp", LocalDateTime.now().toString());

		return ResponseEntity.badRequest().body(corpo);
	}

}
