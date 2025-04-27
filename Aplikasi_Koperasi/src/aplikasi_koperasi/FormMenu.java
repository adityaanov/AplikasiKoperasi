package aplikasi_koperasi;

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
    public FormMenu() {
        initComponents();
        Nonaktif();
        
        model = new DefaultTableModel();
        tabelmenu.setModel(model);
        model.addColumn("ID Menu");
        model.addColumn("Nama Menu");
        model.addColumn("Kategori");
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
            
            while (rs.next()){
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
        txt_nmmenu.setText(""+model.getValueAt(i, 1));
        cb_kategori.setSelectedItem(""+model.getValueAt(i, 2));
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
                }
                    txt_idmenu.setText("MN"+nol+AN);
            }else{
                txt_idmenu.setText("MN001");
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void LoadData(){
        id_menu = txt_idmenu.getText();
        nama_menu = txt_nmmenu.getText();
        jenis = (String)cb_kategori.getSelectedItem();
        harga = txt_harga.getText();
        stok = txt_stok.getText();
        id_pegawai = lb_id.getText();
    }
    
    public void SimpanData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Insert into menu (id_menu, nama_menu, jenis, harga, stok, id_pegawai)" + "values" + "('"+id_menu+"','"+nama_menu+"','"+jenis+"','"+harga+"','"+stok+"','"+id_pegawai+"')";
            PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDb().prepareStatement(sql);
            p.executeUpdate();
            
            GetData();
            Nonaktif();
            
            JOptionPane.showMessageDialog(null, "Data berhasil Disimpan");
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
                    + "id_pegawai='"+id_pegawai+"' WHERE id_menu='"+id_menu+"'";
            
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
                String sql = "Delete From menu Where id_menu"+id_menu+"'";
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
        txt_nmmenu.setEnabled(false);
        cb_kategori.setEnabled(false);
        txt_harga.setEnabled(false);
        txt_stok.setEnabled(false);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    public void Aktif(){
        txt_idmenu.setEnabled(false);
        txt_nmmenu.setEnabled(true);
        cb_kategori.setEnabled(true);
        txt_harga.setEnabled(true);
        txt_stok.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_tambah.setEnabled(false);
        txt_nmmenu.requestFocus();
    }
    
    public void Kosongkan(){
        txt_nmmenu.setText("");
        cb_kategori.setSelectedItem("----------");
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
        jPanel2 = new javax.swing.JPanel();
        btn_keluar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_idmenu = new javax.swing.JTextField();
        txt_nmmenu = new javax.swing.JTextField();
        txt_harga = new javax.swing.JTextField();
        txt_stok = new javax.swing.JTextField();
        cb_kategori = new javax.swing.JComboBox<>();
        btn_tambah = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelmenu = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 75));

        btn_keluar.setBackground(new java.awt.Color(255, 255, 255));
        btn_keluar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_keluar.setForeground(new java.awt.Color(255, 255, 255));
        btn_keluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exit.png"))); // NOI18N
        btn_keluar.setBorder(null);
        btn_keluar.setPreferredSize(new java.awt.Dimension(45, 45));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(743, Short.MAX_VALUE)
                .addComponent(btn_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(800, 550));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("ID Menu");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Nama Menu");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Kategori");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Harga");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setText("Stok");

        txt_idmenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_idmenu.setPreferredSize(new java.awt.Dimension(400, 30));

        txt_nmmenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_nmmenu.setPreferredSize(new java.awt.Dimension(400, 30));
        txt_nmmenu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nmmenuKeyTyped(evt);
            }
        });

        txt_harga.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_harga.setPreferredSize(new java.awt.Dimension(400, 30));
        txt_harga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_hargaKeyTyped(evt);
            }
        });

        txt_stok.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_stok.setPreferredSize(new java.awt.Dimension(400, 30));
        txt_stok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_stokActionPerformed(evt);
            }
        });
        txt_stok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_stokKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_stokKeyTyped(evt);
            }
        });

        cb_kategori.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cb_kategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Makanan", "Minuman" }));
        cb_kategori.setPreferredSize(new java.awt.Dimension(400, 30));
        cb_kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_kategoriActionPerformed(evt);
            }
        });

        btn_tambah.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_tambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btn_tambah.setText("Tambah");
        btn_tambah.setPreferredSize(new java.awt.Dimension(85, 45));
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_simpan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/floppy-disk.png"))); // NOI18N
        btn_simpan.setText("Simpan");
        btn_simpan.setPreferredSize(new java.awt.Dimension(85, 45));
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_edit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit.png"))); // NOI18N
        btn_edit.setText("Edit");
        btn_edit.setPreferredSize(new java.awt.Dimension(85, 45));
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_hapus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_hapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/bin.png"))); // NOI18N
        btn_hapus.setText("Hapus");
        btn_hapus.setPreferredSize(new java.awt.Dimension(85, 45));
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        txt_cari.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_cari.setPreferredSize(new java.awt.Dimension(600, 40));

        btn_cari.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_cari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/3844432_magnifier_search_zoom_icon.png"))); // NOI18N
        btn_cari.setBorder(null);
        btn_cari.setPreferredSize(new java.awt.Dimension(40, 40));

        tabelmenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Menu", "Nama Menu", "Kategori", "Harga", "Stok"
            }
        ));
        tabelmenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelmenuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelmenu);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(124, 124, 124)
                                    .addComponent(txt_idmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_nmmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(29, 29, 29)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btn_hapus, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                .addComponent(btn_edit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_simpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_tambah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_idmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txt_nmmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("MANAGER");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("DATA MENU");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dashboard.png"))); // NOI18N
        jLabel8.setText("Dashboard");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/menu.png"))); // NOI18N
        jLabel9.setText("Data Menu");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/kitchen-table.png"))); // NOI18N
        jLabel10.setText("Data Meja");

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Report.png"))); // NOI18N
        jLabel11.setText("Laporan");

        jLabel12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/309041_users_group_people_icon.png"))); // NOI18N
        jLabel12.setText("Log Aktivitas");

        lb_id.setForeground(new java.awt.Color(204, 204, 204));
        lb_id.setText("ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb_id))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(lb_id))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        IdOtomatis();
        Kosongkan();
        Aktif();
        txt_nmmenu.requestFocus();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void cb_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_kategoriActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
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
        UbahData();
        Kosongkan();
        Nonaktif();
        btn_tambah.setEnabled(true);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
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
        SelectData();
        Aktif();
        txt_idmenu.setEditable(false);
        btn_simpan.setEnabled(false);
    }//GEN-LAST:event_tabelmenuMouseClicked

    private void txt_nmmenuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nmmenuKeyTyped
        if(Character.isDigit(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nama menu hanya bisa memasukan karakter huruf");
        }
    }//GEN-LAST:event_txt_nmmenuKeyTyped

    private void txt_hargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_hargaKeyTyped
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nama menu hanya bisa memasukan karakter angka");
        }
    }//GEN-LAST:event_txt_hargaKeyTyped

    private void txt_stokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_stokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_stokActionPerformed

    private void txt_stokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_stokKeyTyped
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nama menu hanya bisa memasukan karakter angka");
        }
    }//GEN-LAST:event_txt_stokKeyTyped

    private void txt_stokKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_stokKeyReleased
        String input = txt_stok.getText();
        if(input.length()>3){
            JOptionPane.showMessageDialog(rootPane, "Jumlah input melebihi batas");
            txt_stok.setText("");
        }
    }//GEN-LAST:event_txt_stokKeyReleased

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
    private javax.swing.JButton btn_keluar;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JComboBox<String> cb_kategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_id;
    private javax.swing.JTable tabelmenu;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_harga;
    private javax.swing.JTextField txt_idmenu;
    private javax.swing.JTextField txt_nmmenu;
    private javax.swing.JTextField txt_stok;
    // End of variables declaration//GEN-END:variables
}
