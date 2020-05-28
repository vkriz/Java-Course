package hr.fer.zemris.java.gui.calc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.DoubleBinaryOperator;

/**
 * Razred predstavlja implementaciju sučelja CalcModel, 
 * služi za izradu programa koji oponaša rad jednostavnog
 * Windows kalkulatora.
 * 
 * @author Valentina Križ
 *
 */
public class CalcModelImpl implements CalcModel {
	/**
	 * Varijabla koja govori da li je
	 * model editabilan.
	 */
	private boolean isEditable;
	
	/**
	 * Varijabla koja govori da li je
	 * trenutni broj pozitivan.
	 */
	private boolean isPositive;
	
	/**
	 * Varijabla u koju je spremljen
	 * trenutni broj u obliku stringa.
	 */
	private String digits;
	
	/**
	 * Trenutni broj.
	 */
	private double number;
	
	/**
	 * Zamrznuta vrijednost.
	 */
	private String frozenValue;
	
	/**
	 * Aktivni operand.
	 */
	private OptionalDouble activeOperand;
	
	/**
	 * Operacija koja se sljedeća izvodi.
	 */
	private DoubleBinaryOperator pendingOperation;
	
	/**
	 * Lista listenera.
	 */
	private List<CalcValueListener> listeners;
	
	/**
	 * Default konstruktor.
	 */
	public CalcModelImpl() {
		listeners = new ArrayList<>();
		clearAll();
	}
	
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Metoda za prikaz instance razreda u obliku Stringa.
	 * 
	 * @return string frozenValue ako je postavljen,
	 * 			inače trenutnu vrijednost
	 */
	@Override
	public String toString() {
		if(!hasFrozenValue()) {
			if(!digits.isBlank()) {
				return digits;
			}
			return isPositive ? "0" : "-0";
		}
		
		return frozenValue;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public double getValue() {
		return number;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void setValue(double value) {
		number = value;
		isPositive = value >= 0;
		// toString za Double.NaN, Double.POSITIVE_INFINITY i Double.NEGATIVE_INFINITY
		// vraća redom "NaN", "Infinity" i "-Infinity"
		digits = Double.toString(value);
		isEditable = false;
		
		notifyListeners();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void clear() {
		isEditable = true;
		isPositive = true;
		digits = "";
		number = 0;
		
		notifyListeners();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void clearAll() {
		clear();
		clearActiveOperand();
		frozenValue = null;
		pendingOperation = null;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void swapSign() throws CalculatorInputException {
		if(!isEditable) {
			throw new CalculatorInputException();
		}
		
		if(!digits.isBlank()) {
			if(isPositive) {
				digits = "-" + digits;
			} else {
				digits = digits.substring(1);
			}
		}
		
		isPositive = !isPositive;
		number = -number;
		frozenValue = null;
		
		notifyListeners();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if(!isEditable || digits.contains(".") || digits.isEmpty()) {
			throw new CalculatorInputException();
		}
		
		digits += ".";
		frozenValue = null;
		
		notifyListeners();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if(!isEditable) {
			throw new CalculatorInputException();
		}
		
		if(digit < 0 || digit > 9) {
			throw new IllegalArgumentException();
		}
		
		frozenValue = null;
		
		if(digits.equals("0")) {
			digits = Integer.toString(digit);
			notifyListeners();
			return;
		}
		
		digits += digit;
		try {
			number = Double.parseDouble(digits);
			if(Double.isInfinite(number)) {
				throw new CalculatorInputException();
			}
		} catch(NumberFormatException ex) {
			throw new CalculatorInputException();
		}
		
		notifyListeners();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public boolean isActiveOperandSet() {
		return activeOperand.isPresent();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public double getActiveOperand() throws IllegalStateException {
		return activeOperand.orElseThrow(IllegalStateException::new);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = OptionalDouble.of(activeOperand);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void clearActiveOperand() {
		activeOperand = OptionalDouble.empty();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;
		
		notifyListeners();
	}
	
	/**
	 * Metoda postavlja zamrznutu vrijednost
	 * na predanu vrijednost.
	 * 
	 * @param value
	 */
	public void freezeValue(String value) {
		frozenValue = value;
		
		notifyListeners();
	}
	
	/**
	 * Metoda za provjeru postoji li 
	 * postavljena zamrznuta vrijednost.
	 * 
	 * @return
	 */
	public boolean hasFrozenValue() {
		if(frozenValue == null) {
			return false;
		} else {
			return !frozenValue.isBlank();
		}
	}
	
	/**
	 * Metoda obaviještava sve listenere da je došlo
	 * do promjene u vrijednosti.
	 */
	private void notifyListeners() {
		for(CalcValueListener listener : listeners) {
			listener.valueChanged(this);
		}
	}
}
