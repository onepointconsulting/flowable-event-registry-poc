/*
 * Copyright (c) 2022 Onepoint Consulting Lld.  All rights reserved.
 */

package com.onepointltd.example.flowableeventregistry;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component("Logger")
public class Logger {

  @SuppressWarnings("unused")
  public void log(DelegateExecution execution, String message) {
    log.info("LOG: Process {} activity {}: {}", execution.getProcessDefinitionId(), execution.getCurrentActivityId(), message);
    log(execution);
  }

  public void log(DelegateExecution execution, String message, int amount) {
    log.info("LOG: Process {} activity {}: {} - {}", execution.getProcessDefinitionId(), execution.getCurrentActivityId(), message, amount);
    log(execution);
  }

  public void log(DelegateExecution execution) {
    execution.getVariables().forEach((name, value) -> log.debug("LOG: var {} := {}", name, value));
  }
}
