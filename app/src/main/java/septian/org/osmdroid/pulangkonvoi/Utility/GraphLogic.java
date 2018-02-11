package septian.org.osmdroid.pulangkonvoi.Utility;

import android.location.Location;
import android.text.style.LocaleSpan;
import android.util.Log;

import java.util.ArrayList;

import septian.org.osmdroid.pulangkonvoi.Kelas.Graph;

/**
 * Created by root on 1/3/18.
 */

public class GraphLogic {
    ArrayList<Graph> graphs;
    public ArrayList<Graph> graphs_astar;
    public static String TAG = ".asd";
    Location first_location, last_location;


    public GraphLogic(Location first_location, Location last_location, ArrayList<Graph> graphs) {
        graphs_astar = new ArrayList<>();
        this.graphs = graphs;
        this.last_location = last_location;
        this.first_location = first_location;
    }

    public void make_first_jalur(){
        double temp_jarak = 9999999;
//        double temp_jarak_destination = Converter.getDistancePlus(this.first_location, this.last_location, 0);

        Location last_loc = new Location("");
        Graph new_graph = null;
        for (int a=0; a<graphs.size();a++){
            last_loc.setLatitude(graphs.get(a).getStartlat());
            last_loc.setLongitude(graphs.get(a).getStartlong());
            if(Converter.getDistancePlus(this.first_location, last_loc, 0)<=temp_jarak){
                //  if(Converter.getDistancePlus(last_loc,this.last_location,0))
                temp_jarak = Converter.getDistancePlus(this.first_location, last_loc, 0);
//                temp_jarak_destination = Converter.getDistancePlus(last_loc, this.last_location, 0);
                new_graph = new Graph(graphs.get(a).getId_node(),
                        graphs.get(a).getHeuristik(),
                        graphs.get(a).getUrutan_first(),
                        graphs.get(a).getUrutan_last(),
                        graphs.get(a).getStartlat(),
                        graphs.get(a).getStartlong(),
                        graphs.get(a).getEndlat(),
                        graphs.get(a).getEndlong());
            }
        }
        Log.d(TAG, "make_first_jalur: ");

        Graph graph = new Graph(0, 0, 0,
                new_graph.getUrutan_first(),
                this.first_location.getLatitude(),
                this.first_location.getLongitude(),
                new_graph.getStartlat(),
                new_graph.getStartlong());
        graphs_astar.add(graph);

        graphs_astar.add(new_graph);
    }
    public boolean make_second_jalur(Graph graph_first){
        Graph new_graph = null;
        Location first_loc = new Location("");
        first_loc.setLatitude(graph_first.getStartlat());
        first_loc.setLongitude(graph_first.getStartlong());

        double temp_jarak = Converter.getDistancePlus(this.first_location, this.last_location, 0);
        //double temp_jarak_destination = Converter.getDistancePlus(this.first_location, this.last_location, 0);


        for(int a=0;a<graphs.size();a++){

           /* Location ll = new Location("");
            ll.setLatitude( graphs_astar.get(graphs_astar.size()).getEndlat());
            ll.setLongitude(graphs_astar.get(graphs_astar.size()).getEndlong());*/

            if(graph_first.getUrutan_last() == graphs.get(a).getUrutan_first() ){//|| graph_first.getUrutan_first() == graphs.get(a).getUrutan_last()){
                Location last_loc = new Location("");
                last_loc.setLatitude(graphs.get(a).getEndlat());
                last_loc.setLongitude(graphs.get(a).getEndlong());

                double jarak_titik_ke_tujuan = Converter.getDistancePlus(last_loc, this.last_location, 0);
                // Log.d(TAG, "make_second_jalur: "+jarak_titik_ke_tujuan);
                //if(Converter.getDistancePlus(last_loc, this.last_location, 0) <= temp_jarak_destination){
                if(Converter.getDistancePlus(last_loc, this.last_location, graphs.get(a).getHeuristik())<=temp_jarak || jarak_titik_ke_tujuan<0.1){
                    temp_jarak = Converter.getDistancePlus(last_loc, this.last_location, graphs.get(a).getHeuristik());

                    Log.d(TAG, "make_second_jalur: "+jarak_titik_ke_tujuan);

                    //temp_jarak_destination = Converter.getDistancePlus(last_loc, this.last_location, 0);
                    new_graph = new Graph(graphs.get(a).getId_node(),
                            graphs.get(a).getHeuristik(),
                            graphs.get(a).getUrutan_first(),
                            graphs.get(a).getUrutan_last(),
                            graphs.get(a).getStartlat(),
                            graphs.get(a).getStartlong(),
                            graphs.get(a).getEndlat(),
                            graphs.get(a).getEndlong());


                }
                //}
            }
        }

        if(new_graph!=null){
            if(graphs_astar.get(graphs_astar.size()-1).getId_node() != new_graph.getId_node()){
                graphs_astar.add(new_graph);
                return true;
            } else{
                return false;
            }
        }else{
            return false;
        }

    }

    public void make_last_jalur(){
        Location last_loc = new Location("");
        Log.d(TAG, "make_last_jalur: ");
        Graph before_last_node = graphs_astar.get(graphs_astar.size()-1);

        Graph graph = new Graph(998, 0, before_last_node.getUrutan_last(), 998, before_last_node.getEndlat(), before_last_node.getEndlong(), this.last_location.getLatitude(), this.last_location.getLongitude());
        graphs_astar.add(graph);

        /*Graph graph2 = new Graph(999, 0, 998, 999, this.last_location.getLatitude(), this.last_location.getLongitude(), this.last_location.getLatitude(), this.last_location.getLongitude());
        graphs_astar.add(graph2);*/

    }

    public void makeAGraph(){
        boolean isNode = true;
        int isOk = 0;
        int id_temp = 0;

        make_first_jalur();
        while (isNode){
            isNode = make_second_jalur(graphs_astar.get(graphs_astar.size()-1));
            if(isOk==0){
                id_temp = graphs.get(graphs.size()-1).getId_node();
            }else if(isOk==2){
                if(id_temp == graphs.get(graphs.size()-1).getId_node()){
                    isNode = false;
                }else{
                    id_temp = graphs.get(graphs_astar.size()-1).getId_node();
                }
            }
            isOk++;
        }
        make_last_jalur();
    }

    public ArrayList<Graph> getGraphs() {
        return graphs;
    }

    public void setGraphs(Graph graph) {
        this.graphs.add(graph);
    }

    public ArrayList<Graph> getGraphs_astar() {
        return graphs_astar;
    }

    public void setGraphs_astar(Graph graph) {
        this.graphs_astar.add(graph);
    }
}
