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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.util.*;

public class SpaceInvaders extends Application 
{
    private static final int WIDTH=500,HEIGHT=400,TAILLE_JOUEUR=20,STOP_LEFT=20,STOP_RIGHT=WIDTH-TAILLE_JOUEUR-STOP_LEFT,STOP_DOWN=HEIGHT-TAILLE_JOUEUR-10;
    private static final int NOMBRE_ALIENS=6;
    private Canvas canvas;
    private Pane root;
    private Scene scene;
    private GraphicsContext gc;
    private Fusee joueur;
    private Alien alien;
    private ArrayList<Tir> tirs = new ArrayList<Tir>();
    private ArrayList<Alien> aliens = new ArrayList<Alien>();
    private ArrayList<Alien> aliensMort = new ArrayList<Alien>();
    private ArrayList<Tir> tirs_aliens = new ArrayList<Tir>();
    private int temps=0;
    private int nbreVieJoueur=100;
    private int score=0;
    private int maxDiffHauteur = 30;
    private int nbreIteration = 0;
    private int minX = 0;
    private int maxX = 0;



    //@Override
    private void creerContenu(){
        Image JOUEUR_IMG = new Image("joueur.png");

        canvas=new Canvas(WIDTH,HEIGHT);
        gc=canvas.getGraphicsContext2D();

        root = new Pane(canvas);
        root.setPrefSize(WIDTH,HEIGHT);
        
        scene = new Scene(root);

        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0,0,WIDTH,HEIGHT);
        joueur=new Fusee(WIDTH/2,HEIGHT-TAILLE_JOUEUR-10,TAILLE_JOUEUR,JOUEUR_IMG);
        joueur.affiche(gc);

        creerAllien();

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

        enleverAliensMort();
        move();
        temps++;
        if(temps%10==0)
            tir_alien();
        if (nbreVieJoueur == 0)   finDePartie();

        alien_tirs();
        joueur_tirs();
        printStats();
    }

