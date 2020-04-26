package sample;

import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class Tir extends Application {

    private int posX;
    private int posY;
    private int speedY = 10;
    private int speedX = 2;
    private int size = 6;
    private int direction;


    public Tir(int posX,int posY, int direction){
        this.posX=posX;
        this.posY=posY;
        this.direction=direction;
    };

    public int getSize() {
        return size;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void update_joueur(GraphicsContext gc){
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(posX,posY,size,size);
        posY-=speedY;
        posX+= (speedX * direction);
        afficheJoueur(gc);

    };
    public void update_alien(GraphicsContext gc){
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(posX,posY,size,size);
        posY+=speedY;
        afficheAlien(gc);

    }
    public void afficheJoueur(GraphicsContext gc)
    {
        gc.setFill(Color.BLUE);
        gc.fillOval(posX,posY,size,size);
    };
    public void afficheAlien(GraphicsContext gc)
    {
        gc.setFill(Color.RED);
        gc.fillOval(posX,posY,size,size);
    };

    public boolean collision(Alien alien){
        if(SpaceInvaders.distance(this.posX,this.posY,alien.posX,alien.posY)<20){
            return true; 
        }
        return false;
    };



    public void start(Stage primaryStage) {

    }
}
