package com.example.diogo.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.diogo.database.DBOpenHelper;
import com.example.diogo.database.model.ClientesModel;
import com.example.diogo.database.model.VendasModel;
import com.example.diogo.database.model.VinhosModel;

import java.util.ArrayList;
import java.util.List;

public class VinhoDAO extends AbstrataDAO {

    public VinhoDAO(Context context) {
        helper = new DBOpenHelper(context);
    }

    public long insert(VinhosModel vinho) {
        long result = -1;
        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(VinhosModel.COLUNA_NOME, vinho.getNome());
            values.put(VinhosModel.COLUNA_TIPO, vinho.getTipo());
            values.put(VinhosModel.COLUNA_SAFRA, vinho.getSafra());
            values.put(VinhosModel.COLUNA_PAIS_ORIGEM, vinho.getPaisOrigem());
            values.put(VinhosModel.COLUNA_GRADUACAO_ALCOOLICA, vinho.getGraduacaoAlcoolica());
            values.put(VinhosModel.COLUNA_VOLUME, vinho.getVolume());
            values.put(VinhosModel.COLUNA_ESTOQUE, vinho.getEstoque());
            values.put(VinhosModel.COLUNA_PRECO, vinho.getPreco());

            result = db.insert(VinhosModel.TABLE_NAME, null, values);  // Insere no banco
        } finally {
            Close();
        }

