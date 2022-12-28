package proz.calculator.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import proz.calculator.model.JShellCalculations;

/**
 * Klasa reprezentujaca kontroler aplikacji.
 * 
 * @author Radek
 * @version 1.0
 */
public class CalculatorController {

	/**
	 * Etykieta wyswietlajaca tekst wpisywany na kalkulatorze.
	 */
	@FXML
	private Label inputLabel;

	/**
	 * Etykieta przechowujaca rownanie, ktore bedzie potem obliczane. Znaki wciskane
	 * na kalkulatorze sa zamieniane na specjalne symbole, tak by tekst zawarty w
	 * tej etykiecie byl zrozumialy dla JShell-a. Etykieta ta pozostaje ukryta przed
	 * uzytkownikiem.
	 */
	@FXML
	private Label inputJShellLabel;

	/**
	 * Obiekt klasy JShellCalculations na rzecz ktorego bedzie mozna wywolac metode
	 * calculateResult(), ktora dokonuje obliczen.
	 */
	private JShellCalculations jshellCalc = new JShellCalculations();

	/**
	 * Obiekt klasy String, przechowujacy czesc calkowita aktualnie wpisywanej
	 * liczby.
	 */
	private String integerPart = "";

	/**
	 * Obiekt klasy String, przechowujacy czesc ulamkowa aktualnie wpisywanej
	 * liczby.
	 */
	private String fractionalPart = "";

	/**
	 * Zmienna logiczna informujaca, czy ostanim wpisanym znakiem jest operator
	 * 2-argumentowy (+,-,x,/).
	 */
	private boolean isOperatorBefore = false;

	/**
	 * Zmienna logiczna informujaca, czy znajdujemy sie w zasiegu 1-argumentowego
	 * operatora przedrostkowego (np. pierwiastek , logarytm). Jesli tak, to
	 * nastepny mozliwy do wpisania znak musi byc czescia liczby (cyfra lub kropka).
	 */
	private boolean isPrefixOperatorBefore = false;

	/**
	 * Zmienna logiczna informujaca, czy ostatnim wpisanym znakiem jest
	 * 1-argumentowy operator przyrostkowy (np. kwadrat liczby).
	 */
	private boolean isPostfixOperatorBefore = false;

	/**
	 * Zmienna logiczna informujaca, czy ostatni wpisany znak jest czescia liczby.
	 */
	private boolean isNumberBefore = false;

	/**
	 * Zmienna logiczna informujaca, czy kalkulator znajduje sie w warunkach
	 * poczatkowych i oczekuje na pierwsza akcje uzytkownika.
	 */
	private boolean isEquationBeginning = true;

	/**
	 * Zmienna logiczna informujaca, czy obliczenia zostaly zakonczone pomyslnie i
	 * wyliczona wartosc jest obecnie pokazywana na wyswiatlaczu. Taka wartosc mozna
	 * wykorzystac do dalszych obliczen, jesli polaczy sie ja z operatorem
	 * przyjmujacym parametr z lewej strony.
	 */
	private boolean isEquationEnd = false;

	/**
	 * Zmienna logiczna informujaca, czy uzywanym 1-arg operatorem przedrostkowym
	 * jest pierwiastek kwadratowy.
	 */
	private boolean isRootUsed = false;

	/**
	 * Zmienna logiczna informujaca, czy uzywanym 1-arg operatorem przedrostkowym
	 * jest logarytm dziesietny.
	 */
	private boolean isLogUsed = false;

	/**
	 * Metoda sluzaca do sprawdzenia, czy zasieg operatora przedrostkowego (np.
	 * pierwiastka) juz sie skonczyl.
	 */
	private void prefixOperatorCheck() {

		if (isNumberBefore == true && isPrefixOperatorBefore == true) {
			if (isRootUsed == true)
				inputJShellLabel
						.setText(inputJShellLabel.getText() + "root" + "(" + integerPart + fractionalPart + ")");
			if (isLogUsed == true)
				inputJShellLabel
						.setText(inputJShellLabel.getText() + "logarithm" + "(" + integerPart + fractionalPart + ")");
			isRootUsed = false;
			isLogUsed = false;
			isPrefixOperatorBefore = false;
			integerPart = "";
			fractionalPart = "";
		}
	}

