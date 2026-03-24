package com.invendata.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Instancia única de la conexión (Pattern Singleton)
    private static Connection conexion = null;
    
    // Configuración de la base de datos (Coincide con tu Script SQL)
    private static final String DATABASE = "invendata_db";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/" + DATABASE + "?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = "NuevaClave2026";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Cargar el driver de MySQL (registrado en tu pom.xml)
                Class.forName(DRIVER);
                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión exitosa a InvenData DB");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró el Driver de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Error de SQL al conectar: " + e.getMessage());
        }
        return conexion;
    }
    
    // Método opcional para cerrar la conexión si es necesario
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔌 Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
