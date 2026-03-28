package com.invendata.dao;

import com.invendata.config.Conexion;
import com.invendata.model.Movimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // IMPORTANTE: Para leer los datos de la consulta
import java.sql.SQLException;
import java.util.ArrayList; // IMPORTANTE: Para manejar la lista de movimientos
import java.util.List;      // IMPORTANTE: Para el tipo de retorno del método

public class MovimientoDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs; // Variable para almacenar los resultados del SELECT

    // --- MÉTODO 1: REGISTRAR (Ya lo tienes) ---
    public boolean registrar(Movimiento m) {
        String sqlMov = "INSERT INTO movimientos (producto_id, usuario_id, tipo, cantidad) VALUES (?, ?, ?, ?)";
        String sqlProd = (m.getTipo().equalsIgnoreCase("ENTRADA")) 
                ? "UPDATE productos SET stock = stock + ? WHERE id = ?" 
                : "UPDATE productos SET stock = stock - ? WHERE id = ?";

        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false); 

            ps = con.prepareStatement(sqlMov);
            ps.setInt(1, m.getProductoId());
            ps.setInt(2, m.getUsuarioId());
            ps.setString(3, m.getTipo());
            ps.setInt(4, m.getCantidad());
            ps.executeUpdate();

            ps = con.prepareStatement(sqlProd);
            ps.setInt(1, m.getCantidad());
            ps.setInt(2, m.getProductoId());
            ps.executeUpdate();

            con.commit(); 
            return true;
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) { }
            return false;
        }
    }

    // --- MÉTODO 2: LISTAR (Agrégalo aquí abajo) ---
    /**
     * Consulta la tabla movimientos haciendo un JOIN con productos y usuarios.
     * Esto permite mostrar nombres en lugar de solo IDs en el historial.
     */
    public List<Movimiento> listarTodo() {
        List<Movimiento> lista = new ArrayList<>();
        // El query usa alias (as) para que sea fácil identificar cada columna
        String sql = "SELECT m.*, p.nombre as nombre_prod, u.nombre_completo as nombre_user " +
                     "FROM movimientos m " +
                     "INNER JOIN productos p ON m.producto_id = p.id " +
                     "INNER JOIN usuarios u ON m.usuario_id = u.id " +
                     "ORDER BY m.fecha DESC"; 

        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId(rs.getInt("id"));
                m.setProductoId(rs.getInt("producto_id"));
                m.setUsuarioId(rs.getInt("usuario_id"));
                m.setTipo(rs.getString("tipo"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setFecha(rs.getTimestamp("fecha")); // Traemos la marca de tiempo de la BD
                
                // Estos campos los llenamos gracias al INNER JOIN
                m.setNombreProducto(rs.getString("nombre_prod")); 
                m.setNombreUsuario(rs.getString("nombre_user"));
                
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }
    
    
}