package friendsbook.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.HttpMethod;
import org.springframework.http.HttpHeaders;

public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, DELETE, PUT, OPTIONS");
        res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language,"
                + " Host, Referer, Connection, User-Agent, authorization, sw-useragent, sw-version");
        if (((HttpServletRequest)request).getMethod().equals(HttpMethod.OPTIONS)) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }
}
