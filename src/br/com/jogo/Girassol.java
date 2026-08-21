package br.com.jogo;

import javax.swing.Timer;

public class Girassol extends Planta{
    Timer t = new Timer(24000, e -> agir());
    long horaCriacao;

    public static final int CUSTO = 50;
    public static final int RECARGA = 6000;



    public Girassol(PainelJogo painel){
        super(CUSTO,300,RECARGA,0);
        t.setInitialDelay(7000);
        t.setDelay(24000);
        this.painel = painel;
    }

    @Override
    public void iniciar(){
        horaCriacao = System.currentTimeMillis();
        t.start();
    }


    @Override
    public void agir() {
        System.out.println("Girassol gerou sol!");
        System.out.println((System.currentTimeMillis()-horaCriacao)/1000);
        painel.gerarSol(getLinha(),getColuna());
    }

    private PainelJogo painel;
}
