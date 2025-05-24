/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplikasikoperasi;

import Aplikasi.Koneksi;
import static aplikasikoperasi.FormDashboardKasir.lb_username;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author adity
 */
public class FormTransaksi extends javax.swing.JFrame {
    java.util.Date tglsekarang = new java.util.Date();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String tanggal = format.format(tglsekarang);
    private DefaultTableModel model;
    private DefaultTableModel model2;
    
    public FormTransaksi(){
        initComponents();
        Nonaktif();
        model = new DefaultTableModel();
        tabelmenu.setModel(model);
        model.addColumn("ID Menu");
        model.addColumn("Nama Menu");
        model.addColumn("Kategori");
        model.addColumn("Harga");
        model.addColumn("Stok");
        
        model2 = new DefaultTableModel();
        tabeldetailtransaksi.setModel(model2);
        model2.addColumn("ID Transaksi");
        model2.addColumn("ID Menu");
        model2.addColumn("Harga");
        model2.addColumn("Jumlah");
        model2.addColumn("Harga Total");
        
        SetJam();
        Utama();
        GetDataMenu();
        txt_tgl.setText(tanggal);
        txt_totalbyr.setText("0");
        txt_bayar.setText("0");
        txt_kembali.setText("0");
        
        txt_jml.setEnabled(false);
        txt_bayar.setEnabled(false);
        btn_plus.setEnabled(false);
        btn_kurang.setEnabled(false);
        btn_proses.setEnabled(false);
        btn_cetak.setEnabled(false);
        btn_carimeja.setEnabled(false);
        btn_cariplgn.setEnabled(false);
        txt_carimenu.setEnabled(false);
    }
    
