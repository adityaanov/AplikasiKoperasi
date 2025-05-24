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
public class FormMeja extends javax.swing.JFrame {
    private DefaultTableModel model;
    String id_meja, no_meja, jenis_meja, jml_kursi, status, id_pegawai;
    
    /**
     * Creates new form FormMeja
     */
    public FormMeja(){
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
        IdOtomatis();
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
        cb_status.setSelectedItem(""+model.getValueAt(i, 2));
        btn_hapus.setEnabled(true);
        btn_edit.setEnabled(true);
    }
    
    public void IdOtomatis(){
        try{
            com.mysql.jdbc.Statement st = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From meja order by id_meja desc";
            
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                String id = rs.getString("id_meja").substring(3);
                String AN = ""+(Integer.parseInt(id)+1);
                String nol = "";
                
                if(AN.length()==1){
                    nol="00";
                }else if(AN.length()==2){
                    nol="0";
                }else if(AN.length()==3){
                    nol="";
                }txt_idmeja.setText("MJ"+nol+AN);
            }else{
                txt_idmeja.setText("MJ001");
            }
        }catch(SQLException error){
                JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void LoadData(){
        id_meja = txt_idmeja.getText();
        no_meja = txt_nomeja.getText();
        jenis_meja = (String)cb_kategori.getSelectedItem();
        jml_kursi = txt_jmlkursi.getText();
        status = (String)cb_status.getSelectedItem();
        id_pegawai = lb_id.getText();
    }
    
    public void SimpanData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Insert Into meja (id_meja, no_meja, jenis_meja, jml_kursi, status, id_pegawai)"+"values"+"('"+id_meja+"','"+no_meja+"','"+jenis_meja+"','"+jml_kursi+"','"+status+"','"+id_pegawai+"')";
            PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDb().prepareStatement(sql);
            p.executeUpdate();
            
            GetData();
            Nonaktif();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        }catch(SQLException ex){
            Logger.getLogger(FormMeja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UbahData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Update meja Set no_meja='"+no_meja+"',"
                    + "jenis_meja='"+jenis_meja+"',"
                    + "jml_kursi='"+jml_kursi+"',"
                    + "status='"+status+"',"
                    + "id_pegawai='"+id_pegawai+"'WHERE id_meja='"+id_meja+"'";
            
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
    
    public void HapusData(){
        LoadData();
        
        int pesan = JOptionPane.showConfirmDialog(null, "Anda yakin ingin menghapus data meja "+id_meja+"?","konfirmasi",JOptionPane.OK_CANCEL_OPTION);
        if(pesan == JOptionPane.OK_OPTION){
            try{
                com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
                String sql = "Delete From meja Where id_meja='"+id_meja+"'";
                PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDb().prepareStatement(sql);
                p.executeUpdate();

                GetData();

                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
            }catch(SQLException error){
                JOptionPane.showConfirmDialog(null, error.getMessage());
            }
        }
    }
    
    public void Nonaktif(){
        txt_idmeja.setEnabled(false);
        txt_nomeja.setEnabled(false);
        cb_kategori.setEnabled(false);
        txt_jmlkursi.setEnabled(false);
        cb_status.setEnabled(false);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    public void Aktif(){
        txt_idmeja.setEnabled(false);
        txt_nomeja.setEnabled(true);
        cb_kategori.setEnabled(true);
        txt_jmlkursi.setEnabled(true);
        cb_status.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_tambah.setEnabled(false);
        txt_nomeja.requestFocus();
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
        btn_tambah = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelmeja = new javax.swing.JTable();
        cb_status = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lb_dashboard = new javax.swing.JLabel();
        lb_datamenu = new javax.swing.JLabel();
        lb_datameja = new javax.swing.JLabel();
        lb_laptransaksi = new javax.swing.JLabel();
        lb_logaktivitas = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lb_lappendapatan = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(750, 100));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("MANAGER");

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
        txt_nomeja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nomejaKeyTyped(evt);
            }
        });

        cb_kategori.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cb_kategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Single", "Double" }));
        cb_kategori.setPreferredSize(new java.awt.Dimension(350, 30));

