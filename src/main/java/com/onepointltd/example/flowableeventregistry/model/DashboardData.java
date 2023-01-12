package com.onepointltd.example.flowableeventregistry.model;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.flowable.task.api.Task;

/**
 * Gives information about the internal state of the engine.
 */
@Builder(toBuilder = true)
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DashboardData {
  private long caseDeploymentCount;
  private long processDeploymentCount;
  private long caseInstanceCount;
  private long processInstanceCount;
  private long taskCount;
  private long reviewEventCount;
  private List<TaskInfo> taskList;
}
