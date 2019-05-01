package main;

import aiPakSam.AIRikAl;
import gui.GameGUIController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
  private static GameGUIController window;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
//    TestAI testAI1 = new TestAI("BOT0");
//    TestAI testAI2 = new TestAI("BOT1");
//    TestAI testAI3 = new TestAI("BOT2");

//    GUIGamePlayer testAI1 = new GUIGamePlayer("BOT0");
//    GUIGamePlayer testAI2 = new GUIGamePlayer("BOT1");
//    GUIGamePlayer testAI3 = new GUIGamePlayer("BOT2");


    AIRikAl testAI1 = new AIRikAl("BOT0");
    AIRikAl testAI2 = new AIRikAl("BOT1");
    AIRikAl testAI3 = new AIRikAl("BOT2");
//
    AIRikAl testAI4 = new AIRikAl("PakSam");
    setUserAgentStylesheet(STYLESHEET_MODENA);
    window = GameGUIController.NewGameSession(testAI1, testAI2, testAI3, testAI4);
  }
}
