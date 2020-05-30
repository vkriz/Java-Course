package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred predstavlja engine čija
 * je zadaća izvršavanje dokumenta
 * čije parsirano stablo dobije.
 * 
 * @author Valentina Križ
 *
 */
public class SmartScriptEngine {
	/**
	 * parsirani dokument
	 */
	private DocumentNode documentNode;
	
	/**
	 * referenca na RequestContext
	 */
	private RequestContext requestContext;
	
	/**
	 * pomoćni multistack za izvršavanje 
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/**
	 * Posjetitelj pomoću kojeg se izvršava
	 * parsirani dokument.
	 */
	private INodeVisitor visitor = new INodeVisitor() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			
			// inicijaliziraj varijablu s početnom vrijednosti
			multistack.push(variable, new ValueWrapper(node.getStartExpression().asText()));
			
			// sve dok je varijabla <= konačne vrijednosti
			while(multistack.peek(variable).numCompare(node.getEndExpression().asText()) <= 0) {
				// posjeti svu djecu
				for(int i = 0, numChildren = node.numberOfChildren(); i < numChildren; ++i) {
					node.getChild(i).accept(this);
				}
				
				// inkrement varijable
				multistack.peek(variable).add(node.getStepExpression().asText());
			}
			
			multistack.pop(variable);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<>();
			
			for(Element element : node.getElements()) {
				if(element instanceof ElementConstantDouble) {
					stack.push(((ElementConstantDouble) element).getValue());
				}
				
				if(element instanceof ElementConstantInteger) {
					stack.push(((ElementConstantInteger) element).getValue());
				}
				
				if(element instanceof ElementString) {
					stack.push(((ElementString) element).getValue());
				}
				
				if(element instanceof ElementVariable) {
					stack.push(multistack.peek(((ElementVariable) element).getName()).getValue());
				}
				
				if(element instanceof ElementOperator) {
					String symbol = ((ElementOperator) element).getSymbol();
					ValueWrapper firstOperand = new ValueWrapper(stack.pop());
					ValueWrapper secondOperand = new ValueWrapper(stack.pop());
					
					switch(symbol) {
						case "+":
							firstOperand.add(secondOperand);
							break;
						case "-":
							firstOperand.subtract(secondOperand);
							break;
						case "/":
							firstOperand.divide(secondOperand);
							break;
						case "*":
							firstOperand.multiply(secondOperand);
							break;
					}
					
					stack.push(firstOperand.getValue());
				}
				
				if(element instanceof ElementFunction) {
					String functionName = ((ElementFunction) element).getName();
					Object arg1;
					Object arg2;
					
					switch(functionName) {
						case("sin"):
							stack.push(Math.sin(Math.toRadians(Double.parseDouble(stack.pop().toString()))));
							break;
							
						case("decfmt"):
							arg2 = stack.pop(); // f
							arg1 = stack.pop(); // x
							
							DecimalFormat f = new DecimalFormat(arg2.toString());
							stack.push(f.format(arg1));
							break;
							
						case("dup"):
							stack.push(stack.peek());
							break;
							
						case("swap"):
							arg1 = stack.pop();
							arg2 = stack.pop();
							
							stack.push(arg1);
							stack.push(arg2);
							break;
							
						case("setMimeType"):
							requestContext.setMimeType(stack.pop().toString());
							break;
							
						case("paramGet"):
							arg2 = stack.pop(); // defValue
							arg1 = stack.pop();	// name
							
							Object param = requestContext.getParameter(arg1.toString());
							stack.push(param != null ? param : arg2);
							break;
							
						case("pparamGet"):
							arg2 = stack.pop(); // defValue
							arg1 = stack.pop();	// name
							
							Object pparam = requestContext.getPersistentParameter(arg1.toString());
							stack.push(pparam != null ? pparam : arg2);
							break;
							
						case("pparamSet"):
							arg2 = stack.pop(); // name
							arg1 = stack.pop(); // value
							
							requestContext.setPersistentParameter(arg2.toString(), arg1.toString());
							break;
							
						case("pparamDel"):
							arg1 = stack.pop(); // name
						
							requestContext.removePersistentParameter(arg1.toString());
							break;
							
						case("tparamGet"):
							arg2 = stack.pop(); // defValue
							arg1 = stack.pop();	// name
							
							Object tparam = requestContext.getTemporaryParameter(arg1.toString());
							stack.push(tparam != null ? tparam : arg2);
							break;
							
						case("tparamSet"):
							arg2 = stack.pop(); // name
							arg1 = stack.pop(); // value
							
							requestContext.setTemporaryParameter(arg2.toString(), arg1.toString());
							break;
						
						case("tparamDel"):
							arg1 = stack.pop(); // name
						
							requestContext.removeTemporaryParameter(arg1.toString());
					}
				}
			}
			
			for(Object element : stack) {
				try {
					requestContext.write(element.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0, numChildren = node.numberOfChildren(); i < numChildren; ++i) {
				node.getChild(i).accept(this);
			}
		}
	};
	
	/**
	 * Konstruktor
	 * 
	 * @param documentNode
	 * @param requestContext
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}
	
	/**
	 * Metoda za pokretanje engine-a
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
