package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Razred UniqueNumber korisnicima omogućava rad s uređenim binarnim stablima.
 * 
 * @author Valentina Križ
 *
 */

public class UniqueNumbers {
	/**
	 * Pomoćni razred koji predstavlja čvor u binarnom stablu.
	 * Sadrži vrijednost koja se nalazi u čvoru te reference na svoje lijevo i desno dijete.
	 */
	public static class TreeNode {
	    TreeNode left;
	    TreeNode right;
	    int value;
	  }

	/**
	 * Metoda dodaje novi čvor sa zadanom vrijednosti u stablo ako već ne postoji
	 * čvor s istom vrijednosti.
	 * 
	 * @param head glava stabla
	 * @param value vrijednost novog čvora
	 * @return glava modificiranog stabla
	 */
	private static TreeNode addNode(TreeNode head, int value) {
		if(head == null) {
			head = new TreeNode();
			head.left = null;
			head.right = null;
			head.value = value;
		} else if(value < head.value) {
			head.left = addNode(head.left, value);
		} else if(value > head.value) {
			head.right = addNode(head.right, value);
		}
		
		return head;
	}
	
	/**
	 * Metoda računa veličinu stabla.
	 * 
	 * @param head glava stabla
	 * @return broj čvorova u stablu
	 */
	private static int treeSize(TreeNode head) {
		if(head == null) {
			return 0;
		}
		if(head.left == null && head.right == null) {
			return 1;
		}

		return treeSize(head.left) + treeSize(head.right) + 1;
	}
	
	/**
	 * Metoda provjerava nalazi li se u stablu čvor sa zadanom vrijednosti.
	 * 
	 * @param head glava stabla
	 * @param value	vrijednost koju tražimo
	 * @return true ako postoji čvor s tom vrijednosti, false inače
	 */
	private static boolean containsValue(TreeNode head, int value) {
		if(head == null) {
			return false;
		}
		if(head.value == value || containsValue(head.left, value) || containsValue(head.right, value)) {
			return true;
		}

		return false;
	}
	
	/**
	 * Metoda koja omogućava ispis svih elemenata uređenog binarnog stabla od najmanjeg do najvećeg.
	 * 
	 * @param head glava stabla
	 * @return String s popisom vrijednosti svih čvorova od najmanje do najveće
	 */
	private static String minToMax(TreeNode head) {
		String output = new String();
		
		if(head == null) {
			return output;
		}
		
		output += minToMax(head.left);
		output += head.value + " ";
		output += minToMax(head.right);
		
		return output;
	}
	
	/**
	 * Metoda koja omogućava ispis svih elemenata uređenog binarnog stabla od najvećeg do najmanjeg.
	 * 
	 * @param head glava stabla
	 * @return String s popisom vrijednosti svih čvorova od najveće do najmanje
	 */
	private static String maxToMin(TreeNode head) {
		String output = new String();
		
		if(head == null) {
			return output;
		}
		
		output += maxToMin(head.right);
		output += head.value + " ";
		output += maxToMin(head.left);
		
		return output;
	}
	
	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args argumenti zadani preko naredbenog retka
	 */
	public static void main(String[] args) {
		TreeNode glava = null;
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("Unesite broj");
			if(sc.hasNextLine()) {
				String input = sc.nextLine().trim();
				if(input.equals("kraj")) {
					break;
				}
				try {
					int number = Integer.parseInt(input);
					if(!containsValue(glava, number)) {
						glava = addNode(glava, number);
						System.out.println("Dodano.");
					} else {
						System.out.println("Broj već postoji. Preskačem.");
					}
				} catch(NumberFormatException exc) {
					System.out.printf(
							"\'%s\' nije cijeli broj.%n",
							input
					);
				}	
			}
		}
		
		System.out.printf(
				"Ispis od najmanjeg: %s%n",
				minToMax(glava)
		);
		System.out.printf(
				"Ispis od najvećeg: %s%n",
				maxToMin(glava)
		);
		
		sc.close();
	}
}
