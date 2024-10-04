package com.example.diogo.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.diogo.database.DBOpenHelper;
import com.example.diogo.database.model.ClientesModel;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends AbstrataDAO {

    public ClienteDAO(Context context) {
        helper = new DBOpenHelper(context);
    }

    public long insert(ClientesModel cliente) {
        long result = -1;
        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(ClientesModel.COLUNA_CLIENTE, cliente.getNome());
            values.put(ClientesModel.COLUNA_EMAIL, cliente.getEmail());
            values.put(ClientesModel.COLUNA_TELEFONE, cliente.getTelefone());
            values.put(ClientesModel.COLUNA_ENDERECO, cliente.getEndereco());
            values.put(ClientesModel.COLUNA_NUMERO, cliente.getNumero());
            values.put(ClientesModel.COLUNA_DOCUMENTO, cliente.getDocumento());

            result = db.insert(ClientesModel.TABLE_NAME, null, values);
        } finally {
            Close();
        }

        return result;
    }

    public List<ClientesModel> getAll() {
        List<ClientesModel> clientesList = new ArrayList<>();
        Cursor cursor = null;

        try {
            Open();

            // Consulta para selecionar todos os registros da tabela Clientes
            cursor = db.query(ClientesModel.TABLE_NAME, null, null, null, null, null, null);

            // Itera sobre o cursor para criar uma lista de ClientesModel
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ClientesModel cliente = new ClientesModel();

                    // Acessando o ID do cliente
                    int idIndex = cursor.getColumnIndex(ClientesModel.COLUNA_ID);
                    if (idIndex != -1) {
                        cliente.setId(cursor.getLong(idIndex));
                    }

                    // Usando constantes para garantir que os nomes das colunas estejam corretos
                    int nomeIndex = cursor.getColumnIndex(ClientesModel.COLUNA_CLIENTE);
                    if (nomeIndex != -1) {
                        cliente.setNome(cursor.getString(nomeIndex));
                    }

                    int emailIndex = cursor.getColumnIndex(ClientesModel.COLUNA_EMAIL);
                    if (emailIndex != -1) {
                        cliente.setEmail(cursor.getString(emailIndex));
                    }

                    int telefoneIndex = cursor.getColumnIndex(ClientesModel.COLUNA_TELEFONE);
                    if (telefoneIndex != -1) {
                        cliente.setTelefone(cursor.getString(telefoneIndex));
                    }

                    int enderecoIndex = cursor.getColumnIndex(ClientesModel.COLUNA_ENDERECO);
                    if (enderecoIndex != -1) {
                        cliente.setEndereco(cursor.getString(enderecoIndex));
                    }

                    int numeroIndex = cursor.getColumnIndex(ClientesModel.COLUNA_NUMERO);
                    if (numeroIndex != -1) {
                        cliente.setNumero(cursor.getInt(numeroIndex));
                    }

                    int documentoIndex = cursor.getColumnIndex(ClientesModel.COLUNA_DOCUMENTO);
                    if (documentoIndex != -1) {
                        cliente.setDocumento(cursor.getString(documentoIndex));
                    }

                    clientesList.add(cliente); // Adiciona à lista
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close(); // Fecha o cursor para evitar vazamentos de memória
            }
            Close();
        }

        return clientesList;
    }
    public ClientesModel getById(long id) {
        ClientesModel cliente = null;
        Cursor cursor = null;

        try {
            Open();
            String selection = ClientesModel.COLUNA_ID + " = ?";
            String[] selectionArgs = { String.valueOf(id) };

            cursor = db.query(ClientesModel.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                cliente = new ClientesModel();

                int idIndex = cursor.getColumnIndex(ClientesModel.COLUNA_ID);
                int nomeIndex = cursor.getColumnIndex(ClientesModel.COLUNA_CLIENTE);
                int emailIndex = cursor.getColumnIndex(ClientesModel.COLUNA_EMAIL);
                int telefoneIndex = cursor.getColumnIndex(ClientesModel.COLUNA_TELEFONE);
                int enderecoIndex = cursor.getColumnIndex(ClientesModel.COLUNA_ENDERECO);
                int numeroIndex = cursor.getColumnIndex(ClientesModel.COLUNA_NUMERO);
                int documentoIndex = cursor.getColumnIndex(ClientesModel.COLUNA_DOCUMENTO);

                if (idIndex != -1) {
                    cliente.setId(cursor.getLong(idIndex));
                }
                if (nomeIndex != -1) {
                    cliente.setNome(cursor.getString(nomeIndex));
                }
                if (emailIndex != -1) {
                    cliente.setEmail(cursor.getString(emailIndex));
                }
                if (telefoneIndex != -1) {
                    cliente.setTelefone(cursor.getString(telefoneIndex));
                }
                if (enderecoIndex != -1) {
                    cliente.setEndereco(cursor.getString(enderecoIndex));
                }
                if (numeroIndex != -1) {
                    cliente.setNumero(cursor.getInt(numeroIndex));
                }
                if (documentoIndex != -1) {
                    cliente.setDocumento(cursor.getString(documentoIndex));
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            Close();
        }

        return cliente;
    }
    public long update(ClientesModel cliente) {
        long result = -1;
        try {
            Open();
            ContentValues values = new ContentValues();
            values.put(ClientesModel.COLUNA_CLIENTE, cliente.getNome());
            values.put(ClientesModel.COLUNA_EMAIL, cliente.getEmail());
            values.put(ClientesModel.COLUNA_TELEFONE, cliente.getTelefone());
            values.put(ClientesModel.COLUNA_ENDERECO, cliente.getEndereco());
            values.put(ClientesModel.COLUNA_NUMERO, cliente.getNumero());
            values.put(ClientesModel.COLUNA_DOCUMENTO, cliente.getDocumento());

            String whereClause = ClientesModel.COLUNA_ID + " = ?";
            String[] whereArgs = { String.valueOf(cliente.getId()) };

            result = db.update(ClientesModel.TABLE_NAME, values, whereClause, whereArgs);
        } finally {
            Close();
        }
        return result;
    }

    public long delete(long id) {
        long result = -1;
        try {
            Open();
            String whereClause = ClientesModel.COLUNA_ID + " = ?";
            String[] whereArgs = { String.valueOf(id) };

            result = db.delete(ClientesModel.TABLE_NAME, whereClause, whereArgs);
        } finally {
            Close();
        }
        return result; // Return the result of the deletion
    }
}
