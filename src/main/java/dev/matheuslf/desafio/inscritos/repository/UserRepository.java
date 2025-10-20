package dev.matheuslf.desafio.inscritos.repository;

import dev.matheuslf.desafio.inscritos.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

  Optional<UserModel> findByEmail(String email);
}
