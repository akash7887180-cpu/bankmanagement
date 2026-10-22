package com.apexbank.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Enforces session authentication for ATM operations and Admin Panel routes.
 */
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        // Allow static assets, error pages, and login endpoints
        if (path.startsWith("/assets/") || path.equals("/login") || path.equals("/admin/login")
                || path.equals("/atm/login") || path.equals("/otp") || path.equals("/index.jsp") || path.equals("/")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);

        // Protect Admin routes
        if (path.startsWith("/admin")) {
            if (session == null || session.getAttribute("SESSION_ADMIN") == null) {
                response.sendRedirect(contextPath + "/admin/login?error=Please+login+as+admin+to+continue.");
                return;
            }
        }

        // Protect ATM Customer routes
        if (path.startsWith("/atm/")) {
            if (session == null || session.getAttribute("SESSION_CARD") == null) {
                response.sendRedirect(contextPath + "/login?error=Session+expired.+Please+insert+your+card+and+PIN.");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}
