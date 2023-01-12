package com.onepointltd.example.flowableeventregistry.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder(toBuilder = true)
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskInfo {

  private String id;
  private String name;
  private String formId;
}
