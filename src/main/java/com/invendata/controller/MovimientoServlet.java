package com.invendata.controller;

import com.invendata.dao.MovimientoDAO;
import com.invendata.dao.ProductoDAO;
import com.invendata.model.Movimiento;
import com.invendata.model.Usuario; // IMPORTANTE: Para obtener los datos del usuario logueado
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador que gestiona el flujo de inventario (Entradas/Salidas) 
 * y la visualización del historial de auditoría.
 */
@WebServlet(name = "MovimientoServlet", urlPatterns = {"/MovimientoServlet"})
public class MovimientoServlet extends HttpServlet {

    // Instanciamos los DAOs para persistencia de datos
    ProductoDAO pdao = new ProductoDAO();
    MovimientoDAO mdao = new MovimientoDAO();

    /**
     * MÉTODO GET: Muestra el Historial de Movimientos.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. LIMPIEZA DE CACHÉ: Evita que se vea el historial tras cerrar sesión
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        
        // 2. SEGURIDAD: Verificar si el usuario tiene permiso (Sesión activa)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 3. OBTENCIÓN DE DATOS: Llamamos al DAO para traer la lista con JOINs
        List<Movimiento> lista = mdao.listarTodo();
        
        // 4. DESPACHO: Enviamos la lista al JSP de historial
        request.setAttribute("historial", lista);
        request.getRequestDispatcher("historial.jsp").forward(request, response);
    }

    /**
     * MÉTODO POST: Procesa el registro de una Entrada o Salida de stock.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener la sesión y el objeto de usuario logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        // Cast del objeto de sesión a nuestra clase Usuario
        Usuario userLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        // 2. Capturar datos del formulario JSP
        int idProd = Integer.parseInt(request.getParameter("txtIdProducto"));
        int cantidad = Integer.parseInt(request.getParameter("txtCantidad"));
        String tipo = request.getParameter("txtTipo"); // "ENTRADA" o "SALIDA"

        // 🔍 3. VALIDACIÓN DE REGLA DE NEGOCIO: No permitir stock negativo
        if (tipo.equalsIgnoreCase("SALIDA")) {
            int stockActual = pdao.obtenerStock(idProd); 
            
            if (stockActual < cantidad) {
                // Bloqueo: Redirigimos al panel con aviso de stock insuficiente
                response.sendRedirect("ProductoServlet?error=insuficiente");
                return; 
            }
        }

        // 4. CONSTRUCCIÓN DEL OBJETO MOVIMIENTO
        Movimiento mov = new Movimiento();
        mov.setProductoId(idProd);
        // ✅ DINÁMICO: Obtenemos el ID real del usuario que está frente a la pantalla
        mov.setUsuarioId(userLogueado.getId()); 
        mov.setTipo(tipo);
        mov.setCantidad(cantidad);

        // 5. PERSISTENCIA: El DAO se encarga de la transacción (SQL Insert + SQL Update)
        boolean exito = mdao.registrar(mov);

        // 6. RESPUESTA AL USUARIO
        if (exito) {
            response.sendRedirect("ProductoServlet?msj=exito");
        } else {
            response.sendRedirect("ProductoServlet?error=db");
        }
    }
}