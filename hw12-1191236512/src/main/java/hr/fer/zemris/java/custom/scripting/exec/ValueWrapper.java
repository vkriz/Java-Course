package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BinaryOperator;

/**
 * Pomoćni razred koji služi kao omotač
 * oko objekta koji predstavlja neku vrijednost.
 * Prilikom izvršavanja (ako je objekt tipa Integer,
 * Double ili String koji predstavlja numeričku vrijednost),
 * omogućeno je izvođenje osnovnih aritmetičkih operacija.
 * Također, omogućeno je i uspoređivanje vrijednosti.
 * Vrijednost null tretira se kao Integer 0.
 * 
 * @author Valentina Križ
 *
 */
public class ValueWrapper {
	/**
	 * Vrijednost 
	 */
	private Object value;
	
	/**
	 * Konstruktor, prima vrijednost
	 * 
	 * @param value
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}
	
	/**
	 * Getter za varijablu value
	 * 
	 * @return vrijednost varijable value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Setter za varijablu value
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Metoda dodaje varijabli value
	 * vrijednost predanog objekta ako su 
	 * obe varijable ispravnog tipa.
	 * 
	 * @param incValue
	 */
	public void add(Object incValue) {
		BinaryOperator<Double> operator = (x, y) -> x + y;
		calculate(incValue, operator);
	}
	
	/**
	 * Metoda oduzima od varijable value
	 * vrijednost predanog objekta ako su 
	 * obe varijable ispravnog tipa.
	 * 
	 * @param decValue
	 */
	public void subtract(Object decValue) {
		BinaryOperator<Double> operator = (x, y) -> x - y;
		calculate(decValue, operator);
	}
	
	/**
	 * Metoda množi varijablu value s
	 * vrijednosti predanog objekta ako su 
	 * obe varijable ispravnog tipa.
	 * 
	 * @param mulValue
	 */
	public void multiply(Object mulValue) {
		BinaryOperator<Double> operator = (x, y) -> x * y;
		calculate(mulValue, operator);
	}
	
	/**
	 * Metoda dijeli varijablu value s
	 * vrijednosti predanog objekta ako su 
	 * obe varijable ispravnog tipa.
	 * 
	 * @param divValue
	 */
	public void divide(Object divValue) {
		Object numDivValue = getNumericalValue(divValue);
		if(numDivValue.equals(Integer.valueOf(0)) || numDivValue.equals(Double.valueOf(0))) {
			throw new IllegalArgumentException("You cannot divide by zero.");
		}
		
		BinaryOperator<Double> operator = (x, y) -> x / y;
		calculate(divValue, operator);
	}
	
	/**
	 * Metoda uspoređuje vrijednost varijable
	 * value s vrijednosti predanog objekta.
	 * 
	 * @param withValue
	 * @return 0 ako su vrijednosti numerički jednake,
	 * 			negativan broj ako je value numerički manji od withValue,
	 * 			pozitivan broj ako je value numerički veći od withValue
	 */
	public int numCompare(Object withValue) {
		Double numValue = Double.parseDouble(getNumericalValue(value).toString());
		Double numSecondValue = Double.parseDouble(getNumericalValue(withValue).toString());
		
		return Double.compare(numValue, numSecondValue);
	}
	
	/**
	 * Pomoćna metoda za obavljanje operacija nad objektima
	 * dozvoljenog tipa.
	 * 
	 * @param secondOperand
	 * @param operator
	 */
	private void calculate(Object secondOperand, BinaryOperator<Double> operator) {
		Object numValue = getNumericalValue(value);
		Object numSecondValue = getNumericalValue(secondOperand);
		
		// ako su oba tipa Integer, rezultat je Integer
		if(numValue instanceof Integer && numSecondValue instanceof Integer) {
			value = operator.apply(((Integer) numValue).doubleValue(), 
					((Integer) numSecondValue).doubleValue()).intValue();
		} else {
			// inače rezultat mora biti Double
			value = operator.apply(Double.parseDouble(numValue.toString()), 
					Double.parseDouble(numSecondValue.toString()));
		}
	}
	
	/**
	 * Pomoćna metoda koja koja sve vrijednosti
	 * prebacuje u tip Integer ili Double ako je to moguće.
	 * Ako nije, baca prigodnu iznimku.
	 * 
	 * @param object
	 * @return Object (Integer ili Double)
	 * @throws RuntimeException ako je predani objekt String,
	 * 			ali ne predstavlja numeričku vrijednost
	 * @throws IllegalArgumentException ako je predan objekt
	 * 			koji nije ValueWrapper, Integer, Double ili null
	 */
	private Object getNumericalValue(Object object) {
		// null se tretira kao Integer 0
		if(object == null) {
			return Integer.valueOf(0);
		}
		
		if(object instanceof ValueWrapper) {
			// ako je ValueWrapper gledaj samo value
			object = ((ValueWrapper) object).value;
		}
				
		if(object instanceof Integer || object instanceof Double) {
			return object;
		}
		
		// ako je String pokušaj parsirati
		if(object instanceof String) {
			try {
				if(((String) object).contains(".") || ((String) object).contains("E")) {
					return Double.parseDouble((String) object);
				} else {
					return Integer.parseInt((String) object);
				}
			} catch(NumberFormatException ex) {
				throw new RuntimeException("Cannot convert String value to numerical value.");
			}
		}

		throw new IllegalArgumentException("Unsupported argument type.");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return value.toString();
	}
} 