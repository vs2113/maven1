package com.blog;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BlogApp {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.addServlet(new ServletHolder(new BlogServlet()), "/*");

        server.setHandler(handler);
        server.start();
        server.join();
    }

    public static class BlogServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String path = req.getPathInfo();
            if (path == null || path.equals("/")) path = "/index.html";

            try {
                String content = Files.readString(Paths.get("src/main/resources/templates" + path));
                resp.setContentType("text/html");
                resp.getWriter().println(content);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("<h1>404 Not Found</h1>");
            }
        }
    }
}