        return result;
    }

    // Método para buscar todos os vinhos
    public List<VinhosModel> getAll() {
        List<VinhosModel> vinhoList = new ArrayList<>();
        Cursor cursor = null;

        try {
            Open();

            // Consulta para selecionar todos os registros da tabela Vinhos
            cursor = db.query(VinhosModel.TABLE_NAME, null, null, null, null, null, null);

            // Itera sobre o cursor para criar uma lista de VinhoModel
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    VinhosModel vinho = new VinhosModel();

                    // Acessando os campos do vinho
                    int idIndex = cursor.getColumnIndex(VinhosModel.COLUNA_ID);
                    if (idIndex != -1) {
                        vinho.setId(cursor.getLong(idIndex));
                    }

                    int nomeIndex = cursor.getColumnIndex(VinhosModel.COLUNA_NOME);
                    if (nomeIndex != -1) {
                        vinho.setNome(cursor.getString(nomeIndex));
                    }

                    int tipoIndex = cursor.getColumnIndex(VinhosModel.COLUNA_TIPO);
                    if (tipoIndex != -1) {
                        vinho.setTipo(cursor.getString(tipoIndex));
                    }

                    int safraIndex = cursor.getColumnIndex(VinhosModel.COLUNA_SAFRA);
                    if (safraIndex != -1) {
                        vinho.setSafra(cursor.getInt(safraIndex));
                    }

                    int paisOrigemIndex = cursor.getColumnIndex(VinhosModel.COLUNA_PAIS_ORIGEM);
                    if (paisOrigemIndex != -1) {
                        vinho.setPaisOrigem(cursor.getString(paisOrigemIndex));
                    }

                    int graduacaoAlcoolicaIndex = cursor.getColumnIndex(VinhosModel.COLUNA_GRADUACAO_ALCOOLICA);
                    if (graduacaoAlcoolicaIndex != -1) {
                        vinho.setGraduacaoAlcoolica(cursor.getDouble(graduacaoAlcoolicaIndex));
                    }

                    int volumeIndex = cursor.getColumnIndex(VinhosModel.COLUNA_VOLUME);
                    if (volumeIndex != -1) {
                        vinho.setVolume(cursor.getDouble(volumeIndex));
                    }

                    int estoqueIndex = cursor.getColumnIndex(VinhosModel.COLUNA_ESTOQUE);
                    if (estoqueIndex != -1) {
                        vinho.setEstoque(cursor.getInt(estoqueIndex));
                    }

                    int precoIndex = cursor.getColumnIndex(VinhosModel.COLUNA_PRECO);
                    if (precoIndex != -1) {
                        vinho.setPreco(cursor.getDouble(precoIndex));
                    }

                    vinhoList.add(vinho);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            Close();
        }

        return vinhoList;
    }

    // Método para buscar vinho por ID
    public VinhosModel getById(long id) {
        VinhosModel vinho = null;
        Cursor cursor = null;

        try {
            Open();
            String selection = VinhosModel.COLUNA_ID + " = ?";
            String[] selectionArgs = { String.valueOf(id) };

            cursor = db.query(VinhosModel.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                vinho = new VinhosModel();

                // Acessa os campos conforme o ID do vinho
                int idIndex = cursor.getColumnIndex(VinhosModel.COLUNA_ID);
                int nomeIndex = cursor.getColumnIndex(VinhosModel.COLUNA_NOME);
                int tipoIndex = cursor.getColumnIndex(VinhosModel.COLUNA_TIPO);
                int safraIndex = cursor.getColumnIndex(VinhosModel.COLUNA_SAFRA);
                int paisOrigemIndex = cursor.getColumnIndex(VinhosModel.COLUNA_PAIS_ORIGEM);
                int graduacaoAlcoolicaIndex = cursor.getColumnIndex(VinhosModel.COLUNA_GRADUACAO_ALCOOLICA);
                int volumeIndex = cursor.getColumnIndex(VinhosModel.COLUNA_VOLUME);
                int estoqueIndex = cursor.getColumnIndex(VinhosModel.COLUNA_ESTOQUE);
                int precoIndex = cursor.getColumnIndex(VinhosModel.COLUNA_PRECO);

                if (idIndex != -1) {
                    vinho.setId(cursor.getLong(idIndex));
                }
                if (nomeIndex != -1) {
                    vinho.setNome(cursor.getString(nomeIndex));
                }
                if (tipoIndex != -1) {
                    vinho.setTipo(cursor.getString(tipoIndex));
                }
                if (safraIndex != -1) {
                    vinho.setSafra(cursor.getInt(safraIndex));
                }
                if (paisOrigemIndex != -1) {
                    vinho.setPaisOrigem(cursor.getString(paisOrigemIndex));
                }
                if (graduacaoAlcoolicaIndex != -1) {
                    vinho.setGraduacaoAlcoolica(cursor.getDouble(graduacaoAlcoolicaIndex));
                }
                if (volumeIndex != -1) {
                    vinho.setVolume(cursor.getDouble(volumeIndex));
                }
                if (estoqueIndex != -1) {
                    vinho.setEstoque(cursor.getInt(estoqueIndex));
                }
                if (precoIndex != -1) {
                    vinho.setPreco(cursor.getDouble(precoIndex));
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            Close();
        }

        return vinho;
    }

    public long update(VinhosModel vinho) {
        long result = -1;
        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(VinhosModel.COLUNA_NOME, vinho.getNome());
            values.put(VinhosModel.COLUNA_TIPO, vinho.getTipo());
            values.put(VinhosModel.COLUNA_SAFRA, vinho.getSafra());
            values.put(VinhosModel.COLUNA_PAIS_ORIGEM, vinho.getPaisOrigem());
            values.put(VinhosModel.COLUNA_GRADUACAO_ALCOOLICA, vinho.getGraduacaoAlcoolica());
            values.put(VinhosModel.COLUNA_VOLUME, vinho.getVolume());
            values.put(VinhosModel.COLUNA_ESTOQUE, vinho.getEstoque());
            values.put(VinhosModel.COLUNA_PRECO, vinho.getPreco());

            String whereClause = VinhosModel.COLUNA_ID + " = ?";
            String[] whereArgs = { String.valueOf(vinho.getId()) };

            // A chamada a db.update deve retornar o número de linhas afetadas
            result = db.update(VinhosModel.TABLE_NAME, values, whereClause, whereArgs);
        } finally {
            Close();
        }

        return result; // Certifique-se de retornar o número de linhas afetadas
    }

    public long updateVinho(VinhosModel vinho) {
        long result = -1;
        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(VinhosModel.COLUNA_NOME, vinho.getNome());
            values.put(VinhosModel.COLUNA_TIPO, vinho.getTipo());
            values.put(VinhosModel.COLUNA_SAFRA, vinho.getSafra());
            values.put(VinhosModel.COLUNA_PAIS_ORIGEM, vinho.getPaisOrigem());
            values.put(VinhosModel.COLUNA_GRADUACAO_ALCOOLICA, vinho.getGraduacaoAlcoolica());
            values.put(VinhosModel.COLUNA_VOLUME, vinho.getVolume());
            values.put(VinhosModel.COLUNA_ESTOQUE, vinho.getEstoque());
            values.put(VinhosModel.COLUNA_PRECO, vinho.getPreco());

            String whereClause = VinhosModel.COLUNA_ID + " = ?";
            String[] whereArgs = { String.valueOf(vinho.getId()) }; // Certifique-se de que o ID esteja presente

            result = db.update(VinhosModel.TABLE_NAME, values, whereClause, whereArgs);
        } finally {
            Close();
        }

        return result;
    }



    public long delete(long id) {
        long result = -1;
        try {
            Open();
            String whereClause = VinhosModel.COLUNA_ID + " = ?";
            String[] whereArgs = { String.valueOf(id) };

            result = db.delete(VinhosModel.TABLE_NAME, whereClause, whereArgs);
        } finally {
            Close();
        }
        return result;
    }

}