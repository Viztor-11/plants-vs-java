package br.com.jogo;

public class CortadorGrama {

    private int linha;
    private int posicaoX;
    private boolean ativado = false;
    private boolean usado = false;


    public CortadorGrama(int linha, int posicaoX){
        this.linha = linha;
        this.posicaoX = posicaoX;
    }

    public int getLinha() {
        return linha;
    }

    public int getPosicaoX() {
        return posicaoX;
    }
    public boolean isAtivado(){
        return ativado;
    }
    public boolean isUsado(){
        return usado;
    }
    public void ativar(){
        if(!usado){
            ativado = true;
            usado = true;
        }
    }
    public void mover(){
        if(ativado){
            posicaoX += 8;
        }
    }
    public void finalizarSeSaiu(int larguraTela){
        if(ativado && posicaoX > larguraTela + 20){
            ativado = false;
        }
    }



}
