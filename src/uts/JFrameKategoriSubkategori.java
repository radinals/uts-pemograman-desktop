/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;


/*
FIXME: HAS INCONSISTENT STATE, NEED TO DEBUG.
*/


/**
 *
 * @author rss
 */
public class JFrameKategoriSubkategori extends javax.swing.JFrame {

	private DBConnection connection;
	private DBJTableManager tabelRelasi, tabelSingle;

	private enum StateTableSingle {
		TabelKategori, TabelSubkategori
	};
	
	private StateTableSingle jenisTabelSingle;

	private static final String[] kolomTabelKategori = {"kode_kategori", "nama_kategori"};
	private static final String[] kolomTabelSubkategori = {
		"kode_subkategori", "kode_kategori",
		"nama_subkategori", "create_date", "date_modify"
	};

	private Statement statement;

	private HashMap<String, String> dataKategoriMap;

	private Date tanggal;
	private SimpleDateFormat formatTanggal;

	/**
	 * Creates new form JFrameKategoriSubkategori
	 */
	public JFrameKategoriSubkategori() {
		this.tanggal = new Date();
		this.formatTanggal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dataKategoriMap = new HashMap<String, String>();
		initComponents();
		this.jenisTabelSingle = StateTableSingle.TabelKategori;
		hubungkanDB();
		loadDataTabelSingle();
		loadDataTabelRelasi();
	}

	private String getDate() {
		return formatTanggal.format(tanggal);
	}

	private void loadDataTabelSingle() {
		this.tabelSingle.loadData();
	}

	private void loadDataTabelRelasi() {
		this.tabelRelasi.loadData();
	}

