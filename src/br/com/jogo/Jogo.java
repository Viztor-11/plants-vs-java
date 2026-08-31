package br.com.jogo;

import javax.swing.*;
import java.awt.*;

public class Jogo {
    public Jogo() {
        JFrame janela = new JFrame();
        PainelJogo painel = new PainelJogo();

        janela.add(painel);
        janela.pack(); // defino que a janela tera o numero de linha e colunas como tamanho
        janela.setResizable(false); //travar redimensionamento da tela
        janela.setLocationRelativeTo(null); // centralizo a janela na tela
        janela.setTitle("planta vs mortos"); // titulo
        janela.setVisible(true); // deixa a janela visivel
        janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // ao fechar janela encerra programa

    }
}

