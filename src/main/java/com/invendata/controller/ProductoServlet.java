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

@WebServlet(name = "ProductoServlet", urlPatterns = {"/ProductoServlet"})
public class ProductoServlet extends HttpServlet {

    ProductoDAO pdao = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. ANTI-CACHÉ Y SEGURIDAD (Se mantiene igual)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        HttpSession session = request.getSession(false); 
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("index.jsp?error=sesion");
            return;
        }

        // 2. CAPTURAR ACCIÓN
        String accion = request.getParameter("accion");
        List<Producto> lista;

        // Si no hay acción, por defecto es listar todo
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                pdao.eliminarLogico(idEliminar);
                response.sendRedirect("ProductoServlet");
                return;

            case "buscar":
                String texto = request.getParameter("txtBusqueda");
                lista = pdao.buscar(texto); // Usa el método que agregamos al DAO
                break;

            case "verCriticos":
                lista = pdao.listarCriticos(); // Usa el método de stock bajo del DAO
                break;

            default:
                lista = pdao.listar(); // Listado normal (Activos)
                break;
        }

        // 3. DESPACHO ÚNICO
        // Sea cual sea la lista (buscada, crítica o total), se envía al mismo JSP
        request.setAttribute("productos", lista);
        request.getRequestDispatcher("productos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        
        if (accion != null) {
            // Capturamos datos comunes para Guardar y Editar
            String nom = request.getParameter("txtNombre");
            String cat = request.getParameter("txtCategoria");
            double pre = Double.parseDouble(request.getParameter("txtPrecio"));
            int min = Integer.parseInt(request.getParameter("txtStockMin"));

            Producto p = new Producto();
            p.setNombre(nom);
            p.setCategoria(cat);
            p.setPrecio(pre);
            p.setStockMinimo(min);

            if (accion.equalsIgnoreCase("guardar")) {
                // LÓGICA PARA NUEVO PRODUCTO
                int sto = Integer.parseInt(request.getParameter("txtStock"));
                p.setStock(sto);
                pdao.registrar(p);
                
            } else if (accion.equalsIgnoreCase("editar")) {
                // LÓGICA PARA ACTUALIZAR EXISTENTE
                int id = Integer.parseInt(request.getParameter("txtId")); // El ID oculto del modal
                p.setId(id);
                pdao.actualizar(p);
            }
        }
        
        response.sendRedirect("ProductoServlet");
    }
}