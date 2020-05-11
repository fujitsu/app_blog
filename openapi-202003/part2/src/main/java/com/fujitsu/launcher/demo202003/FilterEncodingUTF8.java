package com.fujitsu.launcher.demo202003;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.ws.rs.ext.Provider;

@Provider
public class FilterEncodingUTF8 implements Filter {
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws ServletException, java.io.IOException {
    response.setCharacterEncoding("UTF-8");
    chain.doFilter(request,response);
  }
}
