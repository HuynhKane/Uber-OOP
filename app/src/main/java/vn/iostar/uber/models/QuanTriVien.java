package vn.iostar.uber.models;

public class QuanTriVien {
    private String idTaiKhoan;
    private String ten;

    public QuanTriVien(String idTaiKhoan, String ten) {
        this.idTaiKhoan = idTaiKhoan;
        this.ten = ten;
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
}
