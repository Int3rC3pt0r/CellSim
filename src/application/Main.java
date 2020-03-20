package application;
	
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.PercentageStringConverter;


public class Main extends Application {
	
	static double width = 800;
	static double height = 800;
	static int fSizeX = 50;
	static int fSizeY = 50;
	static boolean[][] cell;
	static int[][] neighbours;
	static boolean rand = true;
	
	Button fill, next, genField;
	static TextField percentage;
	Canvas canvas;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			fill = new Button("Fill");
			next = new Button("Next");
			genField = new Button("genField");
			
			percentage = new TextField("20");
			
			canvas = new Canvas(width,height);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			
			genField.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				fillWhite(gc);
				initiateField(gc);
				rdmField();
				showField(gc);
				genGrid(gc);
			});
			
			next.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				fillCellNeighbours();
				updateCells();
				fillWhite(gc);
				showField(gc);
				genGrid(gc);
			});
			
			fill.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				fillCellNeighbours();
				printNeighbours(neighbours);
			});
			
			HBox top = new HBox();
			top.getChildren().addAll(genField, percentage, fill, next);
			
			BorderPane root = new BorderPane();
			root.setTop(top);
			root.setCenter(canvas);
			
			Scene scene = new Scene(root,width,height+50);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void fillWhite(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
	}
	private static void initiateField(GraphicsContext gc) {
		cell = new boolean[fSizeX][fSizeY];
		neighbours = new int[fSizeX][fSizeY];
		System.out.println("initiateField");
	}
	
	private static void genGrid(GraphicsContext gc) {
		System.out.println("genGrid");
		gc.setStroke(Color.BLACK);
		gc.setFill(Color.BLACK);
		gc.setLineWidth(1);
		gc.strokeRect(0, 0, width, height);
		
		for(int i = 0; i < cell.length; i++) {
			gc.strokeLine(width/fSizeX*i, 0, width/fSizeX*i, height);
		}
		for(int i = 0; i < cell[0].length; i++) {
			gc.strokeLine(0, height/fSizeY*i, width, height/fSizeY*i);
		}
	}
	
	private static void fillRect(GraphicsContext gc, int x, int y){
		gc.setFill(Color.LAWNGREEN);
		gc.fillRect(width/fSizeX*x, height/fSizeY*y, width/fSizeX, height/fSizeY);
	}
	
	private static void rdmField() {
		System.out.println("rdmField");
		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell.length; j++) {
				int r = (int) (Math.random() * 101);
				if(r <= Integer.parseInt(percentage.getText())) {
					cell[i][j] = true;
				}
				else {
					cell[i][j] = false;
				}
			}
		}
	}
	
	private static void showField(GraphicsContext gc) {
		System.out.println("showField");
		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell.length; j++) {
				if(cell[i][j] == true) {
					fillRect(gc,i,j);
				}
			}
		}
	}
	
	private static int countNeighbours(int x, int y) {
		int count = 0;
		for (int i = x-1; i < x+2; i++) {
			for (int j = y-1; j < y+2; j++) {
				if(getValue(i,j)) {
					count++;
				}
			}
		}		
		if(getValue(x,y)) {
			count--;
		}
		return count;
	}
	
	private static boolean getValue(int x, int y) {
		try{
			return cell[x][y];
		}
		catch(Exception e) {
			return rand;
		}
	}
	
	private static void fillCellNeighbours() {
		for (int i = 0; i < neighbours.length; i++) {
			for (int j = 0; j < neighbours[0].length; j++) {
				neighbours[i][j] = countNeighbours(i, j);
			}
		}
	}
	
	private static void updateCells() {
		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell.length; j++) {
				if(neighbours[i][j] == 3) {
					cell[i][j] = true;
				}
				if(neighbours[i][j] < 2 || neighbours[i][j] > 3) {
					cell[i][j] = false;
				}
			}
		}
	}
	
	private static void printNeighbours(int[][] mat){
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				System.out.print(mat[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