	private void updateKategori(String kodeKategori, String namaKategori_baru) {
		try {
			statement.executeUpdate(String.format("UPDATE tkategori SET nama_kategori = '%s' WHERE kode_kategori = '%s'", namaKategori_baru, kodeKategori));
		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
	}

	private void updateSubkategori(String kodeSubkategori, String namaSubkategori_baru, String kodeKategori_baru) {
		try {
			if (namaSubkategori_baru != null && kodeKategori_baru != null) {
				statement.executeUpdate(String.format("UPDATE tsubkategori SET nama_subkategori = '%s', kode_kategori = '%s' WHERE kode_subkategori = '%s'", namaSubkategori_baru, kodeKategori_baru, kodeSubkategori));
			} else if (namaSubkategori_baru != null) {
				statement.executeUpdate(String.format("UPDATE tsubkategori SET nama_subkategori = '%s' WHERE kode_subkategori = '%s'", namaSubkategori_baru, kodeSubkategori));
			} else if (kodeKategori_baru != null) {
				statement.executeUpdate(String.format("UPDATE tsubkategori SET kode_kategori = '%s' WHERE kode_subkategori = '%s'", kodeKategori_baru, kodeSubkategori));
			}

		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
	}

	private void insertSubkategori(String kodeSubkategori, String namaSubkategori, String kodeKategori) {
		try {
			String createDate = getDate();
			statement.executeUpdate(String.format("INSERT INTO tsubkategori(kode_subkategori, nama_subkategori, kode_kategori, create_date, date_modify) values ('%s','%s','%s','%s','%s')", kodeSubkategori, namaSubkategori, kodeKategori, createDate, createDate));
		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
	}

	private void insertKategori(String kodeKategori, String namaKategori) {
		try {
			statement.executeUpdate(String.format("INSERT INTO tkategori(kode_kategori, nama_kategori) values ('%s','%s')", kodeKategori, namaKategori));
		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
	}

	private void hapusKategori(String kodeKategori) {
		try {
			statement.executeUpdate(String.format("DELETE FROM tkategori where kode_kategori = '%s' ", kodeKategori));
		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}

	}

	private void hapusSubkategori(String kodeSubkategori) {
		try {
			statement.executeUpdate(String.format("DELETE FROM tsubkategori where kode_subkategori = '%s' ", kodeSubkategori));
		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
	}

	private boolean kategoriExists(String kodeKategori) {
		try {
			ResultSet result = statement.executeQuery(String.format("SELECT  * FROM tkategori where kode_kategori = '%s' ", kodeKategori));
			return result.next();
		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
		return false;
	}

	private boolean kategoriExists(String kodeKategori, String namaKategori) {
		try {
			ResultSet result = null;
			if (kodeKategori != null && namaKategori != null) {
				result = statement.executeQuery(String.format("SELECT  * FROM tkategori where kode_kategori = '%s' and nama_kategori = '%s' ", kodeKategori, namaKategori));
			} else if (kodeKategori != null) {
				result = statement.executeQuery(String.format("SELECT  * FROM tkategori where kode_kategori = '%s' ", kodeKategori));
			} else if (namaKategori != null) {
				result = statement.executeQuery(String.format("SELECT  * FROM tkategori where nama_kategori = '%s' ", namaKategori));
			} else {
				return false;
			}

			return result.next();
		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
		return false;
	}

	private boolean subkategoriExists(String kodeSubkategori, String namaSubkategori, String kodeKategori) {
		try {
			ResultSet result = null;

			if (kodeSubkategori != null && namaSubkategori != null && kodeKategori != null) {
				result = statement.executeQuery(String.format("SELECT  * FROM tsubkategori where kode_subkategori = '%s' and nama_subkategori = '%s' and kode_kategori = '%s' ", kodeSubkategori, namaSubkategori, kodeKategori));
			} else if (kodeSubkategori != null) {
				result = statement.executeQuery(String.format("SELECT  * FROM tsubkategori where kode_subkategori = '%s' ", kodeSubkategori));
			} else if (namaSubkategori != null) {
				result = statement.executeQuery(String.format("SELECT  * FROM tsubkategori where nama_subkategori = '%s' ", namaSubkategori));
			} else if (kodeKategori != null) {
				result = statement.executeQuery(String.format("SELECT  * FROM tsubkategori where kode_kategori = '%s' ", kodeKategori));
			} else {
				return false;
			}

			return result.next();

		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
		return false;
	}

	private void hubungkanDB() {
		this.connection = new DBConnection("jdbc:mysql://localhost/DB_UTS2", "root", "");
		this.tabelRelasi = new DBJTableManager(this.jTableRelasi, this.connection);
		this.tabelSingle = new DBJTableManager(this.jTableSingle, this.connection);
		try {
			this.statement = this.connection.createStatement();
		} catch (Exception ex) {
			System.err.println(ex);
			System.exit(-1);
		}

		this.tabelRelasi.addDbColumnName("kode_kategori")
			.addDbColumnName("nama_kategori")
			.addDbColumnName("kode_subkategori")
			.addDbColumnName("nama_subkategori")
			.addDbColumnName("create_date")
			.addDbColumnName("date_modify");

		this.tabelRelasi.setUpdateQuery("select tsubkategori.kode_kategori, nama_kategori, kode_subkategori,nama_subkategori, create_date,date_modify from tsubkategori,tkategori where tsubkategori.kode_kategori = tkategori.kode_kategori");

		setupTabelSingleData();
	}

	private void setupTabelSingleData() {
		this.tabelSingle.clearColumnName();
		switch (jenisTabelSingle) {
			case TabelKategori: {
				for (String kolom : kolomTabelKategori) {
					this.tabelSingle.addDbColumnName(kolom);
				}
				this.tabelSingle.setUpdateQuery("select * from tkategori;");
			}
			break;
			case TabelSubkategori: {
				for (String kolom : kolomTabelSubkategori) {
					this.tabelSingle.addDbColumnName(kolom);
				}

				this.tabelSingle.setUpdateQuery("select * from tsubkategori;");

				fillKategoriCombo();

			}
			break;
		}
	}

	private void fillKategoriCombo() {

		try {
			ResultSet result = statement.executeQuery("select * from tkategori");

			this.dataKategoriMap.clear();
			this.jComboBoxKategori.removeAllItems();
			while (result.next()) {
				String kode_kategori = result.getString("kode_kategori"),
					nama_kategori = result.getString("nama_kategori");
				this.dataKategoriMap.put(nama_kategori, kode_kategori);
				this.jComboBoxKategori.addItem(nama_kategori);
			}

		} catch (SQLException ex) {
			System.err.println(ex);
			System.exit(-1);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jScrollPane1 = new javax.swing.JScrollPane();
                jTableRelasi = new javax.swing.JTable();
                jTabelInputTab = new javax.swing.JTabbedPane();
                jPanel1 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jTextFieldKodeKategori = new javax.swing.JTextField();
                jLabel1 = new javax.swing.JLabel();
                jTextFieldNamaKategori = new javax.swing.JTextField();
                jPanel2 = new javax.swing.JPanel();
                jLabel3 = new javax.swing.JLabel();
                jTextFieldKodeSubkategori = new javax.swing.JTextField();
                jLabel4 = new javax.swing.JLabel();
                jComboBoxKategori = new javax.swing.JComboBox<>();
                jLabel5 = new javax.swing.JLabel();
                jTextFieldNamaSubkategori = new javax.swing.JTextField();
                jButton1 = new javax.swing.JButton();
                jLabel6 = new javax.swing.JLabel();
                jScrollPane4 = new javax.swing.JScrollPane();
                jTableSingle = new javax.swing.JTable();
                jButtonSimpan = new javax.swing.JButton();
                jButtonHapus = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                jTableRelasi.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null}
                        },
                        new String [] {
                                "Title 1", "Title 2", "Title 3", "Title 4"
                        }
                ));
                jScrollPane1.setViewportView(jTableRelasi);

                jTabelInputTab.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jTabelInputTab.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jTabelInputTabMouseClicked(evt);
                        }
                });

                jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel2.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jLabel2.setText("Kode Kategori");
                jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

                jTextFieldKodeKategori.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jTextFieldKodeKategori.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextFieldKodeKategoriActionPerformed(evt);
                        }
                });
                jPanel1.add(jTextFieldKodeKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 140, -1));

                jLabel1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jLabel1.setText("Nama Kategori");
                jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

                jTextFieldNamaKategori.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jTextFieldNamaKategori.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextFieldNamaKategoriActionPerformed(evt);
                        }
                });
                jPanel1.add(jTextFieldNamaKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 140, -1));

                jTabelInputTab.addTab("KATEGORI", jPanel1);

                jLabel3.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jLabel3.setText("Kode Subkategori");

                jTextFieldKodeSubkategori.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N

                jLabel4.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jLabel4.setText("Kategori");

                jComboBoxKategori.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jComboBoxKategori.setModel(new javax.swing.DefaultComboBoxModel<>());
                jComboBoxKategori.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jComboBoxKategoriActionPerformed(evt);
                        }
                });

                jLabel5.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
                jLabel5.setText("Nama Subkategori");

                jTextFieldNamaSubkategori.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextFieldKodeSubkategori, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                        .addComponent(jTextFieldNamaSubkategori)
                                        .addComponent(jComboBoxKategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(22, Short.MAX_VALUE))
                );
                jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextFieldKodeSubkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(jTextFieldNamaSubkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jComboBoxKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(22, Short.MAX_VALUE))
                );

                jTabelInputTab.addTab("SUBKATEGORI", jPanel2);

                jButton1.setText("KELUAR");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });

                jLabel6.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
                jLabel6.setText("FORM KATEGORI");

                jTableSingle.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null}
                        },
                        new String [] {
                                "Title 1", "Title 2", "Title 3", "Title 4"
                        }
                ));
                jTableSingle.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jTableSingleMouseClicked(evt);
                        }
                });
                jScrollPane4.setViewportView(jTableSingle);

                jButtonSimpan.setText("SIMPAN");
                jButtonSimpan.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonSimpanActionPerformed(evt);
                        }
                });

                jButtonHapus.setText("HAPUS");
                jButtonHapus.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonHapusActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(jTabelInputTab, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(35, 35, 35)
                                                .addComponent(jButtonSimpan)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonHapus)
                                                .addGap(27, 27, 27)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton1)
                                                .addGap(18, 18, 18))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                                                .addContainerGap())))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap(27, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton1)
                                                        .addComponent(jLabel6))
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(68, 68, 68)
                                                .addComponent(jTabelInputTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButtonSimpan)
                                                        .addComponent(jButtonHapus))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(26, 26, 26)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16))
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jTextFieldKodeKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldKodeKategoriActionPerformed
		// TODO add your handling code here:
        }//GEN-LAST:event_jTextFieldKodeKategoriActionPerformed

        private void jTextFieldNamaKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNamaKategoriActionPerformed
		// TODO add your handling code here:
        }//GEN-LAST:event_jTextFieldNamaKategoriActionPerformed

        private void jComboBoxKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxKategoriActionPerformed
		// TODO add your handling code here:
        }//GEN-LAST:event_jComboBoxKategoriActionPerformed

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		if(confirmationDialog("Keluar dari Aplikasi?"))
			System.exit(0);
        }//GEN-LAST:event_jButton1ActionPerformed

        private void jTabelInputTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabelInputTabMouseClicked
		// DEBUG: MAY REGISTER INCORRECTLY
		int tabIndex = jTabelInputTab.indexAtLocation(evt.getX(), evt.getY());

		if (tabIndex == -1) {
			return;
		}

		if (jTabelInputTab.getTitleAt(tabIndex).equals("KATEGORI")) {
			this.jenisTabelSingle = StateTableSingle.TabelKategori;
		} else {
			this.jenisTabelSingle = StateTableSingle.TabelSubkategori;
		}

		setupTabelSingleData();
		loadDataTabelSingle();
        }//GEN-LAST:event_jTabelInputTabMouseClicked

        private void jButtonSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSimpanActionPerformed
		if (jenisTabelSingle == StateTableSingle.TabelKategori) {

			String kodeKategori = jTextFieldKodeKategori.getText();
			String namaKategori = jTextFieldNamaKategori.getText();

			if (kodeKategori.length() != 6) {
				JOptionPane.showMessageDialog(this, "Kode Kategori harus memiliki panjang 6 karakter.");
				return;
			} else if (namaKategori.length() != 6) {
				JOptionPane.showMessageDialog(this, "Nama Kategori harus memiliki panjang 6 karakter");
				return;
			}

			if (!kodeKategori.isEmpty() && !namaKategori.isEmpty() && kategoriExists(kodeKategori, namaKategori)) {
				JOptionPane.showMessageDialog(this, "Data sudah ada didalam tabel");
			} else if (!kodeKategori.isEmpty() && !namaKategori.isEmpty() && kategoriExists(kodeKategori) && !kategoriExists(null, namaKategori)) {
				if(!confirmationDialog("Lakukan Update?")) { 
					JOptionPane.showMessageDialog(this, "Update Dibatalkan");
					return;
				}
				updateKategori(kodeKategori, namaKategori);
				JOptionPane.showMessageDialog(this, "Data Berhasil Diupdate");
			} else if (!kodeKategori.isEmpty() && !namaKategori.isEmpty() && !kategoriExists(kodeKategori)) {
				if(!confirmationDialog("Lakukan Insert?")) { 
					JOptionPane.showMessageDialog(this, "Insert Dibatalkan");
					return;
				}
				insertKategori(kodeKategori, namaKategori);
				JOptionPane.showMessageDialog(this, "Data Berhasil Ditambahkan");
			} else if (kodeKategori.isEmpty() && namaKategori.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Kode Kategori & Nama Kategori Masih Kosong");
			} else if (kodeKategori.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Kode Kategori Masih Kosong");
			} else if (!kodeKategori.isEmpty() && namaKategori.isEmpty() && !kategoriExists(kodeKategori)) {
				JOptionPane.showMessageDialog(this, "Nama Kategori Masih Kosong");
			}

		} else if (jenisTabelSingle == StateTableSingle.TabelSubkategori) {

			String kodeSubkategori = jTextFieldKodeSubkategori.getText();
			String namaSubkategori = jTextFieldNamaSubkategori.getText();

			if (kodeSubkategori.length() != 6) {
				JOptionPane.showMessageDialog(this, "Kode Subkategori harus memiliki panjang 6 karakter.");
				return;
			} else if (namaSubkategori.length() != 6) {
				JOptionPane.showMessageDialog(this, "Nama Subkategori harus memiliki panjang 6 karakter");
				return;
			}

			String kodeKategori = dataKategoriMap.get(jComboBoxKategori.getSelectedItem().toString());

			if (!kodeSubkategori.isEmpty() && !namaSubkategori.isEmpty() && !kodeKategori.isEmpty() && subkategoriExists(kodeSubkategori, namaSubkategori, kodeKategori)) {
				JOptionPane.showMessageDialog(this, "Data sudah ada didalam tabel");
			} else if (!kodeSubkategori.isEmpty() && !namaSubkategori.isEmpty() && !kodeKategori.isEmpty() && !subkategoriExists(kodeSubkategori, null, null)) {
				if (!confirmationDialog("Lakukan Insert?")) {
					JOptionPane.showMessageDialog(this, "Insert Dibatalkan");
					return;
				}
				insertSubkategori(kodeSubkategori, namaSubkategori, kodeKategori);
				JOptionPane.showMessageDialog(this, "Data Berhasil Ditambahkan");
			} else if (!kodeSubkategori.isEmpty() && !namaSubkategori.isEmpty() && !kodeKategori.isEmpty() && subkategoriExists(kodeSubkategori, null, null) && !subkategoriExists(kodeSubkategori, namaSubkategori, kodeKategori)) {
				if(!confirmationDialog("Lakukan Update?")) { 
					JOptionPane.showMessageDialog(this, "Update Dibatalkan");
					return;
				}
				updateSubkategori(kodeSubkategori, namaSubkategori, kodeKategori);
				JOptionPane.showMessageDialog(this, "Data Berhasil Diupdate");
			} else if (kodeSubkategori.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Kode Subkategori Masih Kosong");
			} else if (namaSubkategori.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Nama Subkategori Masih Kosong");
			}
		}

		loadDataTabelSingle();
		loadDataTabelRelasi();
        }//GEN-LAST:event_jButtonSimpanActionPerformed

	private boolean confirmationDialog(String text) {
		int jawab = JOptionPane.showConfirmDialog(this, text);
		return jawab == JOptionPane.YES_OPTION;
	}
	
	private String getKategoriNama(String kodeKategori) {
		for (String nama : dataKategoriMap.keySet()) {
			if (dataKategoriMap.get(nama).equals(kodeKategori)) {
				return nama;
			}
		}
		return null;
	}

        private void jTableSingleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSingleMouseClicked
		int selectedRow = jTableSingle.getSelectedRow();
		TableModel model = jTableSingle.getModel();

		if (jenisTabelSingle == StateTableSingle.TabelKategori) {

			this.jTextFieldKodeKategori.setText(model.getValueAt(selectedRow, 0) + "");
			this.jTextFieldNamaKategori.setText(model.getValueAt(selectedRow, 1) + "");

		} else if (jenisTabelSingle == StateTableSingle.TabelSubkategori) {
			this.jTextFieldKodeSubkategori.setText(model.getValueAt(selectedRow, 0) + "");
			String kategori = getKategoriNama(model.getValueAt(selectedRow, 1).toString());
			if (kategori != null) {
				this.jComboBoxKategori.setSelectedItem(kategori);
			} else {
				this.jComboBoxKategori.setSelectedItem("");
			}
			this.jTextFieldNamaSubkategori.setText(model.getValueAt(selectedRow, 2) + "");
		}
        }//GEN-LAST:event_jTableSingleMouseClicked

        private void jButtonHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHapusActionPerformed
                if (jenisTabelSingle == StateTableSingle.TabelKategori) {

			String kodeKategori = jTextFieldKodeKategori.getText();

			if (!kodeKategori.isEmpty() && kategoriExists(kodeKategori, null)) {
				if(!confirmationDialog("Lakukan Penghapusan?")) { 
					JOptionPane.showMessageDialog(this, "Penghapusan Dibatalkan");
					return;
				}
				hapusKategori(kodeKategori);
				JOptionPane.showMessageDialog(this, "Data sudah Berhasil Dihapus");
			} else if (!kodeKategori.isEmpty() && !kategoriExists(kodeKategori, null)) {
				JOptionPane.showMessageDialog(this, "Data Tidak Ditemukan!");
			}

		} else if (jenisTabelSingle == StateTableSingle.TabelSubkategori) {

			String kodeSubkategori = jTextFieldKodeSubkategori.getText();

			if (!kodeSubkategori.isEmpty() && subkategoriExists(kodeSubkategori, null, null)) {
				if(!confirmationDialog("Lakukan Penghapusan?")) { 
					JOptionPane.showMessageDialog(this, "Penghapusan Dibatalkan");
					return;
				}
				hapusSubkategori(kodeSubkategori);
				JOptionPane.showMessageDialog(this, "Data sudah Berhasil Dihapus");
			} else if (!kodeSubkategori.isEmpty() && !subkategoriExists(kodeSubkategori, null, null)) {
				JOptionPane.showMessageDialog(this, "Data Tidak Ditemukan!");
			}

		}

		loadDataTabelSingle();
		loadDataTabelRelasi();
        }//GEN-LAST:event_jButtonHapusActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(JFrameKategoriSubkategori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(JFrameKategoriSubkategori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(JFrameKategoriSubkategori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(JFrameKategoriSubkategori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new JFrameKategoriSubkategori().setVisible(true);
			}
		});
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButtonHapus;
        private javax.swing.JButton jButtonSimpan;
        private javax.swing.JComboBox<String> jComboBoxKategori;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JTabbedPane jTabelInputTab;
        private javax.swing.JTable jTableRelasi;
        private javax.swing.JTable jTableSingle;
        private javax.swing.JTextField jTextFieldKodeKategori;
        private javax.swing.JTextField jTextFieldKodeSubkategori;
        private javax.swing.JTextField jTextFieldNamaKategori;
        private javax.swing.JTextField jTextFieldNamaSubkategori;
        // End of variables declaration//GEN-END:variables
}
