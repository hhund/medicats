package de.gecko.medicats.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MedicatsSpringApplication implements WebApplicationInitializer
{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException
	{
		AnnotationConfigWebApplicationContext context = getContext();
		servletContext.addListener(new ContextLoaderListener(context));
	}

	private AnnotationConfigWebApplicationContext getContext()
	{
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation("de.gecko.medicats.web.config");

		return context;
	}
}
