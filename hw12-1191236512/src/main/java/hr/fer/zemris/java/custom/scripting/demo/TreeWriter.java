package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Razred služi za parsiranje dokumenta
 * i reproduciranje njegovo originalnog sadržaja
 * koristeći oblikovni obrazac posjetitelja.
 * 
 * @author Valentina Križ
 *
 */
public class TreeWriter {
	/**
	 * Pomoćni razred za potrebe implementacije
	 * oblikovnog obrasca posjetitelja.
	 * Implementira sučelje INodeVisitor.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitTextNode(TextNode node) {
			System.out.println(node.toString());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			StringBuilder sb = new StringBuilder();
			sb.append("{$ FOR ");
			sb.append(node.getVariable().asText());
			sb.append(" ");
			sb.append(node.getStartExpression().asText());
			sb.append(" ");
			sb.append(node.getEndExpression().asText());
			sb.append(" ");
			if(node.getStepExpression() != null) {
				sb.append(node.getStepExpression().asText());
			}
			sb.append("$}");
			
			System.out.println(sb.toString());
			
			for(int i = 0, size = node.numberOfChildren(); i < size; ++i) {
				node.getChild(i).accept(this);
			}
			System.out.println("{$END$}");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.println(node.toString());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0, size = node.numberOfChildren(); i < size; ++i) {
				node.getChild(i).accept(this);
			}
		}
	}
	
	
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * Metoda očekuje jedan parametar komandne linije
	 * koji predstavlja ime datoteke koju treba parsirati.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Program prima jedan parametar koji predstavlja ime datoteke.");
			return;
		}
		String docBody;
		try {
			docBody = new String(
					 Files.readAllBytes(Paths.get(args[0])),
					 StandardCharsets.UTF_8
					);
		} catch (IOException e1) {
			System.out.println("Greška pri čitanju datoteke.");
			return;
		}
		
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}
}
