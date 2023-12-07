import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public static void main (String[] args)
    {
        FileWriterClass fileWriter = new FileWriterClass();
        fileWriter.createFileMethod();
        FrameGenerator frameGenerator = new FrameGenerator();
        frameGenerator.startGame();


    }
}

class StatisticsClass {
    int[] statistics = {0, 0, 0, 0, 0, 0, 0, 0};

    public StatisticsClass() {
        getStatistics();
    }

    public int[] getStatistics() {
        return statistics;
    }

    void increaseScore(int j) {
        statistics[0] += j;
        System.out.println(Arrays.toString(statistics));
    }

    void increaseToiletPaper(int j) {
        statistics[1] += j;
        System.out.println(Arrays.toString(statistics));
    }

    void increaseSanitiser() {
        statistics[2] ++;
        System.out.println(Arrays.toString(statistics));
    }

    void increaseMasks(int j) {
        statistics[3] += j;
        System.out.println(Arrays.toString(statistics));
    }

    void increaseHandSanitiser(int j) {
        statistics[4] += j;
        System.out.println(Arrays.toString(statistics));
    }

    void increaseMonstersConvinced() {
        statistics[5] ++;
        System.out.println(Arrays.toString(statistics));
    }

    void increaseMonstersKilled() {
        statistics[6] ++;
        System.out.println(Arrays.toString(statistics));
    }

    void increaseLevel() {
        statistics[7] ++;
        System.out.println(Arrays.toString(statistics));
    }

}

class DungeonInputHandler implements KeyListener {
    DungeonGenerator engine;

    public DungeonInputHandler(DungeonGenerator eng) {
        engine = eng;
    }

    @Override
    public void keyTyped(KeyEvent e) {}


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: engine.movePlayerLeft();
                engine.doTurn(); break;  //handle left arrow key
            case KeyEvent.VK_RIGHT: engine.movePlayerRight();
                engine.doTurn(); break;//handle right arrow
            case KeyEvent.VK_UP: engine.movePlayerUp();
                engine.doTurn(); break;      //handle up arrow
            case KeyEvent.VK_DOWN: engine.movePlayerDown();
                engine.doTurn(); break;  //handle down arrow
            case KeyEvent.VK_E:
                try {
                    engine.interact();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                break;   //handle the interaction with objects
            case KeyEvent.VK_A: engine.attack(); break; //handle the attack interaction
            case KeyEvent.VK_ESCAPE: engine.escape(); break;    //handle the escape button being pressed
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}

}

class GameGUI extends JFrame {
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final double HEALTH_BAR_HEIGHT = 3;

    Canvas canvas;

    public GameGUI() {
        initGUI();

    }


    public void registerKeyHandler(DungeonInputHandler i) {
        addKeyListener(i);
    }
    private void initGUI()
    {
        add(canvas = new Canvas());
        canvas.setBackground(Color.BLACK);
        setTitle("CE203_1801808_Ass2");
        setSize(1960, 1072);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        canvas.setVisible(true);
    }

    public void updateDisplay(DungeonGenerator.TileType[][] tiles, Entity player, Entity[] monsters)
    {
        canvas.update(tiles, player, monsters);
    }
}
class Canvas extends JPanel{
    private BufferedImage floor;
    private BufferedImage wall;
    private BufferedImage openedChest;
    private BufferedImage closedChest;
    private BufferedImage openedDoor;
    private BufferedImage closedDoor;
    private BufferedImage stair;
    private BufferedImage player;
    private BufferedImage monster;
    private BufferedImage sanitiser;

    DungeonGenerator.TileType[][] currentTiles;
    Entity currentPlayer;       //the current player object to be drawn
    Entity[] currentMonsters;

    public Canvas()
    {
        loadTileImages();
    }

