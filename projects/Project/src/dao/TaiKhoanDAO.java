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
import model.TaiKhoan;

/**
 *
 * @author dinhv
 */
public class TaiKhoanDAO implements DaoInterface<TaiKhoan> {

    @Override
    public void insert(TaiKhoan t) {
    }

    @Override
    public void update(TaiKhoan t, String masv) {
        try {
            Connection conn = new JDBC().connect();
            String sql = "UPDATE TaiKhoan SET MatKhau = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, masv);
            stm.executeUpdate();
            new JDBC().close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(TaiKhoan t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<TaiKhoan> selectAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TaiKhoan selectById(TaiKhoan t) {
        TaiKhoan ketQua = null;
        try {
            Connection conn = new JDBC().connect();
            String sql = "SELECT * FROM TaiKhoan";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String taiKhoan = rs.getString("TaiKhoan");
                String passWord = rs.getString("MatKhau");
            ketQua = new TaiKhoan(taiKhoan, passWord);
            new JDBC().close(conn);
            }
        } catch (Exception e) {
        }
        return ketQua;
    }

    @Override
    public ArrayList<TaiKhoan> selectCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
