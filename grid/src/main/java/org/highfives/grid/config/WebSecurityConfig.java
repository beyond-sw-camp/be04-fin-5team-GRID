package org.highfives.grid.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.highfives.grid.security.AuthenticationFilter;
import org.highfives.grid.security.GridLogoutFilter;
import org.highfives.grid.security.JwtFilter;
import org.highfives.grid.security.JwtUtil;
import org.highfives.grid.user.command.repository.TokenReissueRepository;
import org.highfives.grid.user.command.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final TokenReissueRepository tokenReissueRepository;
    private final Environment environment;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public WebSecurityConfig(UserService userService,
                             TokenReissueRepository tokenReissueRepository,
                             Environment environment,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             JwtUtil jwtUtil) {
        this.userService = userService;
        this.tokenReissueRepository = tokenReissueRepository;
        this.environment = environment;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {


        http.cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("**").permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

//        http.authorizeHttpRequests((authz) ->
//                            authz
////                                    .requestMatchers(new AntPathRequestMatcher("/users/*", "POST")).hasRole("ADMIN")
////                                    .requestMatchers(new AntPathRequestMatcher("/users/*", "GET")).hasAnyRole("ADMIN", "USER")
//                                    .requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll()
//                                    .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
//                                    .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
//                                    .requestMatchers(new AntPathRequestMatcher("/reissue")).permitAll()
//                                    .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
//                                    .anyRequest().authenticated()
//                )
//                .authenticationManager(authenticationManager)
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilter(getAuthenticationFilter(authenticationManager));
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new GridLogoutFilter(tokenReissueRepository, jwtUtil), LogoutFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, environment, tokenReissueRepository);
    }
}
