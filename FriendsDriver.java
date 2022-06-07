package friends;


import java.io.*;
import java.util.*;

import friends.Friends;
import friends.Graph;

public class FriendsDriver {
	
	private static void cliquesTest(Graph g, Scanner sc) {
		sc.nextLine();
		System.out.print("School name: ");
		String school = sc.nextLine();
		ArrayList<ArrayList<String>> list = Friends.cliques(g, school);
		for (int i = 0; i < list.size(); i++) {
			ArrayList<String> temp = list.get(i);
			Collections.sort(temp);
			System.out.print(temp.toString() + "\n");
		}
	}
	
	private static void connectionsTest(Graph g) {
		ArrayList<String> list = Friends.connectors(g);
		Collections.sort(list);
		System.out.print(list.toString() + "\n");
	}
	
	private static void shortestChainTest(Graph g, Scanner sc) {
		sc.nextLine();
		System.out.print("Start: ");
		String start = sc.nextLine();
		System.out.print("End: ");
		String end = sc.nextLine();
		ArrayList<String> list = Friends.shortestChain(g, start, end);
		System.out.print(list.toString() + "\n");
	}
	
	private static void fileTester(File file, Scanner sc) throws FileNotFoundException, NoSuchElementException{
		Scanner fileReader = new Scanner(file);
		Graph g = new Graph(fileReader);
		fileReader.close();
		String option = "";
		while (true) {
			System.out.print("(1) shortestChain\t(2) cliques\t(3) connectors\t(4) back to menu\t(5) quit: ");
			option = sc.next();
			boolean backToMenu = false;
			switch (option) {
			case "1" : 
				shortestChainTest(g,sc);
				break;
			case "2" : 
				cliquesTest(g, sc);
				break;
			case "3" : 
				connectionsTest(g);
				break;
			case "4" : 
				backToMenu = true;
				break;
			case "5" : 
				System.exit(0);
			default :
				System.out.print("You have not entered a valid option\n");
			}
			if (backToMenu) break;
		}
		
	}
	
	private static void driver() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("(1) to enter a file to scan or enter (2) to quit: ");
			String option = " ";
			String fileName = " ";
			try {
				option = sc.next();
				if ("2".equals(option)) break;
				if (!"1".equals(option)) throw new InputMismatchException("Sorry, but you have entered an invalid option. Try again\n");
				System.out.print("Enter the name of your file: ");
				fileName = sc.next();
				fileTester(new File(fileName), sc);
			} catch (FileNotFoundException e) {
				System.out.print(fileName + " is not a valid file\n");
				continue;
			} catch (NoSuchElementException n) {
				if (n instanceof InputMismatchException) {
					System.out.print(n.getMessage());
				} else System.out.print(fileName + " is missing information\n");
				continue;
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		driver();
	}
}
