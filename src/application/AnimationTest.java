package application;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class AnimationTest extends Application 
		implements EventHandler<ActionEvent> {
	
		Button add, mul, max, del;
		Label sol;
		BorderPane layout = new BorderPane();	
		GridPane grid = new GridPane();
		HBox topbox = new HBox();
		HBox botbox = new HBox();
		
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("Grid");
		
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				grid.add(new TextField(""), j, i);
			}
		}
		
		add = new Button();
		mul = new Button();
		max = new Button();
		del = new Button();
		sol = new Label();
		add.setOnAction(this);
		mul.setOnAction(this);
		max.setOnAction(this);
		del.setOnAction(this);
		add.setText("Addition");
		mul.setText("Multiplikation");
		max.setText("Maximum");
		del.setText("Löschen");
		sol.setText("Ergebnis: ");
		
		botbox.getChildren().add(sol);
		topbox.getChildren().add(mul);
		topbox.getChildren().add(add);
		topbox.getChildren().add(max);
		topbox.getChildren().add(del);
		layout.setTop(topbox);
		layout.setBottom(botbox);
		layout.setCenter(grid);
		
		Scene sc1 = new Scene(layout, 800, 800);
		primaryStage.setScene(sc1);
		primaryStage.show();

		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == add) {
			double d = 0;
			for (Node n : grid.getChildren()) {
				TextField tf = (TextField)n;
				if(!(tf.getText().equals(""))){
					d += Double.parseDouble(tf.getText());
				}
			}
			sol.setText("Ergebnis: " + d);
		}
		
		else if(event.getSource() == max) {
			double d = 0;
			for (Node n : grid.getChildren()) {
				TextField tf = (TextField)n;
				if(!(tf.getText().equals(""))){
					if(Double.parseDouble(tf.getText()) > d) {
						d = Double.parseDouble(tf.getText());
					}
				}
			}
			sol.setText("Ergebnis: " + d);
		}
		
		else if(event.getSource() == mul) {
			double d = 1;
			for (Node n : grid.getChildren()) {
				TextField tf = (TextField)n;
				if(!(tf.getText().equals(""))){
					d = d * Double.parseDouble(tf.getText());
				}
			}
			sol.setText("Ergebnis: " + d);
		}
		
		else if(event.getSource() == del) {
			for(Node n : grid.getChildren()) {
				TextField tf = (TextField)n;
				tf.setText("");
			}

			sol.setText("Ergebnis: ");
		}
		
	}
}