package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Alien extends Fusée{
    private static final int vitesse=2;
    private static int direction=0;

    public static void setDirection(int direction) {
        Alien.direction = direction;
    }

    public static int getVitesse() {
        return vitesse;
    }

    public static int getDirection() {
        return direction;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }



    public Alien(int posX, int posY, int size, Image img) {
        super(posX, posY, size, img);
    }

    public void update(GraphicsContext gc,boolean deplacement_y) {
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(posX,posY,size,size);
        if(deplacement_y){
            posY+=vitesse;
        }
        if(direction==0) {
            posX += vitesse;

        }else if(direction==1){
            posX -= vitesse;
        }
        affiche(gc);
    }
}
