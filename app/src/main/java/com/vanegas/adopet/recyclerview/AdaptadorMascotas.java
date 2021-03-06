package com.vanegas.adopet.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vanegas.adopet.R;

import java.util.ArrayList;

public class AdaptadorMascotas extends RecyclerView.Adapter<AdaptadorMascotas.ViewHolderMascotas> {

    ArrayList<MascotaVo> listaMascotas;
    private ClickListener listener;

    public AdaptadorMascotas(ArrayList<MascotaVo> listaMascotas) {
        this.listaMascotas = listaMascotas;
    }

    @Override
    public ViewHolderMascotas onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutinflater=LayoutInflater.from(parent.getContext());
        View view = layoutinflater.inflate(R.layout.activity_item_mascota, parent, false);
        return new ViewHolderMascotas(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorMascotas.ViewHolderMascotas holder, int position) {
        holder.bindItems(listaMascotas.get(position));
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
    }

    public MascotaVo getItem(int position){
        return listaMascotas.get(position);
    }

    public void setOnClickListener(ClickListener listener){
        this.listener = listener;
    }


    public class ViewHolderMascotas extends RecyclerView.ViewHolder  implements View.OnClickListener{

        TextView nombre, raza, peso, fecna;
        ImageView foto;

        public ViewHolderMascotas(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre = (TextView) itemView.findViewById(R.id.tv_nombreMascotaItem);
            raza = (TextView) itemView.findViewById(R.id.tv_razaMascotaItem);
            fecna = (TextView) itemView.findViewById(R.id.tv_fecnaMascotaItem);
            foto = (ImageView) itemView.findViewById(R.id.iv_imagenMascotaItem);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null)
                listener.onItemClick(v,getAdapterPosition());
        }

        public void bindItems(MascotaVo mascotaVo) {
            nombre.setText(mascotaVo.getNombre());
            raza.setText(mascotaVo.getRaza());
            fecna.setText(mascotaVo.getFecna());
            foto.setImageResource(mascotaVo.getFoto());
        }
    }

    public interface ClickListener{
        public void onItemClick(View v,int position);
    }
}
