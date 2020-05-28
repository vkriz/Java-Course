package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Razred implementira jednostavni kalkulator.
 * Broj se unosi klikanjem po gumbina sa znamenkama, 
 * a ekran prikazuje trenutni broj.
 * Podržane su osnovne matematikčke operacije poput:
 * +, -, *, /, sin, cos, tan, ctg, ln, log, pow, e^x...
 * 
 * @author Valentina Križ
 *
 */
public class Calculator extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Model kalkulatora.
	 */
	private static CalcModelImpl model;
	
	/**
	 * Container u kojem se prikazuje kalkulator.
	 */
	private Container cp;
	
	/**
	 * Boja gumba.
	 */
	private final static Color COLOR = Color.lightGray;
	
	/**
	 * Stog.
	 */
	private Deque<Double> stack;
	
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}
	
	/**
	 * Default konstruktor, stvara novi prozor
	 * i puni ga sadržajem.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(700, 500);
		setTitle("Java Calculator v1.0");
		model = new CalcModelImpl();

		stack = new ArrayDeque<>();
		initGUI();
	}

	/**
	 * Metoda stvara novi container s upravljačem
	 * razmještaja CalcLayout i dodaje potrebne gumbe i labele.
	 */
	private void initGUI() {
		cp = getContentPane();
		cp.setLayout(new CalcLayout(3));
		
		// dodavanje displaya 
		JLabel display = new JLabel("0", SwingConstants.RIGHT);
		display.setBackground(Color.YELLOW);
		display.setOpaque(true);
		display.setFont(display.getFont().deriveFont(30f));
		display.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		model.addCalcValueListener(m -> display.setText(model.toString()));
		cp.add(display, new RCPosition(1, 1));
		
		// dodavanje chechboxa za invertiranje operatora
		JCheckBox inv = new JCheckBox("Inv");
		cp.add(inv, new RCPosition(5, 7));
		
		addSimpleButtons();
		addDigitButtons();
		addBinaryOperationButtons(inv);
		addUnaryOperationButtons(inv);
	}

	/**
	 * Metoda dodaje "jednostavne" gumbe u kalkulator.
	 * To su: 
	 * 		- "=" -> izračun pending operacije
	 * 		- "clr" -> brisanje trenutnog broja
	 * 		- "reset" -> vraćanje stanja kalkulatora na početne postavke
	 * 		- "push" -> stavljanje trenutne vrijednosti na vrh stoga
	 * 		- "pop" -> postavljanje trenutne vrijednosti na onu s vrha stoga
	 * 		- "+/-" -> promjena predznaka
	 * 		- "." -> dodavanje decimalne točke
	 */
	private void addSimpleButtons() {
		// dodavanje gumba = 
		JButton button = new JButton("=");
		ActionListener listener = e -> {
			if(!model.isActiveOperandSet()) {
				model.setActiveOperand(model.getValue());
			} else {
				DoubleBinaryOperator pendingOperator = model.getPendingBinaryOperation();
				double result = model.getValue();
				if(pendingOperator != null) {
					result = pendingOperator.applyAsDouble(model.getActiveOperand(), model.getValue());
				}
				
				model.setActiveOperand(result);
				model.setPendingBinaryOperation(null);
			}
			model.setValue(model.getActiveOperand());
		};
		button.addActionListener(listener);
		button.setBackground(COLOR);
		cp.add(button, new RCPosition(1, 6));
			
		// clr
		button = new JButton("clr");
		listener = e -> {
			model.clear();
		};
		button.addActionListener(listener);
		button.setBackground(COLOR);
		cp.add(button, new RCPosition(1, 7));
			
		// reset
		button = new JButton("reset");
		listener = e -> {
			model.clearAll();
		};
		button.addActionListener(listener);
		button.setBackground(COLOR);
		cp.add(button, new RCPosition(2, 7));
		
		// push
		button = new JButton("push");
		listener = e -> {
			stack.push(model.getValue());
		};
		button.addActionListener(listener);
		button.setBackground(COLOR);
		cp.add(button, new RCPosition(3, 7));
		
		// pop
		button = new JButton("pop");
		listener = e -> {
			model.setValue(stack.pop());
		};
		button.addActionListener(listener);
		button.setBackground(COLOR);
		cp.add(button, new RCPosition(4, 7));
			
		// +/-
		button = new JButton("+/-");
		listener = e -> {
			model.swapSign();
		};
		button.addActionListener(listener);
		button.setBackground(COLOR);
		cp.add(button, new RCPosition(5, 4));
		
		// .
		button = new JButton(".");
		listener = e -> {
			model.insertDecimalPoint();
		};
		button.addActionListener(listener);
		button.setBackground(COLOR);
		cp.add(button, new RCPosition(5, 5));
	}

	/**
	 * Metoda u kalkulator dodaje gumbe za sve unarne operatore.
	 * Prima instancu JCheckBox razreda na čiji klik dolazi do
	 * invertiranja operatora.
	 * 
	 * Podržani unarni operatori i njihovi inverzi su:
	 * 		- sin i arcsin
	 * 		- cos i arccos
	 * 		- tan i arctan
	 * 		- ctg i arcctg
	 * 		- log i 10^x
	 * 		- ln i e^x
	 * 
	 * @param inverter
	 */
	private void addUnaryOperationButtons(JCheckBox inverter) {
		// na klik gumba izvrši operaciju nad trenutnom vrijednosti
		ActionListener listener = e -> {
			UnaryOperatorButton source = (UnaryOperatorButton) e.getSource();
			model.setValue(source.applyOperator());
		};
		
		UnaryOperatorButton button = new UnaryOperatorButton("1/x", "1/x", x -> 1 / x, x -> 1 / x, inverter);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(2, 1));
		
		button = new UnaryOperatorButton("sin", "arcsin", Math::sin, Math::asin, inverter);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(2, 2));
		
		button = new UnaryOperatorButton("cos", "arccos", Math::cos, Math::acos, inverter);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(3, 2));
		
		button = new UnaryOperatorButton("tan", "arctan", Math::tan, Math::atan, inverter);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(4, 2));
		
		button = new UnaryOperatorButton("ctg", "arcctg", x -> 1 / Math.tan(x), x -> Math.atan(1 / x), inverter);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(5, 2));
		
		button = new UnaryOperatorButton("log", "10^x", Math::log10, x -> Math.pow(10, x), inverter);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(3, 1));
		
		button = new UnaryOperatorButton("ln", "e^x", Math::log, x -> Math.pow(Math.E, x), inverter);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(4, 1));
	}

	/**
	 * Metoda u kalkulator dodaje gumbe za sve binarne operatore.
	 * Prima instancu JCheckBox razreda na čiji klik dolazi do
	 * invertiranja operatora ako je on invertibilan.
	 * 
	 * Podržani binarni operatori  su +, -, *, /,
	 * a invertibilni binarni operator je x^n s inverzom x^(1/n)
	 * 
	 * @param inverter
	 */

	private void addBinaryOperationButtons(JCheckBox inverter) {
		ActionListener listener = e -> {
			// ako ne postoji aktivni operand spremi trenutnu vrijednost
			if(!model.isActiveOperandSet()) {
				model.setActiveOperand(model.getValue());	
			} else {
				// inace, pogledaj postoji li već operacija koja se treba izvršiti i izvrši ju 
				// rezultat spremi kao novi aktivni operand
				DoubleBinaryOperator operator = model.getPendingBinaryOperation();
				if(operator != null) {
					double result = operator.applyAsDouble(model.getActiveOperand(), Double.valueOf(model.toString()));
					model.setActiveOperand(result);
					model.setValue(result);
					model.freezeValue(model.toString());
				}
			}
			
			// ovisno da li je source BinaryOperatorButton ili InvertibleBinaryOperatorButton
			if(e.getSource() instanceof BinaryOperatorButton) {
				BinaryOperatorButton source = (BinaryOperatorButton) e.getSource();
				model.setPendingBinaryOperation(source.getOperator());
			} else {
				InvertibleBinaryOperatorButton source = (InvertibleBinaryOperatorButton) e.getSource();
				model.setPendingBinaryOperation(source.getOperator());
			}
			
			String toFreeze = model.toString();
			model.clear();
			model.freezeValue(toFreeze);
		};
		
		// +
		BinaryOperatorButton button = new BinaryOperatorButton("+", (x, y) -> x + y);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(5, 6));
		
		// -
		button = new BinaryOperatorButton("-", (x, y) -> x - y);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(4, 6));
		
		// *
		button = new BinaryOperatorButton("*", (x, y) -> x * y);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(3, 6));
		
		// /
		button = new BinaryOperatorButton("/", (x, y) -> x / y);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(2, 6));
		
		// x^n
		InvertibleBinaryOperatorButton specialButton = new InvertibleBinaryOperatorButton("x^n", "x^(1/n)", (x, n) -> Math.pow(x, n), (x, n) -> Math.pow(x, 1 / n), inverter);
		specialButton.addActionListener(listener);
		specialButton.setBackground(COLOR);
		cp.add(specialButton, new RCPosition(5, 1));
	}

	/**
	 * Metoda dodaje gumbe za unos znamenki od 0 do 9.
	 */
	private void addDigitButtons() {
		// na klik gumba dodaj odabranu znamenku
		ActionListener listener = e -> {
			DigitButton source = (DigitButton) e.getSource();
			model.insertDigit(source.digit);
		};
		
		DigitButton button = new DigitButton(0);
		button.addActionListener(listener);
		cp.add(button, new RCPosition(5, 3));
		
		// odrediti pravu poziciju gumba
		for(int i = 1; i <= 9; ++i) {
			button = new DigitButton(i);
			button.addActionListener(listener);
			cp.add(button, new RCPosition(4 - (i - 1) / 3, (3 + ((i - 1) % 3))));
		}
	}
	
	/**
	 * Razred predstavlja gumb koji sadrži znamenku.
	 * Nasljeđuje razred JButton.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class DigitButton extends JButton {
		private static final long serialVersionUID = 1L;
		/**
		 * Znamenka na gumbu.
		 */
		private int digit;
		
		/**
		 * Konstruktor koji prima znamenku.
		 * 
		 * @param digit
		 */
		public DigitButton(int digit) {
			super(Integer.toString(digit));
			this.digit = digit;
			setBackground(COLOR);
			setFont(getFont().deriveFont(30f));
		}
	}
	
	/**
	 * Razred predstavlja gumb koji sadrži binarni operator.
	 * Nasljeđuje razred JButton.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class BinaryOperatorButton extends JButton {
		private static final long serialVersionUID = 1L;
		/**
		 * Operator.
		 */
		private DoubleBinaryOperator operator;
		
		/**
		 * Konstruktor koji prima ime operatora i sam operator.
		 * 
		 * @param text
		 * @param operator
		 */
		public BinaryOperatorButton(String text, DoubleBinaryOperator operator) {
			super(text);
			this.operator = operator;
			setBackground(COLOR);
		}

		/**
		 * Getter za varijablu operator.
		 * 
		 * @return operator
		 */
		public DoubleBinaryOperator getOperator() {
			return operator;
		}
	}

	/**
	 * Razred predstavlja gumb koji sadrži invertabilni binarni
	 * operator.
	 * Nasljeđuje razred JButton.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class InvertibleBinaryOperatorButton extends JButton {
		private static final long serialVersionUID = 1L;
		/**
		 * Operator.
		 */
		private DoubleBinaryOperator operator;
		
		/**
		 * Invertirani operator.
		 */
		private DoubleBinaryOperator invertedOperator;
		
		/**
		 * Ime operatora.
		 */
		private String operatorName;
		
		/**
		 * Ime invertiranog operatora.
		 */
		private String invertedOperatorName;
		
		/**
		 * Varijabla koja govori da li je zadani CheckBox
		 * odabran ili ne.
		 */
		private boolean invertedChecked;
		
		/**
		 * Konstruktor koji prima ime operatora, sam operator, ime
		 * invertibilnog operatora, sam invertibilni operator i instancu 
		 * razreda JCheckBox na čiji klik se invertira operator.
		 * 
		 * @param operatorName
		 * @param invertedOperatorName
		 * @param operator
		 * @param invertedOperator
		 * @param inverter
		 */
		public InvertibleBinaryOperatorButton(String operatorName, String invertedOperatorName, DoubleBinaryOperator operator, DoubleBinaryOperator invertedOperator, JCheckBox inverter) {
			super();
			this.operator = operator;
			this.invertedOperator = invertedOperator;
			this.operatorName = operatorName;
			this.invertedOperatorName = invertedOperatorName;
			this.invertedChecked = false;
			changeDisplayedName();
			setBackground(COLOR);
			inverter.addItemListener(e -> {
				invertedChecked = !invertedChecked;
				changeDisplayedName();
			});
		}
		
		/**
		 * Metoda dohvaća trenutno aktivni operator.
		 * 
		 * @return operator ako CheckBox nije označen,
		 * 			invertirani operator inače
		 */
		public DoubleBinaryOperator getOperator() {
			return invertedChecked ? invertedOperator : operator;
		}
		
		/**
		 * Metoda za promjenu imena operatora
		 * koji se prikazuje na gumbu.
		 * Prikazuje se ime operatora CheckBox nije označen,
		 * a ime invertiranog operatora inače.
		 */
		public void changeDisplayedName() {
			if(invertedChecked) {
				setText(invertedOperatorName);
			} else {
				setText(operatorName);
			}
		}
	}
	
	/**
	 * Razred predstavlja gumb koji sadrži invertabilni unarni
	 * operator.
	 * Nasljeđuje razred JButton.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class UnaryOperatorButton extends JButton {
		private static final long serialVersionUID = 1L;
		/**
		 * Operator.
		 */
		private DoubleUnaryOperator operator;
		
		/**
		 * Invertirani operator.
		 */
		private DoubleUnaryOperator invertedOperator;
		
		/**
		 * Ime operatora.
		 */
		private String operatorName;
		
		/**
		 * Ime invertiranog operatora.
		 */
		private String invertedOperatorName;
		
		/**
		 * Varijabla koja govori da li je zadani CheckBox
		 * odabran ili ne.
		 */
		private boolean invertedChecked;
		
		/**
		 * Konstruktor koji prima ime operatora, sam operator, ime
		 * invertibilnog operatora, sam invertibilni operator i instancu 
		 * razreda JCheckBox na čiji klik se invertira operator.
		 * 
		 * @param operatorName
		 * @param invertedOperatorName
		 * @param operator
		 * @param invertedOperator
		 * @param inverter
		 */
		public UnaryOperatorButton(String operatorName, String invertedOperatorName, DoubleUnaryOperator operator, DoubleUnaryOperator invertedOperator, JCheckBox inverter) {
			super();
			this.operator = operator;
			this.invertedOperator = invertedOperator;
			this.operatorName = operatorName;
			this.invertedOperatorName = invertedOperatorName;
			this.invertedChecked = false;
			changeDisplayedName();
			setBackground(COLOR);
			inverter.addItemListener(e -> {
				invertedChecked = !invertedChecked;
				changeDisplayedName();
			});
		}

		/**
		 * Metoda izvršava operaciju nad trenutnom vrijednosti
		 * u kalkulatoru.
		 * 
		 * @return rezultat operacije
		 */
		public double applyOperator() {
			if(invertedChecked) {
				return invertedOperator.applyAsDouble(Calculator.model.getValue());
			}
			
			return operator.applyAsDouble(Calculator.model.getValue());
		}
		
		/**
		 * Metoda za promjenu imena operatora
		 * koji se prikazuje na gumbu.
		 * Prikazuje se ime operatora CheckBox nije označen,
		 * a ime invertiranog operatora inače.
		 */
		public void changeDisplayedName() {
			if(invertedChecked) {
				setText(invertedOperatorName);
			} else {
				setText(operatorName);
			}
		}
	}
}
