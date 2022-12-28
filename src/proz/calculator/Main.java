package proz.calculator;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

/**
 * Glowna klasa programu, odpowiedzialna za stworzenie interfejsu graficznego i uruchomienie aplikacji.
 * 
 * @author Radek
 * @version 1.0
 */
public class Main extends Application {

	/**
	 * Metoda tworzaca graficzny interfejs uzytkownika, dolaczajaca go do obiektu
	 * Scene i umieszczajaca w obiekcie Stage, ktory otrzymala jako argument. Metoda
	 * korzysta tez z metody FXMLLoader, by wczytac graf scenki.
	 * 
	 * @param primaryStage Obiekt klasy Stage, ktory reprezentuje glowne okno
	 *                     aplikacji.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/proz/calculator/view/Calculator.fxml"));
			Scene scene = new Scene(root, 600, 400);
			primaryStage.setTitle("Kalkulator");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Glowna metoda calego programu, rozpoczynajaca wykonywanie aplikacji.
	 * 
	 * @param args Tablica obiektow typu String, argument podawany w wierszu polecen.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
