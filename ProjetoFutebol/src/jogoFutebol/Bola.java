package jogoFutebol;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class Bola extends Agent{
	
	public void setup(){
		
		// registrou nas paginas amarelas
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		addBehaviour(new RequisitarPosicao(this));
		addBehaviour(new Chutada(this));
		
	}
	
	private class RequisitarPosicao extends OneShotBehaviour{
		
		public RequisitarPosicao(Agent agente) {
			super(agente);
		}
		public void action() {
			
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setContent("Defina minha Localizacao de Bola");
			msg.addReceiver(new AID("Campo", AID.ISLOCALNAME));
			myAgent.send(msg);
			System.out.println("Definindo a localizacao - Bola");
			
		}	
	}
	
	private class Chutada extends CyclicBehaviour{

		public Chutada(Agent agente) {
			super(agente);
		}

		public void action() {
			
			ACLMessage msg = myAgent.receive();

			if (msg != null) {

				String mensagem = msg.getContent();

				if (mensagem.equals("Chutou a bola")) {
					
					ACLMessage msgResposta = msg.createReply();
					
					//tipo de mensagem
					msgResposta.setPerformative(ACLMessage.CONFIRM);
					
					//conteudo
					msgResposta.setContent("OK");
					myAgent.send(msgResposta);
					System.out.println("Bola Chutada");
				}
				
			}
			
		}
				
	}

}
