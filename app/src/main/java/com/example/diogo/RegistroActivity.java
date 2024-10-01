package com.example.diogo;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private EditText editDocumento;
    private RadioGroup radioGroup;
    private RadioButton radioCPF;
    private RadioButton radioCNPJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro); // Certifique-se de que este é o nome correto do seu layout

        editDocumento = findViewById(R.id.editDocumento);
        radioGroup = findViewById(R.id.radioGroup);
        radioCPF = findViewById(R.id.radioCPF);
        radioCNPJ = findViewById(R.id.radioCNPJ);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        // Configurar o listener para mudar a máscara
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCPF) {
                editDocumento.setHint("CPF");
                editDocumento.setText(""); // Limpa o campo
                // Adicione a máscara CPF
                editDocumento.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
                applyMask("999.999.999-99");
            } else if (checkedId == R.id.radioCNPJ) {
                editDocumento.setHint("CNPJ");
                editDocumento.setText(""); // Limpa o campo
                // Adicione a máscara CNPJ
                editDocumento.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
                applyMask("99.999.999/9999-99");
            }
        });

        // Configure o botão de registro
        btnRegistrar.setOnClickListener(v -> {
            // Lógica de registro aqui
        });
    }

    private void applyMask(String mask) {
        editDocumento.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final String digits = "0123456789";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!str.equals(current)) {
                    String clean = str.replaceAll("[^\\d]", "");
                    StringBuilder masked = new StringBuilder();
                    int maskIndex = 0;

                    for (int i = 0; i < clean.length(); i++) {
                        char c = clean.charAt(i);
                        if (maskIndex < mask.length() && mask.charAt(maskIndex) == '9') {
                            masked.append(c);
                            maskIndex++;
                        } else if (maskIndex < mask.length()) {
                            masked.append(mask.charAt(maskIndex));
                            maskIndex++;
                            masked.append(c);
                        }
                    }

                    current = masked.toString();
                    editDocumento.setText(current);
                    editDocumento.setSelection(current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}

