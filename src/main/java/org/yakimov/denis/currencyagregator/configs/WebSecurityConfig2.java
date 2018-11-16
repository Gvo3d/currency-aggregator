//package org.yakimov.denis.currencyagregator.configs;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.yakimov.denis.currencyagregator.services.SecurityService;
//import org.yakimov.denis.currencyagregator.services.SecurityServiceImpl;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class WebSecurityConfig2 extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement().sessionFixation().none();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
//        http.csrf().disable().httpBasic();
//
//        http .authorizeRequests()
//                .antMatchers("/auth").permitAll()
//                .antMatchers("/currency/**").hasAnyAuthority("ADMIN", "CLIENT")
//                .antMatchers("/history/**").hasAuthority("ADMIN")
//                .antMatchers("/user/change").hasAnyAuthority("ADMIN", "CLIENT")
//                .antMatchers("/user/create").anonymous();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//    @Bean
//    BCryptPasswordEncoder encoder() {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
//        return encoder;
//    }
//
//    @Bean
//    DaoAuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setPasswordEncoder(encoder());
//        authenticationProvider.setUserDetailsService((UserDetailsService) securityService());
//        return authenticationProvider;
//    }
//
//    @Bean
//    protected SecurityService securityService() {
//        return new SecurityServiceImpl();
//    }
//}