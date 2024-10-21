package com.example.diogo.clientes;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.MaskEditText;
import com.example.diogo.R;
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
        setStatusBarColor();
        setContentView(R.layout.activity_registro);

        initializeViews();
        setupMasks();
        setupRadioGroupListener();
        setupButtonClickListener();
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
    }

    private void initializeViews() {
        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editTelefone = findViewById(R.id.editTelefone);
        editEndereco = findViewById(R.id.editEndereco);
        editNumero = findViewById(R.id.editNumero);
        editDocumento = findViewById(R.id.editDocumento);
        radioGroup = findViewById(R.id.radioGroup);
        radioCPF = findViewById(R.id.radioCPF);
        radioCNPJ = findViewById(R.id.radioCNPJ);
        btnRegistrar = findViewById(R.id.btnRegistrar);
    }

    private void setupMasks() {
        telefoneMask = MaskEditText.insert("+## (##) #####-####", editTelefone);
        editTelefone.addTextChangedListener(telefoneMask);
        cpfMask = MaskEditText.insert("###.###.###-##", editDocumento);
        cnpjMask = MaskEditText.insert("##.###.###/####-##", editDocumento);
    }

    private void setupRadioGroupListener() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            editDocumento.removeTextChangedListener(cpfMask);
            editDocumento.removeTextChangedListener(cnpjMask);
            editDocumento.setText("");

            if (checkedId == R.id.radioCPF) {
                editDocumento.addTextChangedListener(cpfMask);
            } else if (checkedId == R.id.radioCNPJ) {
                editDocumento.addTextChangedListener(cnpjMask);
            }
        });
    }

    private void setupButtonClickListener() {
        btnRegistrar.setOnClickListener(v -> {
            if (areFieldsValid()) {
                saveClient();
            } else {
                Toast.makeText(RegistroActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean areFieldsValid() {
        return !editNome.getText().toString().isEmpty() &&
                !editEmail.getText().toString().isEmpty() &&
                !editTelefone.getText().toString().isEmpty() &&
                !editEndereco.getText().toString().isEmpty() &&
                !editNumero.getText().toString().isEmpty() &&
                !editDocumento.getText().toString().isEmpty();

    }

    private void saveClient() {
        clienteDAO = new ClienteDAO(this);
        ClientesModel clientesModel = new ClientesModel();
        clientesModel.setNome(editNome.getText().toString());
        clientesModel.setEmail(editEmail.getText().toString());
        clientesModel.setTelefone(editTelefone.getText().toString());
        clientesModel.setEndereco(editEndereco.getText().toString());
        clientesModel.setNumero(Integer.parseInt(editNumero.getText().toString()));
        clientesModel.setDocumento(editDocumento.getText().toString());

        if (clienteDAO.insert(clientesModel) != -1) {
            Toast.makeText(RegistroActivity.this, "Usuário Salvo!!!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(RegistroActivity.this, "Falha ao Salvar o Usuário!!", Toast.LENGTH_SHORT).show();
        }

        clearFields();
    }

    private void clearFields() {
        editNome.setText("");
        editEmail.setText("");
        editTelefone.setText("");
        editEndereco.setText("");
        editNumero.setText("");
        editDocumento.setText("");
    }
}
