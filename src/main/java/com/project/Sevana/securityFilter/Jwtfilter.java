package com.project.Sevana.securityFilter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.Sevana.service.Jwtservice;
import com.project.Sevana.service.Myuserdetailsservice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class Jwtfilter extends OncePerRequestFilter{//onece per request means meaning it executes exactly once for every incoming HTTP request.
	
	private final Jwtservice jwtservice;
	private final ApplicationContext context;
	@Autowired
	public Jwtfilter(Jwtservice jwtservice,ApplicationContext context) {
		this.jwtservice=jwtservice;
		this.context=context;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbSIsImlhdCI6MTc2NTczMTE4MCwiZXhwIjoxNzY1NzMxMzk2fQ.JbHHzKRPl-yFj-sUgOClGCvxEXaZSQMb_3u2RgKB-3I
		String authHeader = request.getHeader("Authorization");
		String token =null;
		String username =null;
		
		if(authHeader!=null && authHeader.startsWith("Bearer ")) {//removes the bearer from the request token for verification
			token = authHeader.substring(7);
			username = jwtservice.extractusername(token);
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {// checks if user exists and it is not authenticated already
			UserDetails userdetails = context.getBean(Myuserdetailsservice.class).loadUserByUsername(username);
			if(jwtservice.validatetoken(token,userdetails)) {
				UsernamePasswordAuthenticationToken authtoken = new 
						UsernamePasswordAuthenticationToken(userdetails, null, userdetails.getAuthorities());//If the token is valid, it creates an UsernamePasswordAuthenticationToken (an object Spring Security understands).
				authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authtoken);//This is the step that officially "logs the user in" for this specific request.
			}
		}
		filterChain.doFilter(request, response);//filterChain.doFilter(...) allows the request to continue to its destination
	}

}
