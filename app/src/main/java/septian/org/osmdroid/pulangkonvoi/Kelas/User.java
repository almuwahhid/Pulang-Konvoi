package septian.org.osmdroid.pulangkonvoi.Kelas;

/**
 * Created by root on 1/12/18.
 */

public class User {
    int idPeng;
    String namaPeng, NamaLeng, alamat, email;

    public int getIdPeng() {
        return idPeng;
    }

    public void setIdPeng(int idPeng) {
        this.idPeng = idPeng;
    }

    public String getNamaPeng() {
        return namaPeng;
    }

    public void setNamaPeng(String namaPeng) {
        this.namaPeng = namaPeng;
    }

    public String getNamaLeng() {
        return NamaLeng;
    }

    public void setNamaLeng(String namaLeng) {
        NamaLeng = namaLeng;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
