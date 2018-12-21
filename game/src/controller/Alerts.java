package controller;

import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import view.Color;
import static view.Color.*;
import javafx.scene.control.Alert.AlertType;

public class Alerts {
	private static final String TO_WIN = "Aby wygra�, musisz zbi� 4 kulki przeciwnika, spychaj�c je poza pole lini� z�o�on� z Twoich kulek (d�u�sz�, ni� linia przeciwnika, kt�r� chcesz zbi�.\n";
	private static final String RULES = "1. Wybierz pierwsz� kulk�.\n2. Wybierz drug� kulk� tak, by z wybranych kulek oraz kul pomi�dzy nimi powsta�a linia (nie d�u�sza ni� 3 kulki).\n3.Wybierz, w kt�r� stron� chcesz si� ruszy�\n";

	static void makeGameEndAlert(String text, Color color) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Gracz " + color);
			alert.setHeaderText(text);
			alert.setContentText("Koniec gry!");
			Optional<ButtonType> result = alert.showAndWait();
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
			result.ifPresent(res -> {
			});
		});
	}

	static void makeGameStartAlert(Color color) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Gracz " + color);
			alert.setHeaderText("Nowa gra!");
			if (color == WHITE)
				alert.setContentText("Rozpoczynasz gr�.\n" + TO_WIN + RULES);
			else
				alert.setContentText("Poczekaj na swoj� kolejk�.\n" + TO_WIN);

			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
			Optional<ButtonType> result = alert.showAndWait();
			result.ifPresent(res -> {
			});
		});
	}

	static void makeTurnAlert(Color color) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Gracz " + color);
			alert.setHeaderText("Twoja kolej!");
			alert.setContentText(RULES);

			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
			Optional<ButtonType> result = alert.showAndWait();
			result.ifPresent(res -> {
			});
		});
	}
}
