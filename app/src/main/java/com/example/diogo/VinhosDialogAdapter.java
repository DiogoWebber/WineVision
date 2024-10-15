package com.example.diogo;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diogo.database.model.VinhosModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VinhosDialogAdapter extends RecyclerView.Adapter<VinhosDialogAdapter.VinhoViewHolder> {

    private List<VinhosModel> vinhosList;
    private Map<VinhosModel, Integer> vinhosSelecionados = new HashMap<>();

    public VinhosDialogAdapter(List<VinhosModel> vinhosList) {
        this.vinhosList = vinhosList;
    }

    @NonNull
    @Override
    public VinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vinho_dialog, parent, false);
        return new VinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VinhoViewHolder holder, int position) {
        VinhosModel vinho = vinhosList.get(position);
        holder.bind(vinho);
    }

    @Override
    public int getItemCount() {
        return vinhosList.size();
    }

    public Map<VinhosModel, Integer> getVinhosSelecionados() {
        return vinhosSelecionados;
    }

    class VinhoViewHolder extends RecyclerView.ViewHolder {

        TextView nomeVinho, quantidadeDisponivel;
        EditText quantidadeSelecionada;

        public VinhoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeVinho = itemView.findViewById(R.id.nomeVinhoDialog);
            quantidadeDisponivel = itemView.findViewById(R.id.quantidadeDisponivel);
            quantidadeSelecionada = itemView.findViewById(R.id.quantidadeSelecionada);
        }

        public void bind(VinhosModel vinho) {
            nomeVinho.setText(vinho.getNome());
            quantidadeDisponivel.setText("Disponível: " + vinho.getEstoque());

            quantidadeSelecionada.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        int quantidade = Integer.parseInt(editable.toString());
                        if (quantidade <= vinho.getEstoque()) {
                            vinhosSelecionados.put(vinho, quantidade);
                        } else {
                            quantidadeSelecionada.setError("Quantidade indisponível");
                        }
                    } catch (NumberFormatException e) {
                        vinhosSelecionados.remove(vinho);
                    }
                }
            });
        }
    }
}
