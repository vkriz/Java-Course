package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Pomoćno sučelje koje služi za implementaciju
 * oblikovnog obrasca posjetitelja u model čvorova.
 * 
 * @author Valentina Križ
 *
 */
public interface INodeVisitor {
	/**
	 * Metoda posjećuje text čvor
	 * 
	 * @param node
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * Metoda posjećuje for loop čvor
	 * 
	 * @param node
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * Metoda posjećuje echo čvor
	 * 
	 * @param node
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Metoda posjećuje document čvor
	 * 
	 * @param node
	 */
	public void visitDocumentNode(DocumentNode node);
}
