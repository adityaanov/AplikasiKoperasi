/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplikasikoperasi;

import Aplikasi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adity
 */
public class FormPengguna extends javax.swing.JFrame {
    private DefaultTableModel model1;
    private DefaultTableModel model2;
    String username, password, email, jenis, id_pegawai, created_at, update_at, active, nama_pegawai;
    public FormPengguna() {
        initComponents();
        Nonaktif();
        
        model1 = new DefaultTableModel();
        tabelpengguna.setModel(model1);
        model1.addColumn("Username");
        model1.addColumn("Password");
        model1.addColumn("Email");
        model1.addColumn("Jabatan");
        model1.addColumn("ID Pegawai");
        model1.addColumn("Active");
        
        model2 = new DefaultTableModel();
        tabelpegawai.setModel(model2);
        model2.addColumn("ID Pegawai");
        model2.addColumn("Nama Pegawai");
        
        GetDataPengguna();
        GetDataPegawai();
    }
    
    public void GetDataPengguna(){
        model1.getDataVector().removeAllElements();
        model1.fireTableDataChanged();
        try{
            Statement stat = (Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From pengguna";
            ResultSet rs = stat.executeQuery(sql);
            
            while (rs.next()){
                Object[] obj = new Object[6];
                obj [0] = rs.getString("username");
                obj [1] = rs.getString("password");
                obj [2] = rs.getString("email");
                obj [3] = rs.getString("jenis");
                obj [4] = rs.getString("id_pegawai");
                obj [5] = rs.getString("active");
                
                model1.addRow(obj);
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void GetDataPegawai(){
        model2.getDataVector().removeAllElements();
        model2.fireTableDataChanged();
        try{
            Statement stat = (Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From pegawai";
            ResultSet rs = stat.executeQuery(sql);
            
            while (rs.next()){
                Object[] obj = new Object[2];
                obj [0] = rs.getString("id_pegawai");
                obj [1] = rs.getString("nama_pegawai");
                
                model2.addRow(obj);
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void SelectDataPengguna(){
        int i = tabelpengguna.getSelectedRow();
        if(i == -1){
            return;
        }
        txt_username.setText(""+model1.getValueAt(i, 0));
        txt_pass.setText(""+model1.getValueAt(i, 1));
        txt_email.setText(""+model1.getValueAt(i, 2));
        cb_jabatan.setSelectedItem(""+model1.getValueAt(i, 3));
        txt_idpegawai.setText(""+model1.getValueAt(i, 4));
        cb_aktif.setSelectedItem(""+model1.getValueAt(i, 5));
        btn_hapus.setEnabled(true);
        btn_edit.setEnabled(true);
    }
    
    public void SelectDataPegawai(){
        int i = tabelpegawai.getSelectedRow();
        if(i == -1){
            return;
        }
        txt_idpegawai.setText(""+model2.getValueAt(i, 0));
    }
    
    public void LoadData(){
        username = txt_username.getText();
        password = txt_pass.getText();
        email = txt_email.getText();
        jenis = (String)cb_jabatan.getSelectedItem();
        id_pegawai = txt_idpegawai.getText();
        created_at = null;
        update_at = null;
        active = (String)cb_aktif.getSelectedItem();
    }
    
    public void HapusData(){
        LoadData();
        
        int pesan = JOptionPane.showConfirmDialog(null, "Anda yakin ingin menghapus data pengguna "+username+"?","konfirmasi",JOptionPane.OK_CANCEL_OPTION);
        if(pesan == JOptionPane.OK_OPTION){
            try{
                com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
                String sql = "Delete From pengguna Where username = '"+username+"'";
                PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDb().prepareStatement(sql);
                p.executeUpdate();
                
                GetDataPengguna();
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
            }catch(SQLException error){
                JOptionPane.showConfirmDialog(null, error.getMessage());
            }
        }
    }
    
    public void Nonaktif(){
        txt_username.setEnabled(false);
        txt_pass.setEnabled(false);
        txt_email.setEnabled(false);
        cb_jabatan.setEnabled(false);
        txt_idpegawai.setEnabled(false);
        cb_aktif.setEnabled(false);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    public void Aktif(){
        txt_username.setEnabled(true);
        txt_pass.setEnabled(true);
        txt_email.setEnabled(true);
        cb_jabatan.setEnabled(true);
        txt_idpegawai.setEnabled(false);
        cb_aktif.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_tambah.setEnabled(false);
        txt_username.requestFocus();
    }
    
    public void Kosongkan(){
        txt_username.setText("");
        txt_pass.setText("");
        txt_email.setText("");
        cb_jabatan.setSelectedItem("----------");
        cb_aktif.setSelectedItem("----------");
    }
    
    public void CariPengguna(){
        model1.getDataVector().removeAllElements();
        model1.fireTableDataChanged();
        try{
            Connection con = Koneksi.KoneksiDb();
            Statement st = con.createStatement();
            String sql = "Select * From pengguna where username like '%"+txt_caripengguna.getText()+"%'";
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                Object[] ob = new Object[8];
                ob [0] = rs.getString(1);
                ob [1] = rs.getString(2);
                ob [2] = rs.getString(3);
                ob [3] = rs.getString(4);
                ob [4] = rs.getString(5);
                ob [5] = rs.getString(6);
                ob [6] = rs.getString(7);
                ob [7] = rs.getString(8);
                model1.addRow(ob);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public void CariPegawai(){
        model2.getDataVector().removeAllElements();
        model2.fireTableDataChanged();
        try{
            Connection con = Koneksi.KoneksiDb();
            Statement st = con.createStatement();
            String sql = "Select * From pegawai where nama_pegawai like '%"+txt_caripegawai.getText()+"%'";
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                Object[] ob = new Object[2];
                ob [0] = rs.getString(1);
                ob [1] = rs.getString(2);
                model2.addRow(ob);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public void updateactive(){
        String user = txt_username.getText();
        String active = "1";
        try{
            Connection con = Koneksi.KoneksiDb();
            String sql = "Update pengguna Set active = ? Where username = ?";
            PreparedStatement p = con.prepareStatement(sql);
            p.setString(1, active);
            p.setString(2, user);
            
            p.executeUpdate();
            p.close();
        }catch(Exception e){
            
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_username = new javax.swing.JTextField();
        txt_pass = new javax.swing.JTextField();
        txt_idpegawai = new javax.swing.JTextField();
        btn_tambah = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        txt_caripegawai = new javax.swing.JTextField();
        btn_caripegawai = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelpegawai = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelpengguna = new javax.swing.JTable();
        txt_caripengguna = new javax.swing.JTextField();
        btn_caripengguna = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        cb_jabatan = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cb_aktif = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lb_dashboard = new javax.swing.JLabel();
        lb_datapegawai = new javax.swing.JLabel();
        lb_datapengguna = new javax.swing.JLabel();
        lb_logaktivitas = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(750, 100));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("ADMIN");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(750, 500));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Username");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Password");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("ID Pegawai");

        txt_username.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_username.setPreferredSize(new java.awt.Dimension(200, 30));

        txt_pass.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_pass.setPreferredSize(new java.awt.Dimension(200, 30));

        txt_idpegawai.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_idpegawai.setPreferredSize(new java.awt.Dimension(200, 30));

        btn_tambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btn_tambah.setText("Tambah");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/save.png"))); // NOI18N
        btn_simpan.setText("Simpan");
        btn_simpan.setToolTipText("");
        btn_simpan.setPreferredSize(new java.awt.Dimension(115, 41));
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit.png"))); // NOI18N
        btn_edit.setText("Edit");
        btn_edit.setPreferredSize(new java.awt.Dimension(115, 41));
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_hapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/bin.png"))); // NOI18N
        btn_hapus.setText("Hapus");
        btn_hapus.setToolTipText("");
        btn_hapus.setPreferredSize(new java.awt.Dimension(115, 41));
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        txt_caripegawai.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_caripegawai.setPreferredSize(new java.awt.Dimension(75, 30));
        txt_caripegawai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_caripegawaiKeyTyped(evt);
            }
        });

        btn_caripegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search.png"))); // NOI18N
        btn_caripegawai.setPreferredSize(new java.awt.Dimension(65, 30));
        btn_caripegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_caripegawaiActionPerformed(evt);
            }
        });

        tabelpegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID Pegawai", "Nama Pegawai"
            }
        ));
        tabelpegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelpegawaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelpegawai);

        tabelpengguna.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Username", "Password", "Email", "Jabatan", "ID Pegawai", "Active"
            }
        ));
        tabelpengguna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelpenggunaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelpengguna);

        txt_caripengguna.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_caripengguna.setPreferredSize(new java.awt.Dimension(75, 30));
        txt_caripengguna.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_caripenggunaKeyTyped(evt);
            }
        });

        btn_caripengguna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search.png"))); // NOI18N
        btn_caripengguna.setPreferredSize(new java.awt.Dimension(65, 30));
        btn_caripengguna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_caripenggunaActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setText("Email");

        txt_email.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_email.setPreferredSize(new java.awt.Dimension(200, 30));

        cb_jabatan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cb_jabatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Admin", "Kasir", "Manager" }));
        cb_jabatan.setPreferredSize(new java.awt.Dimension(200, 30));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel12.setText("Jabatan");

        jLabel14.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel14.setText("Active");

        cb_aktif.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cb_aktif.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "1", "0" }));
        cb_aktif.setPreferredSize(new java.awt.Dimension(200, 30));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_idpegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel4))
                            .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel12)
                            .addComponent(cb_jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel14)
                    .addComponent(cb_aktif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txt_caripegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_caripegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_tambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txt_caripengguna, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_caripengguna, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_caripegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_caripegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_caripengguna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_caripengguna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_tambah)
                            .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_idpegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_aktif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(65, 87, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("DATA PENGGUNA");

        lb_dashboard.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dashboard.png"))); // NOI18N
        lb_dashboard.setText("Dashboard");
        lb_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_dashboardMouseClicked(evt);
            }
        });

        lb_datapegawai.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_datapegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/people.png"))); // NOI18N
        lb_datapegawai.setText("Data Pegawai");
        lb_datapegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_datapegawaiMouseClicked(evt);
            }
        });

        lb_datapengguna.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_datapengguna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/people.png"))); // NOI18N
        lb_datapengguna.setText("Data Pengguna");
        lb_datapengguna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_datapenggunaMouseClicked(evt);
            }
        });

        lb_logaktivitas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_logaktivitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/people.png"))); // NOI18N
        lb_logaktivitas.setText("Log Aktivitas Pegawai");
        lb_logaktivitas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_logaktivitasMouseClicked(evt);
            }
        });

        lb_id.setBackground(new java.awt.Color(204, 204, 204));
        lb_id.setForeground(new java.awt.Color(204, 204, 204));
        lb_id.setText("ID");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(750, 100));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exit.png"))); // NOI18N
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(693, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel15)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 34, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_logaktivitas)
                    .addComponent(lb_datapengguna)
                    .addComponent(lb_datapegawai)
                    .addComponent(lb_dashboard))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_id))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1)))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lb_id))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_dashboard)
                        .addGap(18, 18, 18)
                        .addComponent(lb_datapegawai)
                        .addGap(18, 18, 18)
                        .addComponent(lb_datapengguna)
                        .addGap(18, 18, 18)
                        .addComponent(lb_logaktivitas)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        // TODO add your handling code here:
        Kosongkan();
        Aktif();
        txt_username.requestFocus();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        // TODO add your handling code here:
        String username = txt_username.getText();
        String password = txt_pass.getText();
        String email = txt_email.getText();
        String jenis = (String)cb_jabatan.getSelectedItem();
        String id_pegawai = txt_idpegawai.getText();
        String created_at = null;
        String update_at = null;
        String active = (String)cb_aktif.getSelectedItem();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            String sql = "Insert Into pengguna Values(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, username);
            p.setString(2, password);
            p.setString(3, email);
            p.setString(4, jenis);
            p.setString(5, id_pegawai);
            p.setString(6, created_at);
            p.setString(7, update_at);
            p.setString(8, active);
            p.executeUpdate();
            p.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        }catch(Exception e){
            System.out.println("Terjadi kesalahan!!");
        }finally{
            GetDataPengguna();
            Kosongkan();
        }
        Nonaktif();
        btn_tambah.setEnabled(true);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(true);
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        int i = tabelpengguna.getSelectedRow();
        if(i == -1){
            return;
        }
        String username = (String) model1.getValueAt(i, 0);
        String username2 = txt_username.getText();
        String password = txt_pass.getText();
        String email = txt_email.getText();
        String jenis = (String)cb_jabatan.getSelectedItem();
        String id_pegawai = txt_idpegawai.getText();
        String created_at = null;
        String update_at = null;
        String active = (String)cb_aktif.getSelectedItem();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            String sql = "UPDATE pengguna SET username=?, password=?, email=?, jenis=?, id_pegawai=?, created_at=?, update_at=?, active=? WHERE username=?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, username2);
            p.setString(2, password);
            p.setString(3, email);
            p.setString(4, jenis);
            p.setString(5, id_pegawai);
            p.setString(6, created_at);
            p.setString(7, update_at);
            p.setString(8, active);
            p.setString(9, username);
            
            p.executeUpdate();
            p.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        }catch(Exception e){
            System.out.println("Update Error!!");
        }finally{
            GetDataPengguna();
            Kosongkan();
        }
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        HapusData();
        Kosongkan();
        Nonaktif();
        btn_tambah.setEnabled(true);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void txt_caripegawaiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_caripegawaiKeyTyped
        CariPegawai();
    }//GEN-LAST:event_txt_caripegawaiKeyTyped

    private void tabelpegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelpegawaiMouseClicked
        SelectDataPegawai();
        Aktif();
    }//GEN-LAST:event_tabelpegawaiMouseClicked

    private void tabelpenggunaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelpenggunaMouseClicked
        SelectDataPengguna();
        Aktif();
    }//GEN-LAST:event_tabelpenggunaMouseClicked

    private void txt_caripenggunaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_caripenggunaKeyTyped
        CariPengguna();
    }//GEN-LAST:event_txt_caripenggunaKeyTyped

    private void btn_caripegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_caripegawaiActionPerformed
        CariPegawai();
    }//GEN-LAST:event_btn_caripegawaiActionPerformed

    private void btn_caripenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_caripenggunaActionPerformed
        CariPengguna();
    }//GEN-LAST:event_btn_caripenggunaActionPerformed

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        new FormLogin().show();
        this.dispose();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void lb_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_dashboardMouseClicked
        new FormDashboardAdmin().show();
        this.dispose();
    }//GEN-LAST:event_lb_dashboardMouseClicked

    private void lb_datapegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_datapegawaiMouseClicked
        new FormPegawai().show();
        this.dispose();
    }//GEN-LAST:event_lb_datapegawaiMouseClicked

    private void lb_datapenggunaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_datapenggunaMouseClicked
        new FormPengguna().show();
        this.dispose();
    }//GEN-LAST:event_lb_datapenggunaMouseClicked

    private void lb_logaktivitasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_logaktivitasMouseClicked
        new FormLogAktivitasPegawaiAdmin().show();
        this.dispose();
    }//GEN-LAST:event_lb_logaktivitasMouseClicked

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
            java.util.logging.Logger.getLogger(FormPengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPengguna().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_caripegawai;
    private javax.swing.JButton btn_caripengguna;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JComboBox<String> cb_aktif;
    private javax.swing.JComboBox<String> cb_jabatan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lb_dashboard;
    private javax.swing.JLabel lb_datapegawai;
    private javax.swing.JLabel lb_datapengguna;
    private javax.swing.JLabel lb_id;
    private javax.swing.JLabel lb_logaktivitas;
    private javax.swing.JTable tabelpegawai;
    private javax.swing.JTable tabelpengguna;
    private javax.swing.JTextField txt_caripegawai;
    private javax.swing.JTextField txt_caripengguna;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_idpegawai;
    private javax.swing.JTextField txt_pass;
    private javax.swing.JTextField txt_username;
    // End of variables declaration//GEN-END:variables
}
