package game;

import game.object.Road;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {

       JFrame f = new JFrame("Need for Speed:Carmageddon"); // создает окно игры
       f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // константа кристик закрыте игры
       f.setSize(1100,600); // размер окна игры
       f.add(new Road()); // добовляет обьект Дорога в окно игры
       f.setVisible(true); // дает разрешение на отрисовку обькта дорога в игре

    }
}
