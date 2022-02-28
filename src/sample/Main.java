package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Main extends Application {

    //private variables for easy access

    //will hold the 8 images
    Image[] images = new Image[16];
    //will display the 8 images each one twice
    ImageView[] imageViews = new ImageView[16];
    Button[] buttons = new Button[16];

    //shuffler method will shuffle an array of 16 numbers 0 - 15
    //to randomize it with out repeating the same number
    //then the images will be assigned to the imageview elements is the same order
    //this way we could
    int[] gameLayout = shuffler();




    //hold the memory cards
    GridPane gridPane = new GridPane();

    //this will hold the values of the two cards the player picked
    ArrayList<Integer> hold = new ArrayList<>();

    //count and display score
    int score = 0;
    Text scoreText = new Text((Integer.toString(score)));
    // count and display number of attempts
    int attempts;
    Text numberOfAttempts = new Text((Integer.toString(attempts)));

    //this array will hold the value for wither the card was flipped or not
    boolean[] foundIt = new boolean[16];

    // count and display time
    int time = 0;
    Text timeText = new Text((Integer.toString(time)));

    //update time
    Timeline tl;



    //will only change the center pane (the cards,asking for the players name, displaying the top scores)
    BorderPane borderPane = new BorderPane();

    //this is used to save and display the top 3 scores
    ArrayList<Player> players = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Project201-memoryCards");

        //method loadImages() will load the images addresses  to the array each image will be loaded
        // to two different elements

        images = loadImages();

        // defined the variables, add the buttons to the gridPane, assign each imageView to a button


        int index = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                buttons[index] = new Button();

                imageViews[index] = new ImageView();
                buttons[index].setPrefSize(60, 60);
                imageViews[index].setFitHeight(80);
                imageViews[index].setFitWidth(80);
                buttons[index].setGraphic(imageViews[index]);
                foundIt[index] = false;
                GridPane.setConstraints(buttons[index], i, j);
                gridPane.getChildren().add(buttons[index]);
                index += 1;
            }

        gridPane.setGridLinesVisible(true);
        gridPane.setAlignment(Pos.CENTER);

        borderPane.setCenter(gridPane);


        //each button will pass its index to Update method where checking the cards,adding score and attempts
        //play sounds,etc
        buttons[0].setOnAction(e -> update(0));
        buttons[1].setOnAction(e -> update(1));
        buttons[2].setOnAction(e -> update(2));
        buttons[3].setOnAction(e -> update(3));
        buttons[4].setOnAction(e -> update(4));
        buttons[5].setOnAction(e -> update(5));
        buttons[6].setOnAction(e -> update(6));
        buttons[7].setOnAction(e -> update(7));
        buttons[8].setOnAction(e -> update(8));
        buttons[9].setOnAction(e -> update(9));
        buttons[10].setOnAction(e -> update(10));
        buttons[11].setOnAction(e -> update(11));
        buttons[12].setOnAction(e -> update(12));
        buttons[13].setOnAction(e -> update(13));
        buttons[14].setOnAction(e -> update(14));
        buttons[15].setOnAction(e -> update(15));


        //This button will restart the game with a different layout and reset the score attempt and time
        Button startOverButton = new Button("StartOver");
        startOverButton.setOnAction(e ->
                startOver()
        );
        //Update time
        EventHandler<ActionEvent> eventHandler = e -> {
            time += 1;
            timeText.setText(Integer.toString(time));
        };

        tl = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
        tl.setCycleCount(Timeline.INDEFINITE);

        // game stateLayout will view for the scores boxes and their respective text
        VBox gameStats = gameStatsLayout();
        gameStats.setSpacing(25);
        gameStats.setAlignment(Pos.CENTER);
        startOverButton.setPrefSize(150,20);
        borderPane.setBottom(startOverButton);
        //set the rest of the layout
        Rectangle backgroundForState = new Rectangle(750, 550, Color.gray(0.8));
        HBox hBox = new HBox(borderPane, gameStats);
        StackPane stackPane = new StackPane(backgroundForState, hBox);
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //make the layout for
    VBox gameStatsLayout() {
        //create time counter GUI
        Rectangle disTime = new Rectangle(100, 50, Color.WHITE);
        timeText.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 24.0));
        StackPane timeArea = new StackPane(disTime, timeText);
        Text textTime = new Text("        Time: ");
        textTime.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 24.0));
        textTime.setFill(Color.WHITE);
        HBox hTime = new HBox(textTime, timeArea);
        hTime.setAlignment(Pos.CENTER);

        //create score counter GUI
        Rectangle disScore = new Rectangle(100, 50, Color.WHITE);
        scoreText.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 24.0));
        StackPane scoreArea = new StackPane(disScore, scoreText);
        Text textScore = new Text("        Score: ");
        textScore.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 24.0));
        textScore.setFill(Color.WHITE);
        HBox hScore = new HBox(textScore, scoreArea);
        hScore.setAlignment(Pos.CENTER);

        //create attempts counter GUI
        Rectangle disAttempts = new Rectangle(100, 50, Color.WHITE);
        numberOfAttempts.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20.0));
        StackPane attemptsArea = new StackPane(disAttempts, numberOfAttempts);
        Text textAttempts = new Text("   Attempts:");
        textAttempts.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 24.0));
        textAttempts.setFill(Color.WHITE);
        HBox hAttempts = new HBox(textAttempts, attemptsArea);
        hAttempts.setAlignment(Pos.CENTER);
        return new VBox(hTime, hScore, hAttempts);
    }

    Image[] loadImages() {
        Image[] image = new Image[16];
        for(int i = 1 ;i<9;i++){
            image[i-1] = new Image((i+".png"));
        }
        for (int i = 0 ;i<8;i++){
            image[i+8] = image[i];
        }
        return image;
    }

    int[] shuffler() {
        Random rand = new Random();
        int[] shuffle = {1, 11, 2, 12, 3, 13, 4, 14, 5, 15, 6, 0, 7, 8, 9, 10};
        for (int i = 0; i < shuffle.length; i++) {
            int randomIndexToSwap = rand.nextInt(shuffle.length);
            int temp = shuffle[randomIndexToSwap];
            shuffle[randomIndexToSwap] = shuffle[i];
            shuffle[i] = temp;
        }
        return shuffle;
    }

    void update(int index) {

        Media bruh = new Media((new File("bruh.mp4")).toURI().toString());
        MediaPlayer bruhGo = new MediaPlayer(bruh);
        Media wowSound = new Media((new File("wow.mp4")).toURI().toString());
        MediaPlayer wowSoundGo = new MediaPlayer(wowSound);
        Media flip = new Media((new File("flip.mp4")).toURI().toString());
        MediaPlayer flipGo = new MediaPlayer(flip);

        boolean bORh;

        if (flipGo.getStatus() == MediaPlayer.Status.PLAYING) {
           flipGo.stop();}
        if (wowSoundGo.getStatus() == MediaPlayer.Status.PLAYING) {
            wowSoundGo.stop();
        } if (bruhGo.getStatus() == MediaPlayer.Status.PLAYING) {
            bruhGo.stop();
        }

        if (!hold.contains(index)) {
            if (imageViews[index].getImage() == null && !foundIt[index]) {
                flipGo.play();
                imageViews[index].setImage(images[gameLayout[index]]);
                hold.add(index);
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        if (hold.size() == 2) {
            //update the attempts every time a player pick 2 cards
            attempts += 1;
            numberOfAttempts.setText(Integer.toString(attempts));


            //Compare the addresses of the two images, because the same image was assigned to two different elements
            // if the address of both imageViews[index] is the same then its the same image

            if (imageViews[hold.get(0)].getImage() == imageViews[hold.get(1)].getImage()) {
                score += 1;
                scoreText.setText(Integer.toString(score));
                foundIt[hold.get(0)] = true;
                foundIt[hold.get(1)] = true;
                imageViews[hold.get(0)].setOpacity(0.3);
                imageViews[hold.get(1)].setOpacity(0.3);
                bORh = true;
            } else {
                bORh = false;
            }

            Timeline tl = new Timeline(new KeyFrame(Duration.millis(100)));
            tl.play();
            tl.setOnFinished(e -> {
                try {
                    if (bORh) {
                        wowSoundGo.play();
                    } else {
                        bruhGo.play();
                    }
                    TimeUnit.SECONDS.sleep(1);
                    if (score == 8) {
                        gameOver();

                    }

                } catch (InterruptedException | IOException interruptedException) {
                    interruptedException.printStackTrace();
                }
                for (int i = 0; i < imageViews.length; i++) {
                    if (!foundIt[i])
                        imageViews[i].setImage(null);
                }

            });
            System.out.println("clear");
            hold.clear();


        }
    }

    void gameOver() throws IOException {
        tl.stop();

        FileInputStream fileInputStream = new FileInputStream("highScores.txt");
        Scanner scan = new Scanner(fileInputStream);
        TextField getName = new TextField();
        HBox hBox = new HBox(new Text("Enter your name:  "), getName);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);


        while (scan.hasNext()) {
            int newAttempts = scan.nextInt();
            int newTime = scan.nextInt();
            String newName = scan.nextLine();
            players.add(new Player(newName, newAttempts, newTime));
        }


        scan.close();
        fileInputStream.close();

        getName.setOnAction(e -> {

            FileOutputStream fileOutputStream = null;
            String tempName = getName.getText();
            try {


                fileOutputStream = new FileOutputStream("highScores.txt");
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            assert fileOutputStream != null;
            PrintWriter pt = new PrintWriter(fileOutputStream);

            players.add(new Player(tempName, attempts, time));
            Collections.sort(players);

            for (int i = 0; i < players.size(); i++) {

                pt.println(players.get(i).getAttempts() + "\t" + players.get(i).getTime() + "\t" + players.get(i).getName().trim());
                if (i == 2)
                    break;
            }
            pt.close();
            displayRes();
        });


    }

    void displayRes() {

        VBox topScores = new VBox();

        for (int i = 0 ;i<players.size();i++) {
            Text playerResults = new Text("Player Name: " + players.get(i).getName() + "\nPlayer Number Of  Attempts: "
                    + players.get(i).getAttempts() + "\tPlayer Time: " + players.get(i).getTime());

            topScores.getChildren().add(playerResults);
            if (i==2){
                break;
            }
        }
        topScores.setSpacing(20);
        topScores.setAlignment(Pos.CENTER);



        borderPane.setCenter(topScores);



    }
    void startOver() {
        gameLayout = shuffler();
        for (int i = 0; i < foundIt.length; i++) {
            foundIt[i] = false;
            imageViews[i].setImage(null);
            imageViews[i].setOpacity(1);
        }


        time = 0;
        timeText.setText(time + "");
        attempts = 0;
        numberOfAttempts.setText(attempts + "");
        score = 0;
        scoreText.setText(score + "");
        borderPane.setCenter(gridPane);
        tl.play();

    }

}



class Player implements Comparable {
    private final String name;
    private final int attempts;
    private final int time;

    public Player(String name, int attempts, int time) {
        this.attempts = attempts;
        this.time = time;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getTime() {
        return time;
    }


    @Override
    public int compareTo(Object o) {
        Player other = (Player) o;
        if (this.getAttempts() < other.getAttempts())
            return -1;
        else if (this.getAttempts() == other.getAttempts()) {
            if (this.getTime() < other.getTime())
                return -1;
            else if (this.getTime() > other.getTime())
                return 1;
            else if (this.getName().compareTo(other.getName()) > 0) {
                return 1;
            } else return -1;
        }

        return 1;
    }
}
