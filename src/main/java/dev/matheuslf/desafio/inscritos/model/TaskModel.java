package dev.matheuslf.desafio.inscritos.model;

import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id", nullable = false)
  private ProjectModel project;

  @ManyToOne
  @JoinColumn(name = "created_by")
  private UserModel createdBy;

  @ManyToOne
  @JoinColumn(name = "assignee_id")
  private UserModel assignee;

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

  public TaskModel(Long id, String title, String description, StatusTask status, PriorityTask priority, Date dueDate, ProjectModel project, UserModel createdBy, UserModel assignee) {
    this(id, title, description, status, priority, dueDate, project);
    this.createdBy = createdBy;
    this.assignee = assignee;
  }

  public UserModel getAssignee() {
    return assignee;
  }

  public TaskModel setAssignee(UserModel assignee) {
    this.assignee = assignee;
    return this;
  }

  public UserModel getCreatedBy() {
    return createdBy;
  }

  public TaskModel setCreatedBy(UserModel createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public Long getId() {
    return id;
  }

  public TaskModel setId(Long id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public TaskModel setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public TaskModel setDescription(String description) {
    this.description = description;
    return this;
  }

  public StatusTask getStatus() {
    return status;
  }

  public TaskModel setStatus(StatusTask status) {
    this.status = status;
    return this;
  }

  public PriorityTask getPriority() {
    return priority;
  }

  public TaskModel setPriority(PriorityTask priority) {
    this.priority = priority;
    return this;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public TaskModel setDueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  public ProjectModel getProject() {
    return project;
  }

  public TaskModel setProject(ProjectModel project) {
    this.project = project;
    return this;
  }
}
