package com.example.diogo.vinhos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.R;
import com.example.diogo.database.dao.VinhoDAO;
import com.example.diogo.database.model.VinhosModel;

public class EditVinhoActivity extends AppCompatActivity {

    private EditText nomeTextView, tipoTextView, anoTextView, precoTextView;
    private EditText safraTextView, paisTextView, graduacaoTextView, volumeTextView, estoqueTextView;
    private Button btnSalvar;
    private VinhoDAO vinhoDAO;
    private long vinhoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_vinho);

        // Retrieve the ID of the wine to be edited
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("COLUNA_ID")) {
            vinhoId = intent.getLongExtra("COLUNA_ID", -1L);
        }

        // Initialize the DAO
        vinhoDAO = new VinhoDAO(this);

        // Bind views
        nomeTextView = findViewById(R.id.editNomeVinho);
        tipoTextView = findViewById(R.id.editTipoVinho);
        safraTextView = findViewById(R.id.editSafra);
        paisTextView = findViewById(R.id.editPaisOrigem);
        graduacaoTextView = findViewById(R.id.editGraduacaoAlcoolica);
        volumeTextView = findViewById(R.id.editVolume);
        estoqueTextView = findViewById(R.id.editEstoque);
        precoTextView = findViewById(R.id.editPrecoVinho);

        btnSalvar = findViewById(R.id.btnSalvarVinho);

        // Load the wine data for the given ID
        loadVinhoData(vinhoId);

        // Set up click listener for the save button
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVinhoData();
            }
        });
    }

    private void loadVinhoData(long vinhoId) {
        VinhosModel vinho = vinhoDAO.getById(vinhoId);
        if (vinho != null) {
            // Populate the EditText fields with wine data
            nomeTextView.setText(vinho.getNome());
            tipoTextView.setText(vinho.getTipo());
            safraTextView.setText(String.valueOf(vinho.getSafra()));
            paisTextView.setText(vinho.getPaisOrigem());
            graduacaoTextView.setText(String.valueOf(vinho.getGraduacaoAlcoolica()));
            volumeTextView.setText(String.valueOf(vinho.getVolume()));
            estoqueTextView.setText(String.valueOf(vinho.getEstoque()));
            precoTextView.setText(String.valueOf(vinho.getPreco()));
        } else {
            Toast.makeText(this, "Vinho não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveVinhoData() {
        String nome = nomeTextView.getText().toString().trim();
        String tipo = tipoTextView.getText().toString().trim();
        String safraStr = safraTextView.getText().toString().trim();
        String pais = paisTextView.getText().toString().trim();
        String graduacaoStr = graduacaoTextView.getText().toString().trim();
        String volumeStr = volumeTextView.getText().toString().trim();
        String estoqueStr = estoqueTextView.getText().toString().trim();
        String precoStr = precoTextView.getText().toString().trim();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(tipo) ||
                TextUtils.isEmpty(safraStr) || TextUtils.isEmpty(pais) ||
                TextUtils.isEmpty(graduacaoStr) || TextUtils.isEmpty(volumeStr) ||
                TextUtils.isEmpty(estoqueStr) || TextUtils.isEmpty(precoStr)) {

            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int safra = Integer.parseInt(safraStr);
            double graduacao = Double.parseDouble(graduacaoStr);
            double volume = Double.parseDouble(volumeStr);
            int estoque = Integer.parseInt(estoqueStr);
            double preco = Double.parseDouble(precoStr);

            if (safra < 0 || volume < 0 || estoque < 0 || preco < 0) {
                Toast.makeText(this, "Valores não podem ser negativos.", Toast.LENGTH_SHORT).show();
                return;
            }

            VinhosModel vinho = new VinhosModel(vinhoId, nome, tipo, safra, pais, graduacao, volume, estoque, preco);
            long result = vinhoDAO.updateVinho(vinho);

            if (result != -1) {
                Toast.makeText(this, "Vinho atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditVinhoActivity.this, VinhosActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar vinho. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Erro ao converter valores numéricos. Verifique os valores.", Toast.LENGTH_SHORT).show();
        }
    }

}