    private void loadTileImages()
    {
        try{
            BufferedImage toiletPaper = ImageIO.read(new File("assets/toiletPaper.png"));
            floor = ImageIO.read(new File("assets/floor.png"));
            assert floor.getHeight() == GameGUI.TILE_HEIGHT &&
                    floor.getWidth() == GameGUI.TILE_WIDTH;
            wall = ImageIO.read(new File("assets/wall.png"));
            assert wall.getHeight() == GameGUI.TILE_HEIGHT &&
                    wall.getWidth() == GameGUI.TILE_WIDTH;
            stair = ImageIO.read(new File("assets/stair.png"));
            assert stair.getHeight() == GameGUI.TILE_HEIGHT &&
                    stair.getWidth() == GameGUI.TILE_WIDTH;
            openedDoor = ImageIO.read(new File("assets/openedDoor.png"));
            assert openedDoor.getHeight() == GameGUI.TILE_HEIGHT &&
                    openedDoor.getWidth() == GameGUI.TILE_WIDTH;
            closedDoor = ImageIO.read(new File("assets/closedDoor.png"));
            assert closedDoor.getHeight() == GameGUI.TILE_HEIGHT &&
                    closedDoor.getWidth() == GameGUI.TILE_WIDTH;
            closedChest = ImageIO.read(new File("assets/closedChest.png"));
            assert closedChest.getHeight() == GameGUI.TILE_HEIGHT &&
                    closedChest.getWidth() == GameGUI.TILE_WIDTH;
            openedChest = ImageIO.read(new File("assets/openedChest.png"));
            assert openedChest.getHeight() == GameGUI.TILE_HEIGHT &&
                    openedChest.getWidth() == GameGUI.TILE_WIDTH;
            sanitiser = ImageIO.read(new File("assets/sanitiser.png"));
            assert sanitiser.getHeight() == GameGUI.TILE_HEIGHT &&
                    sanitiser.getWidth() == GameGUI.TILE_WIDTH;

            player = ImageIO.read(new File("assets/player.png"));
            assert player.getHeight() == GameGUI.TILE_HEIGHT &&
                    player.getWidth() == GameGUI.TILE_WIDTH;
            monster = ImageIO.read(new File("assets/monster.png"));
            assert monster.getHeight() == GameGUI.TILE_HEIGHT &&
                    monster.getWidth() == GameGUI.TILE_WIDTH;

        }
        catch (IOException e){
            System.out.println("Exception loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void update(DungeonGenerator.TileType[][] t, Entity player, Entity[] mon)
    {
        currentTiles = t;
        currentPlayer = player;
        currentMonsters = mon;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDungeon(g);
    }


    private void drawDungeon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (currentTiles != null) {
            for (int i = 0; i < currentTiles.length; i++) {
                for (int j = 0; j < currentTiles[i].length; j++) {
                    if (currentTiles[i][j] != null) {   //checks a tile exists
                        switch (currentTiles[i][j]) {
                            case FLOOR:
                                g2.drawImage(floor, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case WALL:
                                g2.drawImage(wall, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case STAIR:
                                g2.drawImage(stair, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case OCHEST:
                                g2.drawImage(openedChest, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case CCHEST:
                                g2.drawImage(closedChest, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case ODOOR:
                                g2.drawImage(openedDoor, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case CDOOR:
                                g2.drawImage(closedDoor, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case SANITISER:
                                g2.drawImage(sanitiser, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                        }
                    }
                }
            }
        }
        if (currentMonsters != null)
            for(Entity mon : currentMonsters)
                if (mon != null) {
                    g2.drawImage(monster, mon.getX() * GameGUI.TILE_WIDTH, mon.getY() * GameGUI.TILE_HEIGHT, null);
                    drawHealthBar(g2, mon);
                }
        if (currentPlayer != null) {
            g2.drawImage(player, currentPlayer.getX() * GameGUI.TILE_WIDTH, currentPlayer.getY() * GameGUI.TILE_HEIGHT, null);
            drawHealthBar(g2, currentPlayer);
        }
    }
    private void drawHealthBar(Graphics2D g2, Entity e) {
        double remainingHealth = (double)e.getHealth() / (double)e.getMaxHealth();
        g2.setColor(Color.RED);
        g2.fill(new Rectangle2D.Double(e.getX() * GameGUI.TILE_WIDTH, e.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH, GameGUI.HEALTH_BAR_HEIGHT));
        g2.setColor(Color.GREEN);
        g2.fill(new Rectangle2D.Double(e.getX() * GameGUI.TILE_WIDTH, e.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH * remainingHealth, GameGUI.HEALTH_BAR_HEIGHT));
    }
}

class Entity {


    public enum EntityType { PLAYER, MONSTER }

    private final int maxHealth;


    private int health;


    private int xPos;


    private int yPos;

    private EntityType type;

    public Entity(int maxHealth, int x, int y, EntityType type) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        xPos = x;
        yPos = y;
        this.type = type;
    }


    public int getX() {
        return xPos;
    }


    public int getY() {
        return yPos;
    }
    public void setPosition (int x, int y) {
        xPos = x;
        yPos = y;
    }

    public void changeHealth(int change) {
        health += change;
        if (health > maxHealth)
            health = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}


class DungeonGenerator extends JFrame {
    StatisticsClass statisticsClass = new StatisticsClass();

    private GameGUI gui;
    public DungeonGenerator(GameGUI gui) {

        this.gui = gui;
    }
    public void generateDungeon() {
        DungeonGenerator d = new DungeonGenerator(60,30);
        d.setRoomsize(3);
        d.setHallsize(3);
        d.generate();
        tileTypes = d.tileInterpretation();
        player = spawnPlayer();
        monsters = spawnMonsters();
        gui.updateDisplay(tileTypes, player, monsters);
    }

    private  Dimension size;

    private long seed;
    private Random rand;

    private int hallsize;
    private int roomsize;

    private String[][] tiles;

    public void interact() throws FileNotFoundException {
        int Ypos = player.getY();
        int Xpos = player.getX();

        if(tileTypes[Xpos][Ypos-1] == TileType.CCHEST)
        {

            statisticsClass.increaseScore(25);
            Random random = new Random();
            statisticsClass.increaseToiletPaper(random.nextInt(5));
            statisticsClass.increaseHandSanitiser(random.nextInt(2));
            statisticsClass.increaseMasks(random.nextInt(2));
            tileTypes[Xpos][Ypos-1] = TileType.OCHEST;
            doTurn(); /** Change doTurn Method*/
        }
        else if( tileTypes[Xpos][Ypos+1] == TileType.CCHEST)
        {
            statisticsClass.increaseScore(25);
            Random random = new Random();
            statisticsClass.increaseToiletPaper(random.nextInt(5));
            statisticsClass.increaseHandSanitiser(random.nextInt(2));
            statisticsClass.increaseMasks(random.nextInt(2));
            tileTypes[Xpos][Ypos+1] = TileType.OCHEST;
            doTurn();
        }
        else if( tileTypes[Xpos-1][Ypos] == TileType.CCHEST)
        {
            statisticsClass.increaseScore(25);
            Random random = new Random();
            statisticsClass.increaseToiletPaper(random.nextInt(5));
            statisticsClass.increaseHandSanitiser(random.nextInt(2));
            statisticsClass.increaseMasks(random.nextInt(2));
            tileTypes[Xpos-1][Ypos] =TileType.OCHEST;
            doTurn();
        }
        else if( tileTypes[Xpos+1][Ypos] == TileType.CCHEST)
        {
            statisticsClass.increaseScore(25);
            Random random = new Random();
            statisticsClass.increaseToiletPaper(random.nextInt(5));
            statisticsClass.increaseHandSanitiser(random.nextInt(2));
            statisticsClass.increaseMasks(random.nextInt(2));
            tileTypes[Xpos+1][Ypos] = TileType.OCHEST;
            doTurn();
        }
        else if(tileTypes[Xpos][Ypos-1] == TileType.ODOOR)
        {
            System.out.println(tileTypes[Xpos][Ypos-1]);
            tileTypes[Xpos][Ypos-1] = TileType.CDOOR; /**INSERT OPENED DOOR*/
            doTurn(); /** Change doTurn Method*/
        }
        else if( tileTypes[Xpos][Ypos+1] == TileType.ODOOR)
        {
            System.out.println(tileTypes[Xpos][Ypos+1]);
            tileTypes[Xpos][Ypos+1] = TileType.CDOOR;
            doTurn();
        }
        else if( tileTypes[Xpos-1][Ypos] == TileType.ODOOR)
        {
            System.out.println(tileTypes[Xpos-1][Ypos]);
            tileTypes[Xpos-1][Ypos] =TileType.CDOOR;
            doTurn();
        }
        else if( tileTypes[Xpos+1][Ypos] == TileType.ODOOR)
        {
            System.out.println(tileTypes[Xpos+1][Ypos]);
            tileTypes[Xpos+1][Ypos] = TileType.CDOOR;
            doTurn();
        }
        else if(tileTypes[Xpos][Ypos-1] == TileType.CDOOR)
        {
            System.out.println(tileTypes[Xpos][Ypos-1]);
            tileTypes[Xpos][Ypos-1] = TileType.ODOOR; /**INSERT Closed DOOR*/
            doTurn(); /** Change doTurn Method*/
        }
        else if( tileTypes[Xpos][Ypos+1] == TileType.CDOOR)
        {
            System.out.println(tileTypes[Xpos][Ypos+1]);
            tileTypes[Xpos][Ypos+1] = TileType.ODOOR;
            doTurn();
        }
        else if( tileTypes[Xpos-1][Ypos] == TileType.CDOOR)
        {
            System.out.println(tileTypes[Xpos-1][Ypos]);
            tileTypes[Xpos-1][Ypos] =TileType.ODOOR;
            doTurn();
        }
        else if( tileTypes[Xpos+1][Ypos] == TileType.CDOOR)
        {
            System.out.println(tileTypes[Xpos+1][Ypos]);
            tileTypes[Xpos+1][Ypos] = TileType.ODOOR;
            doTurn();
        }
        else if(tileTypes[Xpos][Ypos-1] == TileType.SANITISER ||
                tileTypes[Xpos][Ypos+1] == TileType.SANITISER ||
                tileTypes[Xpos-1][Ypos+1] == TileType.SANITISER ||
                tileTypes[Xpos+1][Ypos+1] == TileType.SANITISER ||
                tileTypes[Xpos-1][Ypos] == TileType.SANITISER ||
                tileTypes[Xpos+1][Ypos] == TileType.SANITISER ||
                tileTypes[Xpos-1][Ypos-1] == TileType.SANITISER ||
                tileTypes[Xpos+1][Ypos-1] == TileType.SANITISER)
        {
            statisticsClass.increaseScore(30);
            statisticsClass.increaseSanitiser();
            healPlayer(25);
            doTurn();
        }
        else if( tileTypes[Xpos][Ypos] == TileType.STAIR)
        {
            int monstersRemaining = 0;
            for (Entity monster : monsters) {
                if (monster!=null) {
                    monstersRemaining++;
                    break;
                }

            }
            if(monstersRemaining <= 0)
            {
                descendLevel();
                doTurn();
            }
            else
            {
                new FrameGenerator("stair");

                System.out.println(monstersRemaining + " clients to be sent home");
            }



        }


    }

    private void healPlayer(int i) {
        player.changeHealth(i);
    }

    public void attack() {
        int Ypos = player.getY();
        int Xpos = player.getX();
        for (Entity monster : monsters) {
            if (monster!=null && ((
                    (monster.getX() == Xpos && monster.getY() == Ypos)
                            || monster.getX() == Xpos + 1 && monster.getY() == Ypos)
                    || (monster.getX() == Xpos - 1 && monster.getY() == Ypos)
                    || (monster.getX() == Xpos && monster.getY() == Ypos + 1)
                    || (monster.getX() == Xpos && monster.getY() == Ypos - 1)
                    || (monster.getX() == Xpos-1 && monster.getY() == Ypos - 1)
                    || (monster.getX() == Xpos+1 && monster.getY() == Ypos - 1)
                    || (monster.getX() == Xpos-1 && monster.getY() == Ypos + 1)
                    || (monster.getX() == Xpos+1 && monster.getY() == Ypos + 1)
            )) {

                JFrame attackFrame = new JFrame("Show Down");
                JPanel panel = new JPanel();
                attackFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                attackFrame.setVisible(true);
                attackFrame.setLocationRelativeTo(null);
                attackFrame.setSize(800, 800);
                JButton convince = new JButton("Convince Logically");
                JButton violence = new JButton("Resolve to Violence");
                JButton mask = new JButton("Put on Mask");
                JButton sanitiser = new JButton("Use Hand Sanitiser");
                JButton leave = new JButton("Back off");
                panel.add(convince);
                panel.add(violence);
                panel.add(mask);
                panel.add(sanitiser);
                panel.add(leave);
                attackFrame.add(panel);
                leave.addActionListener(e ->
                        attackFrame.dispose());
                mask.addActionListener(e -> {


                    healPlayer(150);
                });


                sanitiser.addActionListener(e -> {
                    Random random = new Random();
                    healPlayer(random.nextInt(25) + 15);

                });


                convince.addActionListener(e -> {
                    System.out.println(monster.getHealth());
                    convinceMonster(monster);
                    if(monster.getHealth() <= 0)
                    {attackFrame.dispose();
                        doTurn();
                        statisticsClass.increaseScore(15);
                        statisticsClass.increaseMonstersConvinced();}
                    else
                        doTurn();


                });
                violence.addActionListener(e1 -> {
                    System.out.println(monster.getHealth() + " " + player.getHealth());
                    hitMonster(monster);
                    hitPlayer();
                    if(monster.getHealth() <= 0)
                    {attackFrame.dispose();
                        doTurn();
                        statisticsClass.increaseScore(5);
                        statisticsClass.increaseMonstersKilled();}
                    else if(player.getHealth() <= 0)
                    {
                        attackFrame.dispose();
                        gameOver();
                    }
                    else
                        doTurn();
                });
                break;
            }
        }
    }
    public void saveGame(String string) throws FileNotFoundException {

        FileWriterClass.writeFileMethod(statisticsClass.getStatistics());
    }


    public void gameOver()
    {
        AtomicReference<String> string = new AtomicReference<>("");
        JFrame frame = new JFrame("Your Done For The Moment");
        JPanel panel = new JPanel();
        frame.setEnabled(true);
        panel.setVisible(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 600);
        JButton okButton = new JButton("Ok");
        JTextField nameField = new JTextField();
        panel.add(nameField);
        panel.add(okButton);
        frame.add(panel);
        okButton.addActionListener(e1 -> {
            string.set(nameField.getText());
            try {
                saveGame(string.get());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.exit(0);

        });
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


    }
    public void escape()
    {
        AtomicReference<String> string = new AtomicReference<>("");
        JFrame frame = new JFrame("Menu");
        JPanel panel = new JPanel();
        frame.setEnabled(true);
        panel.setVisible(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 600);
        JButton htpButton = new JButton("How to Play");
        JButton saveAndExit = new JButton("Save and Exit");
        JButton currentScore = new JButton("Current Score");
        JButton backToGame = new JButton("Back to Game");
        panel.add(htpButton);
        panel.add(backToGame);
        panel.add(saveAndExit);
        panel.add(currentScore);
        frame.add(panel);
        backToGame.addActionListener(e ->
                frame.dispose());
        currentScore.addActionListener(e -> {
            frame.dispose();
            JFrame scoreFrame = new JFrame("Current Score");
            JPanel pane = new JPanel();
            JTextArea textField = new JTextArea();
            for(int i=0; i<8; i++)
            {
                switch (i)
                {
                    case 0: textField.append("Score: " + statisticsClass.getStatistics()[i] + "\n"); break;
                    case 1: textField.append("Toilet Paper Collected:" + statisticsClass.getStatistics()[i] + "\n"); break;
                    case 2: textField.append("Sanitiser Visited:" + statisticsClass.getStatistics()[i] + "\n"); break;
                    case 3: textField.append("Masks Collected:" + statisticsClass.getStatistics()[i] + "\n"); break;
                    case 4: textField.append("Hand Sanitiser Collected:" + statisticsClass.getStatistics()[i] + "\n"); break;
                    case 5: textField.append("Clients Convinced:" + statisticsClass.getStatistics()[i] + "\n"); break;
                    case 6: textField.append("Clients Convinced with violence:" + statisticsClass.getStatistics()[i] + "\n"); break;
                    case 7: textField.append("Floors Cleared:" + statisticsClass.getStatistics()[i] + "\n"); break;
                }

            }
            JButton back = new JButton("Back");
            scoreFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            pane.add(textField);
            pane.add(back);
            scoreFrame.add(pane);
            back.addActionListener(e1 ->
                    scoreFrame.dispose());
            scoreFrame.setLocationRelativeTo(null);
            scoreFrame.setSize(200, 250);
            scoreFrame.setVisible(true);
        });
        htpButton.addActionListener(e1 -> {
            frame.dispose();
            try {
                new FrameGenerator("howToPlay");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });
        saveAndExit.addActionListener(e1 -> {
            try {
                saveGame(string.get());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.exit(0);

        });
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


    }


    public enum TileType {
        WALL, FLOOR, OCHEST, STAIR, ODOOR, CCHEST, CDOOR, SANITISER
    }
    private TileType[][] tileTypes;
    private ArrayList<Point> spawns;
    public Entity player;
    public Entity[] monsters;

    public DungeonGenerator(int width,int height,long seed)
    {
        this.roomsize = 8;
        this.hallsize = 8;
        tiles = new String[width][height];
        size = new Dimension(width,height);
        this.seed = seed;
        rand = new Random( seed );
    }

    public DungeonGenerator(int width,int height)
    {
        this(width,height,new Random().nextLong());

    }


    public void setHallsize(int newhallsize)
    {
        this.hallsize = newhallsize;
    }

    public void setRoomsize(int newroomsize)
    {
        this.roomsize = newroomsize;
    }

    public void generate()
    {
        // Fill the dungeon with wall tiles
        rectFill(0,0,size.width,size.height,"#");
        // Create a starting room
        startingRoom();

        // Generate halls/rooms until no prospects remain
        while( hasProspects() )
        {
            hallsGenerate();
            roomsGenerate();
        }

        // Finalize the dungeon
        cleanUp();
    }

    public void startingRoom()
    {
        int center = size.width / 2;
        rectFill( center-2,1,3,3,"R" );

        placeIfWall(center-1,4,"P");
    }

    public void draw()
    {

        for( int y = 0; y < size.height; y++ )
        {
            for( int x = 0; x < size.width; x++ )
            {
                System.out.print( tiles[x][y] );

            }
            System.out.println();
        }

        System.out.println( seed );
    }

    public TileType[][] tileInterpretation(){
        TileType[][] myArray = new TileType[60][30];
        for( int y = 0; y < 30; y++ )
        {

            for( int x = 0; x < 60; x++ )
            {
                try{
                    String tileString = tiles[x][y];
                    if("#".equals(tileString))
                    {
                        myArray[x][y] = TileType.WALL;
                    }
                    else if(" ".equals(tileString))
                    {
                        myArray[x][y] = TileType.FLOOR;
                    }
                    else if("D".equals(tileString))
                    {
                        myArray[x][y] = TileType.CDOOR;
                    }
                    else if("C".equals(tileString))
                    {
                        myArray[x][y] = TileType.CCHEST;
                    }
                    else if("S".equals(tileString))
                    {
                        myArray[x][y] = TileType.STAIR;
                    }
                    else if("T".equals(tileString))
                    {
                        myArray[x][y] = TileType.SANITISER;
                    }
                }
                catch (NullPointerException e)
                {
                    System.out.println("Something went wrong");
                }



            }
        }
        return myArray;

    }

    protected boolean rectCheck(int x,int y,int w,int h)
    {
        for( int ya = y; ya < y+h; ya++ )
        {
            for( int xa = x; xa < x+w; xa++ )
            {
                String tile;
                try
                {
                    tile = tiles[xa][ya];
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    return false;
                }

                if(!"P".equals( tile ) && !"#".equals( tile ))
                {
                    return false;
                }
            }
        }

        return true;
    }

    protected void rectFill(int x,int y,int w,int h,String tile)
    {
        for( int ya = y; ya < y+h; ya++ )
        {
            for( int xa = x; xa < x+w; xa++ )
            {
                tiles[xa][ya] = tile;
            }
        }
    }

    protected void placeTile(int x,int y,String tile)
    {
        tiles[x][y] = tile;
    }

    private void placeIfWall(int x,int y,String tile)
    {
        if( "#".equals(tiles[x][y]) )
        {
            tiles[x][y] = tile;
        }
    }

    private void placeIfEmpty(int x,int y,String tile)
    {
        if( isEmpty(tiles[x][y]) )
        {
            tiles[x][y] = tile;
        }
    }


    private boolean isEmpty(String string)
    {
        if( " ".equals(string) )
        {
            return true;
        }

        if( "H".equals(string) )
        {
            return true;
        }

        return "R".equals(string);
    }

    private boolean hasProspects()
    {
        for( int y = 0; y < size.height; y++ )
        {
            for( int x = 0; x < size.width; x++ )
            {
                if( "P".equals(tiles[x][y]) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    private List<Point> getProspects()
    {
        ArrayList<Point> prospects = new ArrayList<>();

        for( int y = 0; y < size.height; y++ )
        {
            for( int x = 0; x < size.width; x++ )
            {
                if( "P".equals(tiles[x][y]) )
                {

                    prospects.add(new Point(x,y));
                }
            }
        }

        return prospects;
    }


    private void hallsGenerate()
    {
        getProspects().stream().forEach((p) -> {
            try
            {
                hallMake(p.x,p.y);
            }
            catch( ArrayIndexOutOfBoundsException e )
            {
                tiles[p.x][p.y] = "#";
            }
        });
    }

    private void hallMake(int x, int y)
    {
        if( isEmpty(tiles[x-1][y]) && isEmpty(tiles[x+1][y]) )
        {
            tiles[x][y] = "H";
            return;
        }

        if( isEmpty(tiles[x][y-1]) && isEmpty(tiles[x][y+1]) )
        {
            tiles[x][y] = "H";
            return;
        }

        if( isEmpty(tiles[x-1][y]) )
        {
            hallMakeEastbound(x,y,rand.nextInt(this.hallsize));
            return;
        }

        if( isEmpty(tiles[x+1][y]) )
        {
            hallMakeWestbound(x,y,rand.nextInt(this.hallsize));
            return;
        }

        if( isEmpty(tiles[x][y-1]) )
        {
            hallMakeSouthbound(x,y,rand.nextInt(this.hallsize));
            return;
        }

        if( isEmpty(tiles[x][y+1]) )
        {
            hallMakeNorthbound(x,y,rand.nextInt(this.hallsize));
            return;
        }

        tiles[x][y] = "R";
    }


    private void hallMakeEastbound(int x, int y,int length)
    {
        if( rectCheck(x,y-1,length,3) )
        {
            rectFill(x,y,length,1,"H");
            placeIfEmpty(x+(length-1),y,"P");

            // TODO: Branches
        }
        else
        {
            if( length > 0 )
            {
                hallMakeEastbound(x,y,length-1);
            }
            else
            {
                tiles[x][y] = "#";
            }
        }
    }


    private void hallMakeWestbound(int x, int y,int length)
    {
        if( rectCheck(x-length,y-1,length,3) )
        {
            rectFill(x-(length-1),y,length,1,"H");
            placeIfEmpty(x-(length-1),y,"P");

            if( length >= 3 )
            {
                int sbranch = rand.nextInt( this.hallsize );
                if( sbranch > 1 && sbranch < length-1 )
                {
                    placeIfEmpty(x-sbranch,y+1,"P");
                    hallMakeSouthbound(x-sbranch,y+1,rand.nextInt(hallsize));
                }

                int nbranch = rand.nextInt( this.hallsize );
                if( nbranch > 1 && nbranch < length-1 )
                {
                    placeIfEmpty(x-nbranch,y-1,"P");
                    hallMakeNorthbound(x-nbranch,y-1,rand.nextInt(hallsize));
                }
            }

        }
        else
        {
            if( length > 0 )
            {
                hallMakeWestbound(x,y,length-1);
            }
            else
            {
                tiles[x][y] = "#";
            }
        }
    }

    private void hallMakeSouthbound(int x, int y,int length)
    {
        if( rectCheck(x-1,y,3,length+1) )
        {
            rectFill(x,y,1,length,"H");
            tiles[x][y+(length-1)] = "P";

        }
        else
        {

            tiles[x][y] = "#";
        }
    }


    private void hallMakeNorthbound(int x, int y,int length)
    {
        if( rectCheck(x-1,y-length,3,length+1) )
        {
            rectFill(x,y-(length-1),1,length,"H");
            tiles[x][y-(length-1)] = "P";

        }
        else
        {

            tiles[x][y] = "#";
        }
    }


    private void roomsGenerate()
    {
        getProspects().stream().forEach((p) -> {
            try
            {
                roomMake(p.x,p.y);
            }
            catch( ArrayIndexOutOfBoundsException e )
            {

            }
        });
    }

    private void roomMake(int x, int y)
    {
        if( isEmpty(tiles[x-1][y]) && isEmpty(tiles[x+1][y]) )
        {
            tiles[x][y] = "R";
            return;
        }

        if( isEmpty(tiles[x][y-1]) && isEmpty(tiles[x][y+1]) )
        {
            tiles[x][y] = "R";
            return;
        }

        int w = 3 + rand.nextInt( this.roomsize );
        int h = 3 + rand.nextInt( this.roomsize );

        if( isEmpty(tiles[x][y-1]) )
        {
            roomMakeSouthbound(x,y,w,h);
            return;
        }

        if( isEmpty(tiles[x-1][y]) )
        {
            roomMakeEastbound(x,y,w,h);
            return;
        }

        if( isEmpty(tiles[x+1][y]) )
        {
            roomMakeWestbound(x,y,w,h);
            return;
        }

        if( isEmpty(tiles[x][y+1]) )
        {
            roomMakeNorthbound(x,y,w,h);
            return;
        }

        tiles[x][y] = "#";
    }


    private void roomMakeSouthbound(int x, int y,int w,int h)
    {
        int wc = w/2;
        int hc = h/2;

        int xorig = x - wc;
        int yorig = y + 1;

        if( rectCheck(xorig-1,y,w+1,h+1) )
        {
            tiles[x][y] = "H";
            rectFill(xorig,yorig,w,h,"R");


            placeIfWall(xorig+wc,yorig+h,"P");
            placeIfWall(xorig-1,yorig+hc,"P");
            placeIfWall(xorig+w,yorig+hc,"P");
        }
        else
        {

            tiles[x][y] = "#";
        }

    }

    private void roomMakeEastbound(int x, int y,int w,int h)
    {
        int wc = w/2;
        int hc = h/2;

        int xorig = x+1;
        int yorig = y-hc;

        if( rectCheck(xorig,yorig-2,w+1,h+1) )
        {
            tiles[x][y] = "H";
            rectFill(xorig,yorig,w,h,"R");


            placeIfWall(xorig+wc,yorig-1,"P");
            placeIfWall(xorig+wc,y+hc,"P");
            placeIfWall(xorig+w,y,"P");
        }
        else
        {
            if( w > 3 && h > 3 )
            {
                roomMakeEastbound(x,y,w-1,h-1);
            }
            else
            {
                tiles[x][y] = "#";
            }
        }
    }


    private void roomMakeWestbound(int x, int y,int w,int h)
    {

        int hc = h/2;
        int wc = w/2;

        int xorig = x - w;
        int yorig = y - hc;

        if( rectCheck(xorig-1,yorig-1,w+1,h+1) )
        {
            tiles[x][y] = "H";
            rectFill(xorig,yorig,w,h,"R");


            placeIfWall(xorig+wc,yorig-1,"P");
            placeIfWall(xorig+wc,y+hc,"P");
            placeIfWall(xorig-1,y,"P");
        }
        else
        {

            tiles[x][y] = "#";
        }
    }


    private void roomMakeNorthbound(int x, int y,int w,int h)
    {

        int wc = w/2;
        int hc = h/2;

        int xorig = x - wc;
        int yorig = y - h;

        if( rectCheck(xorig-1,yorig-1,w+1,h+1) )
        {
            tiles[x][y] = "H";
            rectFill(xorig,yorig,w,h,"R");


            placeIfWall(x,yorig-1,"P");
            placeIfWall(xorig-1,y-hc,"P");
            placeIfWall(xorig+w,y-hc,"P");
        }
        else
        {
            tiles[x][y] = "#";
        }
    }

    private void cleanUp()
    {
        getProspects().stream().forEach((p) -> {
            tiles[p.x][p.y] = "#";
        });

        fixOneBlockDeadEnds();
        fixOneBlockHalls();

        makeBorders();

        makeDoors();
        makeSanitiser();
        makeChests();

        removeMeta();

        makeStairs();

    }

    private void fixOneBlockDeadEnds()
    {
        for( int x = 0; x < size.width; x++ )
            for( int y = 0; y < size.height; y++ )
            {
                String tile = tiles[x][y];
                if( "H".equals(tile) )
                {
                    // Westbound
                    if( "R".equals(tiles[x+1][y]) && "#".equals(tiles[x-1][y]) )
                    {
                        tiles[x][y] = "#";
                    }

                    // Eastbound
                    if( "R".equals(tiles[x-1][y]) && "#".equals(tiles[x+1][y]) )
                    {
                        tiles[x][y] = "#";
                    }

                    // Southbound
                    if( "R".equals(tiles[x][y-1]) && "#".equals(tiles[x][y+1]) )
                    {
                        tiles[x][y] = "#";
                    }

                    // Northbound
                    if( "R".equals(tiles[x][y+1]) && "#".equals(tiles[x][y-1]) )
                    {
                        tiles[x][y] = "#";
                    }
                }
            }
    }

    private void fixOneBlockHalls()
    {
        for( int x = 0; x < size.width; x++ )
            for( int y = 0; y < size.height; y++ )
            {
                String tile = tiles[x][y];
                if( "H".equals(tile) )
                {
                    // Westbound
                    if( "R".equals(tiles[x+1][y]) && "R".equals(tiles[x-1][y]) )
                    {
                        tiles[x][y] = "D";
                    }

                    // Eastbound
                    if( "R".equals(tiles[x-1][y]) && "R".equals(tiles[x+1][y]) )
                    {
                        tiles[x][y] = "D";
                    }

                    // Southbound
                    if( "R".equals(tiles[x][y-1]) && "R".equals(tiles[x][y+1]) )
                    {
                        tiles[x][y] = "D";
                    }

                    // Northbound
                    if( "R".equals(tiles[x][y+1]) && "R".equals(tiles[x][y-1]) )
                    {
                        tiles[x][y] = "D";
                    }
                }
            }
    }

    private void makeDoors()
    {
        for( int x = 0; x < size.width; x++ )
            for( int y = 0; y < size.height; y++ )
            {
                String tile = tiles[x][y];
                if( "H".equals(tile) )
                {
                    // Southbound
                    if( "R".equals(tiles[x][y-1]) && "H".equals(tiles[x][y+1]) )
                    {
                        tiles[x][y] = "D";
                    }

                    // Northbound
                    if( "R".equals(tiles[x][y+1]) && "H".equals(tiles[x][y-1]) )
                    {
                        tiles[x][y] = "D";
                    }

                    // Westbound
                    if( "R".equals(tiles[x+1][y]) && "H".equals(tiles[x-1][y]) )
                    {
                        tiles[x][y] = "D";
                    }

                    // Eastbound
                    if( "R".equals(tiles[x-1][y]) && "H".equals(tiles[x+1][y]) )
                    {
                        tiles[x][y] = "D";
                    }
                }
            }
    }
    private void makeStairs()
    {
        boolean targetAchieved = false;
        int x=1;
        int y=1;
        while(!targetAchieved)
        {
            if(" ".equals(tiles[x][y]) && " ".equals(tiles[x-1][y]) && " ".equals(tiles[x+1][y])
                    && " ".equals(tiles[x][y-1]) && " ".equals(tiles[x][y+1]))
            {
                targetAchieved = true;
                tiles[x][y] = "S";
            }
            else
            {
                x = rand.nextInt(58) + 1;
                y = rand.nextInt(28) + 1;
            }

        }
    }
    private void makeChests()
    {
        int targetAchieved = rand.nextInt(5) + 5;
        int x=0;
        int y=0;
        while(targetAchieved != 0)
        {
            if("R".equals(tiles[x][y])&&("#".equals(tiles[x-1][y]) || "#".equals(tiles[x+1][y])
                    || "#".equals(tiles[x][y-1]) || "#".equals(tiles[x][y+1])))
            {
                targetAchieved--;
                tiles[x][y] = "C";
                x = rand.nextInt(60);
                y = rand.nextInt(30);
            }
            else
            {
                x = rand.nextInt(60);
                y = rand.nextInt(30);
            }
        }
    }
    private void makeSanitiser()
    {
        int targetAchieved = rand.nextInt(5) + 5;
        int x=0;
        int y=0;
        while(targetAchieved != 0)
        {
            if("R".equals(tiles[x][y])&&("#".equals(tiles[x-1][y]) || "#".equals(tiles[x+1][y])
                    || "#".equals(tiles[x][y-1]) || "#".equals(tiles[x][y+1])))
            {
                targetAchieved--;
                tiles[x][y] = "T";
                x = rand.nextInt(60);
                y = rand.nextInt(30);
            }
            else
            {
                x = rand.nextInt(60);
                y = rand.nextInt(30);
            }
        }
    }
    public void makeBorders()
    {
        for(int x = 0; x<60; x++)
        {
            tiles[x][0] = "#";
            tiles[x][29] = "#";
        }
        for(int y = 0; y<30; y++)
        {
            tiles[0][y] = "#";
            tiles[59][y] = "#";
        }
    }

    private void removeMeta()
    {
        for( int x = 0; x < size.width; x++ )
            for( int y = 0; y < size.height; y++ )
            {
                String tile = tiles[x][y];
                if( "H".equals(tile) || "R".equals(tile) )
                {
                    tiles[x][y] = " ";
                }
            }
    }

    private ArrayList<Point> getSpawns() {
        ArrayList<Point> s = new ArrayList<>();
        TileType[][] spawnArray = tileTypes;
        for (int i = 0; i < 59; i++) {
            for (int j = 0; j < 29; j++) {

                //find tiles in the level array that are suitable spawn points
                try{
                    if (spawnArray[i][j] == TileType.FLOOR) {
                        //Add these points to the ArrayList s
                        s.add(new Point(i, j));


                    }}
                catch(NullPointerException e)
                {
                    System.out.println("Null Point Exeption");
                }

            }
        }
        System.out.println(s);
        return s;
    }
    private Entity spawnPlayer() {
        ArrayList<Point> spawns = getSpawns();
        System.out.println(spawns);

        int sizeOfSpawns = spawns.size();
        System.out.println(sizeOfSpawns);
        Random random = new Random();
        int randomPoint = random.nextInt(sizeOfSpawns);

        System.out.println(randomPoint);
        Point myPlayerPoint = spawns.get(randomPoint);
        Entity player = new Entity(1500, myPlayerPoint.x, myPlayerPoint.y, Entity.EntityType.PLAYER);
        player.setPosition(myPlayerPoint.x, myPlayerPoint.y);
        spawns.remove(myPlayerPoint);
        System.out.println("player: " + player);
        System.out.println("x Value: " + player.getX());
        System.out.println("y value: " + player.getY());
        return player;    //Should be changed to return an Entity (the player) instead of null
    }
    private void placePlayer() {
        spawns = getSpawns();
        Random random = new Random();
        Point myPlayerPoint = spawns.get(random.nextInt(spawns.size()));
        player.setPosition(myPlayerPoint.x, myPlayerPoint.y);
        spawns.remove(myPlayerPoint);
    }
    private Entity[] spawnMonsters() {

        ArrayList<Point> spawns = getSpawns();
        Random random = new Random();
        Entity[] myMonsters = new Entity[random.nextInt(15) + 5];
        int numberOfMonsters = myMonsters.length;

        while(numberOfMonsters != 0)
        {
            Point myMonsterPoint = spawns.get(random.nextInt(spawns.size()));
            Entity monster0 = new Entity(100, myMonsterPoint.x, myMonsterPoint.y, Entity.EntityType.MONSTER);
            numberOfMonsters --;
            myMonsters[numberOfMonsters] = monster0;
            spawns.remove(myMonsterPoint);

        }





        return myMonsters;
    }

    public void movePlayerLeft() {
        if (player.getX() - 1 >= -1 && tileTypes [player.getX() - 1][player.getY()] == TileType.FLOOR ||
                tileTypes [player.getX() - 1][player.getY()] == TileType.ODOOR||
                tileTypes [player.getX() - 1][player.getY()] == TileType.STAIR)
        {
            boolean hit = false;
            for(int i = 0; i< monsters.length ;i++)
            {
                if(monsters[i]!=null && monsters[i].getX()==player.getX() -1 &&monsters[i].getY()==player.getY())
                {
                    hit = true;
                }
            }

            if(!hit)
                player.setPosition(player.getX() - 1, player.getY());


        }

    }
    public void movePlayerRight() {
        if (player.getX() + 1 >= +1 && tileTypes [player.getX() + 1][player.getY()] == TileType.FLOOR ||
                tileTypes [player.getX() + 1][player.getY()] == TileType.ODOOR ||
                tileTypes [player.getX() + 1][player.getY()] == TileType.STAIR) {

            boolean hit = false;
            for(int i = 0; i<monsters.length ;i++)
            {
                if(monsters[i]!=null && monsters[i].getX()==player.getX() +1 &&monsters[i].getY()==player.getY())
                {
                    hit = true;
                }
            }
            if(!hit)
                player.setPosition(player.getX() + 1, player.getY());

        }

    }
    public void movePlayerUp() {
        if (player.getY() - 1 >= -1 && tileTypes [player.getX()][player.getY() - 1] == TileType.FLOOR ||
                tileTypes [player.getX()][player.getY() - 1] == TileType.ODOOR ||
                tileTypes [player.getX()][player.getY() - 1] == TileType.STAIR) {

            boolean hit = false;
            for(int i = 0; i<monsters.length ;i++)
            {
                if(monsters[i]!=null && monsters[i].getX()==player.getX()&&monsters[i].getY()==player.getY()-1)
                {   hit = true;
                }
            }
            if(!hit){
                player.setPosition(player.getX(), player.getY() - 1);
            }
        }

    }

    public void movePlayerDown() {
        if (player.getY() + 1 >= +1 && tileTypes [player.getX()][player.getY() + 1] == TileType.FLOOR ||
                tileTypes[player.getX()][player.getY() + 1] == TileType.ODOOR||
                tileTypes[player.getX()][player.getY() + 1] == TileType.STAIR) {

            boolean hit = false;
            for(int i = 0; i<monsters.length ;i++)
            {
                if(monsters[i]!=null && monsters[i].getX()==player.getX()&&monsters[i].getY()==player.getY()+1)
                {
                    hit = true;
                }
            }
            if(!hit){
                player.setPosition(player.getX(), player.getY() + 1);

            }


        }
    }
    private void descendLevel() {
        statisticsClass.increaseLevel();
        statisticsClass.increaseScore(100);
        DungeonGenerator d = new DungeonGenerator(60,30);
        d.setRoomsize(3);
        d.setHallsize(3);
        d.generate();
        tileTypes = d.tileInterpretation();
        spawns = getSpawns();
        monsters = spawnMonsters();
        this.placePlayer();
        gui.updateDisplay(tileTypes , player, monsters);

    }
    public void doTurn() {
        cleanDeadMonsters();
        moveMonsters();
        if (player != null) {       //checks a player object exists
            if (player.getHealth() < 1) {
                System.exit(0);     //exits the game when player is dead
            }
        }
        gui.updateDisplay(tileTypes, player, monsters);     //updates GUI
    }
    public void hitMonster(Entity monster)
    {
        monster.changeHealth(-25);
    }
    public void convinceMonster(Entity monster)
    {
        monster.changeHealth(-50);
    }
    public void hitPlayer() {
        player.changeHealth(-150);
    }
    private void cleanDeadMonsters() {
        for(int i = 0; i<monsters.length; i++)
        {   if (monsters[i]!= null && monsters[i].getHealth() <=0) {
            monsters[i] = null;

        }
        }
    }
    public void moveMonsters() {
        for (int i = 0; i < monsters.length; i++) {
            if (monsters[i] != null) {
                this.moveMonster(monsters[i]);

            }
        }
    }

    public void moveMonster(Entity m)
    {
        Random random = new Random();
        int randomDirection = random.nextInt(4);
        if(randomDirection == 0 && tileTypes[m.getX()-1][m.getY()] == TileType.FLOOR
                || tileTypes[m.getX()-1][m.getY()] == TileType.ODOOR)
        {
            m.setPosition(m.getX()-1, m.getY());
        }
        else if(randomDirection == 1 && tileTypes[m.getX()+1][m.getY()] == TileType.FLOOR
                || tileTypes[m.getX()+1][m.getY()] == TileType.ODOOR)
        {
            m.setPosition(m.getX()+1, m.getY());
        }
        else if(randomDirection == 2 && tileTypes[m.getX()][m.getY()-1] == TileType.FLOOR
                || tileTypes[m.getX()][m.getY()-1] == TileType.ODOOR)
        {
            m.setPosition(m.getX(), m.getY()-1);
        }
        else if(randomDirection == 3 && tileTypes[m.getX()][m.getY()+1] == TileType.FLOOR
                || tileTypes[m.getX()][m.getY()+1] == TileType.ODOOR)
        {
            m.setPosition(m.getX(), m.getY()+1);
        }
    }
}

class FileWriterClass {
    FileWriterClass()
    {

    }
    public void createFileMethod()
    {
        try {
            File myObj = new File("assets/ScoreBoard.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeFileMethod(int[] statistics) throws FileNotFoundException {
        File myObj = new File("assets/ScoreBoard.txt");
        Scanner myReader = new Scanner(myObj);
        String[] arrayOfStrings = new String[5];
        int i = 0;
        while (myReader.hasNextLine()&& i<=5)
        {   System.out.println(arrayOfStrings[i]);
            arrayOfStrings[i] = myReader.nextLine();
            System.out.println("reader");
            System.out.println(arrayOfStrings[i]);
            i++;
        }
        System.out.println(arrayOfStrings[0]);
        if(myObj.delete())
        {
            System.out.println("File deleted successfully");
        }
        else
        {
            System.out.println("Failed to delete the file");
        }
        myReader.close();
        System.out.println(Arrays.toString(arrayOfStrings) + "from readMethod");
        int statDataInt;
        statDataInt = statistics[0];
        System.out.println(statDataInt);
        StringBuilder statString = new StringBuilder();
        System.out.println(Arrays.toString(arrayOfStrings) + "from write method");
        int j = 0;
        String data = arrayOfStrings[j];
        System.out.println(data + " from try");
        System.out.println(arrayOfStrings[0]);
        for(int x = 0; x<8; x++)
        {
            switch (x)
            {
                case 0: statString.append("Total Score: ").append(statistics[x]).append(" "); break;
                case 1: statString.append("Toilet Paper Collected: ").append(statistics[x]).append(" "); break;
                case 2: statString.append("Sanitiser Dispensers Used: ").append(statistics[x]).append(" "); break;
                case 3: statString.append("Masks Collected: ").append(statistics[x]).append(" "); break;
                case 4: statString.append("Hand Sanitiser Collected: ").append(statistics[x]).append(" "); break;
                case 5: statString.append("Clients sent home peacefully: ").append(statistics[x]).append(" "); break;
                case 6: statString.append("Clients sent home by force: ").append(statistics[x]).append(" "); break;
                case 7: statString.append("Floors cleared: ").append(statistics[x]).append(" "); break;

            }

        }
        try {
            FileWriter myWriter = new FileWriter("assets/ScoreBoard.txt");

            boolean targetAchieved = false;


            if(data!=null)
            {   int minim = 0;
                String[] tokens = data.split("\\s+");
                minim = Integer.parseInt(tokens[2]);
                int minIndex = 0;
                for(j=0; j<5; j++) {
                    int fileInt;
                    data = arrayOfStrings[j];
                    if(data == null)
                        continue;
                    tokens = data.split("\\s+");
                    try{
                        if(tokens[2] == null)
                            continue;
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        continue;
                    }
                    fileInt = Integer.parseInt(tokens[2]);
                    System.out.println(fileInt);
                    if(minim > fileInt)
                    {
                        minIndex = j;
                        minim = fileInt;
                    }
                }
                if(i==5)
                {
                    for(j=0; j<5; j++)
                    {
                        if(minIndex == j)
                        {
                            if(statDataInt > minim)
                                myWriter.write( statString+"\n");
                            else
                            {   data = arrayOfStrings[j];
                                myWriter.write(data + "\n");
                            }

                        }
                        else
                        {
                            data = arrayOfStrings[j];
                            myWriter.write(data + "\n");

                        }
                    }
                }
                else
                {
                    for(j=0; j<i; j++)
                    {
                        data = arrayOfStrings[j];
                        myWriter.write(data + "\n");
                        System.out.println("null from last if");
                        System.out.println(j);
                        System.out.println(i);
                    }
                    myWriter.write(statString+"\n");
                }
            }
            else
                myWriter.write(statString + "\n");

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}


class FrameGenerator extends JFrame {

    boolean aBoolean = false;
    void setaBoolean(String string)
    {
        switch (string)
        {
            case "true": aBoolean = true; break;
            case "false": aBoolean = false; break;
        }
    }
    public FrameGenerator(){}
    public FrameGenerator(String string) throws FileNotFoundException {
        switch (string)

        {   case "startGame": startGame(); break;
            case "howToPlay": howToPlay(); break;
            case "hallOfFame": hallOfFame();break;
            case "stair": stair(); break;
        }

    }


    void stair(){
        JFrame dutyNotFulfilled = new JFrame("Duty not fulfilled");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dutyNotFulfilled.setLocationRelativeTo(null);
        dutyNotFulfilled.setSize(300, 100);
        dutyNotFulfilled.setVisible(true);
        panel.setBackground(Color.BLACK);
        JButton acknowledged = new JButton("Acknowledged!");
        JTextArea stair = new JTextArea(" You have not finished your duty, you still have to send subjects back home! One way or another" );
        stair.setEditable(false);
        panel.add(stair);
        panel.add(acknowledged);
        dutyNotFulfilled.add(panel);
        dutyNotFulfilled.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        acknowledged.addActionListener(e -> dutyNotFulfilled.dispose());
    }
    void startGame(){
        JFrame startFrame = new JFrame("The Mall is Closed");
        JPanel startPanel = new JPanel();
        startFrame.setVisible(true);
        startFrame.setSize(500, 500);
        startFrame.setLocationRelativeTo(null);
        startFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setBackground(Color.BLACK);

        JButton startGame = new JButton("Start Game");
        JButton howToPlayButton = new JButton("How to play");
        JButton hallOfFameButton = new JButton("Hall of Fame");
        JButton exit = new JButton("Quit");
        startPanel.add(startGame);
        startPanel.add(howToPlayButton);
        startPanel.add(hallOfFameButton);
        startPanel.add(exit);
        startFrame.add(startPanel);
        startGame.addActionListener(e -> EventQueue.invokeLater(() -> {
            GameGUI gui = new GameGUI();            //create GUI
            gui.setVisible(true);                   //display GUI
            DungeonGenerator gnrt = new DungeonGenerator(gui);   //create engine
            DungeonInputHandler i = new DungeonInputHandler(gnrt);//create input handler
            gui.registerKeyHandler(i);//registers handler with GUI
            gnrt.generateDungeon(); //starts the game
            startFrame.dispose();



        }));
        howToPlayButton.addActionListener(e ->{
                    if (!aBoolean) {
                        howToPlay();
                    }

                }

        );
        hallOfFameButton.addActionListener(e -> {
            if (!aBoolean) {
                try {
                    hallOfFame();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });
        exit.addActionListener(e -> startFrame.dispose());
    }
    void hallOfFame() throws FileNotFoundException {
        setaBoolean("true");
        JFrame frame = new JFrame("Hall of Fame");
        JPanel panel = new JPanel();
        frame.setEnabled(true);
        panel.setVisible(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(1110, 200);
        JButton okButton = new JButton("Ok");
        JTextArea textArea = new JTextArea();
        panel.add(textArea);
        panel.add(okButton);
        textArea.setEditable(false);
        frame.add(panel);
        File myObj = new File("assets/ScoreBoard.txt");
        Scanner myReader = new Scanner(myObj);
        String[] arrayOfStrings = new String[5];
        int i = 0;
        while (myReader.hasNextLine()&& i<=5)
        {
            arrayOfStrings[i] = myReader.nextLine();
            textArea.append(arrayOfStrings[i] + "\n");
            i++;
        }
        myReader.close();
        okButton.addActionListener(e1 -> {
            setaBoolean("false");
            frame.dispose();

        });
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    void howToPlay(){
        setaBoolean("true");
        JFrame hTPframe = new JFrame("How To Play");
        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea();
        panel.add(textArea);
        textArea.setEditable(false);
        hTPframe.setEnabled(true);
        panel.setVisible(true);
        hTPframe.setVisible(true);
        hTPframe.setLocationRelativeTo(null);
        hTPframe.setSize(700, 300);
        textArea.setText("You are a security guard working at a mall. You have to send all the customers back home due to the pandemic." + "\n"
        + " Use the arrow keys to move through the map." + "\n" + " Use the E key to interact with doors, stairs, chests and sanitiser dispensers." + "\n"
        +" Use the A key to interact with the clients. While interacting with the clients you can " + "\n"
        + " either try to convince them to leave home peacefully or make use of your violence to be sure they leave."+ "\n"
                + " Beware, if you attack somebody they will attack you too. " + "\n" + " You can also choose to put on a mask or use handsanitiser which will give you a boost in combat"+ "\n"
                +" Press the escape key to show a menu which includes these tips, to show the hall of fame" + "\n"
        + " or to finish this attempt." + "\n" + " Throughout the map you can collect various items from chests, most important being the currency during a pandemic" +"\n"
                + "Toilet Paper!" + " You can close the game only by either dying, pressing the Save and Exit or the Quit button.");
        JButton okButton = new JButton("Ok");
        panel.add(okButton);
        hTPframe.add(panel);
        okButton.addActionListener(e1 -> {
            setaBoolean("false");
            hTPframe.dispose();

        });
        hTPframe.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}

