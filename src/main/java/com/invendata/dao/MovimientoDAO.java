package com.invendata.dao;

import com.invendata.config.Conexion;
import com.invendata.model.Movimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovimientoDAO {

    Connection con;
    PreparedStatement ps;

    public boolean registrar(Movimiento m) {
        // SQL 1: Insertar el movimiento
        String sqlMov = "INSERT INTO movimientos (producto_id, usuario_id, tipo, cantidad) VALUES (?, ?, ?, ?)";
        // SQL 2: Actualizar el stock del producto (Suma si es ENTRADA, Resta si es SALIDA)
        String sqlProd = (m.getTipo().equalsIgnoreCase("ENTRADA")) 
                ? "UPDATE productos SET stock = stock + ? WHERE id = ?" 
                : "UPDATE productos SET stock = stock - ? WHERE id = ?";

        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false); // Iniciamos Transacción

            // Ejecutar Registro de Movimiento
            ps = con.prepareStatement(sqlMov);
            ps.setInt(1, m.getProductoId());
            ps.setInt(2, m.getUsuarioId());
            ps.setString(3, m.getTipo());
            ps.setInt(4, m.getCantidad());
            ps.executeUpdate();

            // Ejecutar Actualización de Stock
            ps = con.prepareStatement(sqlProd);
            ps.setInt(1, m.getCantidad());
            ps.setInt(2, m.getProductoId());
            ps.executeUpdate();

            con.commit(); // Si todo está bien, guardamos cambios
            return true;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { } // Si hay error, deshacemos todo
            System.err.println("Error en movimiento: " + e.getMessage());
            return false;
        }
    }
}