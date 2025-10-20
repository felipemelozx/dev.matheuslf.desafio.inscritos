package dev.matheuslf.desafio.inscritos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_user")
public class UserModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;

  @NotBlank
  @Column(unique = true)
  private String email;

  private String password;


  public UserModel() {
  }

  public UserModel(Long id, String name, String email, String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public UserModel setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public UserModel setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public UserModel setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public UserModel setPassword(String password) {
    this.password = password;
    return this;
  }
}
