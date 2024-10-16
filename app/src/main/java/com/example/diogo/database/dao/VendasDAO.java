package com.example.diogo.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.example.diogo.database.DBOpenHelper;
import com.example.diogo.database.model.VendasModel;
import java.util.ArrayList;
import java.util.List;

public class VendasDAO extends AbstrataDAO {

    public VendasDAO(Context context) {
        helper = new DBOpenHelper(context);
    }

    // Método para inserir uma nova venda
    public long insert(VendasModel venda) {
        long result = -1;
        try {
            Open(); // Open the database

            ContentValues values = new ContentValues();
            values.put(VendasModel.COLUMN_CLIENTE, venda.getCliente());
            values.put(VendasModel.COLUMN_VINHO, venda.getVinho());
            values.put(VendasModel.COLUMN_DATA_VENDA, venda.getDataVenda());
            values.put(VendasModel.COLUMN_QUANTIDADE, venda.getQuantidade());

            result = db.insert(VendasModel.TABLE_NAME, null, values);  // Insere no banco
        } finally {
            Close(); // Close the database
        }

        return result;
    }

    // Método para obter todas as vendas
    public List<VendasModel> getAll() {
        List<VendasModel> vendasList = new ArrayList<>();
        Cursor cursor = null;

        try {
            Open(); // Open the database

            // Consulta para selecionar todos os registros da tabela Vendas
            cursor = db.query(VendasModel.TABLE_NAME, null, null, null, null, null, null);

            // Itera sobre o cursor para criar uma lista de VendasModel
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    VendasModel venda = new VendasModel();

                    // Acessando os campos da venda
                    int idIndex = cursor.getColumnIndex(VendasModel.COLUMN_ID);
                    if (idIndex != -1) {
                        venda.setId(cursor.getInt(idIndex));
                    }

                    int clienteIndex = cursor.getColumnIndex(VendasModel.COLUMN_CLIENTE);
                    if (clienteIndex != -1) {
                        venda.setCliente(cursor.getString(clienteIndex));
                    }

                    int vinhoIndex = cursor.getColumnIndex(VendasModel.COLUMN_VINHO);
                    if (vinhoIndex != -1) {
                        venda.setVinho(cursor.getString(vinhoIndex));
                    }

                    int dataVendaIndex = cursor.getColumnIndex(VendasModel.COLUMN_DATA_VENDA);
                    if (dataVendaIndex != -1) {
                        venda.setDataVenda(cursor.getString(dataVendaIndex));
                    }

                    int quantidadeIndex = cursor.getColumnIndex(VendasModel.COLUMN_QUANTIDADE);
                    if (quantidadeIndex != -1) {
                        venda.setQuantidade(cursor.getInt(quantidadeIndex));
                    }

                    vendasList.add(venda);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            Close(); // Close the database
        }

        return vendasList;
    }
    // New method to update wine stock
    public boolean updateVinhoEstoque(String nomeVinho, int quantidadeVendida) {
        String sql = "UPDATE tb_vinhos SET estoque = estoque - ? WHERE nome = ?";
        db.execSQL(sql, new Object[]{quantidadeVendida, nomeVinho});
        // You may want to check the affected rows if this update is critical
        return true; // Update was executed
    }


    // Método para buscar venda por ID
    public VendasModel getById(int id) {
        VendasModel venda = null;
        Cursor cursor = null;

        try {
            Open(); // Open the database
            String selection = VendasModel.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(id) };

            cursor = db.query(VendasModel.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                venda = new VendasModel();

                // Acessa os campos conforme o ID da venda
                int idIndex = cursor.getColumnIndex(VendasModel.COLUMN_ID);
                int clienteIndex = cursor.getColumnIndex(VendasModel.COLUMN_CLIENTE);
                int vinhoIndex = cursor.getColumnIndex(VendasModel.COLUMN_VINHO);
                int dataVendaIndex = cursor.getColumnIndex(VendasModel.COLUMN_DATA_VENDA);
                int quantidadeIndex = cursor.getColumnIndex(VendasModel.COLUMN_QUANTIDADE);

                if (idIndex != -1) {
                    venda.setId(cursor.getInt(idIndex));
                }
                if (clienteIndex != -1) {
                    venda.setCliente(cursor.getString(clienteIndex));
                }
                if (vinhoIndex != -1) {
                    venda.setVinho(cursor.getString(vinhoIndex));
                }
                if (dataVendaIndex != -1) {
                    venda.setDataVenda(cursor.getString(dataVendaIndex));
                }
                if (quantidadeIndex != -1) {
                    venda.setQuantidade(cursor.getInt(quantidadeIndex));
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            Close(); // Close the database
        }

        return venda;
    }

    // Método para atualizar uma venda existente
    public long update(VendasModel venda) {
        long result = -1;
        try {
            Open(); // Open the database

            ContentValues values = new ContentValues();
            values.put(VendasModel.COLUMN_CLIENTE, venda.getCliente());
            values.put(VendasModel.COLUMN_VINHO, venda.getVinho());
            values.put(VendasModel.COLUMN_DATA_VENDA, venda.getDataVenda());
            values.put(VendasModel.COLUMN_QUANTIDADE, venda.getQuantidade());

            String selection = VendasModel.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(venda.getId()) };

            result = db.update(VendasModel.TABLE_NAME, values, selection, selectionArgs);
        } finally {
            Close(); // Close the database
        }
        return result; // Returns the number of rows affected
    }


    // Método para deletar uma venda pelo ID
    public long delete(int id) {
        long result = -1;
        try {
            Open(); // Open the database
            String whereClause = VendasModel.COLUMN_ID + " = ?";
            String[] whereArgs = { String.valueOf(id) };

            result = db.delete(VendasModel.TABLE_NAME, whereClause, whereArgs);
        } finally {
            Close(); // Close the database
        }
        return result;
    }
}
