
package com.example.rod.security.config;

import com.example.rod.security.handler.FailureHandler;
import com.example.rod.security.handler.SuccessHandler;
import com.example.rod.security.jwt.JwtAccessDeniedHandler;
import com.example.rod.security.jwt.JwtAuthFilter;
import com.example.rod.security.jwt.JwtAuthenticationEntryPoint;
import com.example.rod.security.jwt.JwtUtil;
import com.example.rod.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.util.Instantiator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final SuccessHandler successHandler;
    private final FailureHandler failureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();//.ignoringAntMatchers("/h2-console/**").
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests()
                .antMatchers( "/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/questions/all", "/questions/specific/**").permitAll()
                .antMatchers("/admin/auth/**").permitAll()
                .antMatchers("/admin/**").hasRole(UserRole.ADMIN.toString())
//                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();
        http
                .logout().logoutSuccessUrl("/")
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


//        http.oauth2Login(login -> login
//                .successHandler(successHandler)
//                .failureHandler(failureHandler));
//                .userInfoEndpoint();
//                .userService(customOAuth2UserService));
        http.exceptionHandling().accessDeniedPage("/user/forbidden");
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS","HEAD")
                .exposedHeaders("Authorization");
    }
}






