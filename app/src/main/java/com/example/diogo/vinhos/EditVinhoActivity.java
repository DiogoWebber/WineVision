package com.example.diogo.vinhos;

import android.content.Intent;
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

    private EditText nomeTextView;
    private EditText tipoTextView;
    private EditText anoTextView;
    private EditText precoTextView;
    private Button btnSalvar;
    private VinhoDAO vinhoDAO;
    private long vinhoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        anoTextView = findViewById(R.id.editAnoVinho);
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
            anoTextView.setText(String.valueOf(vinho.getSafra())); // Use the correct method to retrieve the year
            precoTextView.setText(String.valueOf(vinho.getPreco())); // Use the correct method to retrieve the price
        } else {
            Toast.makeText(this, "Vinho não encontrado.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the wine is not found
        }
    }

    private void saveVinhoData() {
        String nome = nomeTextView.getText().toString();
        String tipo = tipoTextView.getText().toString();
        String anoStr = anoTextView.getText().toString();
        String precoStr = precoTextView.getText().toString();

        // Validate input fields
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(tipo) || TextUtils.isEmpty(anoStr) || TextUtils.isEmpty(precoStr)) {
            Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int ano = Integer.parseInt(anoStr); // Convert year input to int
            double preco = Double.parseDouble(precoStr); // Convert price input to double

            // Create a new VinhosModel object
            VinhosModel vinho = new VinhosModel(vinhoId, nome, tipo, ano, preco);
            long result = vinhoDAO.updateVinho(vinho); // Update the wine in the DAO

            // Check if the update was successful
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
            Toast.makeText(this, "Erro ao converter o ano ou preço. Verifique os valores.", Toast.LENGTH_SHORT).show();
        }
    }
}