	/**
	 * Metoda sluzaca do podtrzymania precyzji wykonywanych obliczen. Ma
	 * zastosowanie przy wykonywaniu operacji dzielenia, gdzie dokladnosc obliczen
	 * moglaby by stracona, jesli oba parametry bylyby calkowite.
	 */
	private void keepPrecision() {
		if (inputJShellLabel.getText().length() > 0) // jesli obecny lancuch jest niepusty
			if (inputJShellLabel.getText().charAt(inputJShellLabel.getText().length() - 1) != ')') // i jesli nie
																									// konczy sie
																									// wywolaniem
																									// f-cji
																									// operatora
																									// przyrostkowego
				if (fractionalPart == "") // i jesli liczba nie ma czesci ulamkowej, to wstawiamy kropke, by wynik
					fractionalPart = "."; // dzielenia byl liczba rzeczywista (by nie stracic precyzji wyniku)
	}

	/**
	 * Metoda wywolywana po wcisnieciu cyfry. W zaleznosci od tego, czy aktualnie
	 * wprowadzana liczba zawiera juz kropke dziesietna czy nie, to cyfra zostanie
	 * albo dopisana do czesci calkowitej albo do ulamkowej. Metoda dopisuje tez te
	 * cyfre do rownania na wyswietlaczu, jexli nie stoi to w sprzecznoxci ze
	 * skladnia rownania.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void digitButtonPressed(ActionEvent event) {

		if (isPostfixOperatorBefore == true) {
			AlertBox.showAlert("Nie mozna wstawic cyfry bezpoxrednio za jednoargumentowym operatorem przyrostkowym!");
			return;
		}

		Button pressedButton = (Button) event.getSource();
		String digit = pressedButton.getText();

		if (isEquationEnd == true) {
			inputLabel.setText(digit);
			inputJShellLabel.setText("");
			integerPart += digit;
			isEquationEnd = false;
			return;
		}

		inputLabel.setText(inputLabel.getText() + digit);

		if (fractionalPart == "")
			integerPart += digit;
		else
			fractionalPart += digit;

		isNumberBefore = true;
		isOperatorBefore = false;
		isPostfixOperatorBefore = false;
		isEquationBeginning = false;

	}

	/**
	 * Metoda wywolywana po wcisnieciu kropki dziesietnej. Dopisuje ja do rownania
	 * na wyswietlaczu, jesli nie stoi to w sprzecznosci ze skladnia rownania.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void pointButtonPressed(ActionEvent event) {

		if (isPostfixOperatorBefore == true) {
			AlertBox.showAlert("Nie mozna wstawic kropki bezpozrednio za jednoargumentowym operatorem przyrostkowym!");
			return;
		}

		if (isEquationEnd == true) {
			inputLabel.setText(".");
			inputJShellLabel.setText("");
			integerPart = "0";
			fractionalPart = ".";
			isEquationEnd = false;
			return;
		}

		if (fractionalPart == "") {
			inputLabel.setText(inputLabel.getText() + ".");
			fractionalPart += ".";
			if (integerPart == "")
				integerPart = "0";

			isNumberBefore = true;
			isOperatorBefore = false;
			isPostfixOperatorBefore = false;
			isEquationBeginning = false;
		} else
			AlertBox.showAlert("Liczba moze zawierac tylko jedna kropka dziesiatna!");
	}

	/**
	 * Metoda wywolywana po wcisnieciu znaku "+". Informuje o tym, ze skonczono
	 * wprowadzac liczbe i jesli wszystko jest poprawnie, to wypisuje te liczbe oraz
	 * znak "+" do etykiety inputJShellLabel. Dopisuje tez znak "+" na wyswietlaczu.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void additionButtonPressed(ActionEvent event) {

		prefixOperatorCheck();

		if (isEquationEnd == true) {
			inputLabel.setText(inputLabel.getText() + "+");
			inputJShellLabel.setText(inputJShellLabel.getText() + "+");
			isNumberBefore = false;
			isOperatorBefore = true;
			isPrefixOperatorBefore = false;
			isPostfixOperatorBefore = false;
			isEquationEnd = false;
			return;
		}

		if (isOperatorBefore == false && isPrefixOperatorBefore == false) {
			inputLabel.setText(inputLabel.getText() + "+");
			inputJShellLabel.setText(inputJShellLabel.getText() + integerPart + fractionalPart + "+");
			isNumberBefore = false;
			isOperatorBefore = true;
			isPostfixOperatorBefore = false;
			isEquationBeginning = false;
			integerPart = "";
			fractionalPart = "";
		} else
			AlertBox.showAlert("Nie mozna wstawic dwoch operatorow obok siebie!");

	}

	/**
	 * Metoda wywolywana po wcisnieciu znaku "-". Informuje o tym, ze skonczono
	 * wprowadzac liczbe i jesli wszystko jest poprawnie, to wypisuje te liczbe oraz
	 * znak "-" do etykiety inputJShellLabel. Dopisuje tez znak "+" na wyswietlaczu.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void subtractionButtonPressed(ActionEvent event) {

		prefixOperatorCheck();

		if (isEquationEnd == true) {
			inputLabel.setText(inputLabel.getText() + "-");
			inputJShellLabel.setText(inputJShellLabel.getText() + "-");
			isNumberBefore = false;
			isOperatorBefore = true;
			isPrefixOperatorBefore = false;
			isPostfixOperatorBefore = false;
			isEquationEnd = false;
			return;
		}

		if (isPrefixOperatorBefore == true) {
			AlertBox.showAlert(
					"Nie mozna wstawic znaku minus!\n" + "Funkcja jest nieokreslona dla parametrow ujemnych.");
			return;
		}

		if (isOperatorBefore == false) {
			inputLabel.setText(inputLabel.getText() + "-");
			inputJShellLabel.setText(inputJShellLabel.getText() + integerPart + fractionalPart + "-");
			isNumberBefore = false;
			isOperatorBefore = true;
			isPrefixOperatorBefore = false;
			isPostfixOperatorBefore = false;
			isEquationBeginning = false;
			integerPart = "";
			fractionalPart = "";
		} else
			AlertBox.showAlert("Nie mozna wstawic dwoch operatorow obok siebie!");
	}

	/**
	 * Metoda wywolywana po wcisnieciu znaku "x". Informuje o tym, ze skonczono
	 * wprowadzae liczbe i jesli wszystko jest poprawnie, to wypisuje te liczbe oraz
	 * znak "x" do etykiety inputJShellLabel. Dopisuje tez znak "x" na wyswietlaczu.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void multiplicationButtonPressed(ActionEvent event) {

		prefixOperatorCheck();

		if (isEquationBeginning == true) {
			AlertBox.showAlert("Operator mnozenia nie moze zaczynac rownania!\n" + "Wybierz np. cyfre.");
			return;
		}

		if (isEquationEnd == true) {
			inputLabel.setText(inputLabel.getText() + "x");
			inputJShellLabel.setText(inputJShellLabel.getText() + "*");
			isNumberBefore = false;
			isOperatorBefore = true;
			isPrefixOperatorBefore = false;
			isPostfixOperatorBefore = false;
			isEquationEnd = false;
			return;
		}

		if (isOperatorBefore == false && isPrefixOperatorBefore == false) {
			inputLabel.setText(inputLabel.getText() + "x");
			inputJShellLabel.setText(inputJShellLabel.getText() + integerPart + fractionalPart + "*");
			isNumberBefore = false;
			isOperatorBefore = true;
			isPostfixOperatorBefore = false;
			integerPart = "";
			fractionalPart = "";
		} else
			AlertBox.showAlert("Nie mozna wstawic dwoch operatorow obok siebie!");

	}

	/**
	 * Metoda wywolywana po wcisnisciu znaku "/". Informuje o tym, ze skonczono
	 * wprowadzae liczbe i jesli wszystko jest poprawnie, to wypisuje te liczbe oraz
	 * znak "/" do etykiety inputJShellLabel. Dopisuje tez znak "+" na wyswietlaczu.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void divisionButtonPressed(ActionEvent event) {

		prefixOperatorCheck();

		if (isEquationBeginning == true) {
			AlertBox.showAlert("Operator dzielenia nie moze zaczynac rownania!\n" + "Wybierz np. cyfre.");
			return;
		}

		if (isEquationEnd == true) {
			inputLabel.setText(inputLabel.getText() + "/");
			inputJShellLabel.setText(inputJShellLabel.getText() + "/");
			isNumberBefore = false;
			isOperatorBefore = true;
			isPrefixOperatorBefore = false;
			isPostfixOperatorBefore = false;
			isEquationEnd = false;
			return;
		}

		if (isOperatorBefore == false && isPrefixOperatorBefore == false) {
			inputLabel.setText(inputLabel.getText() + "/");
			inputJShellLabel.setText(inputJShellLabel.getText() + integerPart);
			keepPrecision();
			inputJShellLabel.setText(inputJShellLabel.getText() + fractionalPart + "/");

			isNumberBefore = false;
			isOperatorBefore = true;
			isPostfixOperatorBefore = false;
			integerPart = "";
			fractionalPart = "";
		} else
			AlertBox.showAlert("Nie mozna wstawic dwoch operatorow obok siebie!");

	}

	/**
	 * Metoda wywolywana po wcisnieciu znaku "^2". Informuje o tym, ze skonczono
	 * wprowadzac liczbe i jesli wszystko jest poprawnie, to wypisuje wywolanie
	 * metody square dla tej liczby do etykiety inputJShellLabel. Dopisuje tez znak
	 * "^2" na wyswietlaczu.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void squareButtonPressed(ActionEvent event) {

		if (isEquationBeginning == true) {
			AlertBox.showAlert("Operator potegowania nie moze zaczynac rownania!\n" + "Wybierz np. cyfre.");
			return;
		}

		if (isPrefixOperatorBefore == true) {
			AlertBox.showAlert("Operator potegowania nie moze wystepowac w zasiegu operatora przedrostkowego");
			return;
		}

		if (isEquationEnd == true) {
			inputLabel.setText(inputLabel.getText() + "\u00B2");
			inputJShellLabel.setText("square" + "(" + inputJShellLabel.getText() + ")");
			isNumberBefore = false;
			isPostfixOperatorBefore = true;
			isEquationEnd = false;
			return;
		}

		if (isOperatorBefore == false && isPostfixOperatorBefore == false && isPrefixOperatorBefore == false) {
			inputLabel.setText(inputLabel.getText() + "\u00B2");
			inputJShellLabel.setText(inputJShellLabel.getText() + "square" + "(" + integerPart + fractionalPart + ")");
			isNumberBefore = false;
			isPostfixOperatorBefore = true;
			integerPart = "";
			fractionalPart = "";
		} else
			AlertBox.showAlert("Nie mozna wstawic dwoch operatorow obok siebie!");

	}

	/**
	 * Metoda wywolywana po wcisnieciu znaku "^(1/2)". Informuje o tym, ze rozpoczal
	 * sie zasieg funkcji pierwiastka i trzeba dostarczyc mu liczbe dla prawidlowego
	 * wykonania. Dopisuje tez znak "^(1/2)" na wyswietlaczu.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void rootButtonPressed(ActionEvent event) {

		if (isEquationEnd == true) {
			inputLabel.setText("\u221A");
			inputJShellLabel.setText("");
			isNumberBefore = false;
			isPrefixOperatorBefore = true;
			isRootUsed = true;
			isEquationEnd = false;
			return;
		}

		if (isPrefixOperatorBefore == true) {
			AlertBox.showAlert("Nie mozna wstawic kolejnego operatora przedrostkowego w zasiegu poprzedniego!");
			return;
		}

		if (isNumberBefore == true) {
			AlertBox.showAlert("Nie mozna wstawic operatora przedrostkowego bezposrednio za liczba!");
			return;
		}

		if (isPostfixOperatorBefore == false) {
			inputLabel.setText(inputLabel.getText() + "\u221A");
			isOperatorBefore = false;
			isPrefixOperatorBefore = true;
			isRootUsed = true;
			isEquationBeginning = false;
			integerPart = "";
			fractionalPart = "";
		} else
			AlertBox.showAlert("Nie mozna wstawic dwoch operatorow obok siebie!");

	}

	/**
	 * Metoda wywolywana po wcisnieciu znaku "log". Informuje o tym, ze rozpoczal
	 * sie zasieg funkcji pierwiastka i trzeba dostarczyc mu liczbe dla prawidlowego
	 * dzialania. Dopisuje tez znak "log" na wyswietlaczu.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void logarithmButtonPressed(ActionEvent event) {

		if (isEquationEnd == true) {
			inputLabel.setText("log");
			inputJShellLabel.setText("");
			isNumberBefore = false;
			isPrefixOperatorBefore = true;
			isLogUsed = true;
			isEquationEnd = false;
			return;
		}

		if (isPrefixOperatorBefore == true) {
			AlertBox.showAlert("Nie mozna wstawic kolejnego operatora przedrostkowego w zasiegu poprzedniego!");
			return;
		}

		if (isNumberBefore == true) {
			AlertBox.showAlert("Nie mozna wstawic operatora przedrostkowego bezposrednio za liczba!");
			return;
		}

		if (isPostfixOperatorBefore == false) {
			inputLabel.setText(inputLabel.getText() + "log");
			isOperatorBefore = false;
			isPrefixOperatorBefore = true;
			isLogUsed = true;
			isEquationBeginning = false;
			integerPart = "";
			fractionalPart = "";
		} else
			AlertBox.showAlert("Nie mozna wstawic dwoch operatorow obok siebie!");

	}

	/**
	 * Metoda wywolywana po wcisnieciu znaku "=". Informuje o koncu wpisywania
	 * rownania. Wpisuje ostatnie znaki do inputJShellLabel i przekazuje go do
	 * metody calculateResult(), ktora dokonuje obliczen w JShell-u. Metoda
	 * wyswietla tez wynik na wyswietlaczu i zapisuje go w inputJShellLabel, tak by
	 * mozna go bylo wykorzystac do dalszych obliczen.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void resultButtonPressed(ActionEvent event) {

		prefixOperatorCheck();

		if (isOperatorBefore == false && isPrefixOperatorBefore == false) {
			String result = new String();
			inputJShellLabel.setText(inputJShellLabel.getText() + integerPart + fractionalPart);
			result = jshellCalc.calculateResult(inputJShellLabel.getText());
			if (result == null) {
				inputLabel.setText("");
				inputJShellLabel.setText("");
				isNumberBefore = false;
				isPostfixOperatorBefore = false;
				isEquationBeginning = true;
				AlertBox.showAlert("Proba wykonania niedozwolonego obliczenia!");
			} else {
				if (result.contains(".") == false) // jesli wynik jest liczba calkowita to
					result += ".0"; // przeksztalcamy go na liczbe rzeczywista
				inputLabel.setText(result);
				inputJShellLabel.setText(result);
				isNumberBefore = true;
				isPostfixOperatorBefore = false;
				isEquationEnd = true;
			}
			integerPart = "";
			fractionalPart = "";
		} else
			AlertBox.showAlert("Niepoprawny znak konczacy dzialanie!");

	}

	/**
	 * Metoda wywolywana po wcisnieciu znaku "C". Czysci obie etykiety i ustawia
	 * kalkulator w stan poczatkowy.
	 * 
	 * @param event Obiekt klasy ActionEvent zawierajacy informacje o tym, jaki
	 *              przycisk zostal wcisniety.
	 */
	@FXML
	public void clearButtonPressed(ActionEvent event) {
		inputLabel.setText("");
		inputJShellLabel.setText("");
		isOperatorBefore = false;
		isPostfixOperatorBefore = false;
		isPrefixOperatorBefore = false;
		isNumberBefore = false;
		isEquationBeginning = true;
		isEquationEnd = false;
		integerPart = "";
		fractionalPart = "";
	}

}
