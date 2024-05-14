package SnakeGamePackage;

import javax.swing.JFrame; //imports JFrame class providing the base window for the game
public class GameFrame extends JFrame{

    public GameFrame(){                                         //empty parameter constructor defined
        this.add(new GamePanel());                             //adds the visual components of game panel to the frame
        this.setTitle("Snake");                                 //sets the JFrame title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //exits the application when frame is closed
        this.setResizable(false);                               //frame cannot be resized by the user
        this.pack();                                            //sizes window appropriately to fit all contents
        this.setVisible(true);                                  //sets the frame visible to users
        this.setLocationRelativeTo(null);                       //window will appear in middle of screen
    }
}
