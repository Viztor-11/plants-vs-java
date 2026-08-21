package br.com.jogo;

import java.awt.*;

public abstract class CartaPlanta {
    private int x;
    private int y;
    private int largura;
    private int altura;
    private int custo;

    public boolean contem(int mouseX, int mouseY){
        Rectangle areaCarta = new Rectangle(x,y,largura,altura);
        return areaCarta.contains(mouseX,mouseY);

    }

    public int getCusto(){
        return custo;
    }
    protected CartaPlanta(int x, int y, int largura, int altura, int custo){

        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.custo = custo;


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





    public abstract Planta criarPlanta(PainelJogo painel);
}
