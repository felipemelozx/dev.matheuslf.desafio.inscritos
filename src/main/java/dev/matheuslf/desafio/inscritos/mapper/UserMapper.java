package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.RequestUser;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseUser;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserModel toModel(@Valid RequestUser request) {
    return new UserModel()
        .setEmail(request.email())
        .setName(request.name())
        .setPassword(request.password());
  }

  public ResponseUser toResponse(UserModel userModel) {
    return new ResponseUser(userModel.getId(), userModel.getName(), userModel.getEmail());
  }
}
