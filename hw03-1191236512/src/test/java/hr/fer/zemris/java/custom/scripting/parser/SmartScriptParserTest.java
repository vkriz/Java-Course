package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

public class SmartScriptParserTest {
	private String loader(String filename) {
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
		    if(is==null) throw new RuntimeException("Datoteka "+filename+" je nedostupna.");
		    byte[] data = this.getClass().getClassLoader().getResourceAsStream(filename).readAllBytes();
		    String text = new String(data, StandardCharsets.UTF_8);
		    return text;
		  } catch(IOException ex) {
		    throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		  }
	}
	
	@Test
	public void emptyString() {
		String document = "";
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		
		assertEquals(root.numberOfChildren(), 0);
	}
	
	@Test
	public void textOnly() {
		String document = "Ovo je neki test tekst \\\\";
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		
		assertEquals(root.numberOfChildren(), 1);
		assertTrue(root.getChild(0) instanceof TextNode);
	}
	
	@Test
	public void forLoopThreeParameters() {
		String document = "{$ FOR i -1 10 1 $}{$END$}";
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		assertEquals(root.numberOfChildren(), 1);
		assertTrue(root.getChild(0) instanceof ForLoopNode);
	}
	
	@Test
	public void testExampleFromHomework() {
		String document = loader("primjer1.txt");
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		
		assertTrue(root.getChild(0) instanceof TextNode);
		
		assertTrue(root.getChild(1) instanceof ForLoopNode);
		ForLoopNode ch1 = (ForLoopNode)root.getChild(1);
		assertEquals(ch1.getVariable().getName(), "i");
		assertTrue(ch1.getStartExpression() instanceof ElementConstantInteger);
		assertTrue(ch1.getEndExpression() instanceof ElementConstantInteger);
		assertTrue(ch1.getStepExpression() instanceof ElementConstantInteger);
		
		assertTrue(root.getChild(1).getChild(0) instanceof TextNode);
		
		assertTrue(root.getChild(1).getChild(1) instanceof EchoNode);
		EchoNode ch11 = (EchoNode)root.getChild(1).getChild(1);
		assertEquals(ch11.getElements().length, 1);
		assertTrue(ch11.getElements()[0] instanceof ElementVariable);
	
		assertTrue(root.getChild(1).getChild(2) instanceof TextNode);
		
		assertTrue(root.getChild(2) instanceof TextNode);
		
		assertTrue(root.getChild(3) instanceof ForLoopNode);
		ForLoopNode ch3 = (ForLoopNode)root.getChild(3);
		assertEquals(ch3.getVariable().getName(), "i");
		assertTrue(ch3.getStartExpression() instanceof ElementConstantInteger);
		assertTrue(ch3.getEndExpression() instanceof ElementConstantInteger);
		assertTrue(ch3.getStepExpression() instanceof ElementConstantInteger);
		
		assertTrue(root.getChild(3).getChild(0) instanceof TextNode);
		
		assertTrue(root.getChild(3).getChild(1) instanceof EchoNode);
		EchoNode ch31 = (EchoNode)root.getChild(3).getChild(1);
		assertEquals(ch31.getElements().length, 1);
		assertTrue(ch31.getElements()[0] instanceof ElementVariable);
		
		assertTrue(root.getChild(3).getChild(2) instanceof TextNode);
		
		assertTrue(root.getChild(3).getChild(3) instanceof EchoNode);
		EchoNode ch33 = (EchoNode)root.getChild(3).getChild(3);
		assertEquals(ch33.getElements().length, 6);
		assertTrue(ch33.getElements()[0] instanceof ElementVariable);
		assertTrue(ch33.getElements()[1] instanceof ElementVariable);
		assertTrue(ch33.getElements()[2] instanceof ElementOperator);
		assertTrue(ch33.getElements()[3] instanceof ElementFunction);
		assertTrue(ch33.getElements()[4] instanceof ElementString);
		assertTrue(ch33.getElements()[5] instanceof ElementFunction);
		
		assertTrue(root.getChild(3).getChild(4) instanceof TextNode);
	}
	
	@Test
	public void testExtraExample1() {
		String document = loader("extra/primjer1.txt");
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		
		assertTrue(root.getChild(0) instanceof TextNode);
		assertEquals(root.numberOfChildren(), 1);
		assertEquals(root.getChild(0).numberOfChildren(), 0);
	}
	
	@Test
	public void testExtraExample2() {
		String document = loader("extra/primjer2.txt");
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		
		assertTrue(root.getChild(0) instanceof TextNode);
		assertEquals(root.numberOfChildren(), 1);
		assertEquals(root.getChild(0).numberOfChildren(), 0);
	}
	
	@Test
	public void testExtraExample3() {
		String document = loader("extra/primjer3.txt");
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		
		assertTrue(root.getChild(0) instanceof TextNode);
		assertEquals(root.numberOfChildren(), 1);
		assertEquals(root.getChild(0).numberOfChildren(), 0);
	}
	
	@Test
	public void testExtraExample4() {
		String document = loader("extra/primjer4.txt");
		assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(document));
	}
	
	@Test
	public void testExtraExample5() {
		String document = loader("extra/primjer5.txt");
		assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(document));
	}
	
	@Test
	public void testExtraExample6() {
		String document = loader("extra/primjer6.txt");
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		
		assertEquals(root.numberOfChildren(), 2);
		assertTrue(root.getChild(0) instanceof TextNode);
		assertTrue(root.getChild(1) instanceof EchoNode);
	}
	
	@Test
	public void testExtraExample7() {
		String document = loader("extra/primjer7.txt");
		SmartScriptParser parser = null;
		parser = new SmartScriptParser(document);
		DocumentNode root = parser.getDocumentNode();
		
		assertEquals(root.numberOfChildren(), 2);
		assertTrue(root.getChild(0) instanceof TextNode);
		assertTrue(root.getChild(1) instanceof EchoNode);
	}
	
	@Test
	public void testExtraExample8() {
		String document = loader("extra/primjer8.txt");
		assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(document));
	}
	
	@Test
	public void testExtraExample9() {
		String document = loader("extra/primjer9.txt");
		assertThrows(SmartScriptParserException.class, () ->  new SmartScriptParser(document));
	}
}
