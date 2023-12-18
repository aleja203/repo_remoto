package com.egg.appsalud;

import com.egg.appsalud.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;
    
    @Autowired
    public UsuarioServicio us;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(us).passwordEncoder(new BCryptPasswordEncoder());
    }

    public CustomSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/panelAdmin/*").hasRole("ADMIN")
                .antMatchers("/historia_clinica/*").hasAnyRole("PACIENTE","PROFESIONAL")
                .antMatchers("/paciente/*").hasAnyRole("ADMIN", "USER","PACIENTE")
                .antMatchers("/profesional/*").hasAnyRole("ADMIN", "PROFESIONAL")
                .antMatchers("/css/*", "/js/*", "/img/*", "/**", "/portal/*","/util/*").permitAll()
                .and()
                .formLogin()
                .loginPage("/portal/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("username")
                .passwordParameter("password")
                //.defaultSuccessUrl("/")
                .successHandler(customSuccessHandler())
                .permitAll()
                .and().logout()
                .logoutUrl( "/portal/logout")
                .logoutSuccessUrl("/")
                .permitAll()

                .and().csrf()
                .disable()
                .requestCache()
                    .requestCache(new HttpSessionRequestCache());
    }


}
