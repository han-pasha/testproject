package com.problem.test.view;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class LoginScope implements Serializable {

  private String username;
  private String password;

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
}
