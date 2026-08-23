package br.com.jogo;

public class Zumbi {
    private int posicaoX;
    private int linha;
    private int vida;
    private double velocidade;
    public static final int LARGURA = 50;
    public static final int ALTURA = 80;
    private int dano = 80;
    private long ultimoAtaque;
    private int intervaloAtaque = 1000;
    public static  final int VALOR_PONTOS = 1;
    public static final int VIDA_NORMAL = 270;


    public Zumbi(int posicaoX, int linha, double velocidade, int vida){
        this.posicaoX = posicaoX;
        this.linha = linha;
        this.velocidade = velocidade;
        this.vida = vida;
    }

    public boolean podeAtacar(){
        if(System.currentTimeMillis() - ultimoAtaque >= intervaloAtaque){
            return true;
        }
        return false;
    }

    public void registrarAtaque(){
        ultimoAtaque = System.currentTimeMillis();
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

    public int getDano(){return dano;}

    public void receberDano(int dano){
        vida -= dano;

    }

    public void mover(){
        posicaoX -= velocidade;
    }
}
