package com.example.diogo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diogo.database.dao.ClienteDAO;
import com.example.diogo.database.model.ClientesModel;
import com.example.diogo.adapter.ClientesAdapter;

import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClientesAdapter adapter;
    private ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        recyclerView = findViewById(R.id.recyclerViewClientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        clienteDAO = new ClienteDAO(this);
        loadClientes();
    }

    private void loadClientes() {
        List<ClientesModel> clientesList = clienteDAO.getAll();
        adapter = new ClientesAdapter(clientesList);
        recyclerView.setAdapter(adapter);
    }
}
