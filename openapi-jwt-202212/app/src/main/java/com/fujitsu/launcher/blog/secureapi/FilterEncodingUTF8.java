package com.fujitsu.launcher.blog.secureapi;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@WebFilter("/openapi")
public class FilterEncodingUTF8 implements Filter {
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws ServletException, java.io.IOException {
    response.setCharacterEncoding("UTF-8");
    chain.doFilter(request,response);
  }
}
