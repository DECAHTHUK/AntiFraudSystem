package antifraud.config;


import antifraud.persistence.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .mvcMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .mvcMatchers("/api/auth/list").hasAnyRole("ADMINISTRATOR", "SUPPORT")
                .mvcMatchers(HttpMethod.DELETE, "/api/auth/user/{username}").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.DELETE, "/api/auth/user/").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasRole("MERCHANT")
                .mvcMatchers(HttpMethod.PUT, "/api/auth/access").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.PUT, "/api/auth/role").hasRole("ADMINISTRATOR")
                .mvcMatchers("api/antifraud/suspicious-ip").hasRole("SUPPORT")
                .mvcMatchers("api/antifraud/stolencard").hasRole("SUPPORT")
                .mvcMatchers("api/antifraud/stolencard/{number}").hasRole("SUPPORT")
                .mvcMatchers("api/antifraud/suspicious-ip/{ip}").hasRole("SUPPORT")
                .mvcMatchers(HttpMethod.GET, "/api/antifraud/history").hasRole("SUPPORT")
                .mvcMatchers(HttpMethod.PUT, "/api/antifraud/transaction").hasRole("SUPPORT")
                .mvcMatchers(HttpMethod.GET, "/api/antifraud/history/{number}").hasRole("SUPPORT")
                .mvcMatchers("/actuator/shutdown").permitAll().mvcMatchers("/actuator/shutdown").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
