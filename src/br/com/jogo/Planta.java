package br.com.jogo;

public abstract class Planta{
    private int custoSol;
    protected int vida;
    private int recarga;
    private int linha,coluna;
    private int dano;





    protected Planta(int custoSol, int vida, int recarga, int dano) {
        this.custoSol = custoSol;
        this.vida = vida;
        this.recarga = recarga;
        this.dano = dano;

    }
    public abstract void agir();

    public void definirPosicao(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public abstract void iniciar();

    public int getCustoSol(){
        return custoSol;
    }

    public void receberDano(int dano){
        vida -= dano;
    }

    public int getVida() {
        return vida;
    }
}



