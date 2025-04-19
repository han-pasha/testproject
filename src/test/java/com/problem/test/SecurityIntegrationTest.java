package com.problem.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldLoginWithCorrectCredentialsAndCreateSession() throws Exception {
    MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/login")
                                                                    .param("username", "admin")
                                                                    .param("password", "admin"))
                                                    .andExpect(status().is3xxRedirection())
                                                    .andExpect(redirectedUrl("/products.xhtml"))
                                                    .andReturn()
                                                    .getRequest()
                                                    .getSession(false);

    assertThat(session).isNotNull();
  }

  @Test
  void shouldFailLoginWithInvalidCredentials() throws Exception {
    mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "wrongpassword"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login.xhtml?error"));
  }

  @Test
  void shouldAccessProtectedEndpointAfterLogin() throws Exception {
    MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/login")
                                                                    .param("username", "admin")
                                                                    .param("password", "admin"))
                                                    .andReturn()
                                                    .getRequest()
                                                    .getSession(false);

    mockMvc.perform(get("/products.xhtml").session(session))
        .andExpect(status().isOk()); // Assuming this endpoint is accessible and returns view/html
  }

  @Test
  @WithAnonymousUser
  void shouldRedirectToLoginPageWhenAccessingProtectedPageWithoutLogin() throws Exception {
    mockMvc.perform(get("/products.xhtml"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/login.xhtml"));
  }

  @Test
  void shouldLogoutSuccessfully() throws Exception {
    MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/login")
                                                                    .param("username", "admin")
                                                                    .param("password", "admin"))
                                                    .andReturn()
                                                    .getRequest()
                                                    .getSession(false);

    mockMvc.perform(post("/logout").session(session))
        .andExpect(status().is3xxRedirection());

    mockMvc.perform(get("/products.xhtml").session(session))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/login.xhtml"));
  }
}
