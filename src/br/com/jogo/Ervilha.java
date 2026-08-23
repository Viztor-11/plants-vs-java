package br.com.jogo;

import javax.swing.Timer;

public class Ervilha extends Planta{
    public static final int CUSTO = 100;
    public static final int VIDA = 300;
    public static final int DANO = 20;
    public static final int RECARGA = 6000;
    public static final int intervaloAtaque = 1500;
    private final PainelJogo painel;

    Timer timerAtaque = new Timer(intervaloAtaque, e -> agir());


   public Ervilha(PainelJogo painel){
       super(CUSTO, VIDA,RECARGA,DANO);
       this.painel = painel;
   }


    @Override
    public void agir() {
        if(painel.temzumbiNaFrente(getLinha(),getColuna())){
            painel.gerarProjetil(getLinha(),getColuna(),DANO);
        }
    }

    @Override
    public void parar() {
        timerAtaque.stop();
    }

    @Override
    public void iniciar() {
        timerAtaque.start();
    }
}
