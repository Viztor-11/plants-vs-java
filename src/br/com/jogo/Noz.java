package br.com.jogo;

public class Noz extends Planta{
    public static final int CUSTO = 50;
    public static final int VIDA = 4000;
    public static final int RECARGA = 30000;

    public  Noz(){
        super(CUSTO, VIDA, RECARGA, 0);
    }

    public double getPocentagemVida(){
        return (double) getVida() / VIDA;
    }


    @Override
    public void agir() {

    }

    @Override
    public void parar() {

    }

    @Override
    public void iniciar() {

    }
}
