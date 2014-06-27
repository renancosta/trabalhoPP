package Interface;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jogoFutebol.*;

public class CampoFutebol extends Frame {
	static final int linhas = Campo.lenght;
	static final int colunas = Campo.height;
	static GridLayout grid = new GridLayout();

	static JPanel painel = new JPanel(new GridLayout(linhas, colunas));

	public void showGui() {
		pack();
		this.setSize(1024, 768);
		this.setLocationRelativeTo(null);

		super.setVisible(true);
	}

	static int tempNum1 = 0, tempNum2 = 0, tempNum3 = 0;

	public static void atualizaTela() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				limpaTela();
				desenharCampo();
				criaGol();
				int num1 = JogadorDireita.xJogador * (Campo.height - 1)
						+ (JogadorDireita.xJogador + JogadorDireita.yJogador);
				int num2 = JogadorEsquerda.xJogador * (Campo.height - 1)
						+ (JogadorEsquerda.xJogador + JogadorEsquerda.yJogador);
				int num3 = Campo.xBola * (Campo.height - 1)
						+ (Campo.xBola + Campo.yBola);
				tempNum1 = num1;
				tempNum2 = num2;
				tempNum3 = num3;
				painel.getComponent(num1).setBackground(Color.RED);
				painel.getComponent(num2).setBackground(Color.BLUE);
				painel.getComponent(num3).setBackground(Color.YELLOW);

				painel.revalidate();
				painel.repaint();

			}

		});
	}

	public static void limpaTela() {
		painel.getComponent(tempNum1).setBackground(Color.GREEN);
		painel.getComponent(tempNum2).setBackground(Color.GREEN);
		painel.getComponent(tempNum3).setBackground(Color.GREEN);
	}

	public static void desenharCampo() {
		// coloca linha central
		for (int i = 0; i < (Campo.lenght); i++) {
			int num1 = i * (Campo.height - 1) + (i + 38);
			painel.getComponent(num1).setBackground(Color.WHITE);
		}

	}

	private static Dialog janela;

	public static void janelaGol(String placar) {
		janela.add(BorderLayout.CENTER, new Label(placar, Label.CENTER));
		janela.setVisible(true);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		janela.setVisible(false);
	}
	
	public static void criaGol() {
		// Definindo posições gol do lado esquerdo
		painel.getComponent(1725).setBackground(Color.GRAY);
		painel.getComponent(1800).setBackground(Color.GRAY);
		painel.getComponent(1875).setBackground(Color.GRAY);
		painel.getComponent(1950).setBackground(Color.GRAY);
		painel.getComponent(2025).setBackground(Color.GRAY);
		painel.getComponent(2100).setBackground(Color.GRAY);

		// Definindo posições gol lado direito
		painel.getComponent(1799).setBackground(Color.GRAY);
		painel.getComponent(1874).setBackground(Color.GRAY);
		painel.getComponent(1949).setBackground(Color.GRAY);
		painel.getComponent(2024).setBackground(Color.GRAY);
		painel.getComponent(2099).setBackground(Color.GRAY);
		painel.getComponent(2174).setBackground(Color.GRAY);

	}

	public CampoFutebol() {
		this.setTitle("Campo Futebol");

		janela = new Dialog(this, "Placar", true);
		janela.setLayout(new BorderLayout());
		janela.setSize(300, 300);

		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Panel painel1 = new Panel();
				painel1.setBackground(Color.GREEN);
				painel1.setName(i + "" + j);
				painel.add(painel1);
			}
		}
		criaGol();

		this.setLayout(new BorderLayout());
		this.add(BorderLayout.CENTER, painel);
	}

}