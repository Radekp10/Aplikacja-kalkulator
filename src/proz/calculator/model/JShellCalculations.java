package proz.calculator.model;

import java.util.List;
import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;

/**
 * Klasa sluzaca do wykonania obliczen numerycznych z wykorzystaniem JShell API.
 * 
 * @author Radek
 * @version 1.0
 */
public class JShellCalculations {

	/**
	 * Metoda obliczajaca wartosc dostarczonego jej rownania przy uzyciu JShell API.
	 * 
	 * @param equation Obiekt klasy String zawierajacy rownanie, ktore ma zostac
	 *                 obliczone.
	 * @return Obiekt klasy String, zawierajacy wynik przeprowadzonych obliczen lub
	 *         rowny null, jesli podczas obliczen wystapil blad.
	 */
	public String calculateResult(String equation) {
		JShell jshell = JShell.create();

		jshell.eval("private double square (double x)" + "{return Math.pow(x, 2);}");
		jshell.eval("private double root (double x)" + "{return Math.sqrt(x);}");
		jshell.eval("private double logarithm(double x)" + "{return Math.log10(x);}");

		try (jshell) {
			List<SnippetEvent> events = jshell.eval(equation);
			for (SnippetEvent e : events) {
				if (e.causeSnippet() == null)
					if (!e.value().equals("Infinity") && !e.value().equals("-Infinity") && !e.value().equals("NaN")
							&& e.value() != null)
						return e.value();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
