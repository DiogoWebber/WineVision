package com.example.diogo.database.model;

public class VinhosModel {

    // Nome da tabela e colunas
    public static final String TABLE_NAME = "tb_vinhos";

    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_TIPO = "tipo";
    public static final String COLUNA_SAFRA = "safra";
    public static final String COLUNA_PAIS_ORIGEM = "pais_origem";
    public static final String COLUNA_GRADUACAO_ALCOOLICA = "graduacao_alcoolica";
    public static final String COLUNA_VOLUME = "volume";
    public static final String COLUNA_ESTOQUE = "estoque";
    public static final String COLUNA_PRECO = "preco";

    private long id;
    private String nome;
    private String tipo;
    private int safra;
    private String paisOrigem;
    private double graduacaoAlcoolica;
    private double volume;
    private int estoque;
    private double preco;

    public static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_NOME + " TEXT NOT NULL, " +
                    COLUNA_TIPO + " TEXT NOT NULL, " +
                    COLUNA_SAFRA + " INTEGER NOT NULL, " +
                    COLUNA_PAIS_ORIGEM + " TEXT NOT NULL, " +
                    COLUNA_GRADUACAO_ALCOOLICA + " REAL NOT NULL, " +
                    COLUNA_VOLUME + " REAL NOT NULL, " +
                    COLUNA_ESTOQUE + " INTEGER NOT NULL, " +
                    COLUNA_PRECO + " REAL NOT NULL" + ");";

    public static String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public VinhosModel() {
    }

    public VinhosModel(long id, String nome, String tipo, int safra, double preco) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.safra = safra;
        this.preco = preco;
        // Set default values for other fields if necessary
        this.paisOrigem = ""; // or any default value
        this.graduacaoAlcoolica = 0.0; // or any default value
        this.volume = 0.0; // or any default value
        this.estoque = 0; // or any default value
    }

    // Getters e Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getSafra() {
        return safra;
    }

    public void setSafra(int safra) {
        this.safra = safra;
    }

    public String getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(String paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

    public double getGraduacaoAlcoolica() {
        return graduacaoAlcoolica;
    }

    public void setGraduacaoAlcoolica(double graduacaoAlcoolica) {
        this.graduacaoAlcoolica = graduacaoAlcoolica;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
