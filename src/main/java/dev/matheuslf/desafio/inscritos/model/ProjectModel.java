package dev.matheuslf.desafio.inscritos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "tb_project")
public class ProjectModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 255)
  private String description;

  @Column(name = "start_date")
  private Date startDate;

  @Column(name = "end_date")
  private Date endDate;

  @ManyToOne
  @JoinColumn(name = "created_by")
  private UserModel createdBy;


  public ProjectModel() {
  }

  public ProjectModel(Long id, String name, String description, Date startDate, Date endDate, UserModel createdBy) {
    this(id, name, description, startDate, endDate);
    this.createdBy = createdBy;
  }

  public ProjectModel(Long id, String name, String description, Date startDate, Date endDate) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public UserModel getCreatedBy() {
    return createdBy;
  }

  public ProjectModel setCreatedBy(UserModel createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public Long getId() {
    return id;
  }

  public ProjectModel setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public ProjectModel setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public ProjectModel setDescription(String description) {
    this.description = description;
    return this;
  }

  public Date getStartDate() {
    return startDate;
  }

  public ProjectModel setStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public Date getEndDate() {
    return endDate;
  }

  public ProjectModel setEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }
}
