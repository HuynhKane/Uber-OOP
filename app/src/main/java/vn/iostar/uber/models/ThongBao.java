package vn.iostar.uber.models;

import java.util.Date;

public class ThongBao {
    private String idThongBao;
    private String idTaiKhoan;
    private String noiDungThongBao;
    private Date thoiGian;

    public ThongBao(String idThongBao, String idTaiKhoan, String noiDungThongBao, Date thoiGian) {
        this.idThongBao = idThongBao;
        this.idTaiKhoan = idTaiKhoan;
        this.noiDungThongBao = noiDungThongBao;
        this.thoiGian = thoiGian;
    }

    public String getIdThongBao() {
        return idThongBao;
    }

    public void setIdThongBao(String idThongBao) {
        this.idThongBao = idThongBao;
    }

    public String getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(String idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public String getNoiDungThongBao() {
        return noiDungThongBao;
    }

    public void setNoiDungThongBao(String noiDungThongBao) {
        this.noiDungThongBao = noiDungThongBao;
    }

    public Date getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        this.thoiGian = thoiGian;
    }
}
