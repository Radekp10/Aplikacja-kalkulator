/**
 * @author Radek
 * @version 1.0
 */

module proz.calculator {
	requires transitive javafx.controls;
	requires jdk.jshell;
	requires javafx.fxml;
	requires transitive javafx.base;
	requires transitive javafx.graphics;

	exports proz.calculator;
	exports proz.calculator.view;
	exports proz.calculator.model;

	opens proz.calculator.view to javafx.fxml;
}