package com.example.diogo.database.model;

public class VendasModel {

    // Nome da tabela e colunas
    public static final String TABLE_NAME = "tb_vendas";

    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_CLIENTE_ID = "cliente_id";
    public static final String COLUNA_VINHO_ID = "vinho_id";

    public static final String COLUNA_QUANTIDADE = "quantidade";
    public static final String COLUNA_DATA_VENDA = "data_venda";
    public static final String COLUNA_TOTAL = "total";

    private long id;
    private long clienteId;
    private long vinhoId;
    private int quantidade;
    private String dataVenda;
    private double total;

    // SQL para criar a tabela
    public static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_CLIENTE_ID + " INTEGER NOT NULL, " +
                    COLUNA_VINHO_ID + " INTEGER NOT NULL, " +
                    COLUNA_QUANTIDADE + " INTEGER NOT NULL, " +
                    COLUNA_DATA_VENDA + " TEXT NOT NULL, " +
                    COLUNA_TOTAL + " REAL NOT NULL, " +
                    "FOREIGN KEY(" + COLUNA_CLIENTE_ID + ") REFERENCES " + ClientesModel.TABLE_NAME + "(" + ClientesModel.COLUNA_ID + "), " +
                    "FOREIGN KEY(" + COLUNA_VINHO_ID + ") REFERENCES " + VinhosModel.TABLE_NAME + "(" + VinhosModel.COLUNA_ID + ")" + ");";

    // SQL para deletar a tabela
    public static String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public VendasModel() {
    }

    public VendasModel(long id, long clienteId, long vinhoId, int quantidade, String dataVenda, double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.vinhoId = vinhoId;
        this.quantidade = quantidade;
        this.dataVenda = dataVenda;
        this.total = total;
    }

    // Getters e Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClienteId() {
        return clienteId;
    }

    public void setClienteId(long clienteId) {
        this.clienteId = clienteId;
    }

    public long getVinhoId() {
        return vinhoId;
    }

    public void setVinhoId(long vinhoId) {
        this.vinhoId = vinhoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
