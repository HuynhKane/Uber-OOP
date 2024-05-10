package vn.iostar.uber.models;

import java.sql.Timestamp;

public class UuDai {
    private String idUuDai;
    private Timestamp thoiGian;
    private String uuDai;
    private String moTa;
    private String soLuong;

    public UuDai(Timestamp thoiGian, String uuDai, String moTa, String soLuong) {
        this.thoiGian = thoiGian;
        this.uuDai = uuDai;
        this.moTa = moTa;
        this.soLuong = soLuong;
    }

    public UuDai(String idUuDai, Timestamp thoiGian, String uuDai, String moTa, String soLuong) {
        this.idUuDai = idUuDai;
        this.thoiGian = thoiGian;
        this.uuDai = uuDai;
        this.moTa = moTa;
        this.soLuong = soLuong;
    }

    public String getIdUuDai() {
        return idUuDai;
    }

    public void setIdUuDai(String idUuDai) {
        this.idUuDai = idUuDai;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getUuDai() {
        return uuDai;
    }

    public void setUuDai(String uuDai) {
        this.uuDai = uuDai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }
}
