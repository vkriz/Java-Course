package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Razred predstavlja model liste koji inkrementalno
 * generira proste brojeve. Po stvaranju se u modelu
 * nalazi samo broj 1.
 * 
 * @author Valentina Križ
 *
 */
public class PrimListModel implements ListModel<Integer> {
	/**
	 * Lista izgeneriranih prostih brojeva.
	 */
	private List<Integer> list;
	
	/**
	 * Zadnji izgenerirani prosti broj.
	 */
	private int lastPrim;
	
	/**
	 * Lista listenera.
	 */
	private List<ListDataListener> listeners;
	
	/**
	 * Default konstruktor.
	 */
	public PrimListModel() {
		list = new ArrayList<>();
		lastPrim = 1;
		list.add(lastPrim);
		listeners = new ArrayList<>();
	}
	
	/**
	 * Metoda generira prvi sljedeći prosti broj
	 * i dodaje ga na kraj liste.
	 */
	public void next() {
		int nextInt = lastPrim + 1;
		while(!isPrime(nextInt)) {
			++nextInt;
		}
		
		lastPrim = nextInt;

		list.add(lastPrim);
		
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, list.size(), list.size());
		for(ListDataListener l : listeners) {
			l.intervalAdded(event);
		}
	}
	
	/**
	 * Pomoćna metoda za provjeru da li je zadani broj
	 * prost.
	 * 
	 * @param number
	 * @return true ako je prost, false inače
	 */
	private boolean isPrime(int number) {
		if(number <= 2) {
			return true;
		}
		
		for(int i = 2, n = number / 2; i <= n; ++i) {
			if(number % i == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public int getSize() {
		return list.size();
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Integer getElementAt(int index) {
		return list.get(index);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
}
