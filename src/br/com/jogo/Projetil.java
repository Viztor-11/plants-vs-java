package br.com.jogo;

public class Projetil {
    private int posicaoX;
    private int posicaoY;
    private int velocidade;
    private int dano;

    public Projetil(int posicaoX, int posicaoY, int velocidade, int dano){
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.velocidade = velocidade;
        this.dano = dano;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getDano() {
        return dano;
    }

    public void mover(){
       posicaoX = posicaoX + velocidade;
    }


}
