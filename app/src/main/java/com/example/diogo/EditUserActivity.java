package com.example.diogo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Importar a classe Log
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diogo.database.dao.ClienteDAO;
import com.example.diogo.database.model.ClientesModel;

public class EditUserActivity extends AppCompatActivity {

    private static final String TAG = "EditUserActivity";
    private EditText nomeTextView;
    private EditText emailTextView;
    private EditText telefoneTextView;
    private EditText enderecoTextView;
    private EditText numeroTextView;
    private EditText documentoTextView;
    private Button btnSalvar;
    private ClienteDAO clienteDAO;
    private int clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        clienteDAO = new ClienteDAO(this);
        clienteId = getIntent().getIntExtra("COLUNA_ID", -1);
        Log.d(TAG, "ID do cliente recebido: " + clienteId);

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

    private void loadClientData(int clienteId) {
        Log.d(TAG, "Carregando dados do cliente com ID: " + clienteId);
        ClientesModel cliente = clienteDAO.getById(clienteId);
        if (cliente != null) {
            nomeTextView.setText(cliente.getNome());
            emailTextView.setText(cliente.getEmail());
            telefoneTextView.setText(cliente.getTelefone());
            enderecoTextView.setText(cliente.getEndereco());
            numeroTextView.setText(String.valueOf(cliente.getNumero()));
            documentoTextView.setText(cliente.getDocumento());
            Log.d(TAG, "Dados do cliente carregados: " + cliente.toString());
        } else {
            Log.e(TAG, "Cliente não encontrado com ID: " + clienteId);
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
            Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ClientesModel cliente = new ClientesModel(clienteId, nome, email, telefone, endereco, Integer.parseInt(numero), documento);
            long result = clienteDAO.update(cliente); // Assuming you have an update method in ClienteDAO

            if (result != -1) {
                Toast.makeText(this, "Cliente atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Cliente atualizado com sucesso: " + cliente.toString()); // Logando sucesso
                finish(); // Close the activity after saving
            } else {
                Log.e(TAG, "Erro ao atualizar cliente."); // Logando erro de atualização
                Toast.makeText(this, "Erro ao atualizar cliente. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Erro ao converter o número: " + e.getMessage()); // Logando erro de conversão
            Toast.makeText(this, "Erro ao converter o número. Verifique o valor.", Toast.LENGTH_SHORT).show();
        }
    }
}
