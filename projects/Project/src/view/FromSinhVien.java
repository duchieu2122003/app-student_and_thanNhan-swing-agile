/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import dao.SinhVienDAO;
import dao.ThanNhanDAO;
import database.JDBC;
import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import model.SinhVien;
import model.ThanNhan;

/**
 *
 * @author dinhv
 */
public class FromSinhVien extends javax.swing.JFrame {

    List<SinhVien> list;

    int index;
    int gio = 00, phut = 00, giay = 00;

    /**
     * Creates new form FromSinhVien
     */
    public FromSinhVien() {
        this.setUndecorated(true);
        initComponents();
        tblsinhvien.setRowHeight(23);
        setLocationRelativeTo(null);
        time();
        demTime();
        ngaysinh.getSettings().setAllowKeyboardEditing(false);
        fillTotable();
    }

    public String validateNgaySinh() {
        String ngaySinh = "";
        try {
            ngaySinh = ngaysinh.getDateStringOrEmptyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ngaySinh;
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

    public boolean kiemTraTrungMaSV() {
        list = new SinhVienDAO().selectAll();
        for (SinhVien sv : list) {
            if (txtma.getText().equalsIgnoreCase(sv.getMaSV())) {
                return false;
            }
        }
        return true;
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
        if (kiemTraTrong(ngaysinh.getText())) {
            JOptionPane.showMessageDialog(this, "Chưa chọn ngày sinh");
            return false;
        }
        Date dateNow = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            Date date = sdf.parse(ngaysinh.getText());
            if (dateNow.before(date)) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không vượt quá ngày hiện tại");
                return false;
            }
        } catch (Exception e) {
        }
        if (!(kiemTraTrong(txtchucvu.getText()))) {
            if (!(kiemTraChu(txtchucvu.getText()))) {
                JOptionPane.showMessageDialog(this, "Chức vụ phải là kiểu chữ");
                return false;
            }
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

        if (kiemTraTrong(txtemail.getText())) {
            JOptionPane.showMessageDialog(this, "Chưa Nhập email");
            return false;
        }
        Matcher checkEmail = Pattern.compile("^\\D\\w{1,}@\\w+(\\.\\w+){1,2}$").matcher(txtemail.getText().trim().replaceAll(" ", ""));
        if (!(checkEmail.matches())) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng");
            return false;
        }

        return true;
    }

    public void insert() {
        if (!(kiemTraTrungMaSV())) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại");
            return;
        }
        if (kiemTra()) {
            for (SinhVien sv : list) {
                if (txtsdt.getText().equalsIgnoreCase(sv.getSDT())) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại");
                    return;
                }
            }
            for (SinhVien sv : list) {
                if (txtemail.getText().equalsIgnoreCase(sv.getEmail())) {
                    JOptionPane.showMessageDialog(this, "Email đã tồn tại");
                    return;
                }
            }
            SinhVien sv = new SinhVien();
            sv.setMaSV(txtma.getText().trim().replaceAll(" ", ""));
            sv.setTenSV(txtten.getText().trim().replace("  ", " "));
            sv.setXepLoai(cboloai.getSelectedItem() + "");
            sv.setNgaySinh(validateNgaySinh());
            if (kiemTraTrong(txtchucvu.getText())) {
                sv.setChucVu("Null");
            } else {
                sv.setChucVu(txtchucvu.getText().trim().replaceAll(" ", " "));
            }
            sv.setQueQuan(cboquequan.getSelectedItem() + "");
            sv.setEmail(txtemail.getText().trim().replaceAll(" ", ""));
            sv.setSDT(txtsdt.getText().trim().replaceAll(" ", ""));
            new SinhVienDAO().insert(sv);
            JOptionPane.showMessageDialog(this, "Thêm thành công");
        }
    }

    public void fillTotable() {
        DefaultTableModel model = (DefaultTableModel) tblsinhvien.getModel();
        model.setRowCount(0);
        list = new SinhVienDAO().selectAll();
        for (SinhVien sv : list) {
            model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
        }
    }

    public void fillTotableNew() {
        DefaultTableModel model = (DefaultTableModel) tblsinhvien.getModel();
        model.setRowCount(0);
        for (SinhVien sv : list) {
            model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
        }
    }
//

    public void delete() {
        index = tblsinhvien.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đối tượng cần xóa");
        } else {
            new SinhVienDAO().deleteByid(tblsinhvien.getValueAt(index, 0)+"");
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        }
    }
