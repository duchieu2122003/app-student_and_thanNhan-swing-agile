/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author dinhv
 */
public class ThanNhan {
    private String maSV;
    private String hoVaTen;
    private String ngaySinh;
    private String soDienThoai;
    private String queQuan;

    public ThanNhan() {
    }

    public ThanNhan(String maSV, String hoVaTen, String ngaySinh, String soDienThoai, String queQuan) {
        this.maSV = maSV;
        this.hoVaTen = hoVaTen;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.queQuan = queQuan;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }
    
    
    
}
