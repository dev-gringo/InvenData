package com.invendata.controller;

import com.invendata.dao.UsuarioDAO;
import com.invendata.model.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    UsuarioDAO udao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String user = request.getParameter("txtUser");
        String pass = request.getParameter("txtPass");

        Usuario u = udao.validar(user, pass);

        if (u != null) {
            // ✅ Credenciales correctas: Creamos la sesión
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", u);
            // Redirigimos al panel de productos
            response.sendRedirect("ProductoServlet");
        } else {
            // ❌ Error: Redirigimos al login con mensaje de error
            response.sendRedirect("index.jsp?error=invalido");
        }
    }
}