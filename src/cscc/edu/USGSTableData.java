package cscc.edu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "USGSTableData")
public class USGSTableData {
    @Id
    private int id;
    private String time;
    private Double latitude;
    private Double longitude;
    private Double depth;
    private Double mag;
    private String magType;
    private String nst;
    private Double gap;
    private Double dmin;
    private Double rms;
    private String net;
    private String eq_id;
    private String updated;
    private String place;
    private String type;
    private Double horizontalError;
    private Double depthError;
    private Double magError;
    private String magNst;
    private String status;
    private String locationSource;
    private String magSource;
    public int getId() {
        return id;
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

    public Double getHorizontalError() {
        return horizontalError;
    }

    public void setHorizontalError(Double horizontalError) {
        this.horizontalError = horizontalError;
    }

    public Double getDepthError() {
        return depthError;
    }

    public void setDepthError(Double depthError) {
        this.depthError = depthError;
    }

    public Double getMagError() {
        return magError;
    }

    public void setMagError(Double magError) {
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

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Double getMag() {
        return mag;
    }

    public void setMag(Double mag) {
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

    public Double getGap() {
        return gap;
    }

    public void setGap(Double gap) {
        this.gap = gap;
    }

    public Double getDmin() {
        return dmin;
    }

    public void setDmin(Double dmin) {
        this.dmin = dmin;
    }

    public Double getRms() {
        return rms;
    }

    public void setRms(Double rms) {
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


    public USGSTableData() {
    }
}
