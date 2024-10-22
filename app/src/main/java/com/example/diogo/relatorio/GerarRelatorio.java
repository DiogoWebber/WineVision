package com.example.diogo.relatorio;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.R;
import com.example.diogo.database.dao.ClienteDAO;

public class GerarRelatorio extends AppCompatActivity {

    private ClienteDAO clienteDAO;
    private TextView gerarRelatorioMes, gerarRelatorioCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relatorio_select);

        gerarRelatorioMes = findViewById(R.id.gerarRelatorioMes);
        gerarRelatorioCliente = findViewById(R.id.gerarRelatorioCliente);
        clienteDAO = new ClienteDAO(this);

        // Configurar click listeners
        gerarRelatorioCliente.setOnClickListener(v -> navigateToRelatorioClienteActivity());
        gerarRelatorioMes.setOnClickListener(v -> navigateToRelatorioMesActivity());
    }

    private void navigateToRelatorioMesActivity() {
        Intent intent = new Intent(GerarRelatorio.this, RelatorioMesActivity.class);
        startActivity(intent);
    }
    private void navigateToRelatorioClienteActivity() {
        Intent intent = new Intent(GerarRelatorio.this, RelatorioActivity.class);
        startActivity(intent);
    }
}
