package vn.iostar.uber.models;

import androidx.dynamicanimation.animation.SpringAnimation;

public class DanhGia {
    private String idDanhGia;
    private String idKhachHang;
    private String idChuyenXe;
    private String noiDung;
    private Float sao;

    public DanhGia(String idDanhGia, String idKhachHang, String idChuyenXe, String noiDung, Float sao) {
        this.idDanhGia = idDanhGia;
        this.idKhachHang = idKhachHang;
        this.idChuyenXe = idChuyenXe;
        this.noiDung = noiDung;
        this.sao = sao;
    }

    public String getIdDanhGia() {
        return idDanhGia;
    }

    public void setIdDanhGia(String idDanhGia) {
        this.idDanhGia = idDanhGia;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getIdChuyenXe() {
        return idChuyenXe;
    }

    public void setIdChuyenXe(String idChuyenXe) {
        this.idChuyenXe = idChuyenXe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Float getSao() {
        return sao;
    }

    public void setSao(Float sao) {
        this.sao = sao;
    }
}
