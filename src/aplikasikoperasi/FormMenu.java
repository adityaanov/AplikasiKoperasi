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
public class FormMenu extends javax.swing.JFrame {
    private DefaultTableModel model;
    String id_menu, nama_menu, jenis, harga, stok, id_pegawai;
    
    /**
     * Creates new form FormMenu
     */
    public FormMenu(){
        initComponents();
        Nonaktif();
        
        model = new DefaultTableModel();
        tabelmenu.setModel(model);
        model.addColumn("ID Menu");
        model.addColumn("Nama Menu");
        model.addColumn("Jenis");
        model.addColumn("Harga");
        model.addColumn("Stok");
        
        GetData();
        IdOtomatis();
    }
    
    public void GetData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Statement stat = (Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From menu";
            ResultSet rs = stat.executeQuery(sql);
            
            while(rs.next()){
                Object[] obj = new Object[6];
                obj [0] = rs.getString("id_menu");
                obj [1] = rs.getString("nama_menu");
                obj [2] = rs.getString("jenis");
                obj [3] = rs.getString("harga");
                obj [4] = rs.getString("stok");
                obj [5] = rs.getString("id_pegawai");
                
                model.addRow(obj);
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void SelectData(){
        int i = tabelmenu.getSelectedRow();
        if(i == -1){
            return;
        }
        txt_idmenu.setText(""+model.getValueAt(i, 0));
        txt_namamenu.setText(""+model.getValueAt(i, 1));
        cb_jenis.setSelectedItem(""+model.getValueAt(i, 2));
        txt_harga.setText(""+model.getValueAt(i, 3));
        txt_stok.setText(""+model.getValueAt(i, 4));
        btn_hapus.setEnabled(true);
        btn_edit.setEnabled(true);
    }
    
    public void IdOtomatis(){
        try{
            com.mysql.jdbc.Statement st = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From menu order by id_menu desc";
            
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                String id = rs.getString("id_menu").substring(3);
                String AN = ""+(Integer.parseInt(id)+1);
                String nol = "";
                
                if(AN.length()==1){
                    nol="00";
                }else if(AN.length()==2){
                    nol="0";
                }else if(AN.length()==3){
                    nol="";
                }txt_idmenu.setText("MN"+nol+AN);
            }else{
                txt_idmenu.setText("MN001");
            }
        }catch(SQLException error){
                JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void LoadData(){
        id_menu = txt_idmenu.getText();
        nama_menu = txt_namamenu.getText();
        jenis = (String)cb_jenis.getSelectedItem();
        harga = txt_harga.getText();
        stok = txt_stok.getText();
        id_pegawai = lb_id.getText();
    }
    
    public void SimpanData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Insert Into menu (id_menu, nama_menu, jenis, harga, stok, id_pegawai)"+"values"+"('"+id_menu+"','"+nama_menu+"','"+jenis+"','"+harga+"','"+stok+"','"+id_pegawai+"')";
            PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDb().prepareStatement(sql);
            p.executeUpdate();
            
            GetData();
            Nonaktif();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        }catch(SQLException ex){
            Logger.getLogger(FormMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UbahData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Update menu Set nama_menu='"+nama_menu+"',"
                    + "jenis='"+jenis+"',"
                    + "harga='"+harga+"',"
                    + "stok='"+stok+"',"
                    + "id_pegawai='"+id_pegawai+"'WHERE id_menu='"+id_menu+"'";
            
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
        
        int pesan = JOptionPane.showConfirmDialog(null, "Anda yakin ingin menghapus data menu "+id_menu+"?","konfirmasi",JOptionPane.OK_CANCEL_OPTION);
        if(pesan == JOptionPane.OK_OPTION){
            try{
                com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
                String sql = "Delete From menu Where id_menu='"+id_menu+"'";
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
        txt_idmenu.setEnabled(false);
        txt_namamenu.setEnabled(false);
        cb_jenis.setEnabled(false);
        txt_harga.setEnabled(false);
        txt_stok.setEnabled(false);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    public void Aktif(){
        txt_idmenu.setEnabled(false);
        txt_namamenu.setEnabled(true);
        cb_jenis.setEnabled(true);
        txt_harga.setEnabled(true);
        txt_stok.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_tambah.setEnabled(false);
        txt_namamenu.requestFocus();
    }
    
    public void Kosongkan(){
        txt_namamenu.setText("");
        cb_jenis.setSelectedItem("----------");
        txt_harga.setText("");
        txt_stok.setText("");
    }
    
    public void Cari(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Connection con = Koneksi.KoneksiDb();
            Statement st = con.createStatement();
            String sql = "Select * From menu Where nama_menu like '%"+txt_cari.getText()+"%'";
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
        txt_idmenu = new javax.swing.JTextField();
        txt_namamenu = new javax.swing.JTextField();
        cb_jenis = new javax.swing.JComboBox<>();
        txt_harga = new javax.swing.JTextField();
        txt_stok = new javax.swing.JTextField();
        btn_tambah = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelmenu = new javax.swing.JTable();
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
        jLabel3.setText("ID Menu");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Nama Menu");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Jenis");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Harga");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setText("Stok");

        txt_idmenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_idmenu.setPreferredSize(new java.awt.Dimension(350, 30));

        txt_namamenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_namamenu.setPreferredSize(new java.awt.Dimension(350, 30));
        txt_namamenu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_namamenuKeyTyped(evt);
            }
        });

        cb_jenis.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cb_jenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Makanan", "Minuman" }));
        cb_jenis.setPreferredSize(new java.awt.Dimension(350, 30));

        txt_harga.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_harga.setPreferredSize(new java.awt.Dimension(350, 30));
        txt_harga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_hargaKeyTyped(evt);
            }
        });

