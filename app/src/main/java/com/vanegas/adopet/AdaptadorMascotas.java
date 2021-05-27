package com.vanegas.adopet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorMascotas extends RecyclerView.Adapter<AdaptadorMascotas.ViewHolderMascotas> implements View.OnClickListener {

    ArrayList<MascotaVo> listaMascotas;
    private View.OnClickListener listener;

    public AdaptadorMascotas(ArrayList<MascotaVo> listaMascotas) {
        this.listaMascotas = listaMascotas;
    }

    @Override
    public ViewHolderMascotas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_mascota, null, false);

        view.setOnClickListener(this);

        return new ViewHolderMascotas(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorMascotas.ViewHolderMascotas holder, int position) {
        holder.nombre.setText(listaMascotas.get(position).getNombre());
        holder.raza.setText(listaMascotas.get(position).getRaza());
        //holder.peso.setText(listaMascotas.get(position).getPeso());
        holder.fecna.setText(listaMascotas.get(position).getFecna());
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null)
            listener.onClick(v);
    }

    public class ViewHolderMascotas extends RecyclerView.ViewHolder {

        TextView nombre, raza, peso, fecna;

        public ViewHolderMascotas(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.tv_nombreMascotaItem);
            raza = (TextView) itemView.findViewById(R.id.tv_razaMascotaItem);
            //peso  = (TextView) itemView.findViewById(R.id.tv_pesoMascotaItem);
            fecna = (TextView) itemView.findViewById(R.id.tv_fecnaMascotaItem);
        }
    }
}
