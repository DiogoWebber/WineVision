// Adapter class for RecyclerView
package com.example.diogo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diogo.database.model.ClientesModel;
import com.example.diogo.R;
import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder> {

    private List<ClientesModel> clientesList;

    public ClientesAdapter(List<ClientesModel> clientesList) {
        this.clientesList = clientesList;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        ClientesModel cliente = clientesList.get(position);
        holder.nomeTextView.setText(cliente.getNome());
        holder.emailTextView.setText(cliente.getEmail());
        holder.telefoneTextView.setText(cliente.getTelefone());
    }

    @Override
    public int getItemCount() {
        return clientesList.size();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;
        TextView emailTextView;
        TextView telefoneTextView;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.textViewNome);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            telefoneTextView = itemView.findViewById(R.id.textViewTelefone);
        }
    }
}
