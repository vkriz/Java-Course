package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw05.db.lexer.QueryTokenType;

/**
 * Razred koji predstavlja parser za upite.
 * 
 * @author Valentina Križ
 *
 */
public class QueryParser {
	private List<ConditionalExpression> query;
	QueryLexer lexer;
	
	/**
	 * Konstruktor koji prima string koji sadrži upit.
	 * 
	 * @param line
	 */
	public QueryParser(String line) {
		lexer = new QueryLexer(line);
		query = new ArrayList<>();
		parse();
	}
	
	/**
	 * Metoda koja parsira string s upitom.
	 * 
	 * @throws QueryParserException ako dođe do greške pri parsiraju
	 */
	private void parse() {
		try {
			lexer.nextToken();
			while(lexer.getToken().getType() != QueryTokenType.EOL) {
				query.add(getSubquery());
			}
		} catch(QueryLexerException ex) {
			throw new QueryParserException(ex.getMessage());
		}
	}
	
	/**
	 * Metoda koja iz upita dohvaća IFieldValueGetter.
	 * 
	 * @return fieldValueGetter
	 */
	private IFieldValueGetter getFieldValueGetter() {
		IFieldValueGetter fieldValueGetter = null;
		if(lexer.getToken().getType() == QueryTokenType.AND) {
			if(query.size() != 0) {
				lexer.nextToken();
			} else {
				throw new QueryParserException("Upit ne može počinjati logičkim operatorom.");
			}
		}
		
		// ako je dozvoljeno ime atributa
		if(lexer.getToken().getType() == QueryTokenType.ATTRIBUTE_NAME) {
			if(lexer.getToken().getValue().equals("jmbag")) {
				fieldValueGetter = FieldValueGetters.JMBAG;
			} else if(lexer.getToken().getValue().equals("firstName")) {
				fieldValueGetter = FieldValueGetters.FIRST_NAME;
			} else if(lexer.getToken().getValue().equals("lastName")) {
				fieldValueGetter = FieldValueGetters.LAST_NAME;
			} else {
				// inače javi grešku
				throw new QueryParserException("Nepodržano ime atributa");
			}
		} else {
			// upit mora započinjati imenom atributa
			throw new QueryParserException("Očekivano ime atributa.");
		}
		return fieldValueGetter;
	}
	
	/**
	 * Metoda koja iz upita dohvaća operator uspoređivanja.
	 * 
	 * @return operator uspoređivana
	 */
	private IComparisonOperator getComparisonOperator() {
		IComparisonOperator comparisonOperator = null;
		
		// ako je jedan od ponuđenih operatora
		if(lexer.nextToken().getType() == QueryTokenType.COMPARISON_OPERATOR) {
			if(lexer.getToken().getValue().equals("LESS")) {
				comparisonOperator = ComparisonOperators.LESS;
			} else if(lexer.getToken().getValue().equals("LESS_EQUALS")) {
				comparisonOperator = ComparisonOperators.LESS_OR_EQUALS;
			} else if(lexer.getToken().getValue().equals("GREATER")) {
				comparisonOperator = ComparisonOperators.GREATER;
			} else if(lexer.getToken().getValue().equals("GREATER_EQUALS")) {
				comparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;
			} else if(lexer.getToken().getValue().equals("EQUALS")) {
				comparisonOperator = ComparisonOperators.EQUALS;
			} else if(lexer.getToken().getValue().equals("NOT_EQUALS")) {
				comparisonOperator = ComparisonOperators.NOT_EQUALS;
			} else if(lexer.getToken().getValue().equals("LIKE")) {
				comparisonOperator = ComparisonOperators.LIKE;
			} else {
				// ako nije podržan operator baci iznimku 
				throw new QueryParserException("Nepodržano operator uspoređivanja.");
			}
		} else {
			// nakon imena atributa mora slijediti operator uspoređivanja
			throw new QueryParserException("Očekivan operator uspoređivanja.");
		}
		
		return comparisonOperator;
	}
	
	/**
	 * Metoda koja dohvaća string iz upita.
	 * 
	 * @return string
	 */
	private String getStringLiteral() {
		if(lexer.nextToken().getType() == QueryTokenType.STRING_LITERAL) {
			return lexer.getToken().getValue().toString();
		} else {
			// nakon operatora uspoređivanja mora slijediti string
			throw new QueryParserException("Očekivan string.");
		}
	}
	
	/**
	 * Dohvaća sljedeći kondicionalni izraz iz upita.
	 * 
	 * @return kondicinalni izraz
	 */
	private ConditionalExpression getSubquery() {
		IFieldValueGetter fieldValueGetter = getFieldValueGetter();
		IComparisonOperator comparisonOperator = getComparisonOperator();
		String stringLiteral = getStringLiteral();
		lexer.nextToken();
		
		return new ConditionalExpression(fieldValueGetter, stringLiteral, comparisonOperator);
	}
	
	/**
	 * Metoda koja provjerava radi li se o upitu oblika
	 * jmbag = "xxx".
	 * 
	 * @return true ako je upit traženog oblika, false inače
	 */
	public boolean isDirectQuery() {
		if(query.size() == 1) {
			return query.get(0).getComparisonOperator() == ComparisonOperators.EQUALS 
						&& query.get(0).getFieldGetter() == FieldValueGetters.JMBAG;
		}
		return false;
	}
	
	/**
	 * Metoda koja vraća jmbag koji se traži u upitu oblika
	 * jmbag = "xxx".
	 * 
	 * @return jbmag
	 */
	public String getQueriedJMBAG() {
		if(isDirectQuery()) {
			return query.get(0).getStringLiteral();
		} else {
			// ako upit nije traženog oblika baci iznimku
			throw new IllegalStateException("Upit nije oblika jmbag = \"xxx\"");
		}
	}
	
	/**
	 * Vraća listu svih kondicionalnih izraza od kojih 
	 * se sastoji upit.
	 * 
	 * @return lista izraza
	 */
	public List<ConditionalExpression> getQuery() {
		return query;
	}
}