//

    public void showData() {
        index = tblsinhvien.getSelectedRow();
        if (index > -1) {
            txtma.setText((String) tblsinhvien.getValueAt(index, 0));
            txtten.setText((String) tblsinhvien.getValueAt(index, 1));
            try {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");
                Date date = sdf1.parse((String) tblsinhvien.getValueAt(index, 2));
                ngaysinh.setText(sdf2.format(date));
            } catch (Exception e) {
            }
            cboquequan.setSelectedItem((String) tblsinhvien.getValueAt(index, 3));
            txtsdt.setText((String) tblsinhvien.getValueAt(index, 4));
            txtemail.setText((String) tblsinhvien.getValueAt(index, 5));
            cboloai.setSelectedItem(tblsinhvien.getValueAt(index, 6));
            txtchucvu.setText((String) tblsinhvien.getValueAt(index, 7));
        }
    }
//

    public void update() {
        list = new SinhVienDAO().selectAll();
        index = tblsinhvien.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đối tượng cần sửa");
        } else {
            if (!(txtma.getText().equalsIgnoreCase(tblsinhvien.getValueAt(index, 0)+""))) {
                for (SinhVien sv : list) {
                    if (txtma.getText().equalsIgnoreCase(sv.getMaSV())) {
                        JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại");
                        return;
                    }
                }
            }
            if (kiemTra()) {
                if (!(txtsdt.getText().equalsIgnoreCase(tblsinhvien.getValueAt(index, 4)+""))) {
                    for (SinhVien sv : list) {
                        if (txtsdt.getText().equalsIgnoreCase(sv.getSDT())) {
                            JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại");
                            return;
                        }
                    }
                }
                if (!(txtemail.getText().equalsIgnoreCase(tblsinhvien.getValueAt(index, 5)+""))) {
                    for (SinhVien sv : list) {
                        if (txtemail.getText().equalsIgnoreCase(sv.getEmail())) {
                            JOptionPane.showMessageDialog(this, "Email đã tồn tại");
                            return;
                        }
                    }
                }
                SinhVien sv = new SinhVien();
                sv.setMaSV(txtma.getText().trim());
                sv.setTenSV(txtten.getText().trim());
                sv.setXepLoai(cboloai.getSelectedItem() + "");
                sv.setNgaySinh(ngaysinh.getText().trim());
                if (kiemTraTrong(txtchucvu.getText())) {
                    sv.setChucVu("Null");
                } else {
                    sv.setChucVu(txtchucvu.getText().trim());
                }
                sv.setQueQuan(cboquequan.getSelectedItem() + "");
                sv.setEmail(txtemail.getText().trim());
                sv.setSDT(txtsdt.getText().trim());
                new SinhVienDAO().update(sv, tblsinhvien.getValueAt(index, 0)+"");
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            }
        }
    }
//

    public void clear() {
        txtma.setText("");
        txtten.setText("");
        cboloai.setSelectedIndex(0);
        ngaysinh.setText("");
        txtchucvu.setText("");
        cboquequan.setSelectedIndex(0);
        txtemail.setText("");
        txtsdt.setText("");
    }
