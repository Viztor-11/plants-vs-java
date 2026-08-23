package br.com.jogo;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PainelJogo extends JPanel{

    private int contadorOndaInicial;
    private int vidaParaProximaOnda = 0;
    private int vidaZumbisInicioOnda = 0;
    private int indiceZumbiOnda = 0;
    private final int linhas = 5;
    private final int colunas = 9;
    private final int TamanhoCelula = 110;
    private final int TamanhoSol = 50;
    private Planta plantaArrastada = null;
    private int mouseX, mouseY;
    private List<Planta> plantas = new ArrayList<>();
    private int alturaBarraSuperior = 100;
    private List<Sol> sols = new ArrayList<>();
    private int contadorSol = 750000;
    private List<CartaPlanta> cards = new ArrayList<>();
    private Random random = new Random();
    private Timer timerAnimacao;
    private List<Projetil> projeteis = new ArrayList<>();
    private List<Zumbi> zumbis = new ArrayList<>();
    private boolean gameOver = false;
    private Timer timerSpawnZumbi;
    private int zumbisGerados = 0;
    private int ondaAtual = 0;
    private final int TOTAL_ONDAS = 10;
    private List<TipoZumbi> zumbisOnda = new ArrayList<>();
    private int contadorOnda;
    private static final int CONTAGEM_PRIMEIRA_ONDA = 1800;
    private Timer timerOnda;
    private static final int CONTAGEM_ONDA_NORMAL = 2500;
    private static final int VARIACAO_CONTAGEM_ONDA = 600;
    private int ondaEmCampo = 0;
    private int vidaOndaPreparada = 0;

    public PainelJogo(){



        cards.add(new CartaGirassol(10,10,80,80));
        cards.add(new CartaErvilha(100,10,80,80));


        timerAnimacao = new Timer(16, e -> {
            for(Sol s : sols){
                s.mover();

            }
            for(int i = sols.size() - 1; i >= 0;i--){
                Sol s = sols.get(i);
                if(s.tempoFim()){
                    sols.remove(i);
                }
            }
            for(Projetil p : projeteis){
                p.mover();
            }
            for(Zumbi z : zumbis) {
                Planta plantaAlvo = pegarPlantaEncostada(z);

                if (plantaAlvo == null) {
                    z.mover();
                } else {
                    if (z.podeAtacar()) {
                        plantaAlvo.receberDano(z.getDano());
                        z.registrarAtaque();
                    }

                    if(plantaAlvo.getVida() <= 0){
                        plantaAlvo.parar();
                        plantas.remove(plantaAlvo);
                    }
                }
                if(zumbiChegouCasa(z)){
                    gameOver = true;
                    timerAnimacao.stop();
                    timerSpawnZumbi.stop();


                    for(Planta p : plantas){
                        p.parar();
                    }
                }
            }
            for(int i = projeteis.size() - 1; i >=0; i--){
                Projetil p = projeteis.get(i);

                if(p.getPosicaoX() > colunas * TamanhoCelula){
                    projeteis.remove(i);
                }
            }
            for(int i = projeteis.size() - 1; i >= 0; i--){

                Projetil p = projeteis.get(i);

                for(int j = zumbis.size() - 1; j >= 0; j--){

                    Zumbi z = zumbis.get(j);

                    Rectangle areaProjetil = new Rectangle(p.getPosicaoX() -10,
                            p.getPosicaoY() -10, 20,20);
                    int yZumbi = z.getLinha() * TamanhoCelula
                            + TamanhoCelula / 2
                            + alturaBarraSuperior;

                    Rectangle areaZumbi = new Rectangle(z.getPosicaoX() - Zumbi.LARGURA /2,
                            yZumbi - Zumbi.ALTURA /2, Zumbi.LARGURA,Zumbi.ALTURA);


                    if(areaProjetil.intersects(areaZumbi)){

                        z.receberDano(p.getDano());

                        projeteis.remove(i);

                        if(z.getVida() <= 0){
                            zumbis.remove(j);
                        }

                        break;
                    }
                }
            }



            repaint();
        });
        timerAnimacao.start();

        contadorOnda = CONTAGEM_PRIMEIRA_ONDA;
        contadorOndaInicial = CONTAGEM_PRIMEIRA_ONDA;

        timerOnda = new Timer(10, e->{
            if(contadorOnda > 0){
                contadorOnda--;
            }
            if(contadorOnda == 0){
                timerOnda.stop();
                gerarOndaAtual();
                prepararProximaContagem();
                iniciarProximaOnda();
            }
        });
        timerOnda.start();

        // ONDA DE ZUMBIS

        timerSpawnZumbi = new Timer(3000, e -> {
            gerarProximoZumbi();

            if(todosZumbisEnviados()){
                timerSpawnZumbi.stop();
            }
        });

        iniciarProximaOnda();


        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e)  {
                if(gameOver){
                    return;
                }

                int x = e.getX();
                int y = e.getY();
                for(CartaPlanta carta : cards){
                    if(carta.contem(x,y) && contadorSol >= carta.getCusto()){
                        plantaArrastada = carta.criarPlanta(PainelJogo.this);
                        break;
                    }
                }

                for(int i = 0; i < sols.size();i++) {
                    Sol s = sols.get(i);

                    Rectangle areaSol = new Rectangle(s.getPosicaoX() - TamanhoSol / 2,  (s.getPosicaoY() -  TamanhoSol / 2),
                            TamanhoSol, TamanhoSol);

                    if (areaSol.contains(x, y)) {
                        contadorSol += s.getValor();
                        sols.remove(i);
                        repaint();
                        break;
                    }
                }


            }
            @Override
            public void mouseDragged(MouseEvent e){
                if(gameOver){
                    return;
                }

                if (plantaArrastada != null){
                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint();
                }
            }


            @Override
            public void mouseReleased(MouseEvent e){
                if(gameOver){
                    return;
                }

                if (plantaArrastada != null){
                    int x = e.getX();
                    int y = e.getY();
                    int coluna = x / TamanhoCelula;
                    int linha = (y - alturaBarraSuperior)/TamanhoCelula;


                    if(posicaoValida(x,y)){
                        plantaArrastada.definirPosicao(linha,coluna);
                        plantas.add(plantaArrastada);
                        plantaArrastada.iniciar();
                        contadorSol -= plantaArrastada.getCustoSol();

                    }
                    plantaArrastada = null;
                    repaint();
                }
            }
        };
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);


    }

    public int calcularContagemOndaNormal(){
        return CONTAGEM_ONDA_NORMAL + random.nextInt(VARIACAO_CONTAGEM_ONDA);
    }

    public void prepararProximaContagem(){
        contadorOnda = calcularContagemOndaNormal();
        contadorOndaInicial = contadorOnda;
    }

    public void gerarOndaAtual(){
        ondaEmCampo = ondaAtual;
        vidaZumbisInicioOnda = vidaOndaPreparada;
        for(TipoZumbi tipo : zumbisOnda){
            gerarZumbi(tipo);
        }
    }


    public void montarOndaAtual(){
        zumbisOnda.clear();
        indiceZumbiOnda = 0;

        int pontosRestatantes = calcularPontosOnda();

        while(pontosRestatantes >= Zumbi.VALOR_PONTOS){
            zumbisOnda.add(TipoZumbi.NORMAL);

            pontosRestatantes -= Zumbi.VALOR_PONTOS;
        }
        vidaOndaPreparada = calcularVidaInicialOnda();
        vidaParaProximaOnda = vidaZumbisInicioOnda / 2;


    }

    public void iniciarProximaOnda(){
        if(ondaAtual < TOTAL_ONDAS){
            proximaOnda();

            montarOndaAtual();

        }
    }

    public boolean todosZumbisEnviados(){
        return indiceZumbiOnda >= zumbisOnda.size();
    }

    public int calcularVidaInicialOnda() {
        int vidaTotal = 0;

        for (TipoZumbi tipo : zumbisOnda) {
            if(tipo == TipoZumbi.NORMAL){
                vidaTotal += Zumbi.VIDA_NORMAL;
            }
        }
        return vidaTotal;
    }

    public int calcularVidaZumbis(){
        int vidaAtual = 0;
        for(Zumbi z : zumbis){
            if(z.getOndaOrigem() == ondaEmCampo) {
                vidaAtual += z.getVida();
            }
        }
        return vidaAtual;
    }



    public boolean ehOndaBandeirann(){
        return ondaAtual > 0 && ondaAtual % 10 == 0;
    }

    public void proximaOnda(){
        ondaAtual++;
    }

    public int calcularPontosOnda(){

        int indiceOnda = ondaAtual -1;
        int pontos = indiceOnda /3 + 1;
        if(ehOndaBandeirann()){
            pontos =(int)(pontos * 2.5);
        }
        return pontos;
    }


    public boolean zumbiEncostou(Zumbi z){
        for (Planta p : plantas){

            int iniciaCelula = p.getColuna() * TamanhoCelula;
            int fimCelula = iniciaCelula + TamanhoCelula;

            if(p.getLinha() == z.getLinha() && z.getPosicaoX() > iniciaCelula && z.getPosicaoX() < fimCelula ){
                return true;
            }
        }
        return false;
    }

    public boolean zumbiChegouCasa(Zumbi z){
        if(z.getPosicaoX() <=0 ){
            return true;
        }
        return false;
    }


    public Planta pegarPlantaEncostada(Zumbi z){

        for(Planta p : plantas){
            int centroXPlanta = p.getColuna() * TamanhoCelula + TamanhoCelula /2;
            int bordaDireita = centroXPlanta + 25;
            int bordaEsquerdaZumbi = z.getPosicaoX() - Zumbi.LARGURA / 2;
            if(p.getLinha() == z.getLinha()
            && z.getPosicaoX() > centroXPlanta
            && bordaEsquerdaZumbi <= bordaDireita){
                return p;
            }
        }
        return null;
    }

    public void gerarZumbi(TipoZumbi tipo){

        int linhaAleatoria = random.nextInt(linhas);
        int xInicial = colunas* TamanhoCelula + Zumbi.LARGURA / 2;
        if(tipo == TipoZumbi.NORMAL){
            zumbis.add(new Zumbi(xInicial,linhaAleatoria, 5 ,270,ondaAtual));
        }


    }

    public void gerarProximoZumbi(){
        if(indiceZumbiOnda < zumbisOnda.size()){

            TipoZumbi tipo = zumbisOnda.get(indiceZumbiOnda);
            gerarZumbi(tipo);
            indiceZumbiOnda++;
        }
    }





    public void gerarSol(int linha, int coluna) {
        int centroX = coluna * TamanhoCelula + TamanhoCelula/2;
        int centroY= linha * TamanhoCelula + TamanhoCelula/2 + alturaBarraSuperior;

        int destinoX = centroX + random.nextInt(81) - 40;
        int destinoY = centroY + random.nextInt(61) - 30;
        int minimoX  = TamanhoSol /2;
        int maximoX  = colunas * TamanhoCelula - TamanhoSol /2;
        int minimoY  = alturaBarraSuperior + TamanhoSol/2;
        int maximoY  = alturaBarraSuperior + linhas * TamanhoCelula - TamanhoSol/2;


        if(destinoX < minimoX){
            destinoX = minimoX;
        }if(destinoX > maximoX){
            destinoX = maximoX;
        }
        if(destinoY < minimoY){
            destinoY = minimoY;
        }if(destinoY > maximoY){
            destinoY = maximoY;
        }

        sols.add(new Sol(centroX,centroY,destinoX,destinoY));
        repaint();
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(colunas * TamanhoCelula, linhas * TamanhoCelula + alturaBarraSuperior ) ;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.fillRect(0,0,getWidth(),getHeight());



        for(int i = 0; i < linhas;i++){
            g.setColor(Color.RED);
            for(int c = 0; c <colunas;c++){
                g.setColor(Color.RED);
                g.drawRect(c*TamanhoCelula,i * TamanhoCelula + alturaBarraSuperior,TamanhoCelula,TamanhoCelula);
            }
        }
        for(Planta p : plantas){
            int linha = p.getLinha();
            int coluna = p.getColuna();
            int centroX = coluna * TamanhoCelula + TamanhoCelula/2;
            int centroY= linha * TamanhoCelula + TamanhoCelula/2 + alturaBarraSuperior;

            if(p.estaPiscando()){
                g.setColor(Color.WHITE);
            }else{
                g.setColor(Color.YELLOW);
            }
            g.fillOval(centroX - 25, centroY -25, 50, 50);
        }

        for(CartaPlanta carta : cards){
            int x = carta.getX();
            int y =carta.getY();
            int largura = carta.getLargura();
            int altura = carta.getAltura();
            g.setColor(Color.BLUE);
            g.fillRect(x,y,largura,altura);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(carta.getCusto()),x + carta.getLargura () / 2,y + carta.getAltura() / 2);

        }


        g.drawString(String.valueOf(contadorSol),800,82);

        for(Sol s : sols){
            int centroX = s.getPosicaoX();
            int centroY = s.getPosicaoY();
            g.setColor(Color.BLACK);
            g.fillOval(centroX - TamanhoSol/2 , centroY -  TamanhoSol /2, TamanhoSol,TamanhoSol);
        }
        for(Projetil p : projeteis){
            int x = p.getPosicaoX();
            int y = p.getPosicaoY();

            g.setColor(Color.RED);
            g.fillOval(x - 10, y - 10,20,20);
        }
        for(Zumbi z : zumbis){
            int x = z.getPosicaoX();
            int linha = z.getLinha();
            int y = linha * TamanhoCelula + TamanhoCelula /2 + alturaBarraSuperior;
            g.setColor(Color.white);
            g.fillRect(x - Zumbi.LARGURA/2 ,y - Zumbi.ALTURA/2,Zumbi.LARGURA,Zumbi.ALTURA);
        }



        if(plantaArrastada != null && dentroDaGrade(mouseX,mouseY)){

            g.setColor(Color.YELLOW);
            g.fillOval(mouseX - 25, mouseY -25,50,50);
        }
        if(gameOver){
            g.setColor(Color.RED);
            g.drawString("FIM DE JOGO", 450,350);
        }

    }
    private boolean posicaoValida(int mouseX,int mouseY){
        int coluna = mouseX / TamanhoCelula;
        int linha = (mouseY - alturaBarraSuperior)/TamanhoCelula;

        if(mouseY >= alturaBarraSuperior && linha < linhas && coluna >=0 && coluna < colunas && !celulaOcupada(linha,coluna)){
            return true;
        }
        return false;
    }

    private boolean celulaOcupada(int linha, int coluna) {
        for(Planta p : plantas){
            if(p.getLinha() == linha && p.getColuna() == coluna){
                return true;
            }
        }
        return false;
    }
    private boolean dentroDaGrade(int mouseX, int mouseY) {
        int coluna = mouseX / TamanhoCelula;
        int linha = (mouseY - alturaBarraSuperior) / TamanhoCelula;
        if(mouseY >= alturaBarraSuperior &&
        linha >=0 && linha < linhas
        && coluna >=0 && coluna < colunas){
            return true;
        }
        return false;
    }
    public void gerarProjetil(int linha,int coluna,int dano){
        int centroX = coluna * TamanhoCelula + TamanhoCelula/2;
        int centroY = linha * TamanhoCelula + TamanhoCelula/2 + alturaBarraSuperior;
        int posicaoInicialX = centroX + 25;
        int posicaoInicialY = centroY;
        projeteis.add(new Projetil(
                posicaoInicialX,
                posicaoInicialY,
                12,
                dano
        ));

    }

    public boolean temzumbiNaFrente(int linha, int coluna){
        int xPlanta = coluna * TamanhoCelula + TamanhoCelula /2;
        for(Zumbi z : zumbis){
            if(z.getLinha() == linha && z.getPosicaoX() > xPlanta){
                return true;
            }
        }
        return false;
    }


}