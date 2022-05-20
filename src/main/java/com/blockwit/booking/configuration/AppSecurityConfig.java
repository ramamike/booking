package com.blockwit.booking.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    public AppSecurityConfig(@Qualifier("userDetailServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//        //users from DB, memory authentification
//        auth.inMemoryAuthentication()
//                .withUser("userService")
//                    .password(passwordEncoder().encode("password_service"))
//                                                            .roles("SERVICE_PROVIDER")
//                .and()
//                .withUser("userClient1")
//                    .password(passwordEncoder().encode("password1")).roles("CLIENT")
//                .and()
//                .withUser("userClient2")
//                .password(passwordEncoder().encode("password2")).roles("CLIENT");
//    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception{
        // url for users
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/hotels").authenticated()
                .antMatchers("/book/*").hasRole("CLIENT")
                .antMatchers("/add").hasRole("SERVICE_PROVIDER")
                .antMatchers("/edit/*").hasRole("SERVICE_PROVIDER")
                .antMatchers("/service").hasRole("SERVICE_PROVIDER")
                .anyRequest().permitAll()
                .and().logout().logoutSuccessUrl("/")
                .and().formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}
