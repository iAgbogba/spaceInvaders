package sample;

import com.sun.scenario.animation.shared.TimerReceiver;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

import static sample.Alien.*;


public class SpaceInvaders extends Application {

    private static final int WIDTH=500,HEIGHT=400,TAILLE_JOUEUR=20,STOP_LEFT=50,STOP_RIGHT=WIDTH-TAILLE_JOUEUR-STOP_LEFT,STOP_DOWN=HEIGHT-TAILLE_JOUEUR-10;
    private static final int NOMBRE_ALIENS=6;
    private Canvas canvas;
    private Pane root;
    private Scene scene;
    private GraphicsContext gc;
    private Fusée joueur;
    private Alien alien;
    private ArrayList<Tir> tirs = new ArrayList<Tir>();
    private ArrayList<Alien> aliens = new ArrayList<Alien>();
    private ArrayList<Tir> tirs_aliens = new ArrayList<Tir>();
    private int temps=0;




    //@Override
    private void creerContenu(){
        Image JOUEUR_IMG = new Image("joueur.png");
        Image ALIEN_IMG = new Image("alien.png");

        canvas=new Canvas(WIDTH,HEIGHT);
        gc=canvas.getGraphicsContext2D();

        root = new Pane(canvas);
        root.setPrefSize(WIDTH,HEIGHT);

        scene = new Scene(root);

        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0,0,WIDTH,HEIGHT);
        joueur=new Fusée(WIDTH/2,HEIGHT-TAILLE_JOUEUR-10,TAILLE_JOUEUR,JOUEUR_IMG);
        joueur.affiche(gc);


        for(int i=0;i<NOMBRE_ALIENS;i++){
            Alien alien=new Alien(50+i*(TAILLE_JOUEUR+50),50,TAILLE_JOUEUR,ALIEN_IMG);
            aliens.add(alien);
            alien.affiche(gc);

        }


        scene.setOnKeyPressed(e->{
            switch (e.getCode()){
                case LEFT:
                    joueur.moveLeft(gc);
                    break;
                case RIGHT:
                    joueur.moveRight(gc);
                    break;
                case SPACE:
                    Tir tir=new Tir(joueur.posX+TAILLE_JOUEUR/2-3,joueur.posY-TAILLE_JOUEUR);
                    tirs.add(tir);
                    tir.affiche(gc);
                    break;
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),e -> run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


    }


    public void run(){

        move();
        temps++;
        if(temps%10==0)
            tir_alien();
        alien_tirs();
        joueur_tirs();

    }

    public void alien_tirs(){
        for(int i=0;i<tirs_aliens.size();i++){
            tirs_aliens.get(i).update_alien(gc);
            for(int j=0;j<aliens.size();j++){
                if (joueur.collision(tirs_aliens.get(i))){
                    gc.setFill(Color.grayRgb(20));
                    gc.fillRect(tirs_aliens.get(j).getPosX(),tirs_aliens.get(j).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
                    gc.fillRect(joueur.posX,joueur.posY,TAILLE_JOUEUR,TAILLE_JOUEUR);
                    tirs_aliens.remove(tirs_aliens.get(j));
                }
            }
            if(tirs_aliens.get(i).getPosY()==HEIGHT){
                gc.setFill(Color.grayRgb(20));
                gc.fillRect(tirs_aliens.get(i).getPosX(),tirs_aliens.get(i).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
                tirs_aliens.remove(tirs_aliens.get(i));
            }
        }
    }
    public void joueur_tirs(){
        for(int i=0;i<tirs.size();i++){
            tirs.get(i).update(gc);
            for(int j=0;j<aliens.size();j++){
                if (tirs.get(i).collision(aliens.get(j))) {
                    gc.setFill(Color.grayRgb(20));
                    gc.fillRect(aliens.get(j).getPosX(), aliens.get(j).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
                    aliens.remove(aliens.get(j));

                }
            }
            if(tirs.get(i).getPosY()==0){
                gc.setFill(Color.grayRgb(20));
                gc.fillRect(tirs.get(i).getPosX(),tirs.get(i).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
                tirs.remove(tirs.get(i));
            }

        }
    }
    public void move(){
        int minX=aliens.get(0).posX,maxX=aliens.get(aliens.size()-1).posX;
        boolean move_y=false;
        for(int j=0;j<aliens.size();j++){
            if(aliens.get(j).getDirection()==0 && maxX==STOP_RIGHT)
            {
                move_y=true;
                aliens.get(j).setDirection(1);
            }
            if(aliens.get(j).getDirection()==1&&minX==STOP_LEFT)
            {
                move_y=true;
                aliens.get(j).setDirection(0);
            }
            //if(aliens.get(j).posY==STOP_DOWN)

            aliens.get(j).update(gc,move_y);
        }
    }
    private void tir_alien(){
        for(int i=0;i<aliens.size();i++) {

            Tir tir = new Tir(aliens.get(i).posX + TAILLE_JOUEUR /2 - 3, aliens.get(i).posY + TAILLE_JOUEUR);
            tirs_aliens.add(tir);
        }
    }
    public static int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.creerContenu();
        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(this.scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
