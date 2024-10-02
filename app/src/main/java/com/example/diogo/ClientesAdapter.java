package com.example.diogo;

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

import com.example.diogo.database.model.ClientesModel;

import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder> {

    private List<ClientesModel> clientesList;
    private Context context;

    public ClientesAdapter(List<ClientesModel> clientesList, Context context) {
        this.clientesList = clientesList;
        this.context = context;
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

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliente != null) {
                    Intent intent = new Intent(context, EditUserActivity.class);
                    intent.putExtra("COLUNA_ID", cliente.getId());
                    Log.d("ClientesAdapter", "ID do Cliente: " + cliente.getId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Cliente não encontrado.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientesList.size();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;
        TextView emailTextView;
        TextView telefoneTextView;
        ImageView editButton;
        ImageView deleteButton;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.textViewNome);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            telefoneTextView = itemView.findViewById(R.id.textViewTelefone);
            editButton = itemView.findViewById(R.id.imageViewEdit);
            deleteButton = itemView.findViewById(R.id.imageViewDelete);
        }
    }
}
