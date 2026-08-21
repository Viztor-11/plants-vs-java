package br.com.jogo;

public class CartaErvilha extends CartaPlanta {

    protected CartaErvilha(int x, int y, int largura, int altura) {
        super(x, y, largura, altura, Ervilha.CUSTO);
    }

    @Override
    public Planta criarPlanta(PainelJogo painel) {
       return new Ervilha(painel);
    }
}