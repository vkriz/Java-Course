package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Razred koji služi za demonstraciju korištenja razreda ObjectStack.
 * Kao argument komandne linije predaje se jedan string koji sadrži postfix
 * izraz koji treba evaluirati. Dopuštene operacije su +, -, *, / i %
 * 
 * @author Valentina Križ
 *
 */
public class StackDemo {
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Program se mora pokrenuti s točno 1 parametrom.");
			return;
		}
		
		String[] expression = args[0].split("\\s+");
		ObjectStack stack = new ObjectStack();
		
		for(int i = 0, len = expression.length; i < len; ++i) {
			try {
				int num = Integer.parseInt(expression[i]);
				stack.push(Integer.valueOf(num));
			} catch(Exception e) {
				int second = ((Number) stack.pop()).intValue();
				int first = ((Number) stack.pop()).intValue();
				switch(expression[i]) {
				  case "+":
					  stack.push(Integer.valueOf(first + second));
					  break;
				  case "-":
					  stack.push(Integer.valueOf(first - second));
					  break;
				  case "*":
					  stack.push(Integer.valueOf(first * second));
					  break;
				  case "/":
					  if(second == 0) {
						  System.out.println("Nije dozvoljeno dijeljenje s nulom!");
						  return;
					  }
					  stack.push(Integer.valueOf(first / second));
					  break;
				  case "%":
					  if(second == 0) {
						  System.out.println("Ostatak dijeljenja s nulom nije definiran.");
						  return;
					  }
					  stack.push(Integer.valueOf(first % second));
					  break;
				  default:
					  System.out.println("Neispravan izraz.");
					  return;
				}
			}
		}
		if(stack.size() != 1) {
			System.out.println("Neispravan izraz.");
		} else {
			System.out.println("Expression evaluates to " + stack.pop() + ".");
		}
	}
}
