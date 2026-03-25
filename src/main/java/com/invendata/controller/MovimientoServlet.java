package com.invendata.controller;

import com.invendata.dao.MovimientoDAO;
import com.invendata.dao.ProductoDAO;
import com.invendata.model.Movimiento;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador que recibe las peticiones de Entradas y Salidas de inventario.
 */
@WebServlet(name = "MovimientoServlet", urlPatterns = {"/MovimientoServlet"})
public class MovimientoServlet extends HttpServlet {

    // Instanciamos los DAOs necesarios
    ProductoDAO pdao = new ProductoDAO();
    MovimientoDAO mdao = new MovimientoDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Capturar datos que vienen del formulario JSP
        int idProd = Integer.parseInt(request.getParameter("txtIdProducto"));
        int cantidad = Integer.parseInt(request.getParameter("txtCantidad"));
        String tipo = request.getParameter("txtTipo"); // "ENTRADA" o "SALIDA"

        // 🔍 2. VALIDACIÓN CRÍTICA (Johan's Security Check)
        if (tipo.equalsIgnoreCase("SALIDA")) {
            // Consultamos cuánto hay en base de datos antes de permitir la salida
            int stockActual = pdao.obtenerStock(idProd); 
            
            if (stockActual < cantidad) {
                // ⛔ BLOQUEO: No hay suficiente. Redirigimos con aviso de error.
                response.sendRedirect("ProductoServlet?error=insuficiente");
                return; // Cortamos la ejecución aquí mismo
            }
        }

        // 3. Si es ENTRADA o hay Stock suficiente, procedemos a registrar
        Movimiento mov = new Movimiento();
        mov.setProductoId(idProd);
        mov.setUsuarioId(1); // Usuario quemado temporalmente hasta tener el Login
        mov.setTipo(tipo);
        mov.setCantidad(cantidad);

        boolean exito = mdao.registrar(mov);

        // 4. Redirigir según el resultado
        if (exito) {
            response.sendRedirect("ProductoServlet?msj=exito");
        } else {
            response.sendRedirect("ProductoServlet?error=db");
        }
    }
}