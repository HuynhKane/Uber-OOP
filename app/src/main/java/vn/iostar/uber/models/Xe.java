package vn.iostar.uber.models;

public class Xe {
    private String idXe;
    private String bienSo;
    private String loai; //idLoaiXe trong LoaiXe

    public Xe(String bienSo, String loai) {
        this.bienSo = bienSo;
        this.loai = loai;
    }

    public Xe(String idXe, String bienSo, String loai) {
        this.idXe = idXe;
        this.bienSo = bienSo;
        this.loai = loai;
    }

    public String getIdXe() {
        return idXe;
    }

    public void setIdXe(String idXe) {
        this.idXe = idXe;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
}
