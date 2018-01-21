package septian.org.osmdroid.pulangkonvoi.Kelas;

/**
 * Created by root on 1/8/18.
 */

public class Graph {
    int id_node, heuristik, urutan_first, urutan_last;
    Double startlat, startlong, endlat, endlong;

    public Graph() {
    }

    public Graph(int id_node, int heuristik, int urutan_first, int urutan_last, Double startlat, Double startlong, Double endlat, Double endlong) {
        this.id_node = id_node;
        this.heuristik = heuristik;
        this.urutan_first = urutan_first;
        this.urutan_last = urutan_last;
        this.startlat = startlat;
        this.startlong = startlong;
        this.endlat = endlat;
        this.endlong = endlong;
    }

    public int getId_node() {
        return id_node;
    }

    public void setId_node(int id_node) {
        this.id_node = id_node;
    }

    public int getHeuristik() {
        return heuristik;
    }

    public void setHeuristik(int heuristik) {
        this.heuristik = heuristik;
    }

    public int getUrutan_first() {
        return urutan_first;
    }

    public void setUrutan_first(int urutan_first) {
        this.urutan_first = urutan_first;
    }

    public int getUrutan_last() {
        return urutan_last;
    }

    public void setUrutan_last(int urutan_last) {
        this.urutan_last = urutan_last;
    }

    public Double getStartlat() {
        return startlat;
    }

    public void setStartlat(Double startlat) {
        this.startlat = startlat;
    }

    public Double getStartlong() {
        return startlong;
    }

    public void setStartlong(Double startlong) {
        this.startlong = startlong;
    }

    public Double getEndlat() {
        return endlat;
    }

    public void setEndlat(Double endlat) {
        this.endlat = endlat;
    }

    public Double getEndlong() {
        return endlong;
    }

    public void setEndlong(Double endlong) {
        this.endlong = endlong;
    }
}
