 package com.spdcl.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception { http
	 * .csrf() .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	 * }
	 */

	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.headers().contentSecurityPolicy(
	 * "script-src 'self' https://trustedscripts.example.com; object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/"
	 * ); }
	 */
	 
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().xssProtection().block(false);
	}
	
	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception { http
	 * .csrf().disable() .headers() .defaultsDisabled() .xssProtection(); }
	 */
   
   	
}
