package vn.iostar.uber.models;

public class YeuCauDatXe {
    private String idDatXe;
    private String idKhachHang;
    private String idLoaiXe;
    private String idUuDai;
    private String diemDen;
    private String diemDon;
    private Double chiPhi;
    private String trangThai;

    public YeuCauDatXe(String idDatXe, String idKhachHang, String idLoaiXe, String idUuDai, String diemDen, String diemDon, Double chiPhi, String trangThai) {
        this.idDatXe = idDatXe;
        this.idKhachHang = idKhachHang;
        this.idLoaiXe = idLoaiXe;
        this.idUuDai = idUuDai;
        this.diemDen = diemDen;
        this.diemDon = diemDon;
        this.chiPhi = chiPhi;
        this.trangThai = trangThai;
    }

    public YeuCauDatXe(String idKhachHang, String idLoaiXe, String idUuDai, String diemDen, String diemDon, Double chiPhi, String trangThai) {
        this.idKhachHang = idKhachHang;
        this.idLoaiXe = idLoaiXe;
        this.idUuDai = idUuDai;
        this.diemDen = diemDen;
        this.diemDon = diemDon;
        this.chiPhi = chiPhi;
        this.trangThai = trangThai;
    }


    public String getIdDatXe() {
        return idDatXe;
    }

    public void setIdDatXe(String idDatXe) {
        this.idDatXe = idDatXe;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getIdLoaiXe() {
        return idLoaiXe;
    }

    public void setIdLoaiXe(String idLoaiXe) {
        this.idLoaiXe = idLoaiXe;
    }

    public String getIdUuDai() {
        return idUuDai;
    }

    public void setIdUuDai(String idUuDai) {
        this.idUuDai = idUuDai;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public void setDiemDen(String diemDen) {
        this.diemDen = diemDen;
    }

    public String getDiemDon() {
        return diemDon;
    }

    public void setDiemDon(String diemDon) {
        this.diemDon = diemDon;
    }

    public Double getChiPhi() {
        return chiPhi;
    }

    public void setChiPhi(Double chiPhi) {
        this.chiPhi = chiPhi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
