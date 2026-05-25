/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;
import java.sql.*;
/**
 *
 * @author frank
 */
public class ConexionBase {
    Statement consulta; //Objeto que permite crear consultas
    Connection con; //Conexion entre java y la base de datos
    
    String ruta = "C:\\Users\\frank\\OneDrive\\Documentos\\SQLte e-193\\baseConsultorio.db";
    String url = "jdbc:sqlite:" + ruta;
    
    public Connection getConnection(){
        try {
            con = DriverManager.getConnection(url); //Abre la conexion con la base de datos
            consulta = con.createStatement();
            System.out.println("Conexion exitosa");
        } catch (Exception e) {
            System.out.println("Error de conexión");
        }
        return con;
    } 
}
