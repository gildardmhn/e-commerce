package com.ufc.br.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ufc.br.security.UserDetailsServiceImplementacao;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImplementacao userDetailsServiceImplementacao;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable().authorizeRequests()
/*		.antMatchers(HttpMethod.POST,"/produto/salvar").hasRole("ADMIN")*/
		.antMatchers("/produto/listar").permitAll()
		.antMatchers("/cliente/formulario").permitAll()
		.antMatchers("/produto//detalhes/{id}").permitAll()
/*		.antMatchers(HttpMethod.POST,"/cliente/salvar").hasRole("USER")
*/		.antMatchers(/*HttpMethod.GET,*/"/cliente/listar").hasRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		.formLogin().defaultSuccessUrl("/produto/listar")
		.loginPage("/cliente/logar").permitAll()
		//.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
		.and()
		.logout()
		.logoutSuccessUrl("/cliente/logar?logout")
		.permitAll();	
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImplementacao).passwordEncoder(new BCryptPasswordEncoder());
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**","/img/**","/images/**");
	}
}
