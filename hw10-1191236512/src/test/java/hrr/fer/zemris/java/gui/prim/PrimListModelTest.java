package hrr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.gui.prim.PrimListModel;

public class PrimListModelTest {
	@Test
	public void testModelConstructor() {
		PrimListModel model = new PrimListModel();
		
		assertEquals(1, model.getSize());
		assertEquals(1, model.getElementAt(0));
	}
	
	@Test
	public void testNextPrime() {
		PrimListModel model = new PrimListModel();
		model.next();
		
		assertEquals(2, model.getSize());
		assertEquals(1, model.getElementAt(0));
		assertEquals(2, model.getElementAt(1));
	}

}
