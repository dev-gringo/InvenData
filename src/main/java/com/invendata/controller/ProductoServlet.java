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

@WebServlet(name = "ProductoServlet", urlPatterns = {"/ProductoServlet"})
public class ProductoServlet extends HttpServlet {

    ProductoDAO pdao = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener la lista de productos desde el DAO
        List<Producto> lista = pdao.listar();
        
        // 2. Guardar la lista en el "alcance" de la petición (request)
        request.setAttribute("productos", lista);
        
        // 3. Enviar la información a la página JSP (La Vista)
        request.getRequestDispatcher("productos.jsp").forward(request, response);
    }
    
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    String accion = request.getParameter("accion");
    
    if (accion.equalsIgnoreCase("guardar")) {
        // 1. Capturar datos del formulario
        String nom = request.getParameter("txtNombre");
        String cat = request.getParameter("txtCategoria");
        double pre = Double.parseDouble(request.getParameter("txtPrecio"));
        int sto = Integer.parseInt(request.getParameter("txtStock"));
        int min = Integer.parseInt(request.getParameter("txtStockMin"));

        // 2. Crear el objeto Producto
        Producto p = new Producto();
        p.setNombre(nom);
        p.setCategoria(cat);
        p.setPrecio(pre);
        p.setStock(sto);
        p.setStockMinimo(min);

        // 3. Llamar al DAO para insertar en BD
        pdao.registrar(p);
        
        // 4. Redirigir de nuevo al listado para ver el cambio
        response.sendRedirect("ProductoServlet");
    }
}
}
