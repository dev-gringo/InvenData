package com.invendata.dao;

import com.invendata.config.Conexion;
import com.invendata.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // 1. Método para Listar todos los productos (READ)
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE estado = 'ACTIVO'";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // 2. Método para Registrar un nuevo producto (CREATE)
    public boolean registrar(Producto p) {
        String sql = "INSERT INTO productos (nombre, categoria, precio, stock, stock_minimo, estado) VALUES (?, ?, ?, ?, ?, 'ACTIVO')";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getStockMinimo());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }
    /*public static void main(String[] args) {
    ProductoDAO dao = new ProductoDAO();
    List<Producto> productos = dao.listar();
    
    System.out.println("--- LISTA DE PRODUCTOS EN BD ---");
    for (Producto p : productos) {
        System.out.println("Producto: " + p.getNombre() + " | Stock: " + p.getStock());
    }
}*/
    
    /**
 * Consulta el stock actual de un producto específico en la base de datos.
 * Este método es vital para la validación de seguridad antes de una salida.
 */
public int obtenerStock(int id) {
    String sql = "SELECT stock FROM productos WHERE id = ?";
    try {
        con = Conexion.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            // Retorna el valor de la columna 'stock' de la tabla
            return rs.getInt("stock");
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener stock: " + e.getMessage());
    }
    // Si el producto no existe o hay error, devolvemos 0 para evitar salidas
    return 0;
    }

    /**
     * 3. Método para ACTUALIZAR un producto (UPDATE)
     * Modifica los datos básicos sin tocar el stock directamente.
     */
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, categoria=?, precio=?, stock_minimo=? WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStockMinimo());
            ps.setInt(5, p.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * 4. Método para ELIMINACIÓN LÓGICA (DELETE lógico)
     * En lugar de borrar el registro, cambiamos su estado a 'INACTIVO'.
     * para proteger la integridad referencial del historial de movimientos.
     */
    public boolean eliminarLogico(int id) {
        // No borramos, solo marcamos como oculto
        String sql = "UPDATE productos SET estado = 'INACTIVO' WHERE id = ?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al realizar eliminación lógica: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Busca productos por nombre (Coincidencia parcial).
     * Útil para encuentrarr productos rápidamente.
     */
    public List<Producto> buscar(String texto) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? AND estado = 'ACTIVO'";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + texto + "%"); // El % permite buscar "Cel" y que salga "Celular"
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista solo los productos que están en alerta (stock <= stock_minimo).
     * Cumple con el requerimiento de Validación de Stock Crítico.
     */
    public List<Producto> listarCriticos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock <= stock_minimo AND estado = 'ACTIVO'";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar críticos: " + e.getMessage());
        }
        return lista;
    }
    
    // Lista solo los productos que fueron "borrados"
public List<Producto> listarInactivos() {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT * FROM productos WHERE estado = 'INACTIVO'";
    try {
        con = Conexion.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            Producto p = new Producto();
            p.setId(rs.getInt("id"));
            p.setNombre(rs.getString("nombre"));
            p.setCategoria(rs.getString("categoria"));
            p.setPrecio(rs.getDouble("precio"));
            p.setStock(rs.getInt("stock"));
            p.setStockMinimo(rs.getInt("stock_minimo"));
            lista.add(p);
        }
    } catch (SQLException e) { System.err.println(e); }
    return lista;
}

// Cambia el estado de INACTIVO a ACTIVO
public boolean restaurar(int id) {
    String sql = "UPDATE productos SET estado = 'ACTIVO' WHERE id = ?";
    try {
        con = Conexion.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) { return false; }
}
}