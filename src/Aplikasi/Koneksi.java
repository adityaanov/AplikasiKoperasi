/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aplikasi;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author adity
 */
public class Koneksi {
    Connection koneksi = null;
    public static Connection KoneksiDb(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost/aplikasi_koperasi","root","");
            return koneksi;
        }catch(Exception error){
            JOptionPane.showMessageDialog(null, error);
            return null;
        }
    }
}
