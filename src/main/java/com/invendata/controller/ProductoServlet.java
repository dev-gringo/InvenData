package com.invendata.controller;

import com.invendata.dao.ProductoDAO;
import com.invendata.model.Producto;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador principal para la gestión de Productos.
 * Maneja el listado, la creación y la seguridad de acceso a la vista.
 */
@WebServlet(name = "ProductoServlet", urlPatterns = {"/ProductoServlet"})
public class ProductoServlet extends HttpServlet {

    // Instancia del DAO para interactuar con la base de datos de productos
    ProductoDAO pdao = new ProductoDAO();

    /**
     * MÉTODO GET: Se encarga de mostrar la interfaz de usuario.
     * Incluye las reglas de seguridad y limpieza de caché.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        /* * 1. PROTECCIÓN DE CABECERAS (Anti-Caché)
         * Estas líneas obligan al navegador a pedir una copia nueva al servidor 
         * en cada clic, impidiendo que se pueda volver atrás después del Logout.
         */
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // Protocolo HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // Protocolo HTTP 1.0 (Retrocompatibilidad)
        response.setDateHeader("Expires", 0); // Para servidores Proxy

        /* * 2. FILTRO DE SEGURIDAD (Validación de Sesión)
         * Verificamos si existe un usuario autenticado en la sesión actual.
         */
        HttpSession session = request.getSession(false); 
        
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            // Si no hay sesión, abortamos y redirigimos al Login
            response.sendRedirect("index.jsp?error=sesion");
            return; // Importante para detener la ejecución del código inferior
        }

        /* * 3. CARGA DE DATOS
         * Obtenemos la lista de productos y la enviamos al JSP.
         */
        List<Producto> lista = pdao.listar();
        request.setAttribute("productos", lista);
        request.getRequestDispatcher("productos.jsp").forward(request, response);
    }
    /**
     * MÉTODO POST: Se encarga de procesar las acciones que envían datos (Formularios).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Identificamos qué acción desea realizar el usuario
        String accion = request.getParameter("accion");
        
        if (accion != null && accion.equalsIgnoreCase("guardar")) {
            // 1. Capturar datos del formulario JSP
            String nom = request.getParameter("txtNombre");
            String cat = request.getParameter("txtCategoria");
            double pre = Double.parseDouble(request.getParameter("txtPrecio"));
            int sto = Integer.parseInt(request.getParameter("txtStock"));
            int min = Integer.parseInt(request.getParameter("txtStockMin"));

            // 2. Mapear datos al Objeto de Modelo (POJO)
            Producto p = new Producto();
            p.setNombre(nom);
            p.setCategoria(cat);
            p.setPrecio(pre);
            p.setStock(sto);
            p.setStockMinimo(min);

            // 3. Persistencia: Guardar en la base de datos mediante el DAO
            pdao.registrar(p);
            
            // 4. Redirección: Volvemos a llamar al Servlet (GET) para refrescar la tabla
            response.sendRedirect("ProductoServlet");
        }
    }
}