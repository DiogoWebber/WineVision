package com.example.diogo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diogo.database.dao.ClienteDAO;
import com.example.diogo.database.model.ClientesModel;

import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClientesAdapter adapter;
    private ClienteDAO clienteDAO;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        recyclerView = findViewById(R.id.recyclerViewClientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clienteDAO = new ClienteDAO(this);
        loadClientes();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ClientesActivity.this, RegistroActivity.class);
                startActivity(it);
            }
        });
    }
    private void loadClientes() {
        List<ClientesModel> clientesList = clienteDAO.getAll();
        adapter = new ClientesAdapter(clientesList, this);
        recyclerView.setAdapter(adapter);
    }
}
