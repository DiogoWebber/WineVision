package com.example.diogo.database.model;

public class VendasModel {

    public static final String TABLE_NAME = "tb_vendas";

    // Colunas da tabela
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CLIENTE = "cliente";
    public static final String COLUMN_VINHO = "vinho";
    public static final String COLUMN_DATA_VENDA = "data_venda";
    public static final String COLUMN_QUANTIDADE = "quantidade";

    // SQL para criar a tabela
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_CLIENTE + " TEXT," +
                    COLUMN_VINHO + " TEXT," +
                    COLUMN_DATA_VENDA + " TEXT," +
                    COLUMN_QUANTIDADE + " INTEGER)";

    // Atributos
    private int id;
    private String cliente;
    private String vinho;
    private String dataVenda;
    private int quantidade;

    // Construtores
    public VendasModel() {}

    public VendasModel(String cliente, String vinho, String dataVenda, int quantidade) {
        this.cliente = cliente;
        this.vinho = vinho;
        this.dataVenda = dataVenda;
        this.quantidade = quantidade;
    }

    public VendasModel(int id, String cliente, String vinho, String dataVenda, int quantidade) {
        this.id = id;
        this.cliente = cliente;
        this.vinho = vinho;
        this.dataVenda = dataVenda;
        this.quantidade = quantidade;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVinho() {
        return vinho;
    }

    public void setVinho(String vinho) {
        this.vinho = vinho;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
