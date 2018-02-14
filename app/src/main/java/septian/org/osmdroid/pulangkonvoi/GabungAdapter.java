package septian.org.osmdroid.pulangkonvoi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import septian.org.osmdroid.pulangkonvoi.Kelas.User;

public class GabungAdapter extends RecyclerView.Adapter<GabungAdapter.Holder> {
    Context context;
    List<User> users;

    public GabungAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public GabungAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gabung_adapter, parent, false);
        Holder rcv = new Holder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(GabungAdapter.Holder holder, int position) {
        User user = users.get(position);
        holder.tv_name.setText(user.getNamaPeng());
        holder.tv_alamat.setText(user.getAlamat());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_alamat;
        public Holder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_alamat = itemView.findViewById(R.id.tv_alamat);
        }
    }
}
