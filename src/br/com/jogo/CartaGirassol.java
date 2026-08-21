package br.com.jogo;

public class CartaGirassol extends CartaPlanta{

    protected CartaGirassol(int x, int y, int largura, int altura) {
        super(x, y, largura, altura, Girassol.CUSTO);
    }

    @Override
    public Planta criarPlanta(PainelJogo painel) {
        return new Girassol(painel);
    }
}


