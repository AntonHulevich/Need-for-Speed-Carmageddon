package game.object;

import game.constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MyCar extends GameObject {
    Image img1 = new ImageIcon("C:\\Users\\admin\\IdeaProjects\\games\\racing2D\\src\\res\\myCar1.1.png").getImage();
    Image img2 = new ImageIcon("C:\\Users\\admin\\IdeaProjects\\games\\racing2D\\src\\res\\myCar2.1.png").getImage();
    Image img3 = new ImageIcon("C:\\Users\\admin\\IdeaProjects\\games\\racing2D\\src\\res\\myCar3.1.png").getImage();
    Image img = img1;
    private final int pictureWidth = img.getHeight(imageObserver); // ширина машины в пикселях
    private final int pictureHeight = img.getHeight(imageObserver);// высота машины в пикселях
    public int getPictureWidth() {
        return pictureWidth;
    }
    public int getPictureHeight() {
        return pictureHeight;
    }
    int health = 100;
    int speed_up = 0; // ускорение
    int distance = 0; // путь
    int layer1 = 0; // слой дороги
    int layer2 = 1200; // второй слой дороги(1200 потому что пикча первого слоя рамером 1200)
    int turn = 0; // координата поворота по y
    @Override
    public void move(){ // движение (создает илюзию движения машины по факту это сдвиг пикчи дороги)
        distance = distance + speed;
        speed = speed + speed_up;
        if(speed <= 0){ //если скорость ровна нулю то назад не едим а стоим на месте
            speed = 0;
        }
        if (speed >= Constants.MAX_SPEED) { // ограничение выхода за пределы максимальное скорости
            speed = Constants.MAX_SPEED;
        }

        y = y - turn;

        if (y <= Constants.MAX_TOP) { // ограничение выхода машины за пределы экраны вверх
            y = Constants.MAX_TOP;
        }
        if (y >= Constants.MAX_BOTTOM){
            y = Constants.MAX_BOTTOM;
        }
        if (layer2 - speed <= 0){ // зацикленость чередования слоев дороги
            layer1 = 0;
            layer2 = 1200;
        } else {
            layer1 = layer1 - speed;
            layer2 = layer2 - speed;
        }
    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_RIGHT){ // усокрение
            speed_up = 1;
        }
        if(key == KeyEvent.VK_LEFT){ // торможение
            speed_up = -1;
        }
        if(key == KeyEvent.VK_UP){ // поворот вверх
            turn = 5; // смещает наш обьект вверх на 5
            img = img2; // заменяет нашу картику
        }
        if(key == KeyEvent.VK_DOWN){ // поворот вниз
            turn = -5; //// смещает наш обьект вниз на 5
            img = img3; // заменяет нашу картику
        }
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT){ // отпускание клавиши
            speed_up = 0;
        }
        if(key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN){
            turn = 0;
            img = img1;
        }
    }
}