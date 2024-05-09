package vn.iostar.uber.models;

import java.security.Timestamp;
import java.util.Date;

public class ThongBao {
    private String idThongBao;
    private String idTaiKhoan;
    private String noiDungThongBao;
    private Timestamp thoiGian;

    public ThongBao(String idThongBao, String idTaiKhoan, String noiDungThongBao, Timestamp thoiGian) {
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

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }
}
