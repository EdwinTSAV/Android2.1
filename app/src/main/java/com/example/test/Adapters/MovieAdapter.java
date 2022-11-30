package com.example.test.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Class.Movie;
import com.example.test.MoviedetailActivity;
import com.example.test.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> /*implements View.OnClickListener*/{

    List<Movie> mData;

    public MovieAdapter(List<Movie> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        MovieAdapter.MovieViewHolder vh = new MovieAdapter.MovieViewHolder(view,context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        String imagenM = String.valueOf(mData.get(position).getImagen());
        holder.titulo.setText(mData.get(position).getTitulo());
        holder.sinopsis.setText(mData.get(position).getSinopsis());
        Picasso.get().load(imagenM).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, sinopsis;
        ImageView imagen;

        public MovieViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            titulo = itemView.findViewById(R.id.titulo);
            sinopsis = itemView.findViewById(R.id.sinopsis);
            imagen = itemView.findViewById(R.id.imagen);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titleToast =  mData.get(getAdapterPosition()).getTitulo();
                    Toast.makeText(context, titleToast, Toast.LENGTH_SHORT).show();
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MoviedetailActivity.class);
                    int id = mData.get(getAdapterPosition()).getId();
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
