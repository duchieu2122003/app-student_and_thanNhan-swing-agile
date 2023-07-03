/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SinhVien;

/**
 *
 * @author dinhv
 */
public class SinhVienDAO implements DaoInterface<SinhVien> {

    @Override
    public void insert(SinhVien t) {

        try {
            Connection conn = new JDBC().connect();
            String sql = "INSERT SinhVien VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, t.getMaSV());
            stm.setString(2, t.getTenSV());
            stm.setString(3, t.getNgaySinh());
            stm.setString(4, t.getQueQuan());
            stm.setString(5, t.getSDT());
            stm.setString(6, t.getEmail());
            stm.setString(7, t.getXepLoai());
            stm.setString(8, t.getChucVu());
            stm.executeUpdate();
            new JDBC().close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(SinhVien t, String masv) {
        try {
            Connection conn = new JDBC().connect();
            String sql = "UPDATE SinhVien SET MaSV = ?, TenSV = ?, NgaySinh = ?, QueQuan = ?,"
                    + " SoDienThoai = ?, Email = ?, XepLoai = ?, ChucVu = ? WHERE MaSV = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, t.getMaSV());
            stm.setString(2, t.getTenSV());
            stm.setString(3, t.getNgaySinh());
            stm.setString(4, t.getQueQuan());
            stm.setString(5, t.getSDT());
            stm.setString(6, t.getEmail());
            stm.setString(7, t.getXepLoai());
            stm.setString(8, t.getChucVu());
            stm.setString(9, masv);
            stm.executeUpdate();
            new JDBC().close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(SinhVien t) {
        try {
            Connection conn = new JDBC().connect();
            String sql= "DELETE FROM SinhVien WHERE MaSV = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, t.getMaSV());
            stm.executeUpdate();
            new JDBC().close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<SinhVien> selectAll() {
        ArrayList<SinhVien> ketQua = new ArrayList<>();
        try {
            Connection conn = new JDBC().connect();
            String sql = "SELECT * FROM SinhVien";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                String masv = rs.getString("MaSV");
                String tensv = rs.getString("TenSV");
                String ngaysinh = rs.getString("NgaySinh");
                String quequan = rs.getString("QueQuan");
                String sdt = rs.getString("SoDienThoai");
                String email = rs.getString("Email");
                String xeploai = rs.getString("XepLoai");
                String chucvu = rs.getString("ChucVu");
                ketQua.add(new SinhVien(masv, tensv, ngaysinh, quequan, sdt, email, xeploai, chucvu));
            }
            new JDBC().close(conn);
        } catch (Exception e) {
        }
        return ketQua;
    }

    @Override
    public SinhVien selectById(SinhVien t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<SinhVien> selectCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void deleteByid(String condition){
        try {
            Connection conn = new JDBC().connect();
            String sql = "DELETE FROM SinhVien WHERE MaSV = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, condition);
            stm.executeUpdate();
            new JDBC().close(conn);
        } catch (Exception e) {
        }
    }

}
