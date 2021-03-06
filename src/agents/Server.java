package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;


public class Server extends Agent {

    @Override
    protected void setup() {
        System.out.println("L'initialisation :");
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        int random = (int) (Math.random() * 100);
        System.out.println(random);
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                    ACLMessage aclMessage = receive();
                    if (aclMessage != null) {
                        int AGENTNUMBER = 0;
                        ACLMessage messageAcl = new ACLMessage(ACLMessage.INFORM);
                        try {
                            AGENTNUMBER = Integer.parseInt(aclMessage.getContent());
                            if (AGENTNUMBER == random) {
                                // get all agents
                                AMSAgentDescription[] allagents = null;
                                SearchConstraints c = new SearchConstraints();
                                c.setMaxResults ( new Long(-1) );
                                allagents = AMSService.search(Server.this, new AMSAgentDescription(), c);

                                // sending message to all agents
                                for (int i=0; i<allagents.length;i++){
                                    messageAcl.addReceiver(new AID(allagents[i].getName().getLocalName(), AID.ISLOCALNAME));
                                }
                                messageAcl.setContent(aclMessage.getSender().getLocalName()+" Have won this one!");
                                send(messageAcl);
                                doDelete();
                            } else if (AGENTNUMBER > random) {
                                messageAcl.addReceiver(new AID(aclMessage.getSender().getLocalName(), AID.ISLOCALNAME));
                                messageAcl.setContent("Your number "+AGENTNUMBER+", try lower");
                                send(messageAcl);
                            } else {
                                messageAcl.addReceiver(new AID(aclMessage.getSender().getLocalName(), AID.ISLOCALNAME));
                                messageAcl.setContent("Your number "+AGENTNUMBER+", try higher");
                                send(messageAcl);
                            }
                        } catch (Exception e) {
                            messageAcl.addReceiver(new AID(aclMessage.getSender().getLocalName(), AID.ISLOCALNAME));
                            messageAcl.setContent("YOU MUST ENTER A NUMBER");
                            send(messageAcl);
                        }
                    } else {
                        block();
                    }

                }

        });

    }

    @Override
    protected void afterMove() {
        System.out.println("AfterMove");
    }

    @Override
    protected void beforeMove() {
        System.out.println("BeforeMove");
    }

    @Override
    protected void takeDown() {
        System.out.println("TakeDown");
    }

}
