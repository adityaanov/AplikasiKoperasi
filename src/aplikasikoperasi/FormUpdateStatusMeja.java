/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplikasikoperasi;

import Aplikasi.Koneksi;
import static aplikasikoperasi.FormPelanggan.lb_id;
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
public class FormUpdateStatusMeja extends javax.swing.JFrame {
    private DefaultTableModel model;
    String id_meja, no_meja, jenis_meja, jml_kursi, status, id_pegawai;
    
    /**
     * Creates new form FormMeja
     */
    public FormUpdateStatusMeja(){
        initComponents();
        Nonaktif();
        
        model = new DefaultTableModel();
        tabelmeja.setModel(model);
        model.addColumn("ID Meja");
        model.addColumn("No Meja");
        model.addColumn("Kategori");
        model.addColumn("Jumlah Kursi");
        model.addColumn("Status");
        
        GetData();
    }
    
    public void GetData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Statement stat = (Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From meja";
            ResultSet rs = stat.executeQuery(sql);
            
            while(rs.next()){
                Object[] obj = new Object[6];
                obj [0] = rs.getString("id_meja");
                obj [1] = rs.getString("no_meja");
                obj [2] = rs.getString("jenis_meja");
                obj [3] = rs.getString("jml_kursi");
                obj [4] = rs.getString("status");
                obj [5] = rs.getString("id_pegawai");
                
                model.addRow(obj);
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void SelectData(){
        int i = tabelmeja.getSelectedRow();
        if(i == -1){
            return;
        }
        txt_idmeja.setText(""+model.getValueAt(i, 0));
        txt_nomeja.setText(""+model.getValueAt(i, 1));
        cb_kategori.setSelectedItem(""+model.getValueAt(i, 2));
        txt_jmlkursi.setText(""+model.getValueAt(i, 3));
        cb_status.setSelectedItem(""+model.getValueAt(i, 4));
        btn_edit.setEnabled(true);
    }
         
    public void LoadData(){
        id_meja = txt_idmeja.getText();
        no_meja = txt_nomeja.getText();
        jenis_meja = (String)cb_kategori.getSelectedItem();
        jml_kursi = txt_jmlkursi.getText();
        status = (String)cb_status.getSelectedItem();
        id_pegawai = lb_id.getText();
    }
    
    public void UbahData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Update meja Set no_meja='"+no_meja+"',"
                    + "jenis_meja='"+jenis_meja+"',"
                    + "jml_kursi='"+jml_kursi+"',"
                    + "status='"+status+"',"
                    + "id_pegawai='"+id_pegawai+"' WHERE id_meja='"+id_meja+"'";
            
            PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDb().prepareStatement(sql);
            p.executeUpdate();
            
            GetData();
            Kosongkan();
            SelectData();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        }catch(SQLException err){
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
    }
    
    public void Nonaktif(){
        txt_idmeja.setEnabled(false);
        txt_nomeja.setEnabled(false);
        cb_kategori.setEnabled(false);
        txt_jmlkursi.setEnabled(false);
        cb_status.setEnabled(false);
        btn_edit.setEnabled(false);
    }
    
    public void Kosongkan(){
        txt_nomeja.setText("");
        cb_kategori.setSelectedItem("----------");
        txt_jmlkursi.setText("");
        cb_status.setSelectedItem("----------");
    }
    
    public void Cari(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Connection con = Koneksi.KoneksiDb();
            Statement st = con.createStatement();
            String sql = "Select * From meja Where no_meja like '%"+txt_cari.getText()+"%'";
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                Object[] ob = new Object[6];
                ob [0] = rs.getString(1);
                ob [1] = rs.getString(2);
                ob [2] = rs.getString(3);
                ob [3] = rs.getString(4);
                ob [4] = rs.getString(5);
                ob [5] = rs.getString(6);
                
                model.addRow(ob);
            }
        }catch(SQLException e){
            System.out.println(e);
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_idmeja = new javax.swing.JTextField();
        txt_nomeja = new javax.swing.JTextField();
        cb_kategori = new javax.swing.JComboBox<>();
        txt_jmlkursi = new javax.swing.JTextField();
        btn_edit = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelmeja = new javax.swing.JTable();
        cb_status = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lb_dashboard = new javax.swing.JLabel();
        lb_transaksi = new javax.swing.JLabel();
        lb_catatantransaksi = new javax.swing.JLabel();
        lb_datapelanggan = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lb_updatestatus = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();
        lb_jenis = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(750, 100));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("KASIR");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(750, 500));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("ID Meja");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("No Meja");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Kategori");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Jumlah Kursi");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setText("Status");

        txt_idmeja.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_idmeja.setPreferredSize(new java.awt.Dimension(350, 30));

        txt_nomeja.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_nomeja.setPreferredSize(new java.awt.Dimension(350, 30));

        cb_kategori.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cb_kategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Single", "Double" }));
        cb_kategori.setPreferredSize(new java.awt.Dimension(350, 30));

        txt_jmlkursi.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_jmlkursi.setPreferredSize(new java.awt.Dimension(350, 30));

        btn_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit.png"))); // NOI18N
        btn_edit.setText("Edit");
        btn_edit.setPreferredSize(new java.awt.Dimension(115, 41));
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        txt_cari.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_cari.setPreferredSize(new java.awt.Dimension(75, 30));
        txt_cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cariKeyTyped(evt);
            }
        });

        btn_cari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search.png"))); // NOI18N
        btn_cari.setPreferredSize(new java.awt.Dimension(65, 30));
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });

        tabelmeja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Meja", "No Meja", "Kategori", "Jumlah Kursi", "Status"
            }
        ));
        tabelmeja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelmejaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelmeja);

        cb_status.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cb_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "1", "0" }));
        cb_status.setPreferredSize(new java.awt.Dimension(350, 30));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                                .addComponent(jLabel6))
                            .addGap(86, 86, 86)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_idmeja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_nomeja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cb_kategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_jmlkursi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cb_status, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(txt_cari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(79, 79, 79))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_idmeja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_nomeja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_jmlkursi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(cb_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("UPDATE STATUS MEJA");

        lb_dashboard.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dashboard.png"))); // NOI18N
        lb_dashboard.setText("Dashboard");
        lb_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_dashboardMouseClicked(evt);
            }
        });

        lb_transaksi.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_transaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/transaction_1751700.png"))); // NOI18N
        lb_transaksi.setText("Transaksi");
        lb_transaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_transaksiMouseClicked(evt);
            }
        });

        lb_catatantransaksi.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_catatantransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Report.png"))); // NOI18N
        lb_catatantransaksi.setText("Catatan Transaksi");
        lb_catatantransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_catatantransaksiMouseClicked(evt);
            }
        });

        lb_datapelanggan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_datapelanggan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/people.png"))); // NOI18N
        lb_datapelanggan.setText("Data Pelanggan");
        lb_datapelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_datapelangganMouseClicked(evt);
            }
        });

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

        lb_updatestatus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_updatestatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/kitchen-table.png"))); // NOI18N
        lb_updatestatus.setText("Update Status Meja");
        lb_updatestatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_updatestatusMouseClicked(evt);
            }
        });

        lb_id.setForeground(new java.awt.Color(204, 204, 204));
        lb_id.setText("ID");

        lb_jenis.setForeground(new java.awt.Color(204, 204, 204));
        lb_jenis.setText("jenis");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_datapelanggan)
                    .addComponent(lb_catatantransaksi)
                    .addComponent(lb_transaksi)
                    .addComponent(lb_dashboard)
                    .addComponent(lb_updatestatus))
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_id)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_jenis))))
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
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lb_id)
                    .addComponent(lb_jenis))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_dashboard)
                        .addGap(18, 18, 18)
                        .addComponent(lb_transaksi)
                        .addGap(18, 18, 18)
                        .addComponent(lb_catatantransaksi)
                        .addGap(18, 18, 18)
                        .addComponent(lb_datapelanggan)
                        .addGap(18, 18, 18)
                        .addComponent(lb_updatestatus)))
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

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        UbahData();
        Kosongkan();
        Nonaktif();
        btn_edit.setEnabled(false);
    }//GEN-LAST:event_btn_editActionPerformed

    private void tabelmejaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelmejaMouseClicked
        SelectData();
        cb_status.setEnabled(true);
    }//GEN-LAST:event_tabelmejaMouseClicked

    private void txt_cariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cariKeyTyped
        Cari();
    }//GEN-LAST:event_txt_cariKeyTyped

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        Cari();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        new FormLogin().show();
        this.dispose();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void lb_transaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_transaksiMouseClicked
        new FormTransaksi().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai")) &&
                        r.getString("jenis").equals("Kasir")){
                    FormTransaksi.lb_jabatan.setText(r.getString(4));
                    FormTransaksi.txt_idpegawai.setText(r.getString(5));
                }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_transaksiMouseClicked

    private void lb_catatantransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_catatantransaksiMouseClicked
        new FormCatatanTransaksi().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai")) &&
                        r.getString("jenis").equals("Kasir")){
                    FormCatatanTransaksi.txt_jabatan.setText(r.getString(4));
                    FormCatatanTransaksi.txt_idpegawai.setText(r.getString(5));
                }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_catatantransaksiMouseClicked

    private void lb_datapelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_datapelangganMouseClicked
        new FormPelanggan().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai")) &&
                        r.getString("jenis").equals("Kasir")){
                    FormPelanggan.lb_jenis.setText(r.getString(4));
                    FormPelanggan.lb_id.setText(r.getString(5));
                }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_datapelangganMouseClicked

    private void lb_updatestatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_updatestatusMouseClicked
        new FormUpdateStatusMeja().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai")) &&
                        r.getString("jenis").equals("Kasir")){
                    FormUpdateStatusMeja.lb_jenis.setText(r.getString(4));
                    FormUpdateStatusMeja.lb_id.setText(r.getString(5));
                }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_updatestatusMouseClicked

    private void lb_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_dashboardMouseClicked
        new FormDashboardKasir().show();
        this.dispose();
    }//GEN-LAST:event_lb_dashboardMouseClicked

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
            java.util.logging.Logger.getLogger(FormUpdateStatusMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormUpdateStatusMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormUpdateStatusMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormUpdateStatusMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormUpdateStatusMeja().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_edit;
    private javax.swing.JComboBox<String> cb_kategori;
    private javax.swing.JComboBox<String> cb_status;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_catatantransaksi;
    private javax.swing.JLabel lb_dashboard;
    private javax.swing.JLabel lb_datapelanggan;
    public static javax.swing.JLabel lb_id;
    public static javax.swing.JLabel lb_jenis;
    private javax.swing.JLabel lb_transaksi;
    private javax.swing.JLabel lb_updatestatus;
    private javax.swing.JTable tabelmeja;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idmeja;
    private javax.swing.JTextField txt_jmlkursi;
    private javax.swing.JTextField txt_nomeja;
    // End of variables declaration//GEN-END:variables
}
