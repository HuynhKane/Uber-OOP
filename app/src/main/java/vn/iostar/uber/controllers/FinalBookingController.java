package vn.iostar.uber.controllers;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.activitys.client.FinalBookingFormActivity;
import vn.iostar.uber.models.LoaiXe;
import vn.iostar.uber.models.TaiXe;
import vn.iostar.uber.models.UuDai;

public class FinalBookingController {
    public Float getPriceOfTypeCar(LoaiXe typeCar, LatLng posFrom, LatLng posTo){
        double distance=calculateDistanceInKm(posFrom,posTo);
        Float initialPrice= (float) distance* typeCar.getGia();
        Log.d("pRICE",initialPrice.toString());
        return initialPrice;
    }
    public double calculateDistanceInKm(LatLng startLatLng, LatLng endLatLng) {
        // Tính khoảng cách theo mét
        double distanceInMeters = SphericalUtil.computeDistanceBetween(startLatLng, endLatLng);
        // Chuyển đổi sang km
        return distanceInMeters / 1000;
    }

    public Double getPriceUseVoucher(UuDai uuDai,Float initialPrice){
        String uuDaiStr= uuDai.getUuDai().replace("%", "");
        Double finalPrice = (1 - Double.parseDouble(uuDaiStr)/100) * (initialPrice);
        return finalPrice;
    }

    public void paymentInfor(String typePay, Double finalPrice){

    }
    public TaiXe chooseDriver(LatLng posFrom, LatLng posTo, String typePay){
        return new TaiXe("0","Driver Temp","12121212121","094367232","77A1","https://avatar.iran.liara.run/public/boy?username=Ash");
    }
}