package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import model.Account;

@WebFilter(filterName = "AunthenticationFilter", urlPatterns = {"/managerAccount", "/manager", "/managerCategory"})
public class AunthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        // kiem tra dang nhap
        Account account = (Account) session.getAttribute("acc");

        if (account != null) {
            // neu la admin truy cap vao cac trang
            if (account.getIsAdmin() == 1) {
                chain.doFilter(request, response);
            }
            // neu la nguoi ban, chi duoc truy cap managerProduct
            else if (account.getIsSell() == 1 && req.getRequestURI().endsWith("/manager")) {
                chain.doFilter(request, response);
            } else {
                // neu la nguoi dung khong duoc truy cap vao trang nao ca
                 res.sendRedirect("login.jsp");
            }
        } else {
            // neu chua dang nhap thi chuyen huong toi dang nhap 
            res.sendRedirect("login.jsp");
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }
}
