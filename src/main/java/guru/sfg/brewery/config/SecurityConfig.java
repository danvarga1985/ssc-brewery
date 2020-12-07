package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
// Less used method (securedEnabled) - needs @Secured annotation on the Controller method. Unable to use it with SPeL.
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
//        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }
//
//    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
//        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;

    // Needed for use with Spring Data JPA SpEL
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .addFilterBefore(restUrlAuthFilter(authenticationManager()),
//                        UsernamePasswordAuthenticationFilter.class);
//
//        http
//                .addFilterBefore(restHeaderAuthFilter(authenticationManager()),
//                        UsernamePasswordAuthenticationFilter.class)
//                .csrf()
//                .disable();

        http

                .authorizeRequests(authorize -> {
                    /*
                     A. Intercept the authorize request and set permit all, disabling further authentication.
                     B. '**' - standard matching syntax: anything under this path.
                    */
                    authorize
                            .antMatchers("/h2-console/**").permitAll() //do not use in production!
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
//                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**")
//                                .hasAnyRole("ADMIN", "CUSTOMER", "USER")
//                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
//                                .hasAnyRole("ADMIN", "CUSTOMER", "USER")
//                            .mvcMatchers("/beers/find", "/beers/{beerId}")
//                                .hasAnyRole("ADMIN", "CUSTOMER", "USER");
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin(loginConfigurer -> {
                    loginConfigurer
                            .loginProcessingUrl("/login")
                            .loginPage("/").permitAll()
                            .successForwardUrl("/")
                            .defaultSuccessUrl("/")
                            .failureUrl("/?error");
                })
                .logout(logutConfigurer -> {
                    logutConfigurer
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                            .logoutSuccessUrl("/?logout")
                            .permitAll();
                })
                .httpBasic()
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**", "/api/**")
                .and().rememberMe()
                    .tokenRepository(persistentTokenRepository)
                    .userDetailsService(userDetailsService);
//                .and().rememberMe()
//                    .key("sfg-key")
//                    .userDetailsService(userDetailsService);

        // h2 console config
        http.headers().frameOptions().sameOrigin();
    }

    // Useful for more in-depth customization - otherwise see one below.
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        // Set up new UserDetailService
//        return new InMemoryUserDetailsManager(admin, user);
//    }

    // Simpler way of providing a user with the Fluent API.
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("spring")
//                // {noop} - indicates plain text password
////                .password("{noop}guru")
//                .password("{bcrypt}$2a$10$iu/pmpexjVOcrtg6A9BdqOZ1Wq2myrSsOBt61WErW1uNMSTMDRQKK")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}d96d86a1f8a34630367972d96db105b491d5187871af135bd42af0fa438969db65b07cc0055d5ea7")
//                .roles("USER")
//                .and()
//                .withUser("scott")
////                .password("{noop}tiger")
//                .password("{bcrypt10}$2a$15$7ON8i8h3fcSkFVq1gnFogOLJrUtYoFWDT202TU/epkajxje.WW.EG")
//                .roles("CUSTOMER");
//    }


}
