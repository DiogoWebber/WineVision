package com.example.diogo;

import android.os.Bundle;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.database.dao.ClienteDAO;
import com.example.diogo.database.model.ClientesModel;
public class RegistroActivity extends AppCompatActivity {

    private EditText editNome, editEmail, editTelefone, editEndereco, editNumero, editDocumento;
    private Button btnRegistrar;
    private ClienteDAO clienteDAO;
    private RadioGroup radioGroup;
    private RadioButton radioCPF, radioCNPJ;
    private TextWatcher cpfMask, cnpjMask, telefoneMask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar os campos
        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editTelefone = findViewById(R.id.editTelefone);
        editEndereco = findViewById(R.id.editEndereco);
        editNumero = findViewById(R.id.editNumero);
        editDocumento = findViewById(R.id.editDocumento);
        radioGroup = findViewById(R.id.radioGroup);
        radioCPF = findViewById(R.id.radioCPF);
        radioCNPJ = findViewById(R.id.radioCNPJ);
        editTelefone = findViewById(R.id.editTelefone);

        telefoneMask = MaskEditText.insert("+## (##) #####-####", editTelefone);
        editTelefone.addTextChangedListener(telefoneMask);
        cpfMask = MaskEditText.insert("###.###.###-##", editDocumento);
        cnpjMask = MaskEditText.insert("##.###.###/####-##", editDocumento);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            editDocumento.removeTextChangedListener(cpfMask);
            editDocumento.removeTextChangedListener(cnpjMask);

            if (checkedId == R.id.radioCPF) {
                editDocumento.setText("");
                editDocumento.addTextChangedListener(cpfMask);
            } else if (checkedId == R.id.radioCNPJ) {
                editDocumento.setText("");
                editDocumento.addTextChangedListener(cnpjMask);
            }
        });
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> {

            clienteDAO = new ClienteDAO(RegistroActivity.this);

            ClientesModel clientesModel = new ClientesModel();
            clientesModel.setNome(editNome.getText().toString());
            clientesModel.setEmail(editEmail.getText().toString());
            clientesModel.setTelefone(editTelefone.getText().toString());
            clientesModel.setEndereco(editEndereco.getText().toString());
            clientesModel.setNumero(Integer.parseInt(editNumero.getText().toString()));
            clientesModel.setDocumento(editDocumento.getText().toString()); // Adiciona o documento

            if(clienteDAO.insert(clientesModel)!= -1){
                Toast.makeText(RegistroActivity.this, "Usuário Salvo!!!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(RegistroActivity.this, "Falha ao Salvar o Usuário!!", Toast.LENGTH_SHORT).show();

            }
            // Opcional: Limpar os campos após a inserção
            limparCampos();
        });
    }

    private void limparCampos() {
        editNome.setText("");
        editEmail.setText("");
        editTelefone.setText("");
        editEndereco.setText("");
        editNumero.setText("");
        editDocumento.setText("");
    }
}
