package com.example.diogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diogo.database.model.VendasModel;

import java.util.List;

public class VendasAdapter extends RecyclerView.Adapter<VendasAdapter.VendaViewHolder> {

    private List<VendasModel> vendasList;
    private Context context;

    public VendasAdapter(List<VendasModel> vendasList, Context context) {
        this.vendasList = vendasList;
        this.context = context;
    }

    @NonNull
    @Override
    public VendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venda, parent, false);
        return new VendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendaViewHolder holder, int position) {
        VendasModel venda = vendasList.get(position);

        // Preenchendo os dados da venda no layout
        holder.clienteTextView.setText("Nome Cliente: " + venda.getCliente());
        holder.vinhoTextView.setText("Nome Vinho: " +venda.getVinho());
        holder.dataVendaTextView.setText("Data Venda: " +venda.getDataVenda());
        holder.quantidadeTextView.setText("Quantidade Vendidos: " + venda.getQuantidade());
    }

    @Override
    public int getItemCount() {
        return vendasList.size();
    }

    public static class VendaViewHolder extends RecyclerView.ViewHolder {
        TextView clienteTextView;
        TextView vinhoTextView;
        TextView dataVendaTextView;
        TextView quantidadeTextView;

        public VendaViewHolder(@NonNull View itemView) {
            super(itemView);
            clienteTextView = itemView.findViewById(R.id.textViewCliente);
            vinhoTextView = itemView.findViewById(R.id.textViewVinho);
            dataVendaTextView = itemView.findViewById(R.id.textViewDataVenda);
            quantidadeTextView = itemView.findViewById(R.id.textViewQuantidade);
        }
    }

    public void updateVendas(List<VendasModel> novasVendas) {
        this.vendasList.clear();
        this.vendasList.addAll(novasVendas);
        notifyDataSetChanged();
    }
}
