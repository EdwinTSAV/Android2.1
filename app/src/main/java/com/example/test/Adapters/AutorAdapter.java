package com.example.test.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Class.Autor;
import com.example.test.MapsActivity;
import com.example.test.R;

import java.util.List;

public class AutorAdapter extends RecyclerView.Adapter<AutorAdapter.AutorViewHolder> {

    List<Autor> mData;
    public AutorAdapter(List<Autor> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public AutorAdapter.AutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.autor_item, parent, false);
        AutorAdapter.AutorViewHolder vh = new AutorAdapter.AutorViewHolder(view,context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AutorAdapter.AutorViewHolder holder, int position) {
        holder.nombre.setText(mData.get(position).getNombre());
        holder.latitud.setText(mData.get(position).getLatitud());
        holder.longitud.setText(mData.get(position).getLongitud());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AutorViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, latitud, longitud;
        public AutorViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);
            latitud = itemView.findViewById(R.id.latitud);
            longitud = itemView.findViewById(R.id.longitud);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titleToast =  mData.get(getAdapterPosition()).getNombre();
                    Toast.makeText(context, titleToast, Toast.LENGTH_SHORT).show();
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MapsActivity.class);
                    int id = mData.get(getAdapterPosition()).getId();
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });

        }
    }
}
