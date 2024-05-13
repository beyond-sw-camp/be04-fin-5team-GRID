package org.highfives.grid.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

//        http.authorizeHttpRequests((authz) ->
//                        authz.requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll());

        /* 설명. /user-service/users/** 패턴을 제외하고는 특정 IP 주소에서 온 요청에만 접근을 허용하며, 그 외의 모든 요청은 인증된 사용자에게만 허용한다는 것을 보여준다. */
        http.authorizeHttpRequests((authz) ->
                            authz
                                    .requestMatchers(new AntPathRequestMatcher("/user", "POST")).permitAll()
                                    .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
//                                    .requestMatchers(new AntPathRequestMatcher("/**")).hasRole("ENTERPRISE")
                                    .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                                    .anyRequest().authenticated()
                );


        return http.build();
    }

}
