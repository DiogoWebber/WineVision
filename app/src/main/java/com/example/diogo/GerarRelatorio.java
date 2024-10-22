package com.example.diogo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.database.dao.ClienteDAO;

import java.util.List;

public class GerarRelatorio extends AppCompatActivity {
    private Spinner spinnerCliente;
    private ClienteDAO clienteDAO; // Ensure this is initialized appropriately

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relatorio_select); // Use the correct layout file

        spinnerCliente = findViewById(R.id.spinnerCliente);
        clienteDAO = new ClienteDAO(this); // Initialize your DAO
        loadSpinners();

        // Set an onItemSelected listener to the spinner
        spinnerCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position > 0) { // Assuming the first item is a prompt
                    String selectedClient = (String) parent.getItemAtPosition(position);
                    navigateToReport(selectedClient);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle when nothing is selected
            }
        });
    }

    private void loadSpinners() {
        List<String> clientes = clienteDAO.getAllClientesNames();
        clientes.add(0, "Selecione o Cliente");

        ArrayAdapter<String> clienteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientes);
        clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(clienteAdapter);
    }

    private void navigateToReport(String selectedClient) {
        int clienteId = clienteDAO.getClientIdByName(selectedClient); // Assuming this method exists
        Intent intent = new Intent(this, RelatorioActivity.class);
        intent.putExtra("CLIENTE_ID", clienteId); // Pass the clienteId
        startActivity(intent);
    }

    private void showClienteDialog() {
        List<String> clientes = clienteDAO.getAllClientesNames();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecionar Cliente")
                .setItems(clientes.toArray(new String[0]), (dialog, which) -> {
                    String selectedClient = clientes.get(which);
                    spinnerCliente.setSelection(which);
                    navigateToReport(selectedClient);
                });
        builder.create().show();
    }
}
