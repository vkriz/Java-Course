package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

public class SmartScriptParser {
	private ObjectStack stack;
	private DocumentNode document;
	
	/**
	 * Konstruktor koji prima string koji treba parsirati.
	 * 
	 * @param docBody string koji treba parsirati
	 */
	public SmartScriptParser(String docBody) {
		stack = new ObjectStack();
		document = new DocumentNode();
		
		SmartScriptLexer lexer = new SmartScriptLexer(docBody);
		
		parse(lexer);
	}
	
	private void parse(SmartScriptLexer lexer) {
		stack.push(document);
		
		try {
			while(lexer.nextToken().getType() != SmartScriptTokenType.EOF) {
				//getNextNode(lexer, document);
				getNextNode(lexer);
			}
		} catch(SmartScriptLexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}
	}
	
	/**
	 * Pomoćna metoda koja dohvaća sljedeći isparsirani čvor u stablu.
	 * 
	 * @param lexer lekser za leksičku analizu
	 * @param parentNode čvor roditelj
	 * @throws SmartScriptParserException ako ne pronađe ispravan čvor
	 */
	//private void getNextNode(SmartScriptLexer lexer, Node parentNode) {
	private void getNextNode(SmartScriptLexer lexer) {
		// if(!getTextNode(lexer, parentNode) && !getEchoNode(lexer, parentNode) && !getForLoopNode(lexer, parentNode)) {
		if(!getTextNode(lexer) && !getEchoNode(lexer) && !getForLoopNode(lexer)) {
			throw new SmartScriptParserException("Greška u parsiranju.");
		}
	}
	
