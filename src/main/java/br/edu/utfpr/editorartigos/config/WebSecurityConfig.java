package br.edu.utfpr.editorartigos.config;

import br.edu.utfpr.editorartigos.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private final UsuarioServiceImpl usuarioServiceImpl;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.exceptionHandling().accessDeniedPage("/403")
			.and().formLogin().loginPage("/login")
			.defaultSuccessUrl("/", true)
			.failureUrl("/login?error=bad_credentials").permitAll()
			.and().logout()
			.logoutSuccessUrl("/login")
			.and().authorizeRequests()
//				.antMatchers("/categoria/**").hasAnyRole( "ADMIN")
				.antMatchers("/categoria/**").permitAll()
				.antMatchers("/**").authenticated();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/css/**")
				.antMatchers("/js/**")
				.antMatchers("/images/**")
				.antMatchers("/assets/**")
				.antMatchers("/vendors/**")
				.antMatchers("/webjars/**");
	}
	
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		return usuarioServiceImpl;
	}
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) 
				throws Exception {
		auth.userDetailsService( userDetailsService() )
			.passwordEncoder( passwordEncoder() );
	}
}
