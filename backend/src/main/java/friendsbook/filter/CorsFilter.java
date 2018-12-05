package friendsbook.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class CorsFilter implements javax.servlet.Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language,"
                + " Host, Referer, Connection, User-Agent, authorization, sw-useragent, sw-version");
        if (((HttpServletRequest)request).getMethod().equals("OPTIONS")) {
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
