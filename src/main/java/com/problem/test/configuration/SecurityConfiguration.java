package com.problem.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * For security, we are going to use basic session based authentication.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

  /**
   * Since for simplicity reason from the requirement file stated the initial admin data
   * can be inserted directly into the database.
   * We are going to use this method.
   *
   * @return memory based admin account
   */
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    UserDetails user = User.withUsername("admin")
                           .password(new Sha256PasswordEncoder().encode("admin"))
                           .roles("Admin")
                           .build();
    return new InMemoryUserDetailsManager(user);
  }

  /**
   * The requirement from the email stated it need to use SHA256 for password encoder
   * @return PasswordEncoder based on SHA256
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new Sha256PasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
          .antMatchers(
              "/login.xhtml",
              "/public/**"
//              "/api/mobile/**"
          ).permitAll() // Allow public access to these endpoints
          .anyRequest()
          .authenticated()                     // Secure all other endpoints
        .and()
          .formLogin()
          .loginPage("/login.xhtml")
            .loginProcessingUrl("/login")// Custom login page; you can create your own controller/view
            .defaultSuccessUrl("/products.xhtml", true)
            .permitAll()
        .and()
          .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .invalidateHttpSession(true)
            .permitAll();
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authBuilder
        .userDetailsService(userDetailsService())
        .passwordEncoder(passwordEncoder());
    return authBuilder.build();
  }
}
