package com.ojt.config;


import com.ojt.repository.StaffInfoRepository;
import com.ojt.repository.SystemUsersRepository;
import com.ojt.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SystemUsersRepository userAccRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final StaffInfoRepository staffRepo;

    public SecurityConfig(SystemUsersRepository userAccRepository,CustomUserDetailsService customUserDetailsService,StaffInfoRepository staffRepo) {
        this.userAccRepository = userAccRepository;
        this.customUserDetailsService=customUserDetailsService;
        this.staffRepo=staffRepo;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler(userAccRepository,staffRepo );
    }

    // ✅ DaoAuthenticationProvider Bean
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // ✅ AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/",
                                "/login",
                                "/forgot-password",
                                "/change-password-first",
                                "/reset-password",
                                "/css/**",
                                "/js/**",
                                "/fragments/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "TRAINER")
                        .requestMatchers("/operator/**").hasAnyRole("OPERATOR", "ADMIN")
                        .requestMatchers("/instructor/**").hasAnyRole("INSTRUCTOR", "ADMIN", "TRAINER")
                        .requestMatchers("/student/**").hasAnyRole("STUDENT")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customLoginSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(86400)
                        .key("yourSecretKey")
                );
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }
}
