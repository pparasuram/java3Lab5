package cscc.edu;

public class USGSCSVData {
    String time;
/*    double latitude;
    double longitude;
    double depth;
    double mag;*/
    String latitude;
    String longitude;
    String depth;
    String mag;
    String magType;
    String nst;
    /*
    double gap;
    double dmin;
    double rms;
    
     */
    String gap;
    String dmin;
    String rms;
    String net;
    String id;
    String updated;
    String place;
    String type;
/*
    double horizontalError;
    double depthError;
    double magError;
    
 */
    String horizontalError;
    String depthError;
    String magError;
    String magNst;
    String status;
    String locationSource;
    String magSource;

    public USGSCSVData() {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
        this.mag = mag;
        this.magType = magType;
        this.nst = nst;
        this.gap = gap;
        this.dmin = dmin;
        this.rms = rms;
        this.net = net;
        this.id = id;
        this.updated = updated;
        this.place = place;
        this.type = type;
        this.horizontalError = horizontalError;
        this.depthError = depthError;
        this.magError = magError;
        this.magNst = magNst;
        this.status = status;
        this.locationSource = locationSource;
        this.magSource = magSource;
    }
}
