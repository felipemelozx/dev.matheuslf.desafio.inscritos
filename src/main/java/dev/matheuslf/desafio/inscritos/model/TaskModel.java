package dev.matheuslf.desafio.inscritos.model;

import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "tb_task")
public class TaskModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 150)
  private String title;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private StatusTask status;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private PriorityTask priority;

  @Column(name = "due_date")
  private Date dueDate;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private ProjectModel project;

  public TaskModel() {
  }

  public TaskModel(Long id, String title, String description, StatusTask status, PriorityTask priority, Date dueDate, ProjectModel project) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.priority = priority;
    this.dueDate = dueDate;
    this.project = project;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public StatusTask getStatus() {
    return status;
  }

  public void setStatus(StatusTask status) {
    this.status = status;
  }

  public PriorityTask getPriority() {
    return priority;
  }

  public void setPriority(PriorityTask priority) {
    this.priority = priority;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public ProjectModel getProject() {
    return project;
  }

  public void setProject(ProjectModel project) {
    this.project = project;
  }
}
