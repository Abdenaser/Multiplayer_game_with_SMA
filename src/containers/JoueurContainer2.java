package containers;

import agents.Joueur2;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static jade.core.MicroRuntime.startAgent;

public class JoueurContainer2 extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private Joueur2 myAgent;
    private ObservableList<String> list = FXCollections.observableArrayList();

    public void setMyAgent2(Joueur2 myAgent) {
        this.myAgent = myAgent;
    }

    public void startAgent(String name, String className, Object[] args) throws ControllerException {
        Runtime runtime= Runtime.instance();
        ProfileImpl profileImpl=new ProfileImpl();
        profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer agentContainer=runtime.createAgentContainer(profileImpl);
        AgentController agentController=agentContainer.createNewAgent(name,className,args);
        agentController.start();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        startAgent("Joueur2","agents.Joueur2",new Object[]{this});
        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        Label label = new Label("Agent message: ");
        TextField textField = new TextField();
        textField.setPromptText("Enter your message");
        Button button = new Button("Envoyer");
        hBox.getChildren().addAll(label, textField, button);
        borderPane.setTop(hBox);

        ListView<String> listView = new ListView<>(list);
        borderPane.setCenter(listView);

        primaryStage.setScene(new Scene(borderPane, 600, 450));
        primaryStage.setTitle("Agent 2 Container");
        primaryStage.show();

        button.setOnAction(event -> {
            String message = textField.getText();
            GuiEvent guiEvent = new GuiEvent(this, 1);
            guiEvent.addParameter(message);
            myAgent.onGuiEvent(guiEvent);
        });
    }
    public ObservableList<String> getList() {
        return list;
    }
}
