package pl.ymz.todoapp.controller;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

    }
}
