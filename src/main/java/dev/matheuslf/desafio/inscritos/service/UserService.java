package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.request.RequestLogin;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUser;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseLogin;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseUser;
import dev.matheuslf.desafio.inscritos.mapper.UserMapper;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import dev.matheuslf.desafio.inscritos.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public ResponseUser register(@Valid RequestUser request) {
    UserModel user = userMapper.toModel(request);
    user.setPassword(passwordEncoder.encode(request.password()));
    return userMapper.toResponse(userRepository.save(user));
  }

  public ResponseLogin login(@Valid RequestLogin request) throws CredentialException {
    UserModel user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new EntityNotFoundException("usuário com email " + request.email() + " não encontrado;"));
    boolean passwordIsCorrected = passwordEncoder.matches(request.password(), user.getPassword());
    if(!passwordIsCorrected){
      throw new CredentialException("Senha incorreta, por favor tente novamente.");
    }
    String accessToken = jwtService.generateAccessToken(user.getEmail());
    return new ResponseLogin(accessToken);
  }
}