// boom alien et mort lorsque les aliens sont en bas
// arrêter le programme après la fenêtre
// Niveau de joueur, différents types de joueur avec différents type de pistolet
// musique
    public void joueurToucher()
    {
        if (nbreVieJoueur == 0)  
        {
            gc.fillRect(joueur.posX,joueur.posY,TAILLE_JOUEUR,TAILLE_JOUEUR);
            Image BOOM_IMG = new Image("boom.png");
            Fusee joueurMort =new Fusee(joueur.posX,joueur.posY,TAILLE_JOUEUR,BOOM_IMG);
            joueurMort.affiche(gc);
        }
    }

    public void creerAllien()
    {
        Image ALIEN_IMG = new Image("alien.png");
        for(int i=0;i<NOMBRE_ALIENS;i++){
                Alien alien=new Alien(50+i*(TAILLE_JOUEUR+50),50,TAILLE_JOUEUR,ALIEN_IMG);
                aliens.add(alien);
                alien.affiche(gc);
        }
    }
    public void finDePartie()
    {
        JOptionPane jop1;
        jop1 = new JOptionPane();
        String fin = String.format("PARTIE TERMINEE, POINTS: %d", score);
        jop1.showMessageDialog(null, fin, "BOOM", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    public void alien_tirs()
    {
        ArrayList<Tir> tirsSupprim = new ArrayList<Tir>();
        boolean aSupprimer = false;
        for(int i=0;i<tirs_aliens.size();i++)
        {
            aSupprimer = false;
            tirs_aliens.get(i).update_alien(gc);
            // for(int j=0;j<aliens.size();j++){
                if (joueur.collision(tirs_aliens.get(i))){
                    gc.setFill(Color.grayRgb(20));
                    gc.fillRect(tirs_aliens.get(i).getPosX(),tirs_aliens.get(i).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
                    aSupprimer = true;
                    if (nbreVieJoueur>0) nbreVieJoueur--;
                    joueurToucher();
                }
            // }
            if(tirs_aliens.get(i).getPosY()==HEIGHT){
                gc.setFill(Color.grayRgb(20));
                gc.fillRect(tirs_aliens.get(i).getPosX(),tirs_aliens.get(i).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
                aSupprimer = true;
            }
            if(aSupprimer) tirsSupprim.add(tirs_aliens.get(i));
        }
        tirs_aliens.removeAll(tirsSupprim);
    }

    public void printStats()
    {
        String stat = String.format("Point: %d  Vie: %d ",score,  nbreVieJoueur);
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, WIDTH, 20);
        gc.setFill(Color.RED);
        gc.fillText(stat, 0, 10);
        gc.setFill(Color.grayRgb(20));
    }

    public void enleverAliensMort()
    {
        for(int j=0;j<aliensMort.size();j++)
        {
            gc.setFill(Color.grayRgb(20));
            gc.fillRect(aliensMort.get(j).getPosX(), aliensMort.get(j).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
        }
        aliensMort.clear();
    }

    public void joueur_tirs()
    {
        ArrayList<Tir> tirsSupprim = new ArrayList<Tir>();
        boolean aSupprimer = false;
        for(int i=0;i<tirs.size();i++){
            aSupprimer = false;
            tirs.get(i).update(gc);
            for(int j=0;j<aliens.size();j++){
                if (tirs.get(i).collision(aliens.get(j))) 
                {
                    gc.setFill(Color.grayRgb(20));
                    gc.fillRect(tirs.get(i).getPosX(),tirs.get(i).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
                    Image BOOM_IMG = new Image("boom.png");
                    Alien AlienMort =new Alien(aliens.get(j).getPosX(), aliens.get(j).getPosY(),TAILLE_JOUEUR,BOOM_IMG);
                    AlienMort.affiche(gc);
                    aliensMort.add(AlienMort);
                    aSupprimer = true;
                    score++;

                    aliens.remove(aliens.get(j));
                }
            }
            if(tirs.get(i).getPosY()==0){
                gc.setFill(Color.grayRgb(20));
                gc.fillRect(tirs.get(i).getPosX(),tirs.get(i).getPosY(),TAILLE_JOUEUR,TAILLE_JOUEUR);
                aSupprimer = true;
            }

            if(aSupprimer) tirsSupprim.add(tirs.get(i));
        }

        tirs.removeAll(tirsSupprim);
    }

    public boolean  ennemiEnBas(int hauteur)
    {
        if((joueur.posY - hauteur ) < maxDiffHauteur) return true;
        return false;
    }

    public void versLeBas()
    {
        nbreIteration++;
        if (nbreIteration%5 == 0) creerAllien();
    }

    public void setMaxAndMinX()
    {
        minX = aliens.get(0).posX;
        maxX = aliens.get(aliens.size()-1).posX;
        for(int j=0;j<aliens.size();j++)
        {
            if (aliens.get(j).posX < minX) minX = aliens.get(j).posX;
            if (aliens.get(j).posX > maxX) maxX = aliens.get(j).posX;
        }
    }

    public void move()
    {
        if (aliens.size() > 0)
        {
            setMaxAndMinX();
            // int minX=aliens.get(0).posX,maxX=aliens.get(aliens.size()-1).posX;
            boolean move_y=false;
            for(int j=0;j<aliens.size();j++){
                if(aliens.get(j).getDirection()==0 && maxX==(STOP_RIGHT))
                {
                    versLeBas();
                    move_y=true;
                    aliens.get(j).setDirection(1);
                } 
                if(aliens.get(j).getDirection()==1&&minX==(STOP_LEFT))
                {
                    versLeBas();
                    move_y=true;
                    aliens.get(j).setDirection(0);
                }
                aliens.get(j).update(gc,move_y);
                if(ennemiEnBas(aliens.get(j).getPosY()))
                {
                    nbreVieJoueur = 0;
                    joueurToucher();
                }
            }
        }
    }
    private void tir_alien()
    {
        for(int i=0;i<aliens.size();i++) {

            Tir tir = new Tir(aliens.get(i).posX + TAILLE_JOUEUR /2 - 3, aliens.get(i).posY + TAILLE_JOUEUR);
            tirs_aliens.add(tir);
        }
    }
    public static int distance(int x1, int y1, int x2, int y2) 
    {
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
