package jogoFutebol;

import java.util.Random;

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

public class JogadorDireita extends Agent{
	
	int iterator = 0;
	public static int xJogador, yJogador;
	private static final int JOGADOR = 3;
	Random random = new Random();
	
	public boolean equals(AID aid){
		
		if(getAID()==aid){
			return true;
		}else{
			return false;
		}	
	}
	
	protected void setup() {
		
		// registrou nas paginas amarelas
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("jogador");
			sd.setName("JADE-jogador");
			dfd.addServices(sd);
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		int x = 0;
		int y = 0;
		
		do {
			x = random.nextInt(Campo.lenght);
			y = random.nextInt(Campo.height);
		} while (y<=45);
		
		xJogador = x;
		yJogador = y;

		System.out.println("Posicao do jogador direita (X= " + x
				+ "  Y= " + y + ") definida");

		Campo.matriz[x][y] = JOGADOR;
		
		addBehaviour(new AcaoJogador(this));
		
		super.setup();
	}
	
	
	private class AcaoJogador extends CyclicBehaviour{

		public AcaoJogador(Agent agente) {
			super(agente);
		}
		public void action() {
			
			ACLMessage msg = myAgent.receive();

			if (msg != null) {

				String mensagem = msg.getContent();

				if (mensagem.equals("Achou Bola")) {
					
					int num = random.nextInt(3);
					if (num == 1) {
						Campo.xBola -= 7;
						Campo.yBola -= 7;
					} else if (num == 2) {
						Campo.xBola += 7;
						Campo.yBola -= 7;
					} else {
						Campo.yBola -= 7;
					}

					if (Campo.xBola >= Campo.lenght) {
						Campo.xBola = Campo.lenght - 1;
					} else if (Campo.xBola < 0) {
						Campo.xBola = 0;
					}

					if (Campo.yBola >= Campo.height) {
						Campo.yBola = Campo.height - 1;
					} else if (Campo.yBola < 0) {
						Campo.yBola = 0;
					}

					System.out.println(num);

					if(Campo.xBola >= 23 && Campo.xBola<=28 && Campo.yBola==0 ){
						Campo.permitido = true;
						ACLMessage msgGol = new ACLMessage(ACLMessage.INFORM);
						msgGol.setContent("Gol Direita");
						msgGol.addReceiver(new AID("campo", AID.ISLOCALNAME));
						myAgent.send(msgGol);
						System.out.println("Goooool");
					}
					else{
						Campo.matriz[Campo.xBola][Campo.yBola] = 1;
						Campo.permitido = true;
						
						ACLMessage msgMover = new ACLMessage(ACLMessage.INFORM);
						msgMover.setContent("Chutada Jogador");
						msgMover.addReceiver(new AID("bola", AID.ISLOCALNAME));
						myAgent.send(msgMover);
						System.out.println("Bola Chutada");
					}
				}
				
				if (mensagem.equals("Mover")) {
					Campo.matriz[xJogador][yJogador] = 0;

					if (xJogador < Campo.xBola) {
						xJogador++;
					} else if (xJogador > Campo.xBola) {
						xJogador--;
					}
					if (yJogador < Campo.yBola) {
						yJogador++;
					} else if (yJogador > Campo.yBola) {
						yJogador--;
					}
					if (xJogador == Campo.xBola && yJogador == Campo.yBola) {
						if(Campo.permitido==true)
						{
							Campo.permitido=false;
							ACLMessage msgMover = new ACLMessage(ACLMessage.INFORM);
							msgMover.setContent("Achou Bola");
							msgMover.addReceiver(getAID());
							myAgent.send(msgMover);
							System.out.println("Moveu");
						}

					}
					Campo.matriz[xJogador][yJogador] = JOGADOR;
				}
				
				if (mensagem.equals("Voltar")) {
					int x = 0;
					int y = 0;
					
					Campo.matriz[xJogador][yJogador] = 0;
					
					do {
						x = random.nextInt(Campo.lenght);
						y = random.nextInt(Campo.height);
					} while (y<=45);
					
					xJogador = x;
					yJogador = y;

					System.out.println("Posicao do jogador direita (X= " + x
							+ "  Y= " + y + ") definida");

					Campo.matriz[x][y] = JOGADOR;
				}
				
			}
			
		}
		
	}
	
}
