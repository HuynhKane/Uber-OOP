package vn.iostar.uber.callback;

import java.sql.Driver;

import vn.iostar.uber.models.ViTri;

public interface IFirebaseDriverInforListener {
    void onDriverInforLoadSuccess(ViTri driverGeoModel);

}
