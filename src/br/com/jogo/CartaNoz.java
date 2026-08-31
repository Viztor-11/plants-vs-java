package br.com.jogo;

public class CartaNoz extends CartaPlanta{
    protected CartaNoz(int x, int y, int largura, int altura) {
        super(x, y, largura, altura, Noz.CUSTO, Noz.RECARGA);
    }

    @Override
    public Planta criarPlanta(PainelJogo painel) {
        return new Noz();
    }
}



