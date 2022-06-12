package agents;


import containers.JoueurContainer2;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import javafx.application.Platform;

public class Joueur2 extends GuiAgent {
    private transient JoueurContainer2 myGui;

    @Override
    protected void setup() {
        myGui = (JoueurContainer2) getArguments()[0];
        myGui.setMyAgent2(this);
        System.out.println("L'initialisation de l'agent");
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage=receive();
                if(aclMessage!=null) {
                    Platform.runLater(() -> myGui.getList().add("Message: "+aclMessage.getContent()));
                }
                else {
                    block();
                }
            }

        });

    }

    @Override
    protected void afterMove() {
        System.out.println("afterMove");
    }

    @Override
    protected void beforeMove() {
        System.out.println("beforeMove");
    }

    @Override
    protected void takeDown() {
        System.out.println("talkDown");
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        if (guiEvent.getType() == 1) {
            ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
            aclMessage.addReceiver(new AID("Server", AID.ISLOCALNAME));
            aclMessage.setContent((String) guiEvent.getParameter(0));
            send(aclMessage);
        }
    }
}
