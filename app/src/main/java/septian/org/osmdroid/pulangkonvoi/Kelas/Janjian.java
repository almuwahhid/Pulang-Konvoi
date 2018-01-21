package septian.org.osmdroid.pulangkonvoi.Kelas;

import java.io.Serializable;

/**
 * Created by root on 1/13/18.
 */

public class Janjian implements Serializable{
    int idJan, idKoordinator;
    String tujuan, jam, status, lat, lng;

    public int getIdJan() {
        return idJan;
    }

    public void setIdJan(int idJan) {
        this.idJan = idJan;
    }

    public int getIdKoordinator() {
        return idKoordinator;
    }

    public void setIdKoordinator(int idKoordinator) {
        this.idKoordinator = idKoordinator;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
