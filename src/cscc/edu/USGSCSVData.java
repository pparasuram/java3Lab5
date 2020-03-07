package cscc.edu;

public class USGSCSVData {
    int id;
    String time;
    String latitude;
    String longitude;
    String depth;
    String mag;
    String magType;
    String nst;
    String gap;
    String dmin;
    String rms;
    String net;
    String eq_id;
    String updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getMagType() {
        return magType;
    }

    public void setMagType(String magType) {
        this.magType = magType;
    }

    public String getNst() {
        return nst;
    }

    public void setNst(String nst) {
        this.nst = nst;
    }

    public String getGap() {
        return gap;
    }

    public void setGap(String gap) {
        this.gap = gap;
    }

    public String getDmin() {
        return dmin;
    }

    public void setDmin(String dmin) {
        this.dmin = dmin;
    }

    public String getRms() {
        return rms;
    }

    public void setRms(String rms) {
        this.rms = rms;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getEq_id() {
        return eq_id;
    }

    public void setEq_id(String eq_id) {
        this.eq_id = eq_id;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHorizontalError() {
        return horizontalError;
    }

    public void setHorizontalError(String horizontalError) {
        this.horizontalError = horizontalError;
    }

    public String getDepthError() {
        return depthError;
    }

    public void setDepthError(String depthError) {
        this.depthError = depthError;
    }

    public String getMagError() {
        return magError;
    }

    public void setMagError(String magError) {
        this.magError = magError;
    }

    public String getMagNst() {
        return magNst;
    }

    public void setMagNst(String magNst) {
        this.magNst = magNst;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocationSource() {
        return locationSource;
    }

    public void setLocationSource(String locationSource) {
        this.locationSource = locationSource;
    }

    public String getMagSource() {
        return magSource;
    }

    public void setMagSource(String magSource) {
        this.magSource = magSource;
    }

    String place;
    String type;
    String horizontalError;
    String depthError;
    String magError;
    String magNst;
    String status;
    String locationSource;
    String magSource;
    public USGSCSVData() {
    }
}
