package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Main extends Application {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int ANT_COUNT = 100;

    ArrayList<Ant> ants;
    long lastTimeStamp = 0;

    static ArrayList<Ant> createAnts() {
        ArrayList<Ant> ants = new ArrayList<>();
        for (int i = 0; i < ANT_COUNT; i++) {
            Random r = new Random();
            ants.add(new Ant(r.nextInt(WIDTH), r.nextInt(HEIGHT)));
        }
        return ants;
    }

    void drawAnts(GraphicsContext context) {
        context.clearRect(0, 0, WIDTH, HEIGHT);
        for (Ant ant : ants) {
            context.setFill(ant.color);
            context.fillOval(ant.x, ant.y, 5, 5);
        }
    }

    static double randomStep() {
        return Math.random() * 2 - 1;
    }

    Ant moveAnt(Ant ant) {
        try {
            Thread.sleep(1);
        }catch (Exception e){}
        ant.x += randomStep();
        ant.y += randomStep();
        return ant;
    }

    Ant aggravateAnt(Ant ant){
        try{
            Thread.sleep(1);
        }catch (Exception e){}
        ArrayList<Ant> nearbyAnts = new ArrayList<>();
        nearbyAnts = ants.parallelStream()
                .filter(angryAnt -> {
                    return (Math.pow((Math.pow(Math.abs(angryAnt.x - ant.x), 2) + Math.pow(Math.abs(angryAnt.y - ant.y), 2)), 0.5) < 10);
                })
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
        if (nearbyAnts.size() == 1){
            ant.color = Color.BLACK;
            return ant;
        }
        ant.color = Color.RED;
        return ant;
    }

    void updateAnts() {
        ants = ants.parallelStream()
                .map(this::moveAnt)
                .map(this::aggravateAnt)
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
    }

    int fps (long now){
        double diff = now - lastTimeStamp;
        double diffSeconds = diff / 1000000000;
        return (int) (1 / diffSeconds);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = (Canvas) scene.lookup("#canvas");
        GraphicsContext context = canvas.getGraphicsContext2D();
        Label fpslabel = (Label) scene.lookup("#fps");
        primaryStage.setTitle("Ants");
        primaryStage.setScene(scene);
        primaryStage.show();

        ants = createAnts();


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                fpslabel.setText(fps(now) + "");
                lastTimeStamp = now;
                updateAnts();
                drawAnts(context);
            }
        };
        timer.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
