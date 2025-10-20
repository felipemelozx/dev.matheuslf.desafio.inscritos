package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.request.RequestLogin;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUser;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseLogin;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseUser;
import dev.matheuslf.desafio.inscritos.service.UserService;
import dev.matheuslf.desafio.inscritos.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<ResponseUser>> register(@RequestBody
                                                 @Valid
                                                 RequestUser request){
    ResponseUser user = userService.register(request);
    var body = ApiResponse.<ResponseUser>success()
        .message("Usuário criado com sucesso.")
        .data(user)
        .build();
    return ResponseEntity.ok().body(body);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<ResponseLogin>> login(@RequestBody
                                                          @Valid
                                                          RequestLogin request
                                                          ) throws CredentialException {
    ResponseLogin response = userService.login(request);
    var body = ApiResponse.<ResponseLogin>success()
        .message("Login realizado com sucesso.")
        .data(response)
        .build();
    return ResponseEntity.ok().body(body);
  }
}
