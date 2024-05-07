package vn.iostar.uber.models;

public class LoaiXe {
    private String idLoaiXe;
    private String tenLoaiXe;
    private String gia;

    public LoaiXe(String tenLoaiXe, String gia) {
        this.tenLoaiXe = tenLoaiXe;
        this.gia = gia;
    }

    public LoaiXe(String idLoaiXe, String tenLoaiXe, String gia) {
        this.idLoaiXe = idLoaiXe;
        this.tenLoaiXe = tenLoaiXe;
        this.gia = gia;
    }

    public String getIdLoaiXe() {
        return idLoaiXe;
    }

    public void setIdLoaiXe(String idLoaiXe) {
        this.idLoaiXe = idLoaiXe;
    }

    public String getTenLoaiXe() {
        return tenLoaiXe;
    }

    public void setTenLoaiXe(String tenLoaiXe) {
        this.tenLoaiXe = tenLoaiXe;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }
}
