package com.rohtek.exceptions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.rohtek.controller.EmployeeController;

@Component
public class CustomAccessDenied implements AccessDeniedHandler {
	
	public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);


	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
	
		
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.sendError(401, "You don't have permission to perform this operation!");
		logger.warn("You don't have permission to perform this operation!");

		    
	}

}
