package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Razred koji predstavlja bazu svih čvorova grafa.
 * 
 * @author Valentina Križ
 *
 */
public class Node {
	private ArrayIndexedCollection children;
	
	/**
	 * Metoda za ubacivanje novog čvora u listu djece.
	 * 
	 * @param child čvor koji se ubacuje
	 */
	public void addChildNode(Node child) {
		if(children == null) {
			children = new ArrayIndexedCollection();
		}
		children.add(child);
	}
	
	/**
	 * Metoda vraća broj čvorova u listi djece.
	 * 
	 * @return broj čvorova
	 */
	public int numberOfChildren() {
		if(children == null) {
			return 0;
		}
		return children.size();
	}
	
	/**
	 * Metoda dohvaća n-to dijete čvora, ako ono postoji.
	 * 
	 * @param index pozicija djeteta koje korisnika zanima
	 * @return dijete na toj poziciji
	 * @throws IndexOutOfBounds ako je predan neispravan indeks
	 */
	public Node getChild(int index) { 
		// metoda get u razredu ArrayIndexedCollection brine o ispravnosti indeksa
		return (Node)children.get(index);
	}
	
	/**
	 * Pomoćna metoda za provjeravanje međusobne jednakosti svih
	 * elemenata iz children arraya.
	 * 
	 * @param node čvor s kojim uspoređujemo
	 * @return true ako su svi čvorovi jednaki, false inače
	 */
	protected boolean compareChildren(Node node) {
		if(numberOfChildren() != node.numberOfChildren()) {
			return false;
		}
		for(int i = 0, size = numberOfChildren(); i < size; ++i) {
			// ako nisu ista klasa nisu isti (npr. jedan je TextNode, drugi EchoNode)
			if(getChild(i).getClass() != node.getChild(i).getClass()) {
				return false;
			}
			
			// inače pozovi odgovarajuću equals metodu
			// pri pozivu metode getChild sve vrste čvorova se castaju u Node
			if(getChild(i) instanceof TextNode) {
				if(!((TextNode)getChild(i)).equals((TextNode)node.getChild(i))) {
					return false;
				}
			} else if(getChild(i) instanceof EchoNode) {
				if(!((EchoNode)getChild(i)).equals((EchoNode)node.getChild(i))) {
					return false;
				}
			} else if(getChild(i) instanceof ForLoopNode) {
				if(!((ForLoopNode)getChild(i)).equals((ForLoopNode)node.getChild(i))) {
					return false;
				}
			} else {
				if(!getChild(i).equals(node.getChild(i))) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Metoda koja služi za provjeru jednakosti dvaju čvorova.
	 * 
	 * @param node čvor s kojim uspoređujemo
	 * @return true ako su jednaki, false inače
	 */
	public boolean equals(Node node) {
		return compareChildren(node);
	}
}
