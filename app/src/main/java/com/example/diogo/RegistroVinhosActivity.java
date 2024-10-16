package com.example.diogo;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.database.dao.VinhoDAO;
import com.example.diogo.database.model.VinhosModel;

public class RegistroVinhosActivity extends AppCompatActivity {

    private EditText editNomeVinho;
    private EditText editTipoVinho;
    private EditText editSafra;
    private EditText editPaisOrigem;
    private EditText editGraduacaoAlcoolica;
    private EditText editVolume;
    private EditText editEstoque;
    private EditText editPreco;
    private Button btnRegistrarVinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vinhos);

        editNomeVinho = findViewById(R.id.editNomeVinho);
        editTipoVinho = findViewById(R.id.editTipoVinho);
        editSafra = findViewById(R.id.editSafra);
        editPaisOrigem = findViewById(R.id.editPaisOrigem);
        editGraduacaoAlcoolica = findViewById(R.id.editGraduacaoAlcoolica);
        editVolume = findViewById(R.id.editVolume);
        editEstoque = findViewById(R.id.editEstoque);
        editPreco = findViewById(R.id.editPreco);
        btnRegistrarVinho = findViewById(R.id.btnRegistrarVinho);

        btnRegistrarVinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarVinho();
            }
        });
    }

    private void registrarVinho() {
        String nomeVinho = editNomeVinho.getText().toString().trim();
        String tipoVinho = editTipoVinho.getText().toString().trim();
        String safra = editSafra.getText().toString().trim();
        String paisOrigem = editPaisOrigem.getText().toString().trim();
        String graduacaoAlcoolica = editGraduacaoAlcoolica.getText().toString().trim();
        String volume = editVolume.getText().toString().trim();
        String estoque = editEstoque.getText().toString().trim();
        String preco = editPreco.getText().toString().trim();

        // Validate fields
        if (TextUtils.isEmpty(nomeVinho) || TextUtils.isEmpty(tipoVinho) || TextUtils.isEmpty(safra) ||
                TextUtils.isEmpty(paisOrigem) || TextUtils.isEmpty(graduacaoAlcoolica) ||
                TextUtils.isEmpty(volume) || TextUtils.isEmpty(estoque) || TextUtils.isEmpty(preco)) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        VinhosModel vinho = new VinhosModel();
        vinho.setNome(nomeVinho);
        vinho.setTipo(tipoVinho);
        vinho.setSafra(Integer.parseInt(safra));
        vinho.setPaisOrigem(paisOrigem);
        vinho.setGraduacaoAlcoolica(Double.parseDouble(graduacaoAlcoolica));
        vinho.setVolume(Double.parseDouble(volume));
        vinho.setEstoque(Integer.parseInt(estoque));
        vinho.setPreco(Double.parseDouble(preco));

        VinhoDAO vinhoDAO = new VinhoDAO(this);

        long result = vinhoDAO.insert(vinho);
        if (result != -1) {
            Toast.makeText(this, "Vinho registrado com sucesso!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Set result to OK
            finish(); // Close the activity and return to VinhosActivity
        } else {
            Toast.makeText(this, "Erro ao registrar vinho. Tente novamente.", Toast.LENGTH_SHORT).show();
        }

        clearFields();
    }

    private void clearFields() {
        editNomeVinho.setText("");
        editTipoVinho.setText("");
        editSafra.setText("");
        editPaisOrigem.setText("");
        editGraduacaoAlcoolica.setText("");
        editVolume.setText("");
        editEstoque.setText("");
        editPreco.setText("");
    }
}
