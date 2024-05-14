package SnakeGamePackage;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600; // screen width and height set to 600
    static final int UNIT_SIZE = 25; //unit size set to 25
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(int)Math.pow(UNIT_SIZE, 2);
    static final int DELAY = 75;  //represents snake's speed
    int[] x = new int[GAME_UNITS]; //array holding x-coordinates for body parts of the snake
    int[] y = new int[GAME_UNITS]; //array holding y-coordinates for body parts of the snake
    int bodyParts = 6; //initial length of snake set to 6
    int applesEaten, appleX, appleY; //int-type variables declared to hold apple's co-ordinates and amount eaten
    char direction = 'R'; //initial direction of movement for snake set to right
    boolean running = false; //boolean-type variable declared to monitor if game is running
    Timer timer; //defined java.swing.Timer-type variable to repeat action events after the specified delay
    Random random; //Random-type from the java.util package to produce arbitrary values used for apple location

    public GamePanel(){ //no argument constructor defined
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame(); //game begins running
    }

    public void actionPerformed(ActionEvent e){
        //continuously checks for movement, eaten apples, and collisions while the game is running

        if(running){
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    public void startGame(){ //method defined to start the game
        newApple();
        running = true;
        timer = new Timer(DELAY,this); //Timer's delay set to 75
        timer.start();

    }

    public void paintComponent(Graphics g){ //method used to invoke upper-layer graphics
        super.paintComponent(g);
        draw(g);
    }

    public void newApple(){ //method defined to create a new apple
        for(int i=0; i <= bodyParts; i++){ //ensures new apple is not created in a square occupied by the snake's body
            do {
                appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE; //defines x-coordinate of new apple
                appleY = random.nextInt((SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE; //defines y-coordinate of new apple
            } while(x[i] == appleX && y[i] == appleY);
        }
    }
    public void draw(Graphics g) { //method defined to display grid lines, apple, snake, score, & game over screen
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) { //loop used to draw grid lines spaced a unit(25) apart
                g.setColor(new Color(20,20,20)); //line color set to dark grey
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); //vertical lines drawn
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); //horizontal lines drawn
            }
            g.setColor(Color.red); //color of apple set to red
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //apple's location is colored red

            for (int i = 0; i < bodyParts; i++) { //loop used to draw snake
                if (i == 0) {
                    g.setColor(Color.green); //head of snake set to green
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //game starts at (0,0) with width & height of 1 unit
                } else {
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),
                            random.nextInt(255))); //defines snake's body colour
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //squares occupied by snake are coloured
                }
            }

            g.setColor(Color.red); //color of displayed score text set to red
            g.setFont(new Font("Comic Sans MS", Font.PLAIN, 40)); //new font define for score text
            FontMetrics metrics = getFontMetrics(g.getFont()); //FontMetrics-type variable instantiated
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2,
                    g.getFont().getSize()); //text displayed to show score while the game is running

        }
        else{
            gameOver(g); //when game is no longer running end screen will be displayed from gameOver method
        }
    }
    public void move(){

        for(int i = bodyParts; i>0; i--){ //loop making each body square follow the path of the square ahead
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){ //changes the direction of the snake starting with the head unit
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple(){ //method defined to monitor if apple has been consumed by snake
            if ((x[0] == appleX) && y[0] == appleY) { //if head of snake occupies same square as apple...
                bodyParts++;                        //snake length increases by 1
                applesEaten++;                      //counter monitoring total apples eaten increases by 1
                newApple();                         //new apple is created
            }
    }

    public void checkCollisions(){ //method checking if snake crashes into border or itself
        for(int i = bodyParts; i>0; i--){

            if((x[0]==x[i]) && (y[0]==y[i])){ //ends game if a square is occupied by the snake head & it's body
                running = false;
            }

            if(x[0] < 0){ //ends game if snake crashes into left border
                running = false;
            }

            if(x[0] > SCREEN_WIDTH-UNIT_SIZE){ //ends game if snake crashes into right border
                running = false;
            }

            if(y[0] < 0){ //ends game if snake crashes into top border
                running = false;
            }

            if(y[0] > SCREEN_HEIGHT-UNIT_SIZE){ //ends game if snake crashes into bottom border
                running = false;
            }

            if(!running){ //ends times when one of the crash conditions become true
                timer.stop();
            }
        }
    }

    public void gameOver(Graphics g){
        g.setColor(Color.red); //print final score on end screen
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2,
                g.getFont().getSize());

        g.setColor(Color.red); //prints game over on end screen
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    public class MyKeyAdapter extends KeyAdapter{ //regulates constant movement of snake based on user's key input
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
