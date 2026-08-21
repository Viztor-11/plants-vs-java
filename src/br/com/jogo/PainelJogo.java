package br.com.jogo;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PainelJogo extends JPanel{

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





    public PainelJogo(){


        zumbis.add(new Zumbi(900,2,0.001,270));

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
            for(Zumbi z : zumbis){
                z.mover();
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


        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e)  {
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
                if (plantaArrastada != null){
                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint();
                }
            }


            @Override
            public void mouseReleased(MouseEvent e){
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
            g.setColor(Color.YELLOW);
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