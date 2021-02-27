package org.sid.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtProvider jwtProvider;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	    throws ServletException, IOException {
	String jwt = getJwtFromRequest(request);
	
	// Testing if the token is valid
	if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
	    String username = jwtProvider.getUsernameFromJwt(jwt);
	    
	    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	    
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
	String bearerToken = request.getHeader("Authorization");
	if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	    // Example of Token : Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMiJ9.ec0VmL-GYxEYb-41k9OSKxhFQasPr43K47BigpYmVHZubMPez3_mznpDxNkE_SMa1pSLfrU136StDaeRygKbnUwFFvnXsI8xnMb6CEl18LpXNkDo_xDf_8WD9docoQxfSjo1U94HNDcZUEyip5hK5O7kogLSI2_Pxefk6JrQsKxxUIhC1AZbRxgCSA3BmTpWtfvz44n4UVoO0Z9clE7PVfxpYTnaHBEOCu40AoIQ4JhKhj2inNm0nwWok9DdqMQ_Q2wXu-XqC_wUdYG9esAlZraOqn53-d6Z4u0JYMTIDg2S6lBKOB94ceUuQvnYaOCPz_AjZo8DVWeBe3qIBZu2AA
	    // We want only the string after "Bearer" (which is the token)
	    return bearerToken.substring(7);
	}
	return bearerToken;
    }

}
