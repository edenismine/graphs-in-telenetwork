package fciencias.edatos.main;

import fciencias.edatos.network.Network;
import fciencias.edatos.network.NetworkLoader;
import fciencias.edatos.network.Station;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Main class. It implements a command line interface (henceforth referenced as CLI)
 * that allows the user to ...
 *
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class Main {
    private static final String AREA_CODE = loadAreaCodes(); // TODO: ensure this line is always below the initialization of NETWORK.
    private static final String N = "\u001B[0m"; // resets terminal color
    private static final String R = "\u001B[91m"; // red, error.
    private static final String G = "\u001B[92m"; // green, success.
    private static final String Y = "\u001B[93m"; // yellow, prompt.
    private static final String C = "\u001B[96m"; // blue, display.
    private static final String OPS_DEFAULT = "[xX]";
    private static final String OPS_LOADED = OPS_DEFAULT + "|call\\s+" + AREA_CODE + "[\\-\\.]\\d{8}\\s+" + AREA_CODE + "[\\-\\.]\\d{8}" + "|sendPubBy\\s+(phone|areaCode)|sendPubBy\\s+" + AREA_CODE;
    private static final String EXIT_MESSAGE = "1\n2\n3\n4";
    private static final String[] OPS_LOADED_DESCRIPTIONS = {
            "To place a call use the following syntax:" + G + "\n\tcall areaCode-XXXXXXXX areaCode-XXXXXXXX" + Y + "\n\texample: 'call 55-12345678 801-22334455'" + N,
            "To send publicity to all clients in order use:" + G + "\n\tsendPubBy (phone|areaCode)" + Y + "\n\texample: 'sendPubBy phone'" + N,
            "To send publicity to clients of a specific station:" + G + "\n\tsendPubBy areaCode" + Y + "\n\texample: 'sendPubBy 55'" + N};
    private static final String[] OPS_LOADED_REGEX = {"call\\s+" + AREA_CODE + "[\\-\\.]\\d{8}\\s+" + AREA_CODE + "[\\-\\.]\\d{8}",
            "sendPubBy\\s+(phone|areaCode)",
            "sendPubBy\\s+" + AREA_CODE};
    private static final String[] EXIT_ANIMATION_FRAMES = {"", "", ""};
    private static final String RESOURCES = "resources/";
    private static final Network NETWORK = loadNetwork(); // TODO: This must be the topmost statement.
    private static final String TITLE = NETWORK != null ? "Network manager" : "ERROR";
    private static Scanner stdin;
    private static int cols;

    /**
     * This method loads the network from disk.
     *
     * @return The Network, or null if the parse was not successful.
     */
    private static Network loadNetwork() {
        Network network = null;
        try {
            network = NetworkLoader.newInstance().read(RESOURCES + "network.xml");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            error(e.getMessage());
        }
        return network;
    }

    /**
     * This method loads the stations' area codes and creates the appropriate regex.
     *
     * @return Regex that represents all loaded area codes.
     */
    private static String loadAreaCodes() {
        ArrayList<Integer> areaCodes = Station.getAreaCodes();
        if (areaCodes.size() == 0) return "ERROR";
        Collections.sort(areaCodes, Collections.reverseOrder());
        StringBuilder builder = new StringBuilder().append("(");
        for (int code : areaCodes)
            builder.append(code).append('|');
        builder.deleteCharAt(builder.length() - 1);
        builder.append(')');
        return builder.toString();
    }

    /**
     * Initializer method tasked with setting appropriate dimensions for the
     * CLI, as well as facilitating the reference to {@link java.lang.System#in}.
     *
     * @param args the arguments with which the program was run
     */
    private static void init(String[] args) {
        stdin = new Scanner(System.in);
        try {
            cols = Integer.parseInt(args[0]);
            if (cols < 0)
                throw new NumberFormatException();
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            cols = 80;
        }
    }

    /**
     * Main menu.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        init(args);

        boolean close = false;
        String userProvidedString = "";

        boolean condition = NETWORK != null;

        do {
            flush();
            header();
            String regex;
            display("  Enter 'x' to exit, or");
            if (condition) {
                regex = OPS_LOADED;
                for (String OPS_LOADED_DESCRIPTION : OPS_LOADED_DESCRIPTIONS) {
                    display("  " + OPS_LOADED_DESCRIPTION);
                }
            } else {
                regex = OPS_DEFAULT;
                error("Network wasn't loaded successfully, check the logs for more information.");
            }

            // This shows the previous result.
            if (!userProvidedString.isEmpty()) {
                input();
                System.out.print(Y + userProvidedString + "\n" + N);
            }

            // Gets a valid userProvidedString or exit message
            userProvidedString = getValidString(regex);

            if (userProvidedString.matches(OPS_DEFAULT)) {
                close = true;
            } else {
                advancedTask(userProvidedString);
            }
        } while (!close);
        exit();
    }

    /**
     * This method is in charge of handling the different instructions available
     * to the user, updating the program's attributes accordingly and returning
     * a descriptive message. It decides which task to perform by analyzing the
     * provided String.
     *
     * @param task String that represents the task that should be performed.
     * @return a descriptive message with information about what was done or
     * could not be done.
     */
    private static String advancedTask(String task) {
        String message = "Encountered an error while managing the following task: " + task;
        String string;
        if (!task.isEmpty()) {
            int taskID = -1;
            for (int i = 0; i < OPS_LOADED_REGEX.length; i++) {
                if (task.matches(OPS_LOADED_REGEX[i])) {
                    taskID = i;
                    break;
                }
            }
            switch (taskID) {
                case 0:
                    success(message);
                    break;
                case 1:
                    success(message);
                    break;
                case 2:
                    success(message);
                    break;
                default:
                    break;
            }
            return message;
        }
        message = "Invalid task: " + task;
        return message;
    }

    /**
     * Method that asks the user for a valid string using a custom message and
     * only admits String's that match the provided regular expression. It then
     * returns such valid String.
     *
     * @param regex Regular expression to be matched.
     * @return the string provided by the user that matches {@code regex}.
     */
    private static String getValidString(String regex) {
        boolean seen = false;
        String str;
        do {
            if (seen) {
                error("Invalid option, try again.");
                input();
            } else {
                input();
            }
            while (!stdin.hasNextLine()) {
                stdin.next();
            }
            str = stdin.nextLine();
            seen = true;
        } while (!str.matches(regex));
        return str;
    }

    /**
     * Method that extracts the capturing group with index {@code num} from a
     * given String, {@code searchSpace} using the provided regular expression
     * {@code regex}. Capturing groups are indexed from left to right, starting
     * at one. Group zero denotes the entire pattern.
     *
     * @param regex       Regular expression that will be use to match the provided
     *                    string.
     * @param searchSpace The string from which this method will attempt to extract a
     *                    matching group.
     * @param num         the index of the desired matching group.
     * @return the capturing group with index {@code num} or an empty String if
     * no such group exists.
     */
    private static String getGroup(String regex, String searchSpace, int num) {
        Matcher match = Pattern.compile(regex).matcher(searchSpace);
        if (match.find())
            try {
                return match.group(num);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        return "";
    }

    /**
     * Method that shows a little goodbye message and exits the program.
     */
    private static void exit() {
        int[] frameSequence = {1, 0, 1, 2, 1, 0, 1};
        for (int index : frameSequence) {
            flush();
            System.out.println(EXIT_ANIMATION_FRAMES[index]);
            sleep(100);
        }
        System.exit(0);
    }

    /**
     * This method attempts to sleep for {@code millis} milliseconds.
     *
     * @param millis Amount of time the program should sleep for (in milliseconds).
     */
    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            error("Something went very wrong, could not sleep.");
        }
    }

    /**
     * This method takes a String and prints an adorned version using a
     * descriptive color and adding the prefix "ERROR: ". It can be used to
     * signal errors to the user.
     *
     * @param message the String to be decorated.
     */
    private static void error(String message) {
        System.out.println(R + "ERROR: " + message + N);
    }

    /**
     * This method takes a String and prints an adorned version using a
     * descriptive color and adding the suffix "&gt;&gt; ". It can be used
     * to ask the user for input.
     */
    private static void input() {
        System.out.print(Y + ">> " + N);
    }

    /**
     * This method takes a String and prints an adorned version using a
     * descriptive color. It can be used to emphasize the String or to display
     * descriptive messages from successful processes.
     *
     * @param message the String to be decorated.
     */
    private static void success(String message) {
        System.out.println(G + message + N);
    }

    /**
     * This method takes a String and prints an adorned version using a
     * descriptive color. It can be used to emphasize the String or to display
     * descriptions or steps.
     *
     * @param message the String to be decorated.
     */
    private static void display(String message) {
        System.out.println(C + message + N);
    }

    /**
     * This method prints the program's header.
     */
    private static void header() {
        StringBuilder ray = new StringBuilder("");
        for (int i = 0; i < cols; i++) {
            ray.append("=");
        }

        StringBuilder newTitle = new StringBuilder("");
        for (int i = 0; i < ((cols - TITLE.length()) / 2); i++) {
            newTitle.append(" ");
        }
        newTitle.append(TITLE);
        for (int i = 0; i < ((cols - TITLE.length()) / 2); i++) {
            newTitle.append(" ");
        }

        display(ray.toString());
        display(newTitle.toString());
        display(ray.toString());
    }

    /**
     * This method clears the terminal.
     */
    private static void flush() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
