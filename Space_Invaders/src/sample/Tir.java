package sample;

import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class Tir extends Application {

    private int posX;
    private int posY;
    private int speed = 10;
    private int size = 6;


    public Tir(int posX,int posY){
        this.posX=posX;
        this.posY=posY;
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

    public void update(GraphicsContext gc){
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(posX,posY,size,size);
        posY-=speed;
        affiche(gc);

    };
    public void update_alien(GraphicsContext gc){
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(posX,posY,size,size);
        posY+=speed;
        affiche(gc);

    }
    public void affiche(GraphicsContext gc){
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
