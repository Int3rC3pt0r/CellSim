package application;
	
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
	
	static double width = 900;
	static double height = 900;
	static int fSizeX = 90;
	static int fSizeY = 90;
	static boolean[][] cell;
	static boolean[][] cellSave1;
	static int[][] neighbours;
	static boolean rand = true;
	static boolean running = false;
	static boolean editMode = false;
	
	Button fill, next, genField, boarderChange, edit, save, load;
	Canvas canvas;
	static TextField percentage;
	static Slider animTime;
	static Label animTimeValue;
	Label lastAct;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			fill = new Button("PrintNeighbours");
			next = new Button("Start");
			genField = new Button("genField");
			animTime = new Slider(1, 100, 1);
			animTimeValue = new Label("1");
			lastAct = new Label("\tNo last Action, please generate a Field");
			boarderChange = new Button("Change boarder to Dead");
			edit = new Button("Edit");
			save = new Button("Save");
			load = new Button("Load");
			
			
			percentage = new TextField("20");
			
			canvas = new Canvas(width,height);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			
			canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				if(editMode) {
					cell[(int)(e.getX()/(width/fSizeX))][(int)(e.getY()/(height/fSizeY))] = !cell[(int)(e.getX()/(width/fSizeX))][(int)(e.getY()/(height/fSizeY))];
				}
			});
			canvas.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
				if(editMode) {
					fillWhite(gc);
					showField(gc);
					Color col = new Color(0,0.7,0,0.5);
					fillRect(gc,(int)(e.getX()/(width/fSizeX)),(int)(e.getY()/(height/fSizeY)), col);
					genGrid(gc);
				}
			});
			
			genField.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				fillWhite(gc);
				initiateField(gc);
				rdmField();
				showField(gc);
				genGrid(gc);
				lastAct.setText("\tField generated");
			});
			
			next.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				running = !running;
				if(running) {
					next.setText("Stop");
					lastAct.setText("\tSimulation started");
				}
				else {
					next.setText("Start");
					lastAct.setText("\tSimulation stopped");
				}
			});
			
			boarderChange.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				rand = !rand;
				if(rand) {
					boarderChange.setText("Change boarder to Dead");
				}
				else {
					boarderChange.setText("Change boarder to Living");
				}
			});
			
			fill.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				fillCellNeighbours();
				printNeighbours(neighbours);
			});
			edit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				editMode = !editMode;
				if(editMode) {
					lastAct.setText("\tEdit Mode enabled");
				}
				else {
					lastAct.setText("\tEdit Mode disabled");
				}
			});
			save.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				cellSave1 = cell;
				lastAct.setText("\tCurrent cell-positions saved");
			});
			load.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
				cell = cellSave1;
				lastAct.setText("\tLoaded saved cell-positions");
				fillWhite(gc);
				showField(gc);
				genGrid(gc);
			});
			
			
			HBox top = new HBox();
			top.getChildren().addAll(genField, percentage, next, boarderChange, edit, save, load);
			
			HBox bot = new HBox();
			bot.getChildren().addAll(animTime, animTimeValue, lastAct);
			
			BorderPane root = new BorderPane();
			root.setTop(top);
			root.setCenter(canvas);
			root.setBottom(bot);
			
			new AnimationTimer() {
				long lastTick = 0;

				public void handle(long now) {
					if (now - lastTick > 1000000000/Double.parseDouble(animTimeValue.getText()) && running) {
						lastTick = now;
						fillCellNeighbours();
						updateCells();
						fillWhite(gc);
						showField(gc);
						genGrid(gc);
						updateSlider();
					}
				}

			}.start();
			
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
	}
	
	private static void genGrid(GraphicsContext gc) {
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
	
	private static void fillRect(GraphicsContext gc, int x, int y, Color col){
		gc.setFill(col);
		gc.fillRect(width/fSizeX*x, height/fSizeY*y, width/fSizeX, height/fSizeY);
	}
	
	private static void rdmField() {
		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell[0].length; j++) {
				int r = (int) (Math.random() * 101);
				if(r <= Integer.parseInt(percentage.getText()) && Integer.parseInt(percentage.getText()) != 0) {
					cell[i][j] = true;
				}
				else {
					cell[i][j] = false;
				}
			}
		}
	}
	
	private static void showField(GraphicsContext gc) {
		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell[0].length; j++) {
				if(cell[i][j] == true) {
					fillRect(gc,i,j, Color.LAWNGREEN);
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
			for (int j = 0; j < cell[0].length; j++) {
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
	
	private static void updateSlider() {
		animTimeValue.setText(""+animTime.getValue());
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
