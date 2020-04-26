package sample;

import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;

public class Fusee extends Application{
    private static final double HEIGHT = 20;

    public void setPosY(int posY) {
        this.posY = posY;
    }

    protected int posX;
    protected int posY;
    protected int size;
    protected boolean explosion,detruit;
    protected Image img;

    public Fusee(int posX,int posY,int size,Image img){
        this.img=img;
        this.posX=posX;
        this.posY=posY;
        this.size=size;
    };

    public void moveRight(GraphicsContext gc){
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(posX,posY,size,size);
        this.posX+=5;
        affiche(gc);

    };

    public void moveLeft(GraphicsContext gc){
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(posX,posY,size,size);
        this.posX-=5;
        affiche(gc);
    };

    public void affiche(GraphicsContext gc){
        gc.drawImage(img,posX,posY,size,size);
    };

    public void update(){ };


    public boolean collision(Tir tir){
        if(SpaceInvaders.distance(this.posX,this.posY,tir.getPosX(),tir.getPosY())<20){
            return true;
        }
        return false;
    };


    public void start(Stage stage) throws Exception {

    }


}
