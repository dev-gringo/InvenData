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
    /**
 * MÉTODO GET: Gestiona la salida segura del sistema (Logout).
 * Se activa mediante el enlace a href="LoginServlet?accion=logout">
 */

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    // 1. Capturamos el parámetro 'accion' de la URL
    String accion = request.getParameter("accion");

    // 2. Verificamos si la intención es cerrar sesión
    if (accion != null && accion.equalsIgnoreCase("logout")) {
        // 3. Obtenemos la sesión actual si existe (false significa no crear una nueva)
        HttpSession session = request.getSession(false);
        if (session != null) {
            //  PASO CLAVE: Invalidamos la sesión. 
            // Esto borra todos los atributos (como 'usuarioLogueado') del servidor.
            session.invalidate(); 
        }
        // 4. Redirigimos al Login. Al intentar volver atrás, el Filtro 
        // del ProductoServlet verá que la sesión es nula y lo rebotará.
        response.sendRedirect("index.jsp");
    }
}
    /**
     * MÉTODO POST: Se usa para ENTRAR.
     * Recibe los datos del formulario de Login.
     */

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