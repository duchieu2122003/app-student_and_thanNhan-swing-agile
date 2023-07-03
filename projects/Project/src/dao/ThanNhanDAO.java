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
import model.ThanNhan;

/**
 *
 * @author dinhv
 */
public class ThanNhanDAO implements DaoInterface<ThanNhan> {

    @Override
    public void insert(ThanNhan t) {
        try {
            Connection conn = new JDBC().connect();
            String sql = "INSERT ThanNhan VALUES(?,?,?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, t.getMaSV());
            stm.setString(2, t.getHoVaTen());
            stm.setString(3, t.getNgaySinh());
            stm.setString(4, t.getSoDienThoai());
            stm.setString(5, t.getQueQuan());
            stm.executeUpdate();
            new JDBC().close(conn);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @Override
    public void update(ThanNhan t, String sdt) {
        try {
            Connection conn = new JDBC().connect();
            String sql = "UPDATE ThanNhan SET MaSV = ?, TenTN = ?, NgaySinhTN = ?, SoDienThoaiTN = ?, QueQuanTN = ? WHERE SoDienThoaiTN = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, t.getMaSV());
            stm.setString(2, t.getHoVaTen());
            stm.setString(3, t.getNgaySinh());
            stm.setString(4, t.getSoDienThoai());
            stm.setString(5, t.getQueQuan());
            stm.setString(6, sdt);
            stm.executeUpdate();
            new JDBC().close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ThanNhan t) {
        try {
            Connection conn = new JDBC().connect();
            String sql = "DELETE ThanNhan WHERE MaSV = ? AND TenTN = ? AND NgaySinhTN = ? AND SoDienThoaiTN = ? AND QueQuanTN = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, t.getMaSV());
            stm.setString(2, t.getHoVaTen());
            stm.setString(3, t.getNgaySinh());
            stm.setString(4, t.getSoDienThoai());
            stm.setString(5, t.getQueQuan());
            stm.executeUpdate();
            new JDBC().close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ThanNhan> selectAll() {
        ArrayList<ThanNhan> ketqua = new ArrayList<>();
        try {
            Connection conn = new JDBC().connect();
            String sql = "SELECT * FROM ThanNhan";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                String masv = rs.getString("MaSV");
                String ten = rs.getString("TenTN");
                String ngaysinh = rs.getString("NgaySinhTN");
                String sdt = rs.getString("SoDienThoaiTN");
                String quequan = rs.getString("QueQuanTN");
                ketqua.add(new ThanNhan(masv, ten, ngaysinh, sdt, quequan));
            }
            new JDBC().close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketqua;
    }

    @Override
    public ThanNhan selectById(ThanNhan t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<ThanNhan> selectCondition(String condition) {
        return null;
    }  
}
