package View.Console;

import View.Console.UserAction.UserAction;
import View.Console.UserAction.UserActionExit;
import View.Console.UserAction.UserActionMark;
import View.Console.UserAction.UserActionOpen;
import minesweeper4java.MinesweeperView;
import model.GameDifficulty;
import model.GameModel;
import model.GameState;

import java.util.Arrays;
import java.util.Scanner;


/**
 * Created by olivergerhardt on 30.08.17.
 */
public class ConsoleView implements MinesweeperView {

    private final GameModel gameModel;
    private final ConsoleViewDrawer drawer;
    private final Scanner keyboard;
    private final CommandLineUserActionPicker picker;


    public ConsoleView(GameModel gameModel) {
        this.gameModel = gameModel;
        drawer = new ConsoleViewDrawer(gameModel);
        this.keyboard = new Scanner(System.in);
        this.picker = new CommandLineUserActionPicker();
    }


    public void play() {

        int dimension = getPositiveIntegerFromUser(keyboard, "Enter column count (positive integer): ");
        gameModel.startGame(dimension, GameDifficulty.EASY);

        UserAction userAction;
        commandScanner: while (true) {
            drawer.draw();

            userAction = getUserAction();
            userAction.perform(gameModel);

            switch (gameModel.getState()) {
                case WON:
                    System.out.println("You've won!");
                    break commandScanner;
                case LOST:
                    System.out.println("You've lost!");
                    break commandScanner;
            }
        }

        System.out.println();
        gameModel.visitAllAndRemoveMarks();
        drawer.draw();
        System.exit(0);
    }

    private int getPositiveIntegerFromUser(Scanner scanner, String prompt) {
        int value = -1;
        while (value < 1) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return value;
    }


    private UserAction getUserAction() {
        UserAction userAction = null;
        while (userAction == null) {
            System.out.print("Command: ");
            String userInput = keyboard.nextLine();
            userAction = UserActionScanner.getUserAction(userInput);
        }
        return userAction;
    }
}
