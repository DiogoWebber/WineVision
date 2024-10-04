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

import com.example.diogo.database.dao.VinhoDAO;
import com.example.diogo.database.model.ClientesModel;
import com.example.diogo.database.model.VinhosModel;

import java.util.List;

public class VinhosAdapter extends RecyclerView.Adapter<VinhosAdapter.VinhoViewHolder> {

    private List<VinhosModel> vinhosList;
    private Context context;

    public VinhosAdapter(List<VinhosModel> vinhosList, Context context) {
        this.vinhosList = vinhosList;
        this.context = context;
    }

    @NonNull
    @Override
    public VinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vinho, parent, false);
        return new VinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VinhoViewHolder holder, int position) {
        VinhosModel vinho = vinhosList.get(position);
        holder.nomeTextView.setText(vinho.getNome());
        holder.tipoTextView.setText("Tipo: " + vinho.getTipo());
        holder.anoTextView.setText("Ano: " + String.valueOf(vinho.getSafra()));
        holder.estoqueTextView.setText("Disponiveis: " + String.valueOf(vinho.getEstoque()));
        holder.precoTextView.setText(String.format("Preço: R$ %.2f", vinho.getPreco()));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vinho != null) {
                    Intent intent = new Intent(context, EditVinhoActivity.class);
                    intent.putExtra("COLUNA_ID", vinho.getId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Vinho não encontrado.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vinho != null) {
                    new AlertDialog.Builder(context)
                            .setTitle("Excluir Vinho")
                            .setMessage("Você tem certeza que deseja excluir este vinho?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    VinhoDAO vinhoDAO = new VinhoDAO(context);
                                    long result = vinhoDAO.delete(vinho.getId());

                                    if (result != -1) {
                                        Toast.makeText(context, "Vinho excluído com sucesso!", Toast.LENGTH_SHORT).show();
                                        if (context instanceof VinhosActivity) {
                                            ((VinhosActivity) context).refreshVinhos();
                                        }
                                    } else {
                                        Toast.makeText(context, "Erro ao excluir vinho. Tente novamente.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Não", null)
                            .show();
                } else {
                    Toast.makeText(context, "Vinho não encontrado.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vinhosList.size();
    }

    public static class VinhoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;
        TextView tipoTextView;
        TextView anoTextView;
        TextView precoTextView;
        TextView estoqueTextView;
        ImageView editButton;
        ImageView deleteButton;

        public VinhoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.textViewNomeVinho);
            tipoTextView = itemView.findViewById(R.id.textViewTipoVinho);
            anoTextView = itemView.findViewById(R.id.textViewSafra);
            estoqueTextView = itemView.findViewById(R.id.textViewEstoque);
            precoTextView = itemView.findViewById(R.id.textViewPrecoVinho);
            editButton = itemView.findViewById(R.id.imageViewEdit);
            deleteButton = itemView.findViewById(R.id.imageViewDelete);
        }
    }
    public void updateVinhos(List<VinhosModel> novosVinhos) {
        this.vinhosList.clear();
        this.vinhosList.addAll(novosVinhos);
        notifyDataSetChanged();
    }
}
