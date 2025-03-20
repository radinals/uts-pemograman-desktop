/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author rss
 */
public class DBConnection {
	
	private Connection connection;
	
	public DBConnection(String url, String username, String password) {
		this.connection = null;
		createConnection(url,username,password);
	}
	
	public final void createConnection(String url, String username, String password) {
		try {
			this.connection = DriverManager.getConnection(url, username, password); 
		} catch (SQLException e) { 
			System.err.println("gagal menghubungkan database" + e.getMessage());
			System.exit(-1);
		}
	}
	
	public Statement createStatement() throws Exception {
		if(this.connection == null) throw new Exception("Database belum di hubungkan");
		return connection.createStatement();
	}
     
}
