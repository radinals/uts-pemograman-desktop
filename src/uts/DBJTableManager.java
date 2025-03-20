package uts;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rss
 */
public class DBJTableManager {
	private DefaultTableModel tableModel;
	private JTable table;
	private DBConnection database;
	private Statement tableUpdateStatement;
	private String updateQuery;
	private ArrayList<String> dataColumns;
	
	public DBJTableManager(JTable table, DBConnection database) {
		try {
			this.updateQuery = null;
			this.dataColumns = new ArrayList<String>();
			this.tableModel = new DefaultTableModel();
			this.table = table;
			this.table.setModel(this.tableModel);
			this.database = database;
			this.tableUpdateStatement = database.createStatement();
		} catch (Exception ex) {
			System.err.println(ex);
			System.exit(-1);
		}
	}
	
	public DefaultTableModel getModel() {
		return tableModel;
	}
	
	public DBJTableManager addDbColumnName(String column) {
		this.dataColumns.add(column);
		return this;
	}
	
	public void clearColumnName() {
		this.dataColumns.clear();
	}
	
	public void setUpdateQuery(String updateQuery) {
		this.updateQuery = updateQuery;
	}
	
	private void loadNamaKolom() {
		this.tableModel = new DefaultTableModel();
		for(String kolom: dataColumns)
			this.tableModel.addColumn(kolom);
		this.table.setModel(tableModel);
	}
	
	public void loadData() {
		loadNamaKolom();
		
		if(updateQuery == null || updateQuery.isEmpty()) return;
		
		try {
			ResultSet queryResult = tableUpdateStatement.executeQuery(updateQuery);
			
			while(queryResult.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				for(int i=0; i < dataColumns.size(); i++) {
					String column = dataColumns.get(i), 
					       data = queryResult.getString(column);
					
					row.add(data);

				}
				this.tableModel.addRow(row.toArray());
			}
		} catch (SQLException e) {
			System.err.println("Gagal mengupdate Data Tabel, " + e);
			
		}
	}
}
