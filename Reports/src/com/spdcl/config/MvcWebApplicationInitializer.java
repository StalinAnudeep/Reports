package com.spdcl.config;

import javax.servlet.ServletRegistration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer  {

	  @Override
	   protected Class<?>[] getRootConfigClasses() {
		 return new Class[] {WebSecurityConfig.class}; 
	   }

	   @Override
	   protected Class<?>[] getServletConfigClasses() {
	      return new Class[] { WebMvcConfig.class };
	   }

	   @Override
	   protected String[] getServletMappings() {
	      return new String[] { "/" };
	   }

	   @Override
	   protected void customizeRegistration(ServletRegistration.Dynamic registration) {
	       boolean done = registration.setInitParameter("throwExceptionIfNoHandlerFound", "true"); // -> true
	       if(!done) throw new RuntimeException();
	   }
}
