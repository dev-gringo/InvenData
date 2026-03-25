package com.invendata.dao;

import com.invendata.config.Conexion;
import com.invendata.model.Movimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase DAO para gestionar la tabla 'movimientos' y 
 * actualizar el stock de 'productos' en una sola transacción.
 */
public class MovimientoDAO {

    Connection con;
    PreparedStatement ps;

    public boolean registrar(Movimiento m) {
        // SQL 1: Insertar el rastro del movimiento
        String sqlMov = "INSERT INTO movimientos (producto_id, usuario_id, tipo, cantidad) VALUES (?, ?, ?, ?)";
        
        // SQL 2: Actualizar el stock (Suma si entra, Resta si sale)
        String sqlProd = (m.getTipo().equalsIgnoreCase("ENTRADA")) 
                ? "UPDATE productos SET stock = stock + ? WHERE id = ?" 
                : "UPDATE productos SET stock = stock - ? WHERE id = ?";

        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false); // 🚩 INICIO DE TRANSACCIÓN: No guarda nada hasta que todo sea exitoso

            // 1. Registrar el movimiento
            ps = con.prepareStatement(sqlMov);
            ps.setInt(1, m.getProductoId());
            ps.setInt(2, m.getUsuarioId());
            ps.setString(3, m.getTipo());
            ps.setInt(4, m.getCantidad());
            ps.executeUpdate();

            // 2. Actualizar el stock del producto relacionado
            ps = con.prepareStatement(sqlProd);
            ps.setInt(1, m.getCantidad());
            ps.setInt(2, m.getProductoId());
            ps.executeUpdate();

            con.commit(); // ✅ TODO OK: Se guardan ambos cambios permanentemente
            return true;
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback(); // ❌ ERROR: Se deshace cualquier cambio parcial
            } catch (SQLException ex) {
                System.err.println("Error en Rollback: " + ex.getMessage());
            }
            System.err.println("Error en MovimientoDAO: " + e.getMessage());
            return false;
        }
    }
}