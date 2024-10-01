package com.example.diogo.database.dao;

import android.content.ContentValues;
import android.content.Context;

import com.example.diogo.database.DBOpenHelper;
import com.example.diogo.database.model.ClientesModel;

public class ClienteDAO extends AbstrataDAO {

    public ClienteDAO(Context context) {
        helper = new DBOpenHelper(context);
    }

    public long insert(ClientesModel cliente) {

        long result = -1;
        try {
            Open();

            ContentValues values = new ContentValues();
            values.put("nome", cliente.getNome());
            values.put("email", cliente.getEmail());
            values.put("telefone", cliente.getTelefone());
            values.put("endereco", cliente.getEndereco());
            values.put("numero", cliente.getNumero());
            values.put("documento", cliente.getDocumento());

            result = db.insert(ClientesModel.TABLE_NAME, null, values);  // Armazenar o resultado da inserção

        }finally {
            Close();
        }

        return result;
    }
}
