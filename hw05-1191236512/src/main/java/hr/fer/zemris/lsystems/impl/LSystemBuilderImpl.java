package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * Razred koji implemetira sučelje LSystemBuilder
 * i služi za stvaranje objekata za konfiguriranje Lindermayerovih sustava.
 * 
 * @author Valentina Križ
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	private double unitLength;
	private double unitLengthDegreeScaler;
	private Vector2D origin;
	private double angle;
	private String axiom;
	Dictionary<Character, String> productions;
	Dictionary<Character, Command> actions;
	
	/**
	 * Razred koje implementira sučelje LSystem,
	 * služi za modeliranje Lindermayerovog sustava.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private class LSystemImpl implements LSystem {
		/**
		 * Metoda koja uporabom primljenog objekta
		 * za crtanje linija (tipa Painter) crta retultatni
		 * fraktal za zadanu razinu.
		 * 
		 * @param level razina
		 * @param painter 
		 */
		@Override
		public void draw(int level, Painter painter) {
			Context ctx = new Context();
			TurtleState state = new TurtleState(origin, 
												new Vector2D(1, 0).rotated(angle),
												Color.black,
												unitLength * Math.pow(unitLengthDegreeScaler, level));
			ctx.pushState(state);
			
			String generated = generate(level);
	
			for(int i = 0, len = generated.length(); i < len; ++i) {
				Command command = actions.get(generated.charAt(i));
				if(command != null) {
					command.execute(ctx, painter);
				}
			}
		}

		/**
		 * Metoda koja prima razinu i vraća string koji odgovara
		 * generiranom nizu nakon zadanog broja razina primjena
		 * produkcija.
		 * 
		 * @param level
		 */
		@Override
		public String generate(int level) {
			String str = axiom;
			
			for(int currentLevel = 0; currentLevel < level; ++currentLevel) {
				char[] chars = str.toCharArray();
				str = "";
				
				for(int charIndex = 0, len = chars.length; charIndex < len; ++charIndex) {
					String replaceWith = productions.get(chars[charIndex]);
					if(replaceWith != null) {
						str += replaceWith;
					} else {
						str += chars[charIndex];
					}
				}
			}
			return str;
		}
	}
	
	/**
	 * Pomoćna metoda za parsiranje akcije iz stringa.
	 * 
	 * @param action akcija u obliku stringa
	 * @return akcija kao Command varijabla
	 * @throws IllegalArgumentException ako se pojavi nepodržana akcija
	 * 			ili neispravan tip podataka
	 */
	private Command parseAction(String action) {
		String[] parts = action.trim().split("\\s+");
		if(parts.length < 1 || parts.length > 2) {
			throw new IllegalArgumentException("Nepodržana akcija.");
		}
				
		if(parts.length == 1) {
			switch(parts[0]) {
				case "push":
					return new PushCommand();
				
				case "pop":
					return new PopCommand();
					
				default:
					throw new IllegalArgumentException("Nepodržana akcija.");
			}
		}
		
		if(parts.length == 2) {
			switch(parts[0]) {
				case "draw":
					try {
						return new DrawCommand(Double.parseDouble(parts[1]));
					} catch(NumberFormatException ex) {
						throw new IllegalArgumentException("Neispravan broj koraka.");
					}
					
				case "skip":
					try {
						return new SkipCommand(Double.parseDouble(parts[1]));
					} catch(NumberFormatException ex) {
						throw new IllegalArgumentException("Neispravan broj koraka.");
					}
					
				case "scale":
					try {
						return new ScaleCommand(Double.parseDouble(parts[1]));
					} catch(NumberFormatException ex) {
						throw new IllegalArgumentException("Neispravan faktor.");
					}
					
				case "rotate":
					try {
						return new RotateCommand(Math.toRadians(Double.parseDouble(parts[1])));
					} catch(NumberFormatException ex) {
						throw new IllegalArgumentException("Neispravna vrijednost kuta.");
					}
				
				case "color":
					try {
						return new ColorCommand(Color.decode("#" + parts[1]));
					} catch(Exception ex) {
						throw new IllegalArgumentException("Neispravan kod za boju.");
					}
					
				default: 
					throw new IllegalArgumentException("Nepodržana akcija.");
			}
		}
		
		return null;
	}
	
	/**
	 * Default konstruktor.
	 */
	public LSystemBuilderImpl() {
		unitLength = 0.1;
		unitLengthDegreeScaler = 1;
		origin = new Vector2D(0, 0);
		angle = 0;
		axiom = "";
		productions = new Dictionary<>();
		actions = new Dictionary<>();
	}

	/**
	 * Metoda koja vraća jedan konkretan Lindermayerov sustav 
	 * prema zadanoj konfiguraciji.
	 * 
	 * @return dobiveni Lindermayerov sustav
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Metoda koja konfiguraciju sustava izvlači iz niza linija.
	 * Podrazumijeva da svaka linija sadrži po jednu direktivu ili je prazna.
	 * Direktive su: origin, angle, unitLength, unitLengthDegreeScaler, 
	 * command, axiom, production.
	 * 
	 * @return konfiguracija Lindermayerovog sustava
	 * @throws IllegalArgumentException ako se pojavi nepodržana naredba
	 * 			ili neispravan tip podataka
	 */
	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		for(int i = 0, numLines = lines.length; i < numLines; ++i) {
			String[] line = lines[i].trim().split("\\s+");
			if(line.length == 0) {
				return this;
			}
			
			switch(line[0]) {
				case "origin": 
					if(line.length != 3) {
						throw new IllegalArgumentException("Neispravna naredba.");
					}
					try {
						origin = new Vector2D(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
					} catch(NumberFormatException ex) {
						throw new IllegalArgumentException("Neispravna početna točka.");
					}
					break;
					
				case "angle":
					if(line.length != 2) {
						throw new IllegalArgumentException("Neispravna naredba.");
					}
					try {
						angle = Math.toRadians(Double.parseDouble(line[1]));
					} catch(NumberFormatException ex) {
						throw new IllegalArgumentException("Nesipravna vrijednost kuta.");
					}
					break;
					
				case "unitLength":
					if(line.length != 2) {
						throw new IllegalArgumentException("Neispravna naredba.");
					}
					try {
						unitLength = Double.parseDouble(line[1]);
					} catch(NumberFormatException ex) {
						throw new IllegalArgumentException("Neispravna vrijednost jediničnog pomaka.");
					}
					break;
					
				case "unitLengthDegreeScaler":
					if(line.length > 1) {
						StringBuilder sb = new StringBuilder();
						for(int j = 1, lineLen = line.length; j < lineLen; ++j) {
							sb.append(line[j]);
						}
						String str = sb.toString();
						
						String[] parts = str.split("/");
						if(parts.length == 1) {
							try {
								unitLengthDegreeScaler = Double.parseDouble(parts[0]);
							} catch(NumberFormatException ex) {
								throw new IllegalArgumentException("Neispravna vrijednost faktor skaliranja.");
							}
						} else if(parts.length == 2) {
							try {
								unitLengthDegreeScaler = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
							} catch(NumberFormatException ex) {
								throw new IllegalArgumentException("Neispravna vrijednost faktor skaliranja.");
							}
						} else {
							throw new IllegalArgumentException("Neispravna naredba.");
						}
					} else {
						throw new IllegalArgumentException("Neispravna naredba.");
					}
					break;
					
				case "command": 
					StringBuilder sb = new StringBuilder();
					
					// ako je characted
					if(line[1].length() == 1) {
						// ostatak linije shvati kao command
						for(int j = 2, lineLen = line.length; j < lineLen; ++j) {
							sb.append(line[j]);
							sb.append(" ");
						}
						Command command = parseAction(sb.toString());
						actions.put(line[1].charAt(0), command);
					} else {
						throw new IllegalArgumentException("Neispravna naredba.");
					}
					break;
				
				case "axiom":
					if(line.length == 2) {
						axiom = line[1];
					} else {
						throw new IllegalArgumentException("Neispravna naredba.");
					}
					break;
				
				case "production":
					if(line.length != 3 || line[1].length() > 1) {
						throw new IllegalArgumentException("Neispravna naredba.");
					}
					productions.put(line[1].charAt(0), line[2]);
			}
			
		}
		return this;
	}

	/**
	 * Metoda za spremanje akcije u konfiguraciju sustava.
	 * 
	 * @param symbol početni simbol
	 * @param action akcija koju simbol izaziva
	 * @return ažurirani sustav
	 */
	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		actions.put(symbol, parseAction(action));
		return this;
	}

	/**
	 * Metoda za spremanje produkcije u konfiguraciju sustava.
	 * 
	 * @param symbol početni simbol
	 * @param production produkcija koju simbol izaziva
	 * @return ažurirani sustav
	 */
	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		productions.put(symbol, production);
		return this;
	}

	/**
	 * Setter za varijablu angle.
	 * 
	 * @param angle
	 * @return ažurirani sustav
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	/**
	 * Setter za varijablu axiom.
	 * 
	 * @param axiom 
	 * @return ažurirani sustav
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = axiom;
		return this;
	}

	/**
	 * Setter za varijablu origin.
	 * 
	 * @param x x-koordinata početne točke
	 * @param y y-koordinata početne točke
	 * @return ažurirani sustav
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Setter za varijablu unitLength.
	 * 
	 * @param unitLength
	 * @return ažurirani sustav
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	/**
	 * Setter za varijablu unitLengthDegreeScaler.
	 * 
	 * @param unitLengthDegreeScaler
	 * @return ažurirani sustav
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}
	
}
