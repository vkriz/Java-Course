package hr.fer.zemris.java.hw05.db;

import java.util.List;

/**
 * Razred koji implementira sučelje IFilter, a 
 * služi za filtriraje zapisa u bazi.
 * 
 * @author Valentina Križ
 *
 */
public class QueryFilter implements IFilter {
	List<ConditionalExpression> query;
	
	/**
	 * Konstruktor koji prima upit.
	 * 
	 * @param query upit
	 */
	public QueryFilter(List<ConditionalExpression> query) {
		this.query = query;
	}

	/**
	 * Metoda za filtriranje zapisa po upitu.
	 * 
	 * @param record
	 * @return true ako zapis odgovara upitu, false inače
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for(ConditionalExpression subQuery : query) {
			if(!subQuery.getComparisonOperator().satisfied(
					subQuery.getFieldGetter().get(record),
					subQuery.getStringLiteral())) {
				return false;
			}
		}
		return true;
	}

}
