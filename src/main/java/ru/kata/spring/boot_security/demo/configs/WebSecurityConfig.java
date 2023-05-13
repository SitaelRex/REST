package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private UserServiceImpl userDetailsService;
    @Autowired
    public WebSecurityConfig(UserServiceImpl userDetailsService, SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }


   // @Autowired
   // protected void  configureGlobal(AuthenticationManagerBuilder  auth) throws Exception {
   //     auth.inMemoryAuthentication().withUser("user").password("{noop}"+"pass").roles("USER");
   // }

    //public WebSecurityConfig(SuccessUserHandler successUserHandler) {
    //    this.successUserHandler = successUserHandler;
   // }

    @Override
    protected void configure( HttpSecurity http ) throws Exception { //HttpSecurity http ,
        http
                .authorizeRequests()
               // .antMatchers("/", "/index").permitAll()
               // .anyRequest().authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                        .permitAll()
                        .and()
                        .logout()
                        .permitAll();
        }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //HttpSecurity http ,
        auth.userDetailsService(userDetailsService);

      //  http
      //          .authorizeRequests()
      //          //.antMatchers("/", "/index").permitAll()
      //          //.anyRequest().authenticated()
      //          .antMatchers("/admin/**").hasRole("ADMIN")
      //          .antMatchers("/user/**").hasRole("USER")
      //          .and()
      //          .formLogin().successHandler(successUserHandler)
      //          .permitAll()
      //          .and()
      //          .logout()
      //          .permitAll();
    }

    // аутентификация inMemory
   // @Bean
   // @Override
   // public UserDetailsService userDetailsService() {
   //     UserDetails user =
   //             User.withDefaultPasswordEncoder()
   //                     .username("user")
   //                     .password("user")
   //                     .roles("USER")
   //                     .build();
//
   //     UserDetails admin =
   //             User.withDefaultPasswordEncoder()
   //                     .username("admin")
   //                     .password("admin")
   //                     .roles("USER", "ADMIN")
   //                     .build();
//
   //     return new InMemoryUserDetailsManager(user,admin);
   // }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}