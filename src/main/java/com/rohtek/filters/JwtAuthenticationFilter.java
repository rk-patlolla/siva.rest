package com.rohtek.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohtek.entities.Employee;
import com.rohtek.exceptions.Response;
import com.rohtek.jwt.JwtTokenUtil;
import com.rohtek.repository.EmployeeRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	MessageSource message;
	
	 @Autowired
	EmployeeRepository employeeRepository;

	@Value("${jwt.TOKEN_PREFIX}")
    private String TOKEN_PREFIX;
	
	@Value("${jwt.HEADER_STRING}")
    private String HEADER_STRING;
	
	 @Override
	    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
	        String header = req.getHeader(HEADER_STRING);
	        String ename = null;
	        String authToken = null;
           

	        if (header != null && header.startsWith(TOKEN_PREFIX)) {
	            authToken = header.replace(TOKEN_PREFIX,"");
	            try {
	            	ename = jwtTokenUtil.getUsernameFromToken(authToken);
	            	

	            } catch (IllegalArgumentException e) {
	                logger.error("an error occured during getting username from token", e);
	            } catch (ExpiredJwtException e) {
	                logger.warn("the token is expired and not valid anymore", e);
	                Response response = new Response();
	                response.setMessage(message.getMessage("jwt.token.expire", null, Locale.US));
	                   byte[] responseToSend = restResponseBytes(response);
	                    ((HttpServletResponse) res).setHeader("Content-Type", "application/json");
	                    ((HttpServletResponse) res).setStatus(401);
	                    res.getOutputStream().write(responseToSend);
	                    return;  
	            } catch(SignatureException e){
	                logger.error("Authentication Failed. Username or Password not valid.");
	            }
	        } else {
	            logger.warn("couldn't find bearer string, will ignore the header");
	        }
	        if (ename != null && SecurityContextHolder.getContext().getAuthentication() == null) {

	            Employee employeeDetails = employeeRepository.findByename(ename);
	            if (jwtTokenUtil.validateToken(authToken, employeeDetails)) {
	                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(employeeDetails, null, Arrays.asList(new SimpleGrantedAuthority(employeeDetails.getRole())));
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
	                logger.info("authenticated user " + ename + ", setting security context");
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	            }
	        }

	        chain.doFilter(req, res);
	    }
	 private byte[] restResponseBytes(Response exceptionResponse) throws IOException {
	       String serialized = new ObjectMapper().writeValueAsString(exceptionResponse);
	       return serialized.getBytes();
	   }

}
