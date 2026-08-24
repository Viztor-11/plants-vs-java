package br.com.jogo;

import java.awt.*;

public abstract class CartaPlanta {
    private int x;
    private int y;
    private int largura;
    private int altura;
    private int custo;
    private int recarga;
    private long ultimoUso = 0;

    public boolean contem(int mouseX, int mouseY){
        Rectangle areaCarta = new Rectangle(x,y,largura,altura);
        return areaCarta.contains(mouseX,mouseY);

    }

    public int getCusto(){
        return custo;
    }
    protected CartaPlanta(int x, int y, int largura, int altura, int custo, int recarga){

        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.custo = custo;
        this.recarga = recarga;


    }
    public int getX(){
        return x;
    }
    public int getY() {
        return y;
    }
    public int getLargura(){
        return largura;
    }
    public int getAltura(){
        return altura;
    }

    public boolean estaEmRecarga(){
        long tempoAtual = System.currentTimeMillis();
        long tempoPassado = tempoAtual - ultimoUso;

        return tempoPassado < recarga;
    }

    public void iniciarRecarga(){
        ultimoUso = System.currentTimeMillis();
    }

    public long getTempoRestanteRecarga(){
        long tempoPassado = System.currentTimeMillis() - ultimoUso;
        long restante = recarga - tempoPassado;

        if(restante < 0){
            return  0;
        }
        return  restante;
    }

    public double getProgressoRecarga(){
        long tempoPassado = System.currentTimeMillis() - ultimoUso;
        double progresso = (double) tempoPassado / recarga;

        if(progresso > 1.0){
            progresso = 1.0;
        }
        return progresso;
    }
    public boolean podeComprar(int quantidadeSol){
        return quantidadeSol >= custo;
    }




    public abstract Planta criarPlanta(PainelJogo painel);
}
