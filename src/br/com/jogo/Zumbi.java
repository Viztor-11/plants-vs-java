package br.com.jogo;

public class Zumbi {
    private int posicaoX;
    private int linha;
    private int vida;
    private double velocidade;
    public static final int LARGURA = 50;
    public static final int ALTURA = 80;

    public Zumbi(int posicaoX, int linha, double velocidade, int vida){
        this.posicaoX = posicaoX;
        this.linha = linha;
        this.velocidade = velocidade;
        this.vida = vida;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public int getLinha() {
        return linha;
    }

    public int getVida() {
        return vida;
    }

    public double getVelocidade() {
        return velocidade;
    }

    public void receberDano(int dano){
        vida -= dano;

    }

    public void mover(){
        posicaoX -= velocidade;
    }
}
