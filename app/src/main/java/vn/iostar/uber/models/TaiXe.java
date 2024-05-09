package vn.iostar.uber.models;

public class TaiXe {
    private String idTaiKhoan;
    private String ten;
    private String cccd;
    private String sdt;
    private String idXe;

    public TaiXe(String idTaiKhoan, String ten, String cccd, String sdt, String idXe) {
        this.idTaiKhoan = idTaiKhoan;
        this.ten = ten;
        this.cccd = cccd;
        this.sdt = sdt;
        this.idXe = idXe;
    }

    public String getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(String idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getIdXe() {
        return idXe;
    }

    public void setIdXe(String idXe) {
        this.idXe = idXe;
    }
}