	/**
	 * Pomoćna metoda koja dohvaća sljedeći tag ako je on ispravan END-tag.
	 * 
	 * @param lexer lekser za leksičku analizu
	 * @return true ako je sljedeći tag ispravan END-tag, false inače
	 * @throws SmartScriptParserException ako END-tag nije prazan tag
	 */
	private boolean getEndTag(SmartScriptLexer lexer) {
		if(lexer.getToken().getType() == SmartScriptTokenType.TAGNAME) {
			Object name = lexer.getToken().getValue();
			if(name.equals("END")) {
				// probaj dohvatiti sljedeći token, ako se dogodi greška javi grešku parsiranja
				try {
					// ako nakon {$END ne slijedi $} javi grešku
					if(lexer.nextToken().getType() == SmartScriptTokenType.TAGCLOSING) {
						return true;
					} else {
						throw new SmartScriptParserException("Očekivano $}");
					}
				} catch(SmartScriptLexerException ex) {
					throw new SmartScriptParserException(ex.getMessage());
				}
			}
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja dohvaća text-node ako je on je on sljedeći na redu za parsiranje.
	 * 
	 * @param lexer lekser za leksičku analizu
	 * @param parent čvor roditelj
	 * @return true ako je dohvaćen text-node, false inače
	 */
	//private boolean getTextNode(SmartScriptLexer lexer, Node parent) {
	private boolean getTextNode(SmartScriptLexer lexer) {
		if(lexer.getToken().getType() == SmartScriptTokenType.TEXT) {
			Node child = new TextNode(lexer.getToken().getValue());
			Node parent = (Node)stack.peek();
			parent.addChildNode(child);
			return true;
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda za dodavanje novog elementa u array elemenata.
	 * 
	 * @param elements array u koji se ubacuje
	 * @param newElement element koji se ubacuje
	 * @return array s dodanim elementom
	 */
	private Element[] addToElementArray(Element[] elements, Element newElement) {
		Element[] newArray =  Arrays.copyOf(elements, elements.length + 1);
		newArray[elements.length] = newElement;
		return newArray;
	}
	
	/**
	 * Pomoćna metoda koja dohvaća echo-node ako je on sljedeći na redu za parsiranje
	 * i ispravnog oblika.
	 * 
	 * @param lexer lekser za leksičku analizu
	 * @param parent čvor roditelj
	 * @return true ako je dohvaćen ispravan echo-node, false inače
	 * @throws SmartScriptParserException ako je dohvaćeni echo-node neispravan
	 */
	//private boolean getEchoNode(SmartScriptLexer lexer, Node parent) {
	private boolean getEchoNode(SmartScriptLexer lexer) {
		if(lexer.getToken().getType() == SmartScriptTokenType.TAGNAME) {
			Object name = lexer.getToken().getValue();
			if(name.equals("=")) {
				Element[] elements = new Element[0];
				while(true) {
					Element nextExp = getNextExpression(lexer, true, true, true);
					
					// ako nije TAGCLOSING, spremi expression
					if(nextExp != null) {
						elements = addToElementArray(elements, nextExp);
					} else {
						// ako je TAGCLOSING, gotovi smo
						break;
					}
				}

				Node child = new EchoNode(elements);
				Node parent = (Node)stack.peek();
				parent.addChildNode(child);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja dohvaća forloop-node ako je on sljedeći na redu za parsiranje
	 * i ispravnog oblika.
	 * 
	 * @param lexer lekser za leksičku analizu
	 * @param parent čvor roditelj
	 * @return true ako je dohvaćen ispravan forloop-node, false inače
	 */
	//private boolean getForLoopNode(SmartScriptLexer lexer, Node parent) {
	private boolean getForLoopNode(SmartScriptLexer lexer) {
		if(lexer.getToken().getType() == SmartScriptTokenType.TAGNAME) {
			Object name = lexer.getToken().getValue();
			if(name.equals("FOR")) {
				ForLoopNode child = new ForLoopNode();
				
				if(lexer.nextToken().getType() == SmartScriptTokenType.VARIABLE) {
					child.setVariable(new ElementVariable((String)lexer.getToken().getValue()));
				} else {
					throw new SmartScriptParserException("Očekivana varijabla.");
				}
				
				child.setStartExpression(getNextExpression(lexer, false, false, false));
				child.setEndExpression(getNextExpression(lexer, false, false, false));
				child.setStepExpression(getNextExpression(lexer, true, false, false));
				
				Node parent = (Node)stack.peek();
				parent.addChildNode(child);
				stack.push(child);
				
				// provjeri da li slijedi $}, ako ne - baci grešku
				if(child.getStepExpression() != null && lexer.nextToken().getType() != SmartScriptTokenType.TAGCLOSING) {
					throw new SmartScriptParserException("Očekivano $}");
				}
				
				// dohvati sljedeći token i sve dok ne dođeš do {$END$} čvorove dodaj kao dijete for-taga
				// ako do kraja dokumenta nema niti jednog {$END$} dogodit će se SmartScriptParserException
				lexer.nextToken();
				while(!getEndTag(lexer)) {
					getNextNode(lexer);
					lexer.nextToken();
				}
				
				// kad dođeš do {$END$} makni zadnji čvor sa stoga
				stack.pop();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja dohvaća sljedeći neparsirani expression ako je dozvoljenog tipa.
	 * 
	 * @param lexer lekser za leksičku analizu
	 * @param canBeTagClosing može li biti $}
	 * @param canBeOperator može li biti operator
	 * @param canBeFunction može li biti funkcija
	 * @return novogenerirani expression
	 * @throws SmartScriptParserException ako je generiran expression nedozvoljenog tipa
	 * 			ili je došlo do greške u leksiranju
	 */
	private Element getNextExpression(SmartScriptLexer lexer, boolean canBeTagClosing, boolean canBeOperator, boolean canBeFunction) {
		// ako se dogodila greška u dohvaćanju sljedećeg tokena baci grešku
		try {
			lexer.nextToken(); 
		} catch(SmartScriptLexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}
		
		// ovisno o tipu dohvaćenog tokena vrati odgovarajući element, null ili baci grešku
		switch(lexer.getToken().getType()) {
			case VARIABLE:
				return new ElementVariable((String)lexer.getToken().getValue());
			case DOUBLE:
				return new ElementConstantDouble((Double)lexer.getToken().getValue());
			case INTEGER:
				return new ElementConstantInteger((Integer)lexer.getToken().getValue());
			case STRING:
				return new ElementString((String)lexer.getToken().getValue());
			case TAGCLOSING:
				if(canBeTagClosing) {
					return null;
				} else {
					throw new SmartScriptParserException("Neočekivan token $}.");
				}
			case FUNCTION:
				if(canBeFunction) {
					return new ElementFunction((String)lexer.getToken().getValue());
				} else {
					throw new SmartScriptParserException("Neočekivana pojava funkcije.");
				}
			case OPERATOR:
				if(canBeOperator) {
					return new ElementOperator(lexer.getToken().getValue().toString());
				} else {
					throw new SmartScriptParserException("Neočekivana pojava operatora.");
				}
			default:
				throw new SmartScriptParserException("Neočekivani token.");
		}
	}
	
	/**
	 * Getter za document.
	 * 
	 * @return document čvor
	 */
	public DocumentNode getDocumentNode() {
		return document;
	}
}
