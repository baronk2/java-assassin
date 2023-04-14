/*
Kevin Baron
4/11/13
CSE 143 Assignment #3
Assassin Manager
*/

public class AssassinManager {
	
	private AssassinNode killRingFirst;// reference to the first person in the kill ring
	private AssassinNode graveyardFirst;//reference to the first person in the graveyard
	
	//pre : given a non-empty array of names. If array is empty, throw an IllegalArgumentException
	//post: all the names from the array have been copied into a linked list with AssassinNode links,
	//      graveyard is initialized to contain nobody
	public AssassinManager(String[] names) {
		if (names.length == 0)
			throw new IllegalArgumentException();
		//construct the first AssassinNode using the first name in the array 
		AssassinNode killRingFirst = new AssassinNode(names[0]);
		//for the rest of the names (if there are anymore), keep constructing new AssassinNodes
		//and attaching them to their respective "previous" nodes
		AssassinNode prev = killRingFirst;
		for (int i = 1; i < names.length; i++) {
			prev.next = new AssassinNode(names[i]);
			prev = prev.next;
		}//eo for
		//initialize the class's fields
		this.killRingFirst = killRingFirst;
		this.graveyardFirst = null;
	}//eo AssassinManager constructor
	
	//pre : killRingFirst does not refer to null
	//post: all the players' names and who they are stalking has been printed.
	public void printKillRing() {
		AssassinNode current = killRingFirst;
		//this while loop handles every AssassinNode in the list except for the last one
		while (current.next != null) {
			System.out.println("    " + current.name + " is stalking " + current.next.name);
			current = current.next;
		}//eo while
		//deal with the last AssassinNode by refering to the name at the front of the list
		//instead of current.next.name because this would cause a NullPointerException (current.next is null)
		System.out.println("    " + current.name + " is stalking " + killRingFirst.name);
	}//eo printKillRing
	
	//pre : graveyard has at least one node in it. otherwise printGraveyard will do nothing.
	//      each AssassinNode in the graveyard has had its killer field initialized.
	//post: every dead person has been reported along with who killed him/her
	public void printGraveyard() {
		//need at least one node in the graveyard to pass the while test without a NullPointerException
		if (graveyardFirst != null) {
			AssassinNode current = graveyardFirst;
			while (current.next != null) {
				System.out.println("    " + current.name + " was killed by " + current.killer);
				current = current.next;
			}//eo while
			//the .next of the last person is null, so he/she was not printed in the while loop
			System.out.println("    " + current.name + " was killed by " + current.killer);
		}//eo if
	}//eo printGraveyard
	
	public boolean killRingContains(String name) {
		return ringContains(name, killRingFirst);
	}//eo killRingContains
	
	public boolean graveyardContains(String name) {
		return ringContains(name, graveyardFirst);
	}//eo graveyardContains
	
	//handles both killRingContains and graveyardContains
	//pre : given a name and a reference to a linked list to search
	//post: true has been returned if the name was found; false if not found
	private static boolean ringContains(String name, AssassinNode current) {
		//an empty list will not contain the search name
		if (current == null)
			return false;
		//try to find the search name in all but the last node
		while (current.next != null) {
			if (name.equalsIgnoreCase(current.name))
				return true;
			current = current.next;
		}//eo while
		//try to find the search name in the last node
		if (name.equalsIgnoreCase(current.name))
			return true;
		//search name has not been found
		return false;
	}//eo ringContains
	
	//post: returns true if there is only one person left in the kill ring
	public boolean gameOver() {
		return killRingFirst.next == null;
	}//eo gameOver
	
	//pre : the game is over. returns null if the game is still active
	//post: returns the name of the last person in the kill ring
	public String winner() {
		if (gameOver())
			return killRingFirst.name;
		return null;
	}//eo winner
	
	//pre : the game is still active. throws an IllegalStateException if not.
	//      the person to kill is in the kill ring. throws an IllegalArgumentException if not.
	public void kill(String name) {
		if (gameOver())
			throw new IllegalStateException();
		if (!killRingContains(name))
			throw new IllegalArgumentException();
		AssassinNode current = killRingFirst;
		//cycle through the list until current is the killer and current.next is the victim
		while (current.next != null) {
			if (name.equalsIgnoreCase(current.next.name)) {
				current.next.killer = current.name;//assign the killer of the victim
				AssassinNode temp = current.next;//move the victim to purgatory. the grim reaper is looking directly into the victim's eyes
				current.next = current.next.next;//remove the victim's bonds to the living world
				temp.next = graveyardFirst;//the grim reaper shows its prey where it is about to be sent
				graveyardFirst = temp;//the grim reaper mercilessly drops its pitiful prey into the grave for eternity
				break;
			}//eo if
			current = current.next;
		}//eo while
		//same steps as a normal kill except for using killRingFirst instead of current.next.
		//for the case of the victim being at the front of the list
		if (name.equalsIgnoreCase(killRingFirst.name)) {
			killRingFirst.killer = current.name;
			AssassinNode temp = killRingFirst;
			killRingFirst = killRingFirst.next;
			temp.next = graveyardFirst;
			graveyardFirst = temp;
		}//eo if
	}//eo kill
	
}//eo AssassinManager class