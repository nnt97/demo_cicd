package com.nnthai1997.cicd.bean;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

@Component
public class GitEvent {
  
  @EventListener
  public void handleEvent (RequestHandledEvent e) {
      System.out.println("-- RequestHandledEvent --");
      System.out.println(e);
  }
}
