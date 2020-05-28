package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;

public class LSystemBuilderImplTest {
	@Test
	public void testGenerateLevel0() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();
		builder.setAxiom("F");
		builder.registerProduction('F', "F+F--F+F");
		LSystem system = builder.build();
		
		assertEquals("F", system.generate(0));
	}
	
	@Test
	public void testGenerateLevel1() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();
		builder.setAxiom("F");
		builder.registerProduction('F', "F+F--F+F");
		LSystem system = builder.build();
		
		assertEquals("F+F--F+F", system.generate(1));
	}
	
	@Test
	public void testGenerateLevel2() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();
		builder.setAxiom("F");
		builder.registerProduction('F', "F+F--F+F");
		LSystem system = builder.build();
		
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", system.generate(2));
	}
	
	@Test
	public void testGenerateTwoProductions() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();
		builder.setAxiom("AB");
		builder.registerProduction('A', "ABB");
		builder.registerProduction('B', "CCA");
		LSystem system = builder.build();

		assertEquals("ABBCCA", system.generate(1));
		assertEquals("ABBCCACCACCABB", system.generate(2));
	}
	
	@Test
	public void testGenerateFromText() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();
		String data[] =  new String[] {
				"origin 0.05 0.4",
				"angle 0",
				"unitLength 0.9",
				"unitLengthDegreeScaler 1.0 / 3.0",
				"",
				"command F draw 1",
				"command + rotate 60",
				"command - rotate -60",
				"",
				"axiom F",
				"",
				"production F F+F--F+F"
		};
		builder.configureFromText(data);
		LSystem system = builder.build();
		
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", system.generate(2));
	}
}