    public void GetDataMenu(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Statement stat = (Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From menu";
            ResultSet rs = stat.executeQuery(sql);
            
            while(rs.next()){
                Object[] obj = new Object[5];
                obj [0] = rs.getString("id_menu");
                obj [1] = rs.getString("nama_menu");
                obj [2] = rs.getString("jenis");
                obj [3] = rs.getString("harga");
                obj [4] = rs.getString("stok");
                
                model.addRow(obj);
            }
            rs.close();
            stat.close();
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void SelectDataMenu(){
        int i = tabelmenu.getSelectedRow();
        if(i == -1){
            return;
        }
        txt_idmenu.setText(""+model.getValueAt(i, 0));
        txt_harga.setText(""+model.getValueAt(i, 3));
    }
    
    public void GetDataDetailTransaksi(){
        DefaultTableModel model = (DefaultTableModel) tabeldetailtransaksi.getModel();
        model.addRow(new Object[]{
            txt_idtransaksi.getText(),
            txt_idmenu.getText(),
            txt_harga.getText(),
            txt_jml.getText(),
            txt_hrgtotal.getText()
        });
    }
    
    public void IdOtomatis(){
        try{
            com.mysql.jdbc.Statement st = (com.mysql.jdbc.Statement) Koneksi.KoneksiDb().createStatement();
            String sql = "Select * From kasir order by id_transaksi desc";
            
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                String id = rs.getString("id_transaksi").substring(3);
                String AN = ""+(Integer.parseInt(id)+1);
                String nol = "";
                
                if(AN.length()==1){
                    nol="00";
                }else if(AN.length()==2){
                    nol="0";
                }else if(AN.length()==3){
                    nol="";
                }txt_idtransaksi.setText("TR"+nol+AN);
            }else{
                txt_idtransaksi.setText("TR001");
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void Nonaktif(){
        txt_idtransaksi.setEnabled(false);
        txt_tgl.setEnabled(false);
        txt_jam.setEnabled(false);
        txt_idpegawai.setEnabled(false);
        txt_idmenu.setEnabled(false);
        txt_harga.setEnabled(false);
        txt_hrgtotal.setEnabled(false);
        txt_totalbyr.setEnabled(false);
        txt_kembali.setEnabled(false);
        txt_idplgn.setEnabled(false);
        txt_idmeja.setEnabled(false);
    }
    
    public void Cari(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Connection con = Koneksi.KoneksiDb();
            Statement st = con.createStatement();
            String sql = " Select * From menu Where id_menu like '%"+txt_carimenu.getText()+"%' "
                    + "or nama_menu like '%"+txt_carimenu.getText()+"%'";
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                Object[] ob = new Object[5];
                ob [0] = rs.getString(1);
                ob [1] = rs.getString(2);
                ob [2] = rs.getString(3);
                ob [3] = rs.getString(4);
                ob [4] = rs.getString(5);
                
                model.addRow(ob);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public void SetJam(){
        ActionListener taskPerform = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                String nol_jam = "", nol_menit = "", nol_detik = "";
                    
                java.util.Date dateTime = new java.util.Date();
                int nilai_jam = dateTime.getHours();
                int nilai_menit = dateTime.getMinutes();
                int nilai_detik = dateTime.getSeconds();
                    
                if(nilai_jam <= 9) nol_jam = "0";
                if(nilai_menit <= 9) nol_menit = "0";
                if(nilai_detik <= 9) nol_detik = "0";
                    
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                    
                txt_jam.setText(jam+":"+menit+":"+detik+" "); 
            }
        };
        new Timer(1000, taskPerform).start();
    }
    
    public void TotalBiaya(){
        int jumlahBaris = tabeldetailtransaksi.getRowCount();
        int totalBiaya = 0;
        int jumlah, harga;
        for(int i = 0; i < jumlahBaris; i++){
            jumlah = Integer.parseInt(tabeldetailtransaksi.getValueAt(i, 2).toString());
            harga = Integer.parseInt(tabeldetailtransaksi.getValueAt(i, 3).toString());
            totalBiaya = totalBiaya + (jumlah * harga);
        }
        txt_totalbyr.setText(String.valueOf(totalBiaya));
    }
    
    public void TambahTransaksi(){
        int jumlah, harga, total;
        jumlah = Integer.valueOf(txt_jml.getText());
        harga = Integer.valueOf(txt_harga.getText());
        total = jumlah * harga;
        
        txt_hrgtotal.setText(String.valueOf(total));
        GetDataDetailTransaksi();
        TotalBiaya();
        Kosongkan2();
    }
    
    public void Kosong(){
        DefaultTableModel model = (DefaultTableModel) tabeldetailtransaksi.getModel();
        
        while(model.getRowCount()>0){
            model.removeRow(0);
        }
    }
    
    public void Utama(){
        txt_idplgn.setText("");
        txt_carimenu.setText("");
        txt_idmenu.setText("");
        txt_idmeja.setText("");
        txt_harga.setText("");
        txt_jml.setText("");
        IdOtomatis();
    }
    
    public void Kosongkan(){
        txt_carimenu.setText("");
        txt_totalbyr.setText("");
        txt_bayar.setText("");
        txt_kembali.setText("");
    }
    
    public void Kosongkan2(){
        txt_idmenu.setText("");
        txt_harga.setText("");
        txt_jml.setText("");
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_idplgn = new javax.swing.JTextField();
        txt_carimenu = new javax.swing.JTextField();
        txt_idmeja = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txt_idtransaksi = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txt_tgl = new javax.swing.JTextField();
        txt_jam = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txt_idpegawai = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelmenu = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        txt_idmenu = new javax.swing.JTextField();
        txt_harga = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txt_jml = new javax.swing.JTextField();
        txt_hrgtotal = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txt_totalbyr = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txt_bayar = new javax.swing.JTextField();
        txt_kembali = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabeldetailtransaksi = new javax.swing.JTable();
        btn_cariplgn = new javax.swing.JButton();
        btn_carimeja = new javax.swing.JButton();
        btn_plus = new javax.swing.JButton();
        btn_kurang = new javax.swing.JButton();
        btn_proses = new javax.swing.JButton();
        btn_tambah = new javax.swing.JButton();
        btn_cetak = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lb_dashboard = new javax.swing.JLabel();
        lb_transaksi = new javax.swing.JLabel();
        lb_catatantransaksi = new javax.swing.JLabel();
        lb_datapelanggan = new javax.swing.JLabel();
        lb_jabatan = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lb_updatestatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(750, 100));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("KASIR");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(750, 500));

        jLabel15.setText("ID Pelanggan");

        jLabel16.setText("Pencarian Menu");

        jLabel17.setText("ID Meja");

        txt_idplgn.setPreferredSize(new java.awt.Dimension(150, 25));

        txt_carimenu.setPreferredSize(new java.awt.Dimension(150, 25));

        txt_idmeja.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel18.setText("ID Transaksi");

        txt_idtransaksi.setPreferredSize(new java.awt.Dimension(75, 25));

        jLabel19.setText("Tanggal / Jam");

        txt_tgl.setPreferredSize(new java.awt.Dimension(75, 25));

        txt_jam.setPreferredSize(new java.awt.Dimension(75, 25));

        jLabel20.setText("ID Pegawai");

        txt_idpegawai.setPreferredSize(new java.awt.Dimension(75, 25));

        tabelmenu.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelmenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelmenuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelmenu);

        jLabel21.setText("ID Menu");

        txt_idmenu.setPreferredSize(new java.awt.Dimension(140, 25));

        txt_harga.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel22.setText("Harga");

        jLabel23.setText("Jumlah");

        txt_jml.setPreferredSize(new java.awt.Dimension(150, 25));
        txt_jml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_jmlActionPerformed(evt);
            }
        });

        txt_hrgtotal.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel24.setText("Harga Total");

        txt_totalbyr.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel25.setText("Total Bayar");

        jLabel26.setText("Bayar");

        txt_bayar.setPreferredSize(new java.awt.Dimension(150, 25));
        txt_bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_bayarActionPerformed(evt);
            }
        });

        txt_kembali.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel27.setText("Kembali");

        tabeldetailtransaksi.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabeldetailtransaksi);

        btn_cariplgn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search2.png"))); // NOI18N
        btn_cariplgn.setPreferredSize(new java.awt.Dimension(40, 40));
        btn_cariplgn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariplgnActionPerformed(evt);
            }
        });

        btn_carimeja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search2.png"))); // NOI18N
        btn_carimeja.setPreferredSize(new java.awt.Dimension(40, 30));
        btn_carimeja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_carimejaActionPerformed(evt);
            }
        });

        btn_plus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/plus.png"))); // NOI18N
        btn_plus.setPreferredSize(new java.awt.Dimension(45, 30));
        btn_plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_plusActionPerformed(evt);
            }
        });

        btn_kurang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/minus.png"))); // NOI18N
        btn_kurang.setPreferredSize(new java.awt.Dimension(45, 30));
        btn_kurang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kurangActionPerformed(evt);
            }
        });

        btn_proses.setText("Proses");
        btn_proses.setPreferredSize(new java.awt.Dimension(70, 25));
        btn_proses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prosesActionPerformed(evt);
            }
        });

        btn_tambah.setText("Tambah");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_cetak.setText("Cetak");
        btn_cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20)
                    .addComponent(btn_cetak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_tgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_jam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_idpegawai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_idtransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_carimenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(27, 27, 27)
                                .addComponent(txt_idplgn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cariplgn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_idmeja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_carimeja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22)
                            .addComponent(jLabel24)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txt_harga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(txt_idmenu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_jml, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(txt_hrgtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btn_kurang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btn_plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(btn_proses, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel27))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_totalbyr, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(jLabel17)
                        .addComponent(txt_idplgn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_idmeja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_carimeja, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btn_cariplgn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txt_carimenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_idtransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel19)
                        .addGap(12, 12, 12)
                        .addComponent(txt_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_idpegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txt_idmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel21))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel22))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txt_jml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel23)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txt_totalbyr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txt_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel26))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txt_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel27))))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_hrgtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btn_plus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_kurang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_proses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(btn_tambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cetak))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36))
        );

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("TRANSAKSI");

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

        lb_jabatan.setBackground(new java.awt.Color(204, 204, 204));
        lb_jabatan.setForeground(new java.awt.Color(204, 204, 204));
        lb_jabatan.setText("Jabatan");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(750, 100));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exit.png"))); // NOI18N
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(693, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel12)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 41, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_datapelanggan)
                    .addComponent(lb_catatantransaksi)
                    .addComponent(lb_transaksi)
                    .addComponent(lb_dashboard)
                    .addComponent(lb_updatestatus))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_jabatan))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
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
                    .addComponent(lb_jabatan))
                .addGap(42, 42, 42)
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

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        new FormLogin().show();
        this.dispose();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        txt_jml.setEnabled(true);
        txt_bayar.setEnabled(true);
        btn_plus.setEnabled(true);
        btn_kurang.setEnabled(true);
        btn_proses.setEnabled(true);
        btn_cetak.setEnabled(true);
        btn_carimeja.setEnabled(true);
        btn_cariplgn.setEnabled(true);
        txt_carimenu.setEnabled(true);
        Kosong();
        GetDataMenu();
        Kosongkan();
        Kosongkan2();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_cariplgnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariplgnActionPerformed
        DaftarPelanggan a = new DaftarPelanggan();
        a.setVisible(true);
    }//GEN-LAST:event_btn_cariplgnActionPerformed

    private void btn_carimejaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_carimejaActionPerformed
        DaftarMeja a = new DaftarMeja();
        a.setVisible(true);
    }//GEN-LAST:event_btn_carimejaActionPerformed

    private void tabelmenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelmenuMouseClicked
        SelectDataMenu();
        txt_jml.requestFocus();
    }//GEN-LAST:event_tabelmenuMouseClicked

    private void txt_jmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_jmlActionPerformed
        int tothrg = 0;
        int jumlah, harga;
        jumlah = Integer.parseInt(txt_jml.getText());
        harga = Integer.parseInt(txt_harga.getText());
        tothrg = (jumlah * harga);
        
        txt_hrgtotal.setText(String.valueOf(tothrg));
    }//GEN-LAST:event_txt_jmlActionPerformed

    private void txt_bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bayarActionPerformed
        int bayar, total, kembalian;
        total = Integer.valueOf(txt_totalbyr.getText());
        bayar = Integer.valueOf(txt_bayar.getText());
        
        if(total > bayar){
            JOptionPane.showMessageDialog(null, "Dana tidak cukup untuk melakukan pembayaran");
        }else{
            kembalian = bayar - total;
            txt_kembali.setText(String.valueOf(kembalian));
        }
    }//GEN-LAST:event_txt_bayarActionPerformed

    private void btn_plusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_plusActionPerformed
        TambahTransaksi();
    }//GEN-LAST:event_btn_plusActionPerformed

    private void btn_kurangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kurangActionPerformed
        DefaultTableModel model = (DefaultTableModel)tabeldetailtransaksi.getModel();
        int row = tabeldetailtransaksi.getSelectedRow();
        model.removeRow(row);
        TotalBiaya();
        txt_bayar.setText("0");
        txt_kembali.setText("0");
    }//GEN-LAST:event_btn_kurangActionPerformed

    private void btn_prosesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prosesActionPerformed
        DefaultTableModel model = (DefaultTableModel)tabeldetailtransaksi.getModel();
        
        String notrans = txt_idtransaksi.getText();
        String tanggal = txt_tgl.getText();
        String idpegawai = txt_idpegawai.getText();
        String idpel = txt_idplgn.getText();
        String meja = txt_idmeja.getText();
        String subtotal = txt_hrgtotal.getText();
        String totalbayar = txt_totalbyr.getText();
        String bayar = txt_bayar.getText();
        String kembali = txt_kembali.getText();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            String sql = "Insert Into kasir (id_transaksi, tanggal, id_pegawai, id_pelanggan, id_meja, subtotal, total, bayar, kembali) Values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, notrans);
            p.setString(2, tanggal);
            p.setString(3, idpegawai);
            p.setString(4, idpel);
            p.setString(5, meja);
            p.setString(6, subtotal);
            p.setString(7, totalbayar);
            p.setString(8, bayar);
            p.setString(9, kembali);
            p.executeUpdate();
            p.close();
        }catch(Exception ex){
            Logger.getLogger(FormTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            Connection c = Koneksi.KoneksiDb();
            int baris = tabeldetailtransaksi.getRowCount();
            for (int i = 0; i < baris; i++){
                String sql = "Insert Into detail_transaksi (id_transaksi, id_menu, harga, jumlah, total_harga) Values ('"
                        +tabeldetailtransaksi.getValueAt(i, 0)+"','"+tabeldetailtransaksi.getValueAt(i, 1)+"','"+tabeldetailtransaksi.getValueAt(i, 2)
                        +"','"+tabeldetailtransaksi.getValueAt(i, 3)+"','"+tabeldetailtransaksi.getValueAt(i, 4)+"')";
                PreparedStatement p = c.prepareStatement(sql);
                p.executeUpdate();
                p.close();
            }
        }catch(Exception ex){
            Logger.getLogger(FormTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
        Kosongkan();
        Utama();
        Kosong();
        IdOtomatis();
        GetDataMenu();
        txt_jml.setEnabled(false);
        txt_bayar.setEnabled(false);
        btn_plus.setEnabled(false);
        btn_kurang.setEnabled(false);
        btn_proses.setEnabled(false);
        btn_carimeja.setEnabled(false);
        btn_cariplgn.setEnabled(false);
        txt_carimenu.setEnabled(false);
    }//GEN-LAST:event_btn_prosesActionPerformed

    private void lb_catatantransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_catatantransaksiMouseClicked
        new FormCatatanTransaksi().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+txt_idpegawai.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(txt_idpegawai.getText().equals(r.getString("id_pegawai")) &&
                        r.getString("jenis").equals("Kasir")){
                    FormCatatanTransaksi.txt_jabatan.setText(r.getString(4));
                    FormCatatanTransaksi.txt_idpegawai.setText(r.getString(5));
                }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_catatantransaksiMouseClicked

    private void lb_transaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_transaksiMouseClicked
        new FormTransaksi().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where username = '"+lb_username.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_username.getText().equals(r.getString("username")) &&
                        r.getString("jenis").equals("Kasir")){
                    FormTransaksi.lb_jabatan.setText(r.getString(4));
                    FormTransaksi.txt_idpegawai.setText(r.getString(5));
                }
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_lb_transaksiMouseClicked

    private void lb_datapelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_datapelangganMouseClicked
        new FormPelanggan().show();
        this.dispose();
        
        try{
            Connection c = Koneksi.KoneksiDb();
            Statement s = c.createStatement();
            
            String sql = "Select * From pengguna Where id_pegawai = '"+txt_idpegawai.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(txt_idpegawai.getText().equals(r.getString("id_pegawai")) &&
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
            
            String sql = "Select * From pengguna Where username = '"+lb_username.getText()+"'";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                if(lb_username.getText().equals(r.getString("username")) &&
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

    private void btn_cetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cetakActionPerformed
        String nota = JOptionPane.showInputDialog("Masukan ID Transaksi");
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String DB = "jdbc:mysql://localhost/aplikasi_koperasi";
            String user = "root";
            String pass = "";
            
            con = DriverManager.getConnection(DB, user, pass);
            Statement stat = con.createStatement();
            
            try{
                String report = ("src\\aplikasikoperasi\\notakoperasi.jrxml");
                HashMap hash = new HashMap();
                hash.put("kode", nota);
                JasperReport jasperReport = JasperCompileManager.compileReport(report);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hash, con);
                JasperViewer.viewReport(jasperPrint, false);
            }catch(Exception ex){
                Logger.getLogger(FormTransaksi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_btn_cetakActionPerformed

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
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTransaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_carimeja;
    private javax.swing.JButton btn_cariplgn;
    private javax.swing.JButton btn_cetak;
    private javax.swing.JButton btn_kurang;
    private javax.swing.JButton btn_plus;
    private javax.swing.JButton btn_proses;
    private javax.swing.JButton btn_tambah;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_catatantransaksi;
    private javax.swing.JLabel lb_dashboard;
    private javax.swing.JLabel lb_datapelanggan;
    public static javax.swing.JLabel lb_jabatan;
    private javax.swing.JLabel lb_transaksi;
    private javax.swing.JLabel lb_updatestatus;
    private javax.swing.JTable tabeldetailtransaksi;
    private javax.swing.JTable tabelmenu;
    private javax.swing.JTextField txt_bayar;
    private javax.swing.JTextField txt_carimenu;
    private javax.swing.JTextField txt_harga;
    private javax.swing.JTextField txt_hrgtotal;
    public static javax.swing.JTextField txt_idmeja;
    private javax.swing.JTextField txt_idmenu;
    public static javax.swing.JTextField txt_idpegawai;
    public static javax.swing.JTextField txt_idplgn;
    private javax.swing.JTextField txt_idtransaksi;
    private javax.swing.JTextField txt_jam;
    private javax.swing.JTextField txt_jml;
    private javax.swing.JTextField txt_kembali;
    private javax.swing.JTextField txt_tgl;
    private javax.swing.JTextField txt_totalbyr;
    // End of variables declaration//GEN-END:variables
}
