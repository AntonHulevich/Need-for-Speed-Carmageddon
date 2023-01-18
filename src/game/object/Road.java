package game.object;

import game.constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Road extends JPanel implements ActionListener {
    Timer mainTimer = new Timer(20, this); // таймер обновления событий(метод actionPerformed)
    Image img = new ImageIcon("C:\\Users\\admin\\IdeaProjects\\games\\racing2D\\src\\res\\road4.jpg").getImage();
    MyCar myCar = new MyCar(); // наша машина
    Thread policeCarFactory = new Thread(new TreadAddPoliceCar()); // поток для генерации полицейских машин
    Thread zombieFactory = new Thread(new TreadAddZombie()); // поток для генерации зомби
    Thread bombFactory = new Thread(new TreadAddBoom()); // поток для генерации бомб
    ConcurrentHashMap<GameObject, String> concurrentHashMap = new ConcurrentHashMap<>();

    public Road() {
        mainTimer.start(); //запускает таймер
        policeCarFactory.start();//запуск потока создания полицейских машин
        zombieFactory.start();//запуск потока создания зомби машин
        bombFactory.start();//запуск потока создания бомб машин
        addKeyListener(new MyKeyAdapter()); // обькт для работы с клавиатурой
        setFocusable(true); // фокус на нажатие клавишь
    }

    class TreadAddPoliceCar implements Runnable {
        @Override
        public void run() {
            while (true) {
                Random random = new Random();
                try {
                    Thread.sleep(random.nextInt(2000));
                    PoliceCar policeCar = new PoliceCar(1200, random.nextInt(600), random.nextInt(60), Road.this);
                    concurrentHashMap.put(policeCar, "policeCar");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class TreadAddZombie implements Runnable {
        @Override
        public void run() {
            while (true) {
                Random random = new Random();
                try {
                    Thread.sleep(random.nextInt(2000));
                    Zombie zombie = new Zombie(1200, random.nextInt(600), 0, Road.this);
                    concurrentHashMap.put(zombie, "zombie");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class TreadAddBoom implements Runnable {
        @Override
        public void run() {
            while (true) {
                Random random = new Random();
                try {
                    Thread.sleep(random.nextInt(2000));
                    Bomb bomb = new Bomb(1200, random.nextInt(600), 0, Road.this);
                    concurrentHashMap.put(bomb, "bomb");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class MyKeyAdapter extends KeyAdapter { // класс адаптер для работы с клавиатурой
        @Override
        public void keyPressed(KeyEvent e) {
            myCar.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            myCar.keyReleased(e);
        }
    }

    @Override
    public void paint(Graphics g) {
        (g).drawImage(img, myCar.layer1, 0, null); //ресует первый слой дороги
        (g).drawImage(img, myCar.layer2, 0, null); //ресует второй слой дороги
        (g).drawImage(myCar.img, myCar.x, myCar.y, null); // ресует машину

        double v = (200.0 / Constants.MAX_SPEED) * myCar.speed;
        g.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.ITALIC, 30);
        g.setFont(font);
        g.drawString("Speed: " + v, 100, 30);
        g.drawString("Point: " + Zombie.point, 450, 30);
        g.drawString("Health: " + myCar.health, 800, 30);

        Iterator<GameObject> iterator = concurrentHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            if (gameObject.getClass().equals(PoliceCar.class)) {
                PoliceCar policeCar = (PoliceCar) gameObject;
                if (policeCar.x >= 2400 || policeCar.x <= -2400 ||
                        policeCar.y <= Constants.MAX_TOP || policeCar.y >= Constants.MAX_BOTTOM) {
                    iterator.remove();
                } else {
                    (g).drawImage(policeCar.img, policeCar.x, policeCar.y, null); // ресует полицейских
                }
            } else if (gameObject.getClass().equals(Zombie.class)) {
                Zombie zombie = (Zombie) gameObject;
                if (zombie.x >= 2400 || zombie.x <= -2400 ||
                        zombie.y <= Constants.MAX_TOP || zombie.y >= Constants.MAX_BOTTOM) {
                    iterator.remove();
                } else {
                    (g).drawImage(zombie.img, zombie.x, zombie.y, null); // ресует полицейских
                }
            } else if (gameObject.getClass().equals(Bomb.class)) {
                Bomb bomb = (Bomb) gameObject;
                if (bomb.x >= 2400 || bomb.x <= -2400 ||
                        bomb.y <= Constants.MAX_TOP || bomb.y >= Constants.MAX_BOTTOM) {
                    iterator.remove();
                } else {
                    (g).drawImage(bomb.img, bomb.x, bomb.y, null); // ресует полицейских
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        myCar.move(); // Логика движения машины
        repaint(); // перересовывет paint
        testCollisionWithEnemies(); // метод обработки события столкновения
        testWin(); // метод обработки события выйграша
        concurrentHashMap.keySet().stream().forEach(policeCar -> policeCar.move()); //логика движения Обьктов
    }

    private void testWin() {
        if (myCar.distance + Zombie.point > 200000) {
            JOptionPane.showMessageDialog(null, "Yuo Win!!!");
            System.exit(0);
        }
    }

    private void testCollisionWithEnemies() { // метод обработки события столкновения
        PoliceCar policeCar;
        Zombie zombie;
        Bomb bomb;
        Iterator<GameObject> iterator = concurrentHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            if (gameObject.getClass().equals(PoliceCar.class)) {
                policeCar = (PoliceCar) gameObject;
                if (myCar.getRect().intersects(policeCar.getRect())) { // получаем обьект и проверяем пересекаются ли наши треугольники
                    JOptionPane.showInternalMessageDialog(null, "Yuo lose!");
                    System.exit(1);
                } else if (myCar.health <= 0) {
                    JOptionPane.showInternalMessageDialog(null, "Yuo lose!");
                    System.exit(1);
                }
            } else if (gameObject.getClass().equals(Zombie.class)) {
                zombie = (Zombie) gameObject;
                if (myCar.getRect().intersects(zombie.getRect())) {
                    iterator.remove(); // удаляет офицера
                    Zombie.point = Zombie.point + 5000;
                }
            } else if (gameObject.getClass().equals(Bomb.class)) {
                bomb = (Bomb) gameObject;
                if (myCar.getRect().intersects(bomb.getRect())) {
                    iterator.remove();
                    myCar.speed = myCar.speed / 3;
                    myCar.health = myCar.health - Bomb.damage;
                }
            }
        }
    }
}
