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
public class FormPegawai extends javax.swing.JFrame {
    private DefaultTableModel model;
    String id_pegawai, nama_pegawai, jk, jenis, no_hp, alamat;
    
    /**
     * Creates new form FormMenu
     */
    public FormPegawai() {
        initComponents();
        Nonaktif();
        
        model = new DefaultTableModel();
        tabelpegawai.setModel(model);
        model.addColumn("ID Pegawai");
        model.addColumn("Nama Pegawai");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Jenis");
        model.addColumn("No Handphone");
        model.addColumn("Alamat");
        
        GetData();
        IdOtomatis();
    }
    
    public void GetData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Statement stat = (Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From pegawai";
            ResultSet rs = stat.executeQuery(sql);
            
            while (rs.next()){
                Object[] obj = new Object[6];
                obj [0] = rs.getString("id_pegawai");
                obj [1] = rs.getString("nama_pegawai");
                obj [2] = rs.getString("jk");
                obj [3] = rs.getString("jenis");
                obj [4] = rs.getString("no_hp");
                obj [5] = rs.getString("alamat");
                
                model.addRow(obj);
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void SelectData(){
        int i = tabelpegawai.getSelectedRow();
        if(i == -1){
            return;
        }
        txt_idpegawai.setText(""+model.getValueAt(i, 0));
        txt_nmpegawai.setText(""+model.getValueAt(i, 1));
        if("p".equals(model.getValueAt(i, 2).toString())){
            rb_perempuan.setSelected(true);
        }else{
            rb_laki.setSelected(true);
        }
        cb_jabatan.setSelectedItem(""+model.getValueAt(i, 3));
        txt_nohp.setText(""+model.getValueAt(i, 4));
        txt_alamat.setText(""+model.getValueAt(i, 5));
        btn_hapus.setEnabled(true);
        btn_edit.setEnabled(true);
    }
    
    public void IdOtomatis(){
        try{
            com.mysql.jdbc.Statement st = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From pegawai order by id_pegawai desc";
            
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                String id = rs.getString("id_pegawai").substring(3);
                String AN = ""+(Integer.parseInt(id)+1);
                String nol = "";
                
                if(AN.length()==1){
                    nol="00";
                }else if(AN.length()==2){
                    nol="0";
                }else if(AN.length()==3){
                    nol="";
                }
                    txt_idpegawai.setText("PG"+nol+AN);
            }else{
                txt_idpegawai.setText("PG001");
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void LoadData(){
        id_pegawai = txt_idpegawai.getText();
        nama_pegawai = txt_nmpegawai.getText();
        jk = null;
        if(rb_laki.isSelected()){
            jk = "L";
        }else if(rb_perempuan.isSelected()){
            jk = "P";
        }
        jenis = (String)cb_jabatan.getSelectedItem();
        no_hp = txt_nohp.getText();
        alamat = txt_alamat.getText();
    }
    
    public void SimpanData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Insert into pegawai (id_pegawai, nama_pegawai, jk, jenis, no_hp, alamat)" + "values" + "('"+id_pegawai+"','"+nama_pegawai+"','"+jk+"','"+jenis+"','"+no_hp+"','"+alamat+"')";
            PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDb().prepareStatement(sql);
            p.executeUpdate();
            
            GetData();
            Nonaktif();
            
            JOptionPane.showMessageDialog(null, "Data berhasil Disimpan");
        }catch(SQLException ex){
            Logger.getLogger(FormPegawai.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UbahData(){
        LoadData();
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Update pegawai Set nama_pegawai='"+nama_pegawai+"',"
                    + "jk='"+jk+"',"
                    + "jenis='"+jenis+"',"
                    + "no_hp='"+no_hp+"',"
                    + "alamat='"+alamat+"'WHERE id_pegawai='"+id_pegawai+"'";
            
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
        
        int pesan = JOptionPane.showConfirmDialog(null, "Anda yakin ingin menghapus data pegawai "+id_pegawai+"?","konfirmasi",JOptionPane.OK_CANCEL_OPTION);
        if(pesan == JOptionPane.OK_OPTION){
            try{
                com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
                String sql = "Delete From pegawai Where id_pegawai"+id_pegawai+"'";
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
        txt_idpegawai.setEnabled(false);
        txt_nmpegawai.setEnabled(false);
        rb_laki.setEnabled(false);
        rb_perempuan.setEnabled(false);
        cb_jabatan.setEnabled(false);
        txt_nohp.setEnabled(false);
        txt_alamat.setEnabled(false);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    public void Aktif(){
        txt_idpegawai.setEnabled(false);
        txt_nmpegawai.setEnabled(true);
        rb_laki.setEnabled(true);
        rb_perempuan.setEnabled(true);
        cb_jabatan.setEnabled(true);
        txt_nohp.setEnabled(true);
        txt_alamat.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_tambah.setEnabled(false);
        txt_nmpegawai.requestFocus();
    }
    
    public void Kosongkan(){
        txt_nmpegawai.setText("");
        rb_laki.isSelected();
        rb_perempuan.isSelected();
        cb_jabatan.setSelectedItem("----------");
        txt_nohp.setText("");
        txt_alamat.setText("");
    }
    
    public void Cari(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Connection con = Koneksi.KoneksiDb();
            Statement st = con.createStatement();
            String sql = "Select * From pegawai Where nama_pegawai like '%"+txt_cari.getText()+"%'";
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

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        txt_idpegawai = new javax.swing.JTextField();
        txt_nmpegawai = new javax.swing.JTextField();
        txt_nohp = new javax.swing.JTextField();
        cb_jabatan = new javax.swing.JComboBox<>();
        btn_tambah = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelpegawai = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        rb_laki = new javax.swing.JRadioButton();
        rb_perempuan = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_alamat = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
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
        btn_keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_keluarActionPerformed(evt);
            }
        });

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
        jLabel3.setText("ID Pegawai");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Nama Pegawai");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Jabatan");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("No. Handphone");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setText("Alamat");

        txt_idpegawai.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_idpegawai.setPreferredSize(new java.awt.Dimension(220, 30));
        txt_idpegawai.setRequestFocusEnabled(false);

        txt_nmpegawai.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_nmpegawai.setPreferredSize(new java.awt.Dimension(220, 30));
        txt_nmpegawai.setRequestFocusEnabled(false);
        txt_nmpegawai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nmpegawaiKeyTyped(evt);
            }
        });

        txt_nohp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_nohp.setPreferredSize(new java.awt.Dimension(220, 30));
        txt_nohp.setRequestFocusEnabled(false);
        txt_nohp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_nohpKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nohpKeyTyped(evt);
            }
        });

        cb_jabatan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cb_jabatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Admin", "Manager", "Kasir" }));
        cb_jabatan.setPreferredSize(new java.awt.Dimension(220, 30));
        cb_jabatan.setRequestFocusEnabled(false);
        cb_jabatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_jabatanActionPerformed(evt);
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
        btn_hapus.setPreferredSize(new java.awt.Dimension(65, 45));
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        txt_cari.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_cari.setPreferredSize(new java.awt.Dimension(300, 30));
        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });
        txt_cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cariKeyTyped(evt);
            }
        });

        btn_cari.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_cari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/3844432_magnifier_search_zoom_icon.png"))); // NOI18N
        btn_cari.setBorder(null);
        btn_cari.setPreferredSize(new java.awt.Dimension(30, 30));

        tabelpegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Pegawai", "Nama Pegawai", "Jenis Kelamin", "Jabatan", "No Handphone", "Alamat"
            }
        ));
        tabelpegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelpegawaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelpegawai);

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setText("Jenis Kelamin");

        rb_laki.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rb_laki);
        rb_laki.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rb_laki.setText("Laki-Laki");
        rb_laki.setPreferredSize(new java.awt.Dimension(110, 25));

        rb_perempuan.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rb_perempuan);
        rb_perempuan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rb_perempuan.setText("Perempuan");
        rb_perempuan.setPreferredSize(new java.awt.Dimension(110, 25));
        rb_perempuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_perempuanActionPerformed(evt);
            }
        });

        txt_alamat.setColumns(20);
        txt_alamat.setRows(5);
        txt_alamat.setPreferredSize(new java.awt.Dimension(220, 94));
        jScrollPane2.setViewportView(txt_alamat);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(txt_nmpegawai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addComponent(jLabel5)
                    .addComponent(cb_jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(rb_laki, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rb_perempuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_idpegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(txt_nohp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txt_cari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 23, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_idpegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nmpegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rb_laki, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rb_perempuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_nohp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(31, 31, 31))
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
        jLabel1.setText("ADMIN");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("DATA PEGAWAI");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dashboard.png"))); // NOI18N
        jLabel8.setText("Dashboard");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/309041_users_group_people_icon.png"))); // NOI18N
        jLabel9.setText("Data Pegawai");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/309041_users_group_people_icon.png"))); // NOI18N
        jLabel10.setText("Data Pengguna");

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
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(70, 70, 70)
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
                        .addContainerGap()
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
        txt_nmpegawai.requestFocus();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void cb_jabatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_jabatanActionPerformed
        
    }//GEN-LAST:event_cb_jabatanActionPerformed

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

    private void tabelpegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelpegawaiMouseClicked
        SelectData();
        Aktif();
        txt_idpegawai.setEditable(false);
        btn_simpan.setEnabled(false);
    }//GEN-LAST:event_tabelpegawaiMouseClicked

    private void txt_nmpegawaiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nmpegawaiKeyTyped
        if(Character.isDigit(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nama menu hanya bisa memasukan karakter huruf");
        }
    }//GEN-LAST:event_txt_nmpegawaiKeyTyped

    private void txt_nohpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nohpKeyTyped
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nama menu hanya bisa memasukan karakter angka");
        }
    }//GEN-LAST:event_txt_nohpKeyTyped

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        
    }//GEN-LAST:event_txt_cariActionPerformed

    private void txt_cariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cariKeyTyped
        Cari();
    }//GEN-LAST:event_txt_cariKeyTyped

    private void btn_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_keluarActionPerformed
        dispose();
    }//GEN-LAST:event_btn_keluarActionPerformed

    private void rb_perempuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_perempuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_perempuanActionPerformed

    private void txt_nohpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nohpKeyReleased
        String input = txt_nohp.getText();
        if(input.length()>13){
            JOptionPane.showMessageDialog(rootPane, "Jumlah input melebihi batas");
            txt_nohp.setText("");
        }
    }//GEN-LAST:event_txt_nohpKeyReleased

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
            java.util.logging.Logger.getLogger(FormPegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPegawai().setVisible(true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cb_jabatan;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_id;
    private javax.swing.JRadioButton rb_laki;
    private javax.swing.JRadioButton rb_perempuan;
    private javax.swing.JTable tabelpegawai;
    private javax.swing.JTextArea txt_alamat;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idpegawai;
    private javax.swing.JTextField txt_nmpegawai;
    private javax.swing.JTextField txt_nohp;
    // End of variables declaration//GEN-END:variables
}
