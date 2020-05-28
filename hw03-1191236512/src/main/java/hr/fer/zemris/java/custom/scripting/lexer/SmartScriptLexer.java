package hr.fer.zemris.java.custom.scripting.lexer;

public class SmartScriptLexer {
	private char[] data;		// ulazni tekst
	private SmartScriptToken token;		// trenutni token
	private int currentIndex;	// indeks prvog neobrađenog znaka
	private SmartScriptLexerState state;	// stanje u kojemu se nalazi lekser
	
	/**
	 * Konstruktor koji prima ulazni tekst koji se tokenizira.
	 * 
	 * @param text ulazni tekst
	 */
	public SmartScriptLexer(String text) {
		if(text == null) {
			throw new NullPointerException();
		}
		data = text.toCharArray();
		token = null;
		currentIndex = 0;
		setState(SmartScriptLexerState.TEXT );
	}
	
	/**
	 * Setter za state varijablu.
	 * 
	 * @param state nova vrijednost state varijable
	 * @throws NullPointerException ako je predana vrijednost null
	 */
	public void setState(SmartScriptLexerState state) {
		if(state == null) {
			throw new NullPointerException();
		}
		this.state = state;
	}
	
	/**
	 * Pomoćna metoda koja pomiče currentIndex na sljedeći znak koji nije whitespace
	 * (ili na kraj data).
	 */
	private void moveToNextNonWhitespace() {
		while(currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}
	
	/**
	 * Pomoćna metoda koja redom sve znamenke u nizu sprema u StringBuilder i 
	 * vraća ga.
	 * 
	 * @param numberString StringBuilder u koji spremamo znamenke
	 * @return modificirani StringBuilder
	 */
	private StringBuilder getAllDigits(StringBuilder numberString) {
		while(currentIndex < data.length
				&& Character.isDigit(data[currentIndex])) {
			numberString.append(data[currentIndex]);
			++currentIndex;
		}
		return numberString;
	}
	
	/**
	 * Pomoćna metoda koja provjerava slijedi li kombinacija znakova {$.
	 * 
	 * @return true ako slijedi, false inače
	 */
	private boolean nextIsTagOpening() {
		if(data[currentIndex] == '{' && currentIndex + 1 < data.length && data[currentIndex + 1] == '$') {
			return true;
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja provjerava slijedi li kombinacija znakova $}.
	 * 
	 * @return true ako slijedi, false inače
	 */
	private boolean nextIsTagClosing() {
		if(data[currentIndex] == '$' && currentIndex + 1 < data.length && data[currentIndex + 1] == '}') {
			return true;
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja pronalazi ispravno ime varijable ako se ono nalazi
	 * na trenutnoj poziciji.
	 * 
	 * @param hasWhitespace false ako dohvaćamo ime varijable unutar taga - lekser treba dohvatiti što više znakova u jedan token
	 * @return ime varijable
	 * @throws SmartScriptLexerException ako ime sadrži nedozvoljene znakove
	 */
	private String getVariableName(boolean hasWhitespace) {
		StringBuilder variableName = new StringBuilder();
		
		// ispravno ime varijable započinje slovom
		if(Character.isLetter(data[currentIndex])) {
			variableName.append(data[currentIndex]);
			++currentIndex;
			
			// nakon slova slijedi kombinacija slova, znamenki i _
			while(currentIndex < data.length
					&& !Character.isWhitespace(data[currentIndex])
					&& !nextIsTagClosing()) {
				if(Character.isLetter(data[currentIndex]) 
						|| Character.isDigit(data[currentIndex])
						|| data[currentIndex] == '_') {
					variableName.append(data[currentIndex]);
					++currentIndex;
				} else {
					if(hasWhitespace) {
						throw new SmartScriptLexerException("Nedozvoljeno ime taga " + variableName.toString());
					} else {
						break;
					}
				}
			}
		}
		
		return variableName.toString();
	}
	
	/**
	 * Metoda koja pronalazi ime otvorenog taga.
	 * Pri pozivu metode currentIndex pokazuje na znak iza $.
	 * U slučaju kombinacije {$ $} vraća prazan string.
	 * 
	 * @return ime taga (String)
	 */
	public String getTagName() {
		moveToNextNonWhitespace();
		
		// ima taga može biti =
		if(data[currentIndex] == '=') {
			++currentIndex;
			return "=";
		}
		
		// ili ime varijable nakon koje slijedi razmak
		return getVariableName(true);
	}
	
	/**
	 * Metoda generira i vraća sljedeći token.
	 * 
	 * @return sljedeći token
	 * @throws LexerException ukoliko dođe do pogreške
	 */
	public SmartScriptToken nextToken() {
		// ako smo već pročitali zadnji znak
		if(token != null && token.getType() == SmartScriptTokenType.EOF) {
			throw new SmartScriptLexerException("Zadnji token je već pročitan.");
		}
		
		if(state != SmartScriptLexerState.TEXT) {
			moveToNextNonWhitespace();
		}
		
		// ako čitamo zadnji znak
		if(currentIndex == data.length) {
			token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
			return token;
		}
		
		// ako je lexer u text state-u
		if(state == SmartScriptLexerState.TEXT) {
			StringBuilder text = new StringBuilder();
			
			// sve dok ne dođeš do kraja dokumenta ili do {$ čitaj kao tekst
			while(currentIndex < data.length) {
				// ako je početak taga, prebaci se u tag state i vrati text token
				if(nextIsTagOpening()) {
					setState(SmartScriptLexerState.TAG);
					
					if(text.length() != 0) {
						token = new SmartScriptToken(SmartScriptTokenType.TEXT, text.toString());
						return token;
					} else {
						// ako dokument počinje s tagom odmah dohvati ime taga
						currentIndex += 2;
						String tagName = getTagName();
						
						if(!tagName.isEmpty()) {
							token = new SmartScriptToken(SmartScriptTokenType.TAGNAME, tagName.toUpperCase());
							return token;
						} else {
							throw new SmartScriptLexerException("Neispravno ime taga.");
						}
					}
				}
				
				// dopušteni escape znakovi u text state-u su \\ i \{
				if(data[currentIndex] == '\\') {
					if(currentIndex + 1 < data.length
							&& (data[currentIndex + 1] == '\\' 
								|| data[currentIndex + 1] == '{' )) {
						text.append(data[currentIndex]);
						text.append(data[currentIndex + 1]);
						currentIndex += 2;
						continue;
					} else {
						// ako je nedopušteni escape javi grešku
						throw new SmartScriptLexerException("Nedopušten escape znaka.");
					}
				}
				
				// dodaj znak u tekst i pomakni se na sljedeći znak
				text.append(data[currentIndex]);
				++currentIndex;
			}
			// ako je kraj dokumenta, vrati text token
			token = new SmartScriptToken(SmartScriptTokenType.TEXT, text.toString());
			return token;
		}
		
		// ako je lexer u tag state-u
		if(state == SmartScriptLexerState.TAG) {
			// ako slijedi {$ dohvati ime taga
			if(nextIsTagOpening()) {
				currentIndex += 2;
				String tagName = getTagName();
				
				if(!tagName.isEmpty()) {
					token = new SmartScriptToken(SmartScriptTokenType.TAGNAME, tagName.toUpperCase());
					return token;
				} else {
					throw new SmartScriptLexerException("Neispravno ime taga.");
				}
			}
			
			// ako slijedi $} prebaci se u text state i vrati tagclosing token
			if(nextIsTagClosing()) {
				setState(SmartScriptLexerState.TEXT);
				currentIndex += 2;
				token = new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null);
				return token;
			}
			
			// ako je funkcija
			if(data[currentIndex] == '@') {
				++currentIndex;
				// dozvoljeno ime fje je isto kao i ime varijable
				String functionName = getVariableName(false);
				if(!functionName.isBlank()) {
					token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, functionName);
					return token;
				}
			}
			
			// ako je operator
			if(data[currentIndex] == '+'
					// minus je operator ako iza njega slijedi whitespace ili ništa (kraj ulaza)
					|| (data[currentIndex] == '-' 
						&& ((currentIndex + 1 < data.length && Character.isWhitespace(data[currentIndex + 1]))
								|| (currentIndex + 1 == data.length)))
					|| data[currentIndex] == '*'
					|| data[currentIndex] == '^'
					|| data[currentIndex] == '/') {
				
				token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, data[currentIndex]);
				++currentIndex;
				return token;
			}
			
			// ako je broj (minus kao operator je već obrađen)
			if(Character.isDigit(data[currentIndex])
					|| (data[currentIndex] == '-')) {
				StringBuilder numberString = new StringBuilder();
				boolean isDouble = false;
				
				if(data[currentIndex] == '-') {
					numberString.append('-');
					++currentIndex;
				}
				
				numberString = getAllDigits(numberString);
				
				// ako je izašao iz while petlje zbog točke, onda je double
				if(currentIndex < data.length
						&& data[currentIndex] == '.') {
					isDouble = true;
					numberString.append(data[currentIndex]);
					++currentIndex;
					
					numberString = getAllDigits(numberString);
				}
				
				if(isDouble) {
					// pokušaj parsirati double, ako uspiješ vrati double token
					try {
						double number = Double.parseDouble(numberString.toString());
						token = new SmartScriptToken(SmartScriptTokenType.DOUBLE, number);
						return token;
					} catch(NumberFormatException ex) {
						throw new SmartScriptLexerException("Neispravan double.");
					}
				} else {
					// pokušaj parsirati integer, ako uspiješ vrati integer token
					try {
						int number = Integer.parseInt(numberString.toString());
						token = new SmartScriptToken(SmartScriptTokenType.INTEGER, number);
						return token;
					} catch(NumberFormatException ex) {
						throw new SmartScriptLexerException("Neispravan integer.");
					}
				}
			}
			
			// ako je string
			if(data[currentIndex] == '\"') {
				++currentIndex;
				
				StringBuilder stringBuild = new StringBuilder();
				
				while(true) {
					// ako prije pronalaska sljedećeg znaka " dođemo do kraja dokumenta
					if(currentIndex == data.length) {
						throw new SmartScriptLexerException("String nema kraj.");
					}
					
					// kad dođemo do kraja stringa vrati string token
					if(data[currentIndex] == '\"') {
						++currentIndex;
						token = new SmartScriptToken(SmartScriptTokenType.STRING, stringBuild.toString());
						return token;
					}
					
					// dopušteni znakovi za escape su \n, \t, \r, \\, \"
					if(data[currentIndex] == '\\') {
						if(currentIndex + 1 < data.length) {
							++currentIndex;
							switch(data[currentIndex]) {
							case 'n':
								stringBuild.append("\n");
								++currentIndex;
								break;
							case 't':
								stringBuild.append("\t");
								++currentIndex;
								break;
							case 'r':
								stringBuild.append("\r");
								++currentIndex;
								break;
							case '\\':
								stringBuild.append("\\");
								++currentIndex;
								break;
							case '\"':
								stringBuild.append("\"");
								++currentIndex;
								break;
							default:
								// ako je nedopušten escape javi grešku
								throw new SmartScriptLexerException("Nedozvoljeni escape znaka.");
							}
						} else {
							// ako je nedopušten escape (samo \) javi grešku
							throw new SmartScriptLexerException("Nedozvoljeni escape znaka.");
						}
					} else {
						// dodaj pročitani znak u string i pomakni se na sljedeći znak
						stringBuild.append(data[currentIndex]);
						++currentIndex;
					}	
				}
			}
			
			// ako je varijabla
			String variableName = getVariableName(false);
			if(!variableName.isBlank()) {
				token = new SmartScriptToken(SmartScriptTokenType.VARIABLE, variableName);
				return token;
			}
		}
		
		// ako slijedi nešto što nije navedeno, javi grešku
		throw new SmartScriptLexerException("Neispravan token.");
	}
	
	/**
	 * Metoda vraća zadnji generirani token.
	 * Može se pozivati više puta; ne pokreće generiranje
	 * sljedećeg tokena.
	 * 
	 * @return zadnji generirani token
	 */
	public SmartScriptToken getToken() {
		return token;
	}
}
