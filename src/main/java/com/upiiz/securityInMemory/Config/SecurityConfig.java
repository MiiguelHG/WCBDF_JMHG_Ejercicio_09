package com.upiiz.securityInMemory.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    //Security filter chain -> cadena de filtros de seguridad
    // Singleton -> una sola instancia
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Configuración de la seguridad personalizada
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http
                            .requestMatchers(HttpMethod.GET, "/api/v1/facturas/**").hasAnyAuthority("READ")
                            .requestMatchers(HttpMethod.POST, "/api/v1/facturas/**").hasAnyAuthority("CREATE")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/facturas/**").hasAnyAuthority("UPDATE")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/facturas/**").hasAnyAuthority("DELETE")
                            .anyRequest().denyAll();
                })
                .build();
    }

    //Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Autehntication Provider -> Dao -> Va a proporcionar la autenticación
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    //Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    //UserDetailsService -> base de datos o memoria
    @Bean
    public UserDetailsService userDetailsService() {
        // Definir usuarios en memoria
        // No vamos a usar una base de datos
        UserDetails userMiguel = User.withUsername("Miguel")
                .password("123456")
                .roles("ADMIN")
                .authorities("READ", "CREATE", "UPDATE", "DELETE")
                .build();
        UserDetails userRodrigo = User.withUsername("Rodrigo")
                .password("321")
                .roles("User")
                .authorities("READ", "CREATE")
                .build();
        UserDetails usuarioInvitado = User.withUsername("guest")
                .password("111")
                .roles("GUEST")
                .authorities("READ")
                .build();

        List<UserDetails> usersDetailList = new ArrayList<>();
        usersDetailList.add(userMiguel);
        usersDetailList.add(userRodrigo);
        usersDetailList.add(usuarioInvitado);
        return new InMemoryUserDetailsManager(usersDetailList);
    }
}
