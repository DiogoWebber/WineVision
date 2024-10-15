package com.example.diogo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diogo.database.dao.ClienteDAO;
import com.example.diogo.database.model.ClientesModel;

public class EditUserActivity extends AppCompatActivity {

    private EditText nomeTextView;
    private EditText emailTextView;
    private EditText telefoneTextView;
    private EditText enderecoTextView;
    private EditText numeroTextView;
    private EditText documentoTextView;
    private Button btnSalvar;
    private ClienteDAO clienteDAO;
    private long clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("COLUNA_ID")) {
            clienteId = getIntent().getLongExtra("COLUNA_ID", -1L);
        }

        // Inicializando o DAO
        clienteDAO = new ClienteDAO(this);

        nomeTextView = findViewById(R.id.editNome);
        emailTextView = findViewById(R.id.editEmail);
        telefoneTextView = findViewById(R.id.editTelefone);
        enderecoTextView = findViewById(R.id.editEndereco);
        numeroTextView = findViewById(R.id.editNumero);
        documentoTextView = findViewById(R.id.editDocumento);
        btnSalvar = findViewById(R.id.btnSalvar);

        loadClientData(clienteId);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClientData();
            }
        });
    }

    private void loadClientData(long clienteId) {
        ClientesModel cliente = clienteDAO.getById(clienteId);
        if (cliente != null) {
            nomeTextView.setText(cliente.getNome());
            emailTextView.setText(cliente.getEmail());
            telefoneTextView.setText(cliente.getTelefone());
            enderecoTextView.setText(cliente.getEndereco());
            numeroTextView.setText(String.valueOf(cliente.getNumero()));
            documentoTextView.setText(cliente.getDocumento());
        } else {
            Toast.makeText(this, "Cliente não encontrado.", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveClientData() {
        String nome = nomeTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String telefone = telefoneTextView.getText().toString();
        String endereco = enderecoTextView.getText().toString();
        String numero = numeroTextView.getText().toString();
        String documento = documentoTextView.getText().toString();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(telefone)) {
            Toast.makeText(this, "Por favor, prencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ClientesModel cliente = new ClientesModel(clienteId, nome, email, telefone, endereco, Integer.parseInt(numero), documento);
            long result = clienteDAO.update(cliente);

            if (result != -1) {
                Toast.makeText(this, "Cliente atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditUserActivity.this, ClientesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar cliente. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Erro ao converter o número. Verifique o valor.", Toast.LENGTH_SHORT).show();
        }
    }

}