        txt_stok.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_stok.setPreferredSize(new java.awt.Dimension(350, 30));
        txt_stok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_stokKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_stokKeyTyped(evt);
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

        tabelmenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Menu", "Nama Menu", "Jenis", "Harga", "Stok"
            }
        ));
        tabelmenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelmenuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelmenu);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
                                    .addComponent(txt_idmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_namamenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                                .addGap(64, 64, 64)
                                .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_idmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_namamenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(btn_tambah)
                        .addGap(12, 12, 12)
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_cari, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(btn_cari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("DATA MENU");

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_id))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(74, 74, 74)
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
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lb_id))
                .addGap(43, 43, 43)
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
        txt_namamenu.requestFocus();
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

    private void tabelmenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelmenuMouseClicked
        // TODO add your handling code here:
        SelectData();
        Aktif();
        txt_idmenu.setEnabled(false);
        btn_simpan.setEnabled(false);
    }//GEN-LAST:event_tabelmenuMouseClicked

    private void txt_namamenuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_namamenuKeyTyped
        // TODO add your handling code here:
        if(Character.isDigit(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nama menu hanya bisa memasukan karakter huruf");
        }
    }//GEN-LAST:event_txt_namamenuKeyTyped

    private void txt_hargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_hargaKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom harga hanya bisa memasukan karakter angka");
        }
    }//GEN-LAST:event_txt_hargaKeyTyped

    private void txt_stokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_stokKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom stok hanya bisa memasukan karakter angka");
        }
    }//GEN-LAST:event_txt_stokKeyTyped

    private void txt_stokKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_stokKeyReleased
        // TODO add your handling code here:
        String input = txt_stok.getText();
        if(input.length()>3){
            JOptionPane.showMessageDialog(rootPane, "Jumlah input melebihi batas");
            txt_stok.setText("");
        }
    }//GEN-LAST:event_txt_stokKeyReleased

    private void txt_cariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cariKeyTyped
        Cari();
    }//GEN-LAST:event_txt_cariKeyTyped

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        Cari();
    }//GEN-LAST:event_btn_cariActionPerformed

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
                FormLaporanPendapatan.lb_id.setText(r.getString(5));
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
                FormLogAktivitasPegawaiManager.lb_id.setText(r.getString(5));
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
            java.util.logging.Logger.getLogger(FormMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JComboBox<String> cb_jenis;
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
    private javax.swing.JTable tabelmenu;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_harga;
    private javax.swing.JTextField txt_idmenu;
    private javax.swing.JTextField txt_namamenu;
    private javax.swing.JTextField txt_stok;
    // End of variables declaration//GEN-END:variables
}
