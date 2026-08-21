package br.com.jogo;

public class Sol {
    private double posicaoY;
    private double posicaoX;
    private final int valor = 25;
    private int destinoX;
    private int destinoY;
    private double velocidadeY = -4;
    private double gravidade = 0.2;
    private double velocidadeX;
    private long horaCriacao;

    public Sol(double posicaoX, double posicaoY,int destinoX,int destinoY) {
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.destinoX = destinoX;
        this.destinoY = destinoY;
        velocidadeX =(destinoX - posicaoX) / 40.0;
        horaCriacao = System.currentTimeMillis();

    }



    public int getPosicaoX() {
        return (int)posicaoX;
    }
    public int getPosicaoY(){
        return (int)posicaoY;
    }
    public int getDestinoX(){
        return destinoX;
    }
    public int getDestinoY(){
        return destinoY;
    }
    public int getValor(){
        return valor;
    }

    public void mover(){
        posicaoX = posicaoX + velocidadeX;
        //movimento y
        posicaoY = posicaoY + velocidadeY;
        // gravidade
        velocidadeY = velocidadeY + gravidade;

        if(posicaoY >= destinoY && velocidadeY > 0){
            posicaoY = destinoY;
            velocidadeY = 0;
            gravidade = 0;
            posicaoX = destinoX;
            velocidadeX = 0;
        }
    }

    public boolean tempoFim(){
        long tempoPassado = System.currentTimeMillis() - horaCriacao;

        if (tempoPassado >= 8000){
            return true;
        }
        return false;
    }

}
