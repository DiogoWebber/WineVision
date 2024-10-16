package com.example.diogo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.diogo.database.dao.ClienteDAO;
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
        holder.nomeTextView.setText("Nome: " +cliente.getNome());
        holder.emailTextView.setText("Email: " +cliente.getEmail());
        holder.telefoneTextView.setText("Telefone: " +cliente.getTelefone());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliente != null) {
                    Intent intent = new Intent(context, EditUserActivity.class);
                    intent.putExtra("COLUNA_ID", cliente.getId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Cliente não encontrado.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliente != null) {
                    // Criar um AlertDialog para confirmação
                    new AlertDialog.Builder(context)
                            .setTitle("Excluir Cliente")
                            .setMessage("Você tem certeza que deseja excluir este cliente?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    ClienteDAO clienteDAO = new ClienteDAO(context);
                                    long result = clienteDAO.delete(cliente.getId());

                                    if (result != -1) {
                                        Toast.makeText(context, "Cliente excluído com sucesso!", Toast.LENGTH_SHORT).show();

                                        if (context instanceof ClientesActivity) {
                                            ((ClientesActivity) context).refreshClientes();
                                        }
                                    } else {
                                        Toast.makeText(context, "Erro ao excluir cliente. Tente novamente.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Não", null)
                            .show();
                } else {
                    Toast.makeText(context, "Cliente não encontrado.", Toast.LENGTH_SHORT).show();
                }
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

    public void updateClientes(List<ClientesModel> novosClientes) {
        this.clientesList.clear();
        this.clientesList.addAll(novosClientes);
        notifyDataSetChanged();
    }
}
