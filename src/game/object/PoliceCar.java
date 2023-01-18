package game.object;

import javax.swing.*;
import java.awt.*;

public class PoliceCar extends GameObject {
    Image img = new ImageIcon("C:\\Users\\admin\\IdeaProjects\\games\\racing2D\\src\\res\\policeCar1.1.png").getImage();
    Road road;
    private final int pictureWidth = img.getHeight(imageObserver); // ширина машины в пикселях
    private final int pictureHeight = img.getHeight(imageObserver);// высота машины в пикселях
    public int getPictureWidth() {
        return pictureWidth;
    }
    public int getPictureHeight() {
        return pictureHeight;
    }
    public PoliceCar(int x, int y, int speed, Road road) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.road = road;
    }
    @Override
    public void move(){
        x = x - road.myCar.speed + speed; //скорость движения врагов в обратном направлении
    }
}
