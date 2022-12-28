package proz.calculator.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Klasa odpowiadajaca za widok okna wyswietlajacego komunikat
 * 
 * @author Radek
 * @version 1.0
 */
public class AlertBox {

	/**
	 * Metoda wyswietlajaca okienko Alert z odpowiednim komunikatem.
	 * 
	 * @param message Obiekt klasy String zawierajacy tekst, ktory ma zostac
	 *                wyswietlony w okienku Alert.
	 */
	public static void showAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Blad!");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
