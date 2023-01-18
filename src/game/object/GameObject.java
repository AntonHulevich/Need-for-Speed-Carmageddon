package game.object;

import java.awt.*;
import java.awt.image.ImageObserver;

public abstract class GameObject {
    ImageObserver imageObserver = (img, infoFlags, x, y, width, height) -> false;
    int pictureWidth;
    int pictureHeight;

    int speed = 10; // скорость
    int x = 0; // координата расположения машины по оси Х
    int y = 100; // координата расположения машины по оси У

    public abstract void move();

    public Rectangle getRect() { //обьект прямоугольник для столкновения машин
        return new Rectangle(x, y, getPictureWidth(), getPictureHeight());
    }

    public int getPictureWidth() {
        return pictureWidth;
    }

    public int getPictureHeight() {
        return pictureHeight;
    }

}
