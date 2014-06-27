package jogoFutebol;

import java.awt.Container;
import java.util.Random;

import javax.swing.JOptionPane;

import Interface.CampoFutebol;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;

//classe para inicializar o campo de futebol

public class Campo extends Agent {

	private static final int BOLA = 1;
	public static int lenght = 50;
	public static int height = 75;
	public static int xBola;
	public static int yBola;
	static boolean permitido = true;
	int golEsquerda = 0, golDireita = 0;

	public static int matriz[][] = new int[lenght][height];
	public static AgentController agentController1, agentController2,
			agentController3;
	private CampoFutebol campoFutebolInterface;

	public void setup() {

		try {

			PlatformController container = getContainerController();

			agentController1 = container.createNewAgent("jogador1",
					"jogoFutebol.JogadorEsquerda", null);
			agentController3 = container.createNewAgent("jogador2",
					"jogoFutebol.JogadorDireita", null);
			agentController2 = container.createNewAgent("bola",
					"jogoFutebol.Bola", null);

			System.out.println("Agente criado " + agentController1.getName());
			System.out.println("Agente criado " + agentController2.getName());
			System.out.println("Agente criado " + agentController3.getName());

			agentController1.start();
			agentController2.start();
			agentController3.start();

			matriz[23][0] = 5;
			matriz[24][0] = 5;
			matriz[25][0] = 5;
			matriz[26][0] = 5;
			matriz[27][0] = 5;
			matriz[28][0] = 5;
			matriz[23][74] = 5;
			matriz[24][74] = 5;
			matriz[25][74] = 5;
			matriz[26][74] = 5;
			matriz[27][74] = 5;
			matriz[28][74] = 5;
			
			campoFutebolInterface = new CampoFutebol();
			campoFutebolInterface.showGui();

		} catch (ControllerException e) {
			e.printStackTrace();
		}

		addBehaviour(new DefinirPosicao(this));
		addBehaviour(new CicloCampo(this, 1000));

		super.setup();
	}

	private class DefinirPosicao extends CyclicBehaviour {

		public DefinirPosicao(Agent agente) {
			super(agente);
		}

		public void action() {

			// definindo posicao
			Random random = new Random();

			ACLMessage msg = myAgent.receive();

			if (msg != null) {

				String mensagem = msg.getContent();

				if (mensagem.equals("Defina minha Localizacao de Bola")) {

					int x = 0;
					int y = 0;

					do {
						x = random.nextInt(lenght);
						y = random.nextInt(height);
					} while (y < 30 || y > 45);

					ACLMessage msgResposta = msg.createReply();
					msgResposta.setPerformative(ACLMessage.CONFIRM); // tipo de
																		// mensagem
					msgResposta.setContent("x,y"); // conteudo
					myAgent.send(msgResposta);

					xBola = x;
					yBola = y;
					System.out.println("Posicao da bola (X= " + x + "  Y= " + y
							+ ") definida");

					matriz[x][y] = BOLA;

				}

				if (mensagem.contains("Gol")){
					int i;
					
					if(mensagem.equals("Gol Esquerda")){
						golEsquerda++;
					} else {
						golDireita++;
					}
					
					matriz[xBola][yBola]=0;
					
					ACLMessage msgGol = new ACLMessage(ACLMessage.REQUEST);
					msgGol.setContent("Defina minha Localizacao de Bola");
					msgGol.addReceiver(new AID("Campo", AID.ISLOCALNAME));
					myAgent.send(msgGol);
					
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("jogador");
					template.addServices(sd);

					try {
						DFAgentDescription[] result = DFService.search(myAgent,
								template);
						for (i = 0; i < result.length; ++i) {

							ACLMessage msgVoltar = new ACLMessage(ACLMessage.INFORM);
							msgVoltar.setContent("Voltar");
							msgVoltar.addReceiver(result[i].getName());
							myAgent.send(msgVoltar);
						}
					} catch (FIPAException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String placar = golEsquerda+" x "+golDireita;
					JOptionPane.showMessageDialog(null, placar);
					System.out.println("Placar: "+golEsquerda+" x "+golDireita);

				}

			}

		}

	}

	private class CicloCampo extends TickerBehaviour {

		public CicloCampo(Agent a, long period) {
			super(a, period);
		}

		protected void onTick() {

			int i, j;
			/*for (i = 0; i < lenght; i++) {
				for (j = 0; j < height; j++) {
					System.out.print(matriz[i][j] + " ");
				}
				System.out.println(" ");
			}*/

			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("jogador");
			template.addServices(sd);

			try {
				DFAgentDescription[] result = DFService.search(myAgent,
						template);
				for (i = 0; i < result.length; ++i) {

					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					CampoFutebol.atualizaTela();
					msg.setContent("Mover");
					msg.addReceiver(result[i].getName());
					myAgent.send(msg);
					System.out.println("Mover");
				}
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
