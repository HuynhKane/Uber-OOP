package vn.iostar.uber.models;

import com.firebase.geofire.GeoLocation;

public class ViTri {
    private String key;
    private GeoLocation geoLocation;
    private TaiXe driverInforModel;

    public ViTri() {
    }

    public ViTri(String key, GeoLocation geoLocation) {
        this.key = key;
        this.geoLocation = geoLocation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public TaiXe getDriverInforModel() {
        return driverInforModel;
    }

    public void setDriverInforModel(TaiXe driverInforModel) {
        this.driverInforModel = driverInforModel;
    }
}
