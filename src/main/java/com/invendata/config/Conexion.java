package com.invendata.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Instancia única de la conexión (Pattern Singleton)
    private static Connection conexion = null;
    
    // Configuración de la base de datos
    private static final String DATABASE = "invendata_db";
    
    // 🚩 CAMBIO AQUÍ: Cambiamos 'UTC' por 'America/Bogota'
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/" + DATABASE + 
                                     "?useSSL=false&serverTimezone=America/Bogota";
                                     
    private static final String USER = "root"; 
    private static final String PASSWORD = "NuevaClave2026";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        try {
            // Verificamos si la conexión es nula o se ha cerrado
            if (conexion == null || conexion.isClosed()) {
                Class.forName(DRIVER);
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión exitosa a InvenData DB (Zona Horaria: Bogotá)");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró el Driver: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Error de SQL: " + e.getMessage());
        }
        return conexion;
    }
    
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔌 Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar: " + e.getMessage());
        }
    }
}