package com.example.diogo.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.example.diogo.database.DBOpenHelper;
import com.example.diogo.database.model.VendasModel;

import java.sql.Date;
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
            values.put(VendasModel.ID_CLIENTE, venda.getIdCliente()); // Insert id_cliente
            values.put(VendasModel.COLUMN_CLIENTE, venda.getCliente());
            values.put(VendasModel.COLUMN_VINHO, venda.getVinho());
            values.put(VendasModel.COLUMN_DATA_VENDA, venda.getDataVenda().toString()); // Converte Date para String
            values.put(VendasModel.COLUMN_QUANTIDADE, venda.getQuantidade());
            values.put(VendasModel.COLUMN_TOTALVENDA, venda.getTotalVenda()); // Insert total_venda

            result = db.insert(VendasModel.TABLE_NAME, null, values);  // Insere no banco
        } finally {
            Close(); // Close the database
        }

        return result;
    }

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

                    int idClienteIndex = cursor.getColumnIndex(VendasModel.ID_CLIENTE); // Get id_cliente
                    if (idClienteIndex != -1) {
                        venda.setIdCliente(cursor.getInt(idClienteIndex)); // Set id_cliente
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
                        String dataVendaStr = cursor.getString(dataVendaIndex);
                        if (dataVendaStr != null) {
                            venda.setDataVenda(Date.valueOf(dataVendaStr)); // Converte e seta a data
                        }
                    }

                    int quantidadeIndex = cursor.getColumnIndex(VendasModel.COLUMN_QUANTIDADE);
                    if (quantidadeIndex != -1) {
                        venda.setQuantidade(cursor.getInt(quantidadeIndex));
                    }

                    int totalVendaIndex = cursor.getColumnIndex(VendasModel.COLUMN_TOTALVENDA); // Get total_venda
                    if (totalVendaIndex != -1) {
                        venda.setTotalVenda(cursor.getDouble(totalVendaIndex)); // Set total_venda
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
                int idClienteIndex = cursor.getColumnIndex(VendasModel.ID_CLIENTE);
                int clienteIndex = cursor.getColumnIndex(VendasModel.COLUMN_CLIENTE);
                int vinhoIndex = cursor.getColumnIndex(VendasModel.COLUMN_VINHO);
                int dataVendaIndex = cursor.getColumnIndex(VendasModel.COLUMN_DATA_VENDA);
                int quantidadeIndex = cursor.getColumnIndex(VendasModel.COLUMN_QUANTIDADE);
                int totalVendaIndex = cursor.getColumnIndex(VendasModel.COLUMN_TOTALVENDA); // Get total_venda

                if (idIndex != -1) {
                    venda.setId(cursor.getInt(idIndex));
                }
                if (idClienteIndex != -1) {
                    venda.setIdCliente(cursor.getInt(idClienteIndex)); // Set id_cliente
                }
                if (clienteIndex != -1) {
                    venda.setCliente(cursor.getString(clienteIndex));
                }
                if (vinhoIndex != -1) {
                    venda.setVinho(cursor.getString(vinhoIndex));
                }
                if (dataVendaIndex != -1) {
                    String dataVendaStr = cursor.getString(dataVendaIndex);
                    if (dataVendaStr != null) {
                        venda.setDataVenda(Date.valueOf(dataVendaStr)); // Converte e seta a data
                    }
                }
                if (quantidadeIndex != -1) {
                    venda.setQuantidade(cursor.getInt(quantidadeIndex));
                }
                if (totalVendaIndex != -1) {
                    venda.setTotalVenda(cursor.getDouble(totalVendaIndex)); // Set total_venda
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

    public int update(VendasModel venda) {
        int rowsAffected = 0;
        try {
            Open(); // Open the database

            ContentValues values = new ContentValues();
            values.put(VendasModel.ID_CLIENTE, venda.getIdCliente()); // Update id_cliente
            values.put(VendasModel.COLUMN_CLIENTE, venda.getCliente());
            values.put(VendasModel.COLUMN_VINHO, venda.getVinho());
            values.put(VendasModel.COLUMN_DATA_VENDA, venda.getDataVenda().toString()); // Converte Date para String
            values.put(VendasModel.COLUMN_QUANTIDADE, venda.getQuantidade());
            values.put(VendasModel.COLUMN_TOTALVENDA, venda.getTotalVenda()); // Update total_venda

            String selection = VendasModel.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(venda.getId()) };

            rowsAffected = db.update(VendasModel.TABLE_NAME, values, selection, selectionArgs);
        } finally {
            Close(); // Close the database
        }

        return rowsAffected;
    }
}
