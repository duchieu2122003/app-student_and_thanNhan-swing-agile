/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.SinhVienDAO;
import dao.ThanNhanDAO;
import database.JDBC;
import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.SinhVien;
import model.ThanNhan;

/**
 *
 * @author dinhv
 */
public class FromThanNhan extends javax.swing.JFrame {

    List<SinhVien> listSinhVien;
    List<ThanNhan> listThanNhan;
    int gio = 00, phut = 00, giay = 00;
    int index;

    /**
     * Creates new form FromThanNhan
     */
    public FromThanNhan() {
        this.setUndecorated(true);
        initComponents();
        tblthannhan.setRowHeight(23);
        setLocationRelativeTo(null);
        time();
        demTime();
        fillToTable();
        txtngaysinh.getSettings().setAllowKeyboardEditing(false);
    }

    public void time() {
        new Thread() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
                while (true) {
                    Date date = new Date();
                    try {
                        sleep(1000);
                    } catch (Exception e) {
                    }
                    lbltime.setText(sdf.format(date));
                }
            }
        }.start();
    }

    public void demTime() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                    } catch (Exception e) {
                    }
                    giay++;
                    if (giay == 60) {
                        giay = 00;
                        phut++;
                    }
                    if (phut == 60) {
                        phut = 00;
                        gio++;
                    }
                    lbldemtime.setText(gio + ":" + phut + ":" + giay);
                }
            }
        }.start();
    }

    public boolean kiemTraTrong(String txt) {
        Matcher checknull = Pattern.compile("^\\s*$").matcher(txt);
        return checknull.matches();
    }

    public boolean kiemTraChu(String txt) {
        Matcher checkChu = Pattern.compile("^[a-zA-Z ]*.*[^\\d]").matcher(txt);
        return checkChu.matches();
    }

    public boolean kiemTra() {
        if (kiemTraTrong(txtma.getText())) {
            JOptionPane.showMessageDialog(this, "Chưa nhập mã sinh viên");
            return false;
        }
        if (kiemTraTrong(txtten.getText())) {
            JOptionPane.showMessageDialog(this, "Chưa nhập tên");
            return false;
        }
        if (!(kiemTraChu(txtten.getText()))) {
            JOptionPane.showMessageDialog(this, "Tên phải là kiểu chữ");
            return false;
        }
        if (kiemTraTrong(txtngaysinh.getText())) {
            JOptionPane.showMessageDialog(this, "Chưa chọn ngày sinh");
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            Date dateNow = new Date();
            Date date = sdf.parse(txtngaysinh.getText());
            if (dateNow.before(date)) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không vượt quá ngày hiện tại");
                return false;
            }
        } catch (Exception e) {
        }

        if (kiemTraTrong(txtsdt.getText())) {
            JOptionPane.showMessageDialog(this, "Chưa nhập số điện thoại");
            return false;
        }
        Matcher checkSDT = Pattern.compile("^0[0-9]{9}$").matcher(txtsdt.getText().trim().replaceAll(" ", ""));
        if (!(checkSDT.matches())) {
            JOptionPane.showMessageDialog(this, "Phải là kiểu số, số thứ nhất là 0 và có 10 số");
            return false;
        }
        return true;
    }

    public String validateNgaySinh() {
        String ngaySinh = "";
        try {
            ngaySinh = txtngaysinh.getDateStringOrEmptyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ngaySinh;
    }

//    public void in() {
//        listSinhVien = new SinhVienDAO().selectAll();
//        listThanNhan = new ThanNhanDAO().selectAll();
//        if (new ThanNhanDAO().selectAll() == null) {
//            System.out.println("Không có");
//        }
//
//    }
    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblthannhan.getModel();
        model.setRowCount(0);
        listThanNhan = new ThanNhanDAO().selectAll();
        for (ThanNhan tn : listThanNhan) {
            model.addRow(new Object[]{tn.getMaSV(), tn.getHoVaTen(), tn.getNgaySinh(), tn.getSoDienThoai(), tn.getQueQuan()});
        }
    }

    public void showData() {
        index = tblthannhan.getSelectedRow();
        if (index > -1) {
            ThanNhan tn = listThanNhan.get(index);
            txtma.setText((String) tblthannhan.getValueAt(index, 0));
            txtten.setText((String) tblthannhan.getValueAt(index, 1));
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");
                Date date = sdf.parse((String) tblthannhan.getValueAt(index, 2));
                txtngaysinh.setText(sdf2.format(date));
            } catch (Exception e) {
            }
            txtsdt.setText((String) tblthannhan.getValueAt(index, 3));
            cboquequan.setSelectedItem((String) tblthannhan.getValueAt(index, 4));
        }
    }

    public void insert() {
        int check = 0;
        listSinhVien = new SinhVienDAO().selectAll();
        listThanNhan = new ThanNhanDAO().selectAll();
        if (kiemTra()) {
            for (SinhVien sinhvien : listSinhVien) {
                if (txtma.getText().trim().equalsIgnoreCase(sinhvien.getMaSV())) {
                    check = 1;
                    for (ThanNhan tn1 : listThanNhan) {
                        if (txtsdt.getText().trim().equalsIgnoreCase(tn1.getSoDienThoai())) {
                            JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại");
                            return;
                        }
                    }
                    ThanNhan tn = new ThanNhan();
                    tn.setMaSV(txtma.getText().trim().replaceAll(" ", ""));
                    tn.setHoVaTen(txtten.getText().trim().replace("  ", " "));
                    tn.setNgaySinh(validateNgaySinh());
                    tn.setSoDienThoai(txtsdt.getText().trim().replaceAll(" ", ""));
                    tn.setQueQuan(cboquequan.getSelectedItem() + "");
                    new ThanNhanDAO().insert(tn);
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    return;
                }
            }
            if (check == 0) {
                JOptionPane.showMessageDialog(this, "Mã sinh viên không tồn tại");
                return;
            }
        }
    }

    public void update() {
        int check = 0;
        listSinhVien = new SinhVienDAO().selectAll();
        listThanNhan = new ThanNhanDAO().selectAll();
        index = tblthannhan.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đối tượng cần sửa");
            return;
        }
        ThanNhan tn2 = listThanNhan.get(index);
        if (kiemTra()) {
            for (SinhVien sinhvien : listSinhVien) {
                if (txtma.getText().trim().equalsIgnoreCase(sinhvien.getMaSV())) {
                    check = 1;
                    if (!(txtsdt.getText().trim().equalsIgnoreCase(tn2.getSoDienThoai()))) {
                        for (ThanNhan tn1 : listThanNhan) {
                            if (txtsdt.getText().trim().equalsIgnoreCase(tn1.getSoDienThoai())) {
                                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại");
                                return;
                            }
                        }
                    }
                    ThanNhan tn = new ThanNhan();
                    tn.setMaSV(txtma.getText().trim().replaceAll(" ", ""));
                    tn.setHoVaTen(txtten.getText().trim().replace("  ", " "));
                    tn.setNgaySinh(validateNgaySinh());
                    tn.setSoDienThoai(txtsdt.getText().trim().replaceAll(" ", ""));
                    tn.setQueQuan(cboquequan.getSelectedItem() + "");
                    new ThanNhanDAO().update(tn, tn2.getSoDienThoai());
                    JOptionPane.showMessageDialog(this, "Sửa thành công");
                    return;
                }
            }
            if (check == 0) {
                JOptionPane.showMessageDialog(this, "Mã sinh viên không tồn tại");
                return;
            }
        }
    }

    public void delete() {
        listThanNhan = new ThanNhanDAO().selectAll();
        index = tblthannhan.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đối tượng cần xóa");
        } else {
            ThanNhan tn = listThanNhan.get(index);
            new ThanNhanDAO().delete(tn);
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        }
    }

    public void timKiem() {
        listThanNhan = new ThanNhanDAO().selectAll();
        int xac_Nhan = 0;
        if (kiemTraTrong(txttimkiemma.getText())) {
            JOptionPane.showMessageDialog(this, "Nhập dữ liệu cần tìm kiếm");
            return;
        }
        if (!(kiemTraTrong(txttimkiemma.getText()))) {
            DefaultTableModel model = (DefaultTableModel) tblthannhan.getModel();
            model.setRowCount(0);
            for (ThanNhan tn : listThanNhan) {
                if (txttimkiemma.getText().trim().toLowerCase().equalsIgnoreCase(tn.getMaSV().toLowerCase())) {
                    model.addRow(new Object[]{tn.getMaSV(), tn.getHoVaTen(), tn.getNgaySinh(), tn.getSoDienThoai(), tn.getQueQuan()});
                    xac_Nhan = 1;
                }
            }
            if (xac_Nhan == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy mã sinh viên");
                return;
            }
        }
    }

    public void clear() {
        txtma.setText("");
        txtngaysinh.setText("");
        txtten.setText("");
        txtsdt.setText("");
        cboquequan.setSelectedIndex(0);
    }

    public void huyTim() {
        fillToTable();
    }

    public void exit() {
        System.exit(0);
    }

    public void fromSinhVien() {
        this.dispose();
        new FromSinhVien().setVisible(true);
    }

    public void dangNhap() {
        this.dispose();
        new FromDangNhap().setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblthannhan = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtten = new javax.swing.JTextField();
        txtma = new javax.swing.JTextField();
        txtsdt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txttimkiemma = new javax.swing.JTextField();
        txtngaysinh = new com.github.lgooddatepicker.components.DatePicker();
        cboquequan = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        kButton1 = new com.k33ptoo.components.KButton();
        kButton2 = new com.k33ptoo.components.KButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        lbltime = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lbldemtime = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Thân Nhân");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_Tools_20px_1.png"))); // NOI18N
        jButton4.setText("Sửa");
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_Delete_Key_20px.png"))); // NOI18N
        jButton5.setText("Xóa");
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        tblthannhan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã SV", "Họ và tên", "Ngày sinh", "Số điện thoại", "Quê quán"
            }
        ));
        tblthannhan.setSelectionBackground(new java.awt.Color(232, 57, 95));
        tblthannhan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblthannhanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblthannhan);

        jLabel4.setText("• Thông tin thân nhân");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_broom_20px.png"))); // NOI18N
        jButton9.setText("Clear");
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel5.setText("Mã sinh viên");

        jLabel6.setText("Ngày sinh");

        jLabel8.setText("Họ tên");

        jLabel10.setText("Quê quán");

        jLabel11.setText("Số điện thoại");

        txtma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmaActionPerformed(evt);
            }
        });

        jLabel1.setText("Mã sinh viên");

        cboquequan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "An Giang", "Bà rịa – Vũng tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội ", "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "Thành phố Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái" }));
        cboquequan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboquequanActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_search_20px.png"))); // NOI18N
        jButton1.setText("Tìm kiếm");
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_add_administrator_20px.png"))); // NOI18N
        jButton3.setText("Thêm");
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_cancel_20px.png"))); // NOI18N
        jButton2.setText("Hủy Tìm");
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("• Danh sách thân nhân");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N

        kGradientPanel1.setkEndColor(new java.awt.Color(153, 153, 153));
        kGradientPanel1.setkStartColor(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        kGradientPanel2.setkEndColor(new java.awt.Color(153, 153, 153));
        kGradientPanel2.setkStartColor(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        kButton1.setText("Sinh Viên");
        kButton1.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        kButton1.setForeground(new java.awt.Color(0, 0, 0));
        kButton1.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        kButton1.setkBorderRadius(20);
        kButton1.setkEndColor(new java.awt.Color(153, 153, 153));
        kButton1.setkForeGround(new java.awt.Color(0, 0, 0));
        kButton1.setkHoverEndColor(new java.awt.Color(24, 119, 242));
        kButton1.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton1.setkHoverStartColor(new java.awt.Color(24, 119, 242));
        kButton1.setkPressedColor(new java.awt.Color(153, 153, 153));
        kButton1.setkSelectedColor(new java.awt.Color(204, 0, 51));
        kButton1.setkStartColor(new java.awt.Color(204, 204, 204));
        kButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton1ActionPerformed(evt);
            }
        });

        kButton2.setText("Thân Nhân");
        kButton2.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        kButton2.setForeground(new java.awt.Color(0, 0, 0));
        kButton2.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        kButton2.setkBorderRadius(20);
        kButton2.setkEndColor(new java.awt.Color(153, 153, 153));
        kButton2.setkForeGround(new java.awt.Color(0, 0, 0));
        kButton2.setkHoverEndColor(new java.awt.Color(24, 119, 242));
        kButton2.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton2.setkHoverStartColor(new java.awt.Color(24, 119, 242));
        kButton2.setkStartColor(new java.awt.Color(204, 204, 204));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons-log-out-24.png"))); // NOI18N
        jButton10.setText("Thoát");
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_home.png"))); // NOI18N
        jButton11.setText("Đăng nhập");
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_clock.png"))); // NOI18N

        lbltime.setText("   ");
        lbltime.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbltime.setForeground(new java.awt.Color(255, 0, 0));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_clock_cat.png"))); // NOI18N

        lbldemtime.setText(" ");
        lbldemtime.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbldemtime.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel8)
                                                .addComponent(jLabel6))
                                            .addGap(19, 19, 19)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtten, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtngaysinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtma, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(13, 13, 13)
                                            .addComponent(jButton3)
                                            .addGap(76, 76, 76)
                                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(84, 84, 84)
                                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(48, 48, 48)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txttimkiemma, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton2))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtsdt, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addGap(31, 31, 31)
                                    .addComponent(cboquequan, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(12, 12, 12))
                    .addComponent(kGradientPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbltime, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbldemtime, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel14)
                        .addComponent(lbltime))
                    .addComponent(lbldemtime, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txttimkiemma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addGap(27, 27, 27)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(txtsdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cboquequan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(txtten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtngaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton4)
                            .addComponent(jButton5)
                            .addComponent(jButton9))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jButton11)
                        .addGap(76, 76, 76)
                        .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(kButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        update();
        fillToTable();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        delete();
        fillToTable();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tblthannhanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblthannhanMouseClicked
        showData();
    }//GEN-LAST:event_tblthannhanMouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        clear();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmaActionPerformed

    private void cboquequanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboquequanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboquequanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        timKiem();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        insert();
        fillToTable();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        huyTim();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        exit();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        dangNhap();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void kButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton1ActionPerformed
        fromSinhVien();
    }//GEN-LAST:event_kButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(FromThanNhan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FromThanNhan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FromThanNhan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FromThanNhan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FromThanNhan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboquequan;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.k33ptoo.components.KButton kButton1;
    private com.k33ptoo.components.KButton kButton2;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel2;
    private javax.swing.JLabel lbldemtime;
    private javax.swing.JLabel lbltime;
    private javax.swing.JTable tblthannhan;
    private javax.swing.JTextField txtma;
    private com.github.lgooddatepicker.components.DatePicker txtngaysinh;
    private javax.swing.JTextField txtsdt;
    private javax.swing.JTextField txtten;
    private javax.swing.JTextField txttimkiemma;
    // End of variables declaration//GEN-END:variables
}
