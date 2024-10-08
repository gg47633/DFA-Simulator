/*****************************************************************
 *
 * DFASimulator
 * Garrett Goshorn
 *
 * This program simulates a deterministic finite automata by reading a file in the local directory
 * named "DFA.txt" to "create" the DFA. Then, it takes user input and computes whether a given string is
 * accepted or rejected by the DFA. To exit, enter "quit".
 *
 *****************************************************************/

import java.io.*;
import java.util.*;

public class DFASimulator {
  private static int numberOfStates;
  private static ArrayList<Integer> acceptingStates = new ArrayList<>();
  private static char[] alphabet;
  private static int[][] transitionTable;

  /****************************************************************
   * main(String[] args)
   ***************************************************************/
  //Purpose: To execute the program and read user input
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    //Load DFA from file
    System.out.println(">>>Loading DFA.txt...");
    loadDFA();

    //Input and evaluate strings
    while (true) {
      System.out.print(">>>Please enter a string to evaluate: ");
      String input = scanner.nextLine();
      if (input.equalsIgnoreCase("Quit")) {
        System.out.println(">>>Goodbye!");
        scanner.close();
        return;
      }
      evaluateString(input);
    }
  }
  /****************************************************************
   * loadDFA()
   ***************************************************************/
  //Purpose: loadDFA takes the DFA.txt file and loads the file's contents into different variables,
  //  which "creates" the desired DFA. It stores the number of states into an int variable, the accepting
  //  states in an ArrayList, the alphabet in a character array, and the transitions in a 2D array (like
  //  a table.
  private static void loadDFA() {
    try (BufferedReader reader = new BufferedReader(new FileReader("DFA.txt"))) {
      //Line 1: Number of states
      numberOfStates = Integer.parseInt(reader.readLine());

      //Line 2: Accepting states
      String[] acceptingStatesInput = reader.readLine().split(" ");
      for (String state : acceptingStatesInput) {
        acceptingStates.add(Integer.parseInt(state));
      }

      //Line 3: Alphabet
      alphabet = reader.readLine().replaceAll(" ", "").toCharArray();

      //Line 4-end: Transition table
      transitionTable = new int[numberOfStates][alphabet.length];
      for (int i = 0; i < numberOfStates; i++) {
        String[] transitions = reader.readLine().split(" ");
        for (int j = 0; j < alphabet.length; j++) {
          transitionTable[i][j] = Integer.parseInt(transitions[j]);
        }
      }
    } catch (IOException e) {
      System.out.println("Error reading DFA file: " + e.getMessage());
    }
  }
  /****************************************************************
   * evaluateString(String input)
   ***************************************************************/
  //Purpose: evaluateString takes user input as a parameter and evaluates whether it is accepted
  //  or rejected by the provided DFA. The for-each loop is used to iterate through every character
  //  within the input string. Input validation is performed using getCharIndex, which will return -1
  //  if a character is not within the given alphabet. If only one character remains in the input, a
  //  final evaluation is done, leaving just the empty string. Once the for-each loop is finished, the
  //  function outputs whether the current (final) state is accepting or not.
  private static void evaluateString(String input) {
    int currentState = 0;
    String remainingInput = input;

    System.out.println(">>>Computation...");
    for (char ch : input.toCharArray()) {
      int charIndex = getCharIndex(ch);
      //Character in input isn't in alphabet
      if (charIndex == -1) {
        System.out.println(currentState + "," + ch + " -> INVALID INPUT");
        System.out.println("REJECTED");
        return;
      }
      //Final character in input
      if (remainingInput.length() == 1) {
        System.out.print(currentState + "," + remainingInput + " -> ");
        currentState = transitionTable[currentState][charIndex];
        System.out.println(currentState + ",{ε}");
        break;
      }
      //More than one character remaining in the input
      System.out.print(currentState + "," + remainingInput + " -> ");
      currentState = transitionTable[currentState][charIndex];
      System.out.println(currentState + "," + remainingInput.substring(1));
      remainingInput = remainingInput.substring(1);
    }
    //Determines whether the final state is within the acceptingStates ArrayList
    System.out.println((acceptingStates.contains(currentState) ? "ACCEPTED" : "REJECTED"));
  }
  /****************************************************************
   * getCharIndex(char ch)
   ***************************************************************/
  //Purpose: getCharIndex takes a character as input and iterates through the alphabet array to determine
  //  if the given character is within the alphabet of the DFA. If it is, the index of the matching
  // character in the alphabet array is returned. If not, -1 is returned.
  private static int getCharIndex(char ch) {
    for (int i = 0; i < alphabet.length; i++) {
      if (alphabet[i] == ch) {
        //ch is in the alphabet
        return i;
      }
    }
    //ch is not in the alphabet
    return -1;
  }
}
