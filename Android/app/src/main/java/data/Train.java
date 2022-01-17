package data;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Train {
    private String rn;
    private String destSt;
    private String destNm;
    private String trDr;
    private String nextStaId;
    private String nextStpId;
    private String nextStaNm;
    private String prdt;
    private String arrt;
    private String line;
    private int isApp;
    private int isDly;
    private double lat;
    private double lon;
    private int heading;

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getDestSt() {
        return destSt;
    }

    public void setDestSt(String destSt) {
        this.destSt = destSt;
    }

    public String getDestNm() {
        return destNm;
    }

    public void setDestNm(String destNm) {
        this.destNm = destNm;
    }

    public String getTrDr() {
        return trDr;
    }

    public void setTrDr(String trDr) {
        this.trDr = trDr;
    }

    public String getNextStaId() {
        return nextStaId;
    }

    public void setNextStaId(String nextStaId) {
        this.nextStaId = nextStaId;
    }

    public String getNextStpId() {
        return nextStpId;
    }

    public void setNextStpId(String nextStpId) {
        this.nextStpId = nextStpId;
    }

    public String getNextStaNm() {
        return nextStaNm;
    }

    public void setNextStaNm(String nextStaNm) {
        this.nextStaNm = nextStaNm;
    }

    public String getPrdt() {
        return prdt;
    }

    public void setPrdt(String prdt) {
        this.prdt = prdt;
    }

    public String getArrT() {
        return arrt;
    }

    public void setArrT(String arrt) {
        this.arrt = arrt;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getIsApp() {
        return isApp;
    }

    public void setIsApp(String isApp) {
        this.isApp = Integer.parseInt(isApp);
    }

    public int getIsDly() {
        return isDly;
    }

    public void setIsDly(String isDly) {
        this.isDly = Integer.parseInt(isDly);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = Double.parseDouble(lat);
    }

    public double getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = Double.parseDouble(lon);
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = Integer.parseInt(heading);
    }



    public Train(){};
    public Train(String rn, String destSt, String destNm, String trDr, String nextStaId, String nextStpId, String nextStaNm, String prdt, String arrt, String isApp, String isDly, String lat, String lon, String heading, String line) {
        this.rn = rn;
        this.destSt = destSt;
        this.destNm = destNm;
        this.trDr = trDr;
        this.nextStaId = nextStaId;
        this.nextStpId = nextStpId;
        this.nextStaNm = nextStaNm;
        this.prdt = prdt;
        this.arrt = arrt;
        this.isApp = Integer.parseInt(isApp);
        this.isDly = Integer.parseInt(isDly);
        this.lat = Double.parseDouble(lat);
        this.lon = Double.parseDouble(lon);
        this.heading = Integer.parseInt(heading);
        this.line = line;
    }

    //generate a latlng for a train
    public LatLng generateLatLng() {
        LatLng latLng = new LatLng(this.lat, this.lon);
        return latLng;
    }

    @Override
    public String toString(){
        String returnString = "Train Line: " + line + "\n" + "Destination: " + destNm + "\n"
                + "Destination Arrival Time: " + formatTime(arrt) + "\n" + "Next Stop: " + nextStpId
                + "Next Stop Arrival Time: " + formatTime(prdt) + "Train Arrival: " + formatApp(isApp);
        return returnString;
    }

    public static String formatDelay(int delay) {
        String isDelay = "On Time";
        if (delay == 1) {
            isDelay = "Train Delayed";
        }
        return isDelay;
    }
    public static String formatApp(int app) {
        String isApp = "Due Soon";
        if (app == 1) {
            isApp = "Now Approaching";
        }
        return isApp;
    }


    public static String formatTime(String arrt) {
        String arrivalTime = arrt;
        String time = " AM";
        try {
             arrivalTime = arrt.substring(arrt.length() - 8);
             int ampm = Integer.valueOf(arrivalTime.substring(0,2));
             if (ampm > 12) {
                 ampm -=12;
                 time = " PM";
             }
             else if (ampm == 0) {
                 ampm +=12;
                 time = " AM";
             }
             arrivalTime = ampm + arrivalTime.substring(2) + time;
             return arrivalTime;
        } catch (StringIndexOutOfBoundsException e) {
            return arrivalTime;
        }
    }
}