//

    public void timKiem() {
        list = new SinhVienDAO().selectAll();
        int xac_Nhan = 0;
        if (kiemTraTrong(txttimkiemma.getText()) && kiemTraTrong(txttimkiemten.getText())) {
            JOptionPane.showMessageDialog(this, "Nhập dữ liệu cần tìm kiếm");
            return;
        }
        if (!(kiemTraTrong(txttimkiemma.getText())) && kiemTraTrong(txttimkiemten.getText())) {
            DefaultTableModel model = (DefaultTableModel) tblsinhvien.getModel();
            model.setRowCount(0);
            for (SinhVien sv : list) {
                if (txttimkiemma.getText().trim().toLowerCase().equalsIgnoreCase(sv.getMaSV().toLowerCase())) {
                    model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên");
        } else if (kiemTraTrong(txttimkiemma.getText()) && !(kiemTraTrong(txttimkiemten.getText()))) {
            DefaultTableModel model = (DefaultTableModel) tblsinhvien.getModel();
            model.setRowCount(0);
            for (SinhVien sv : list) {
                if (sv.getTenSV().toLowerCase().contains(txttimkiemten.getText().toLowerCase().trim())) {
                    model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
                    xac_Nhan = 1;
                }
            }
            if (xac_Nhan == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên");
                return;
            }
            xac_Nhan = 0;
        } else {
            DefaultTableModel model = (DefaultTableModel) tblsinhvien.getModel();
            model.setRowCount(0);
            for (SinhVien sv : list) {
                if (sv.getTenSV().toLowerCase().contains(txttimkiemten.getText().trim().toLowerCase()) && txttimkiemma.getText().trim().toLowerCase().equalsIgnoreCase(sv.getMaSV().toLowerCase())) {
                    model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên");
        }
    }

    public void huyTim() {
        txttimkiemma.setText("");
        txttimkiemten.setText("");
        fillTotable();
    }

    public void sapXepTenTang() {
        list = new SinhVienDAO().selectAll();
        Collections.sort(list, new Comparator<SinhVien>() {
            @Override
            public int compare(SinhVien o1, SinhVien o2) {
                return o1.getTenSV().compareTo(o2.getTenSV());
            }
        });
        fillTotableNew();
    }

    public void sapXepTenGiam() {
        list = new SinhVienDAO().selectAll();
        Collections.sort(list, new Comparator<SinhVien>() {
            @Override
            public int compare(SinhVien o1, SinhVien o2) {
                return o2.getTenSV().compareTo(o1.getTenSV());
            }
        });
        fillTotableNew();
    }
//

    public void sapXepLoaiTang() {
        list = new SinhVienDAO().selectAll();
        DefaultTableModel model = (DefaultTableModel) tblsinhvien.getModel();
        model.setRowCount(0);
        for (SinhVien sv : list) {
            if (sv.getXepLoai().equalsIgnoreCase("Yếu")) {
                model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
            }
        }
        for (SinhVien sv : list) {
            if (sv.getXepLoai().equalsIgnoreCase("Trung bình")) {
                model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
            }
        }
        for (SinhVien sv : list) {
            if (sv.getXepLoai().equalsIgnoreCase("Khá")) {
                model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
            }
        }
        for (SinhVien sv : list) {
            if (sv.getXepLoai().equalsIgnoreCase("Giỏi")) {
                model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
            }
        }
    }

    public void sapXepLoaiGiam() {
        list = new SinhVienDAO().selectAll();
        DefaultTableModel model = (DefaultTableModel) tblsinhvien.getModel();
        model.setRowCount(0);
        for (SinhVien sv : list) {
            if (sv.getXepLoai().equalsIgnoreCase("Giỏi")) {
                model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
            }
        }
        for (SinhVien sv : list) {
            if (sv.getXepLoai().equalsIgnoreCase("Khá")) {
                model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
            }
        }
        for (SinhVien sv : list) {
            if (sv.getXepLoai().equalsIgnoreCase("Trung bình")) {
                model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
            }
        }
        for (SinhVien sv : list) {
            if (sv.getXepLoai().equalsIgnoreCase("Yếu")) {
                model.addRow(new Object[]{sv.getMaSV(), sv.getTenSV(), sv.getNgaySinh(), sv.getQueQuan(), sv.getSDT(), sv.getEmail(), sv.getXepLoai(), sv.getChucVu()});
            }
        }
    }

    public void xapXep() {
        if (cbotitle.getSelectedIndex() == 0 && cbosort.getSelectedIndex() == 1) {
            sapXepTenTang();
        } else if (cbotitle.getSelectedIndex() == 0 && cbosort.getSelectedIndex() == 2) {
            sapXepTenGiam();
        } else if (cbotitle.getSelectedIndex() == 1 && cbosort.getSelectedIndex() == 1) {
            sapXepLoaiTang();
        } else if (cbotitle.getSelectedIndex() == 1 && cbosort.getSelectedIndex() == 2) {
            sapXepLoaiGiam();
        } else {
            fillTotable();
        }
    }

    public void exit() {
        System.exit(0);
    }

    public void dangNhap() {
        this.setVisible(false);
        new FromDangNhap().setVisible(true);
    }

    public void fromThanNhan() {
        this.dispose();
        new FromThanNhan().setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txttimkiemma = new javax.swing.JTextField();
        txttimkiemten = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblsinhvien = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtten = new javax.swing.JTextField();
        txtma = new javax.swing.JTextField();
        txtchucvu = new javax.swing.JTextField();
        txtsdt = new javax.swing.JTextField();
        txtemail = new javax.swing.JTextField();
        cboloai = new javax.swing.JComboBox<>();
        ngaysinh = new com.github.lgooddatepicker.components.DatePicker();
        cboquequan = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        kButton1 = new com.k33ptoo.components.KButton();
        kButton2 = new com.k33ptoo.components.KButton();
        jButton10 = new javax.swing.JButton();
        lbldemtime = new javax.swing.JLabel();
        lbltime = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        cbotitle = new javax.swing.JComboBox<>();
        cbosort = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quản Lý Sinh Viên");
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Mã sinh viên");

        jLabel2.setText("Tên sinh viên");

        txttimkiemten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttimkiemtenActionPerformed(evt);
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

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_cancel_20px.png"))); // NOI18N
        jButton2.setText("Hủy Tìm");
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("• Danh sách sinh viên");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N

        tblsinhvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã SV", "Họ và tên", "Ngày Sinh", "Quê Quán", "Số Điện Thoại", "Email", "Xếp Loại", "Chức Vụ"
            }
        ));
        tblsinhvien.setSelectionBackground(new java.awt.Color(232, 57, 95));
        tblsinhvien.getTableHeader().setReorderingAllowed(false);
        tblsinhvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblsinhvienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblsinhvien);

        jLabel4.setText("• Thông tin sinh viên");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N

        jLabel5.setText("Mã sinh viên");

        jLabel6.setText("Ngày sinh");

        jLabel7.setText("Xếp loại");

        jLabel8.setText("Họ tên");

        jLabel9.setText("Chức vụ");

        jLabel10.setText("Quê quán");

        jLabel11.setText("Số điện thoại");

        jLabel12.setText("Email");

        txtma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmaActionPerformed(evt);
            }
        });

        cboloai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Yếu", "Trung bình", "Khá", "Giỏi" }));

        cboquequan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "An Giang", "Bà rịa – Vũng tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội ", "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "Thành phố Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái" }));
        cboquequan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboquequanActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_add_administrator_20px.png"))); // NOI18N
        jButton3.setText("Thêm ");
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

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

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_broom_20px.png"))); // NOI18N
        jButton9.setText("Clear");
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

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
            .addGap(0, 0, Short.MAX_VALUE)
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
        kButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton2ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons-log-out-24.png"))); // NOI18N
        jButton10.setText("Thoát");
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        lbldemtime.setText(" ");
        lbldemtime.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbldemtime.setForeground(new java.awt.Color(255, 0, 0));

        lbltime.setText("   ");
        lbltime.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbltime.setForeground(new java.awt.Color(255, 0, 0));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_clock.png"))); // NOI18N

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_clock_cat.png"))); // NOI18N

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_home.png"))); // NOI18N
        jButton11.setText("Đăng nhập");
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        cbotitle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Họ Tên", "Xếp loại" }));
        cbotitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbotitleActionPerformed(evt);
            }
        });

        cbosort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Không sắp", "Sắp tăng", "Sắp giảm" }));
        cbosort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbosortActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(kButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(kButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                        .addGap(12, 12, 12)
                        .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttimkiemma, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttimkiemten, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbotitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbosort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel8)
                                                    .addComponent(jLabel6)
                                                    .addComponent(jLabel10))
                                                .addGap(26, 26, 26)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtten)
                                                    .addComponent(cboquequan, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(ngaysinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtma, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(79, 79, 79)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel9))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cboloai, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtemail, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtsdt, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtchucvu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
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
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(lbltime)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbldemtime, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(txttimkiemma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttimkiemten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addGap(37, 37, 37)
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
                            .addComponent(txtten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(ngaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboloai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(cboquequan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtchucvu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4)
                            .addComponent(jButton5)
                            .addComponent(jButton9)
                            .addComponent(cbotitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbosort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(17, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jButton11)
                        .addGap(103, 103, 103)
                        .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(kButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmaActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        insert();
//        save();

        fillTotable();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        update();
//        save();
        fillTotable();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        delete();
//        save();
        fillTotable();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tblsinhvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblsinhvienMouseClicked
        showData();
    }//GEN-LAST:event_tblsinhvienMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        timKiem();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        huyTim();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        clear();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txttimkiemtenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttimkiemtenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttimkiemtenActionPerformed

    private void cboquequanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboquequanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboquequanActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        exit();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        dangNhap();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void kButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton2ActionPerformed
        fromThanNhan();
    }//GEN-LAST:event_kButton2ActionPerformed

    private void cbotitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbotitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbotitleActionPerformed

    private void cbosortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbosortActionPerformed
        xapXep();
    }//GEN-LAST:event_cbosortActionPerformed

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
            java.util.logging.Logger.getLogger(FromSinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FromSinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FromSinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FromSinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FromSinhVien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboloai;
    private javax.swing.JComboBox<String> cboquequan;
    private javax.swing.JComboBox<String> cbosort;
    private javax.swing.JComboBox<String> cbotitle;
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
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private com.k33ptoo.components.KButton kButton1;
    private com.k33ptoo.components.KButton kButton2;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel2;
    private javax.swing.JLabel lbldemtime;
    private javax.swing.JLabel lbltime;
    private com.github.lgooddatepicker.components.DatePicker ngaysinh;
    private javax.swing.JTable tblsinhvien;
    private javax.swing.JTextField txtchucvu;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtma;
    private javax.swing.JTextField txtsdt;
    private javax.swing.JTextField txtten;
    private javax.swing.JTextField txttimkiemma;
    private javax.swing.JTextField txttimkiemten;
    // End of variables declaration//GEN-END:variables
}
