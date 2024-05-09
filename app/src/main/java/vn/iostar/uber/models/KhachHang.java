package vn.iostar.uber.models;

public class KhachHang {
    private String idTaiKhoan;
    private String ten;
    private String sdt;

    public KhachHang(String idTaiKhoan, String ten, String sdt) {
        this.idTaiKhoan = idTaiKhoan;
        this.ten = ten;
        this.sdt = sdt;
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

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
