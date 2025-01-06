package backend.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtUtils jwtUtils;

    private final String authHeader = "Bearer ";

    // protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
    //         throws ServletException, IOException {
    //     final String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        
    //     // header validation
    //     if (header == null || header.isEmpty() || !header.startsWith(authHeader)) {
    //         filterChain.doFilter(req, resp);
    //         return;
    //     }

    //     // get jwt token
    //     final String jwtToken = header.split(" ")[1].trim();
    //     // jwt token validation
    //     if (!jwtUtils.validateAccessToken(jwtToken)) {
    //         filterChain.doFilter(req, resp);
    //         return;
    //     }
        
    //     // UserDetails userDetails = userRepository.findByName(jwtUtils.getUsername(jwtToken));
    // }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        final String header = ((HttpServletRequest) req).getHeader(HttpHeaders.AUTHORIZATION);
        
        // header validation
        if (header == null || header.isEmpty() || !header.startsWith(authHeader)) {
            filterChain.doFilter(req, resp);
            return;
        }

        // get jwt token
        final String jwtToken = header.split(" ")[1].trim();
        // jwt token validation
        if (!jwtUtils.validateAccessToken(jwtToken)) {
            filterChain.doFilter(req, resp);
            return;
        }
    }    
}
