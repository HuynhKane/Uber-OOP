package vn.iostar.uber.models;

public class LoaiXe {
    private String idLoaiXe;
    private String tenLoaiXe;
    private Float gia;

    public LoaiXe(String tenLoaiXe, Float gia) {
        this.tenLoaiXe = tenLoaiXe;
        this.gia = gia;
    }

    public LoaiXe(String idLoaiXe, String tenLoaiXe, Float gia) {
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

    public Float getGia() {
        return gia;
    }

    public void setGia(Float gia) {
        this.gia = gia;
    }
}
