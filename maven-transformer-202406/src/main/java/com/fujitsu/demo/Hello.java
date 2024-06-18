package com.fujitsu.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class Hello extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Hello !\n");
        String javaVersion = System.getProperty("java.version");
        String glassfishVersion = System.getProperty("glassfish.version");
        response.getWriter().append("java.version : " + javaVersion + "\n");
        response.getWriter().append("glassfish.version : " + glassfishVersion + "\n");
    }

}
