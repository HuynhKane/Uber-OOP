package vn.iostar.uber.models;

import java.security.Timestamp;
import java.util.Date;

public class LichSuChuyenXe {
    private String idChuyenXe;
    private String idTaiXe;
    private String idYeuCauDatXe;
    private Timestamp thoiGian;
    private String idDanhGia;

    public LichSuChuyenXe(String idChuyenXe, String idTaiXe, String idYeuCauDatXe, Timestamp thoiGian, String idDanhGia) {
        this.idChuyenXe = idChuyenXe;
        this.idTaiXe = idTaiXe;
        this.idYeuCauDatXe = idYeuCauDatXe;
        this.thoiGian = thoiGian;
        this.idDanhGia = idDanhGia;
    }

    public String getIdChuyenXe() {
        return idChuyenXe;
    }

    public void setIdChuyenXe(String idChuyenXe) {
        this.idChuyenXe = idChuyenXe;
    }

    public String getIdTaiXe() {
        return idTaiXe;
    }

    public void setIdTaiXe(String idTaiXe) {
        this.idTaiXe = idTaiXe;
    }

    public String getIdYeuCauDatXe() {
        return idYeuCauDatXe;
    }

    public void setIdYeuCauDatXe(String idYeuCauDatXe) {
        this.idYeuCauDatXe = idYeuCauDatXe;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getIdDanhGia() {
        return idDanhGia;
    }

    public void setIdDanhGia(String idDanhGia) {
        this.idDanhGia = idDanhGia;
    }
}
