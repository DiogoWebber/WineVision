package com.example.diogo.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.diogo.database.DBOpenHelper;
import com.example.diogo.database.model.VendasModel;

import java.util.ArrayList;
import java.util.List;

public class VendaDAO extends AbstrataDAO {

    public VendaDAO(Context context) {
        helper = new DBOpenHelper(context);
    }

    // Inserir uma venda no banco
    public long insert(VendasModel venda) {
        long result = -1;
        try {
            Open();
            ContentValues values = new ContentValues();
            values.put(VendasModel.COLUNA_CLIENTE_ID, venda.getClienteId());
            values.put(VendasModel.COLUNA_VINHO_ID, venda.getVinhoId());
            values.put(VendasModel.COLUNA_QUANTIDADE, venda.getQuantidade());
            values.put(VendasModel.COLUNA_TOTAL, venda.getTotal());
            values.put(VendasModel.COLUNA_DATA_VENDA, venda.getDataVenda());

            result = db.insert(VendasModel.TABLE_NAME, null, values);
        } finally {
            Close();
        }

        return result;
    }

    // Listar todas as vendas
    public List<VendasModel> getAll() {
        List<VendasModel> vendaList = new ArrayList<>();
        Cursor cursor = null;

        try {
            Open();
            cursor = db.query(VendasModel.TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    VendasModel venda = new VendasModel();

                    int idIndex = cursor.getColumnIndex(VendasModel.COLUNA_ID);
                    int clienteIdIndex = cursor.getColumnIndex(VendasModel.COLUNA_CLIENTE_ID);
                    int vinhoIdIndex = cursor.getColumnIndex(VendasModel.COLUNA_VINHO_ID);
                    int quantidadeIndex = cursor.getColumnIndex(VendasModel.COLUNA_QUANTIDADE);
                    int precoTotalIndex = cursor.getColumnIndex(VendasModel.COLUNA_TOTAL);
                    int dataVendaIndex = cursor.getColumnIndex(VendasModel.COLUNA_DATA_VENDA);

                    venda.setId(cursor.getLong(idIndex));
                    venda.setClienteId(cursor.getLong(clienteIdIndex));
                    venda.setVinhoId(cursor.getLong(vinhoIdIndex));
                    venda.setQuantidade(cursor.getInt(quantidadeIndex));
                    venda.setTotal(cursor.getDouble(precoTotalIndex));
                    venda.setDataVenda(cursor.getString(dataVendaIndex));

                    vendaList.add(venda);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            Close();
        }

        return vendaList;
    }

    public long delete(long id) {
        long result = -1;
        try {
            Open();
            String whereClause = VendasModel.COLUNA_ID + " = ?";
            String[] whereArgs = { String.valueOf(id) };

            result = db.delete(VendasModel.TABLE_NAME, whereClause, whereArgs);
        } finally {
            Close();
        }

        return result;
    }
}
