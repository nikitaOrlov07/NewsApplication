package com.example.demo.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Configure Security Filter chain - chain (цепочка) of methods , that will control how your security works
    UserDetailsService userDetailsService;
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {this.userDetailsService = userDetailsService;}
    @Bean
    public static PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // отключает защиту от межсайтовой подделки запросов (CSRF). В реальном приложении это может быть небезопасно, и вы должны включить защиту CSRF, если ваше приложение подвержено этому типу атак.
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/news")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/news/{newsId}")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/news/find")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/comments")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/events/{eventId}")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/register/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/assets/**")).permitAll()

                        // Only administrator can get access to these web-pages
                        .requestMatchers(new AntPathRequestMatcher("/users-list")).hasAuthority("ADMIN") // hasAutority - checks users role
                        .requestMatchers(new AntPathRequestMatcher("/users/{userId}/delete")).hasAuthority("ADMIN")
                        // nobody can delete ADMIN user (even ADMIN himself)
                        .requestMatchers(new AntPathRequestMatcher("/users/2/delete")).denyAll()
                        .anyRequest().authenticated()) //other URLs are only allowed authenticated users.

                // Это указывает, что запросы к определенным URL-адресам должны быть разрешены для всех пользователей, включая анонимных.
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/news?successLogin",true)
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")// = ?fail
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );
        return http.build();}
    //Authentication Manager
    public void configure(AuthenticationManagerBuilder builder) throws Exception
    {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}