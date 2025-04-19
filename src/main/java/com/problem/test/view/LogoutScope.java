package com.problem.test.view;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named
@RequestScoped
public class LogoutScope {

  public String logout() {
    HttpServletRequest request =
        (HttpServletRequest) javax.faces.context.FacesContext.getCurrentInstance()
                                 .getExternalContext().getRequest();

    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate(); // Invalidate session
    }

    return "/login.xhtml?faces-redirect=true"; // Redirect to login page
  }
}
