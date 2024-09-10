package com.example.photocontest.config;

//import com.example.photocontest.services.CustomOAuth2UserService;
import com.example.photocontest.security.CustomOAuth2User;
import com.example.photocontest.services.CustomOAuth2UserService;
import com.example.photocontest.services.contracts.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final int BCRYPT_STRENGTH = 11;
    private final CustomOAuth2UserService oauthUserService;
    private final UserService userService;

    public SecurityConfig(CustomOAuth2UserService oauthUserService, @Lazy UserService userService) {
        this.oauthUserService = oauthUserService;
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers("/myCards").authenticated()
                                        .requestMatchers("/auth/login").permitAll()
                                        .requestMatchers("/home").permitAll()
                                        .requestMatchers("/posts").permitAll()
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/users/**").authenticated()
                                        .requestMatchers("/users").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/users/").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/api/users").authenticated()
                                        .requestMatchers(HttpMethod.POST, "api/users/register").permitAll()
                                        .requestMatchers(HttpMethod.POST, "api/users/admin/register").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "api/users/admin/register/").hasRole("ADMIN")
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/contact").permitAll()

                )
                .httpBasic(withDefaults())
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/auth/login?error=true")
                        .permitAll())
                .oauth2Login(oauth -> oauth
                        .loginPage("/auth/login")
                        .userInfoEndpoint(info -> info.userService(oauthUserService))
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                                userService.processOAuthPostLogin(oauthUser.getEmail());
                                response.sendRedirect("/home");
                            }
                        }))
                .logout(logout ->
                        logout
                                .logoutUrl("/auth/logout") // URL за логаут
                                .invalidateHttpSession(true) // Изтриване на сесията
                                .clearAuthentication(true) // Изчистване на аутентикацията
                                .deleteCookies("JSESSIONID") // Изтриване на бисквитките, свързани със сесията
                                .logoutSuccessUrl("/home") // Пренасочване към /home след успешен логаут
                                .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }



}