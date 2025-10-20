package dev.matheuslf.desafio.inscritos.exception;

import dev.matheuslf.desafio.inscritos.utils.ApiResponse;
import dev.matheuslf.desafio.inscritos.utils.CustomFieldError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.CredentialException;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<List<CustomFieldError>>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    List<CustomFieldError> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> new CustomFieldError(
            error.getField(),
            error.getDefaultMessage()
        ))
        .toList();

    ApiResponse<List<CustomFieldError>> response = ApiResponse.<List<CustomFieldError>>failure()
        .message("Validation failed")
        .errors(errors)
        .build();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
    ApiResponse<Void> response = ApiResponse.<Void>failure()
        .message(ex.getMessage() != null ? ex.getMessage() : "Entity not found")
        .build();

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException() {
    ApiResponse<Void> response = ApiResponse.<Void>failure()
        .message("Invalid or missing request body")
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(CredentialException.class)
  public ResponseEntity<ApiResponse<CustomFieldError>> handleCredentialException(CredentialException ex) {
    var body = ApiResponse.<CustomFieldError>failure()
        .message("Error ao tentar fazer login.")
        .errors(new CustomFieldError("Password" ,ex.getMessage()))
        .build();
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(body);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<CustomFieldError>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    String defaultMessage = "Erro de integridade no banco de dados.";
    String causeMessage = ex.getLocalizedMessage();

    String fieldName = "Geral";
    if (ex.getCause() != null && ex.getCause().getCause() != null) {
      String dbMessage = ex.getCause().getCause().getMessage();
      if (dbMessage != null && dbMessage.contains("email")) {
        fieldName = "Email";
        causeMessage = "Este e-mail já está cadastrado!";
      }
    }

    var body = ApiResponse.<CustomFieldError>failure()
        .message(defaultMessage)
        .errors(new CustomFieldError(fieldName, causeMessage))
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }
}