        txt_jmlkursi.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_jmlkursi.setPreferredSize(new java.awt.Dimension(350, 30));
        txt_jmlkursi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jmlkursiKeyTyped(evt);
            }
        });

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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
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
                                    .addComponent(txt_idmeja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_nomeja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_jmlkursi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txt_cari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_tambah)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(74, 74, 74))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
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
                        .addComponent(btn_tambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("DATA MEJA");

        lb_dashboard.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dashboard.png"))); // NOI18N
        lb_dashboard.setText("Dashboard");
        lb_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_dashboardMouseClicked(evt);
            }
        });

        lb_datamenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_datamenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/menu.png"))); // NOI18N
        lb_datamenu.setText("Data Menu");
        lb_datamenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_datamenuMouseClicked(evt);
            }
        });

        lb_datameja.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_datameja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/kitchen-table.png"))); // NOI18N
        lb_datameja.setText("Data Meja");
        lb_datameja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_datamejaMouseClicked(evt);
            }
        });

        lb_laptransaksi.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_laptransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Report.png"))); // NOI18N
        lb_laptransaksi.setText("Laporan Transaksi");
        lb_laptransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_laptransaksiMouseClicked(evt);
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

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exit.png"))); // NOI18N
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(693, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel8)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        lb_lappendapatan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lb_lappendapatan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Report.png"))); // NOI18N
        lb_lappendapatan.setText("Laporan Pendapatan");
        lb_lappendapatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_lappendapatanMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_laptransaksi)
                    .addComponent(lb_logaktivitas)
                    .addComponent(lb_datameja)
                    .addComponent(lb_datamenu)
                    .addComponent(lb_dashboard)
                    .addComponent(lb_lappendapatan))
                .addGap(33, 33, 33)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_id))))
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
                        .addComponent(lb_datamenu)
                        .addGap(18, 18, 18)
                        .addComponent(lb_datameja)
                        .addGap(18, 18, 18)
                        .addComponent(lb_laptransaksi)
                        .addGap(18, 18, 18)
                        .addComponent(lb_lappendapatan)
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
        IdOtomatis();
        Kosongkan();
        Aktif();
        txt_nomeja.requestFocus();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        // TODO add your handling code here:
        SimpanData();
        Kosongkan();
        IdOtomatis();
        Nonaktif();
        btn_tambah.setEnabled(true);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        // TODO add your handling code here:
        UbahData();
        Kosongkan();
        Nonaktif();
        btn_tambah.setEnabled(true);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
        HapusData();
        Kosongkan();
        IdOtomatis();
        Nonaktif();
        btn_tambah.setEnabled(true);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void tabelmejaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelmejaMouseClicked
        // TODO add your handling code here:
        SelectData();
        Aktif();
        txt_idmeja.setEnabled(false);
        btn_simpan.setEnabled(false);
    }//GEN-LAST:event_tabelmejaMouseClicked

    private void txt_nomejaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nomejaKeyTyped
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nomor meja hanya bisa memasukan karakter angka");
        }
    }//GEN-LAST:event_txt_nomejaKeyTyped

    private void txt_cariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cariKeyTyped
        Cari();
    }//GEN-LAST:event_txt_cariKeyTyped

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        Cari();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void txt_jmlkursiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jmlkursiKeyTyped
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom stok hanya bisa memasukan karakter angka");
        }
    }//GEN-LAST:event_txt_jmlkursiKeyTyped

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        new FormLogin().show();
        this.dispose();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void lb_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_dashboardMouseClicked
        new FormDashboardManager().show();
        this.dispose();
    }//GEN-LAST:event_lb_dashboardMouseClicked

    private void lb_datamenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_datamenuMouseClicked
        new FormMenu().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai"))){
                FormMenu.lb_id.setText(r.getString(5));
            }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_datamenuMouseClicked

    private void lb_datamejaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_datamejaMouseClicked
        new FormMeja().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai"))){
                FormMeja.lb_id.setText(r.getString(5));
            }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_datamejaMouseClicked

    private void lb_laptransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_laptransaksiMouseClicked
        new FormLaporanTransaksi().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai"))){
                FormLaporanTransaksi.lb_id.setText(r.getString(5));
            }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_laptransaksiMouseClicked

    private void lb_lappendapatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_lappendapatanMouseClicked
        new FormLaporanPendapatan().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai"))){
                FormLogAktivitasPegawaiManager.lb_id.setText(r.getString(5));
            }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_lappendapatanMouseClicked

    private void lb_logaktivitasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_logaktivitasMouseClicked
        new FormLogAktivitasPegawaiManager().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+lb_id.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_id.getText().equals(r.getString("id_pegawai"))){
                FormLaporanPendapatan.lb_id.setText(r.getString(5));
            }
            }
        }catch(Exception e){
            
        }
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
            java.util.logging.Logger.getLogger(FormMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormMeja().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JComboBox<String> cb_kategori;
    private javax.swing.JComboBox<String> cb_status;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_dashboard;
    private javax.swing.JLabel lb_datameja;
    private javax.swing.JLabel lb_datamenu;
    public static javax.swing.JLabel lb_id;
    private javax.swing.JLabel lb_lappendapatan;
    private javax.swing.JLabel lb_laptransaksi;
    private javax.swing.JLabel lb_logaktivitas;
    private javax.swing.JTable tabelmeja;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idmeja;
    private javax.swing.JTextField txt_jmlkursi;
    private javax.swing.JTextField txt_nomeja;
    // End of variables declaration//GEN-END:variables
}
