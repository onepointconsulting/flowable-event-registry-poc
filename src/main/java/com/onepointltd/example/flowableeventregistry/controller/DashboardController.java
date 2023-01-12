package com.onepointltd.example.flowableeventregistry.controller;

import com.onepointltd.example.flowableeventregistry.model.DashboardData;
import com.onepointltd.example.flowableeventregistry.model.TaskInfo;
import java.util.List;
import java.util.stream.Collectors;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRepositoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.form.api.FormInfo;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

  private final CmmnRuntimeService cmmnRuntimeService;
  private final RepositoryService repositoryService;
  private final CmmnTaskService cmmnTaskService;
  private final CmmnHistoryService cmmnHistoryService;
  private final RuntimeService runtimeService;
  private final CmmnRepositoryService cmmnRepositoryService;

  private final TaskService taskService;

  public DashboardController(CmmnRuntimeService cmmnRuntimeService, RepositoryService repositoryService, CmmnTaskService cmmnTaskService, CmmnHistoryService cmmnHistoryService,
      RuntimeService runtimeService, CmmnRepositoryService cmmnRepositoryService, TaskService taskService) {
    this.cmmnRuntimeService = cmmnRuntimeService;
    this.repositoryService = repositoryService;
    this.cmmnTaskService = cmmnTaskService;
    this.cmmnHistoryService = cmmnHistoryService;
    this.runtimeService = runtimeService;
    this.cmmnRepositoryService = cmmnRepositoryService;
    this.taskService = taskService;
  }

  @GetMapping("/dashboard")
  public String greetingForm(Model model) {
    long caseDeploymentCount = cmmnRepositoryService.createDeploymentQuery().count();
    long processDeploymentCount = repositoryService.createDeploymentQuery().count();
    long caseInstanceCount = cmmnRuntimeService.createCaseInstanceQuery().count();
    long processInstanceCount = runtimeService.createProcessInstanceQuery().count();
    TaskQuery taskQuery = taskService.createTaskQuery();
    long taskCount = taskQuery.count();
    List<Task> list = taskQuery.list();
    List<TaskInfo> taskInfos = list.stream().map(t -> {
      FormInfo form = taskService.getTaskFormModel(t.getId());
      String formId = form != null ? form.getId() : "";
      return TaskInfo.builder().id(t.getId()).name(t.getName()).formId(formId).build();
    }).collect(Collectors.toList());
    DashboardData dashboardData = DashboardData.builder()
        .caseDeploymentCount(caseDeploymentCount)
        .caseInstanceCount(caseInstanceCount)
        .processInstanceCount(processInstanceCount)
        .processDeploymentCount(processDeploymentCount)
        .taskCount(taskCount)
        .taskList(taskInfos)
        .build();
    model.addAttribute("data", dashboardData);
    return "dashboard";
  }


}
