package main;

import aiPakSam.AIPakSam;
import gui.GameGUIController;
import javafx.application.Application;
import javafx.stage.Stage;

import static gui.GameGUIController.GUIGamePlayer;

public class Main extends Application {
  private static GameGUIController window;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    TestAI testAI1 = new TestAI("BOT0");
    TestAI testAI2 = new TestAI("BOT1");
    TestAI testAI3 = new TestAI("BOT2");
//    TestAI testAI4 = new TestAI("BOT3");

    //
//    GUIGamePlayer testAI1 = new GUIGamePlayer("BOT0");
//    GUIGamePlayer testAI2 = new GUIGamePlayer("BOT1");
//    GUIGamePlayer testAI3 = new GUIGamePlayer("BOT2");


//    AIPakSam testAI1 = new AIPakSam("BOT0");
//    AIPakSam testAI2 = new AIPakSam("BOT1");
//    AIPakSam testAI3 = new AIPakSam("BOT2");
//
    AIPakSam testAI4 = new AIPakSam("PakSam");
    setUserAgentStylesheet(STYLESHEET_MODENA);
    window = GameGUIController.NewGameSession(testAI1, testAI2, testAI3, testAI4);
  }
}
