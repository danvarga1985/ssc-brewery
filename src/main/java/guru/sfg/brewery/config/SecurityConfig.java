package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    /*
                     A. Intercept the authorize request and set permit all, disabling further authentication.
                     B. '**' - standard matching syntax: anything under this path.
                    */
                    authorize
                            .antMatchers("/", "/webjars/**", "/login", "/beers/find",
                                    "/resources/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();

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
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                // {noop} - indicates plain text password
//                .password("{noop}guru")
                .password("{bcrypt}$2a$10$iu/pmpexjVOcrtg6A9BdqOZ1Wq2myrSsOBt61WErW1uNMSTMDRQKK")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}d96d86a1f8a34630367972d96db105b491d5187871af135bd42af0fa438969db65b07cc0055d5ea7")
                .roles("USER")
                .and()
                .withUser("scott")
//                .password("{noop}tiger")
                .password("{bcrypt15}$2a$15$7ON8i8h3fcSkFVq1gnFogOLJrUtYoFWDT202TU/epkajxje.WW.EG")
                .roles("CUSTOMER");

    }
}
