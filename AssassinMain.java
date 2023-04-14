// Class AssassinMain is the driver program for the assassin management task.
// It reads names from the file names.txt, shuffles them, and uses them to
// start the game.  The user is asked for the name of the next victim until
// the game is over.

import java.io.*;
import java.util.*;

public class AssassinMain {
    public static void main(String[] args) throws FileNotFoundException {
        // read names into a Set to eliminate duplicates
        Scanner input = new Scanner(new File("names.txt"));
        Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        while (input.hasNextLine()) {
            String name = input.nextLine().trim();
            if (name.length() > 0)
                names.add(name);
        }

        // transfer to an ArrayList, shuffle and build an AssassinManager
        ArrayList<String> names2 = new ArrayList<String>(names);
        Collections.shuffle(names2);
        String[] data = (String[])names2.toArray(new String[names2.size()]);
        AssassinManager manager = new AssassinManager(data);

        // prompt the user for victims until the game is over
        Scanner console = new Scanner(System.in);
        while (!manager.gameOver())
            oneKill(console, manager);

        // report who won
        System.out.println("Game was won by " + manager.winner());
        System.out.println("Final graveyard is as follows:");
        manager.printGraveyard();
    }

    // Handles the details of recording one victim.  Shows the current kill
    // ring and graveyard to the user, prompts for a name and records the
    // kill if the name is legal.
    public static void oneKill(Scanner console, AssassinManager manager) {
        System.out.println("Current kill ring:");
        manager.printKillRing();
        System.out.println("Current graveyard:");
        manager.printGraveyard();
        System.out.println();
        System.out.print("next victim? ");
        String name = console.nextLine().trim();
        if (manager.graveyardContains(name))
            System.out.println(name + " is already dead.");
        else if (!manager.killRingContains(name))
            System.out.println("Unknown person.");
        else
            manager.kill(name);
        System.out.println();
    }
}
