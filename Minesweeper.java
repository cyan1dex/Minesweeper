import java.awt.*;
 
import javax.imageio.ImageIO;
import javax.swing.*;
 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
public class Minesweeper extends JPanel implements MouseListener,
        ActionListener
{
    private boolean[][]     board;
    private boolean[][]     flags;
    private boolean[][]     bombs;
    private short           gameState;
    public JTextField       label;
 
    final int               WON         = 1;
    final int               LOST        = -1;
    final int               IN_PLAY     = 0;
 
    static BufferedImage    bomb;
    static BufferedImage    flag;
    private int             size;
 
    static final int        MENU_HEIGHT = 50;
 
    public static void main(String[] args) throws IOException
    {
        bomb = ImageIO.read(new File("bomb.gif"));
        flag = ImageIO.read(new File("flag.gif"));
 
        JFrame frame = new JFrame();
        frame.setTitle("Minesweeper!");
        frame.setSize(500, 500);
 
        Minesweeper panel = new Minesweeper(12);
        panel.addMouseListener(panel);
        Container c = frame.getContentPane();
 
        JPanel bFrame = new JPanel();
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(panel);
        bFrame.add(newGame);
        panel.label = new JTextField("");
        panel.label.setEditable(false);
        bFrame.add(panel.label);
 
        c.setLayout(new BorderLayout());
        c.add(panel, BorderLayout.CENTER);
        c.add(bFrame, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
 
    public Minesweeper()
    {
        this(12);
    }
 
    public Minesweeper(int size)
    {
        this.size = size;
        this.board = new boolean[size][size];
        this.flags = new boolean[size][size];
        this.bombs = new boolean[size][size];
 
        newGame(); // after arrays are initialized
    }
 
    // Use the following code to randomly fill the array with bombs
    public void newGame()
    {
        // initialize arrays here
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                this.board[i][j] = false;
                this.flags[i][j] = false;
                this.bombs[i][j] = false;
            }
        }
        int numBombs = size * size / 7;
        while (numBombs > 0)
        {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            if (bombs[x][y] == false)
            {
                bombs[x][y] = true;
                numBombs--;
            }
        }
        gameState = IN_PLAY;
 
        this.repaint();
    }
 
    // unused methods
    public void mouseClicked(MouseEvent e)
    {}
 
    public void mousePressed(MouseEvent e)
    {}
 
    public void mouseEntered(MouseEvent e)
    {}
 
    public void mouseExited(MouseEvent e)
    {}
 
    public void paint(Graphics g)
    {
        int boxWidth = getWidth() / board.length;
        int boxHeight = getHeight() / board[0].length;
 
        // draws the board
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
            {
                if (board[i][j])
                    g.setColor(Color.LIGHT_GRAY);
                else
                    g.setColor(Color.DARK_GRAY);
                g.fillRect(i * boxWidth, j * boxHeight, boxWidth, boxHeight);
            }
 
        // draws lines
        g.setColor(Color.BLACK);
        for (int i = 0; i < getWidth(); i += boxWidth)
        {
            g.drawLine(i, 0, i, boxHeight * board[0].length);
 
        }
        for (int i = 0; i < getHeight(); i += boxHeight)
        {
            g.drawLine(0, i, boxWidth * board.length, i);
 
        }
        // draws labels
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if (board[i][j])
                {
                    int bombsNear = calcBombs(i, j);
                    g.setColor(Color.black);
                    if (bombsNear != 0)
                        g.drawString("" + bombsNear,
                                (int) (boxWidth * (i + 0.45)),
                                (int) (boxHeight * (j + 0.65)));
                }
        if (gameState == LOST)
        {
            // draws bombs
            for (int i = 0; i < bombs.length; i++)
                for (int j = 0; j < bombs[i].length; j++)
                {
                    if (bombs[i][j])
                        g.drawImage(bomb, i * boxWidth, j * boxHeight,
                                boxWidth, boxHeight, null);
                }
            label.setText("You Lost! :P    ");
        } else if (gameState == WON)
        {
            label.setText("You Win!!! Gratz");
        } else
        {
            label.setText("                                 ");
            // draws flags
            for (int i = 0; i < board.length; i++)
                for (int j = 0; j < board[i].length; j++)
                {
                    if (!board[i][j] && flags[i][j])
                        g.drawImage(flag, i * boxWidth, j * boxHeight, null);
                }
        }
    }
 
    // called when the mouse is released
    public void mouseReleased(MouseEvent e)
    {
        // Use left click (button1) to uncover a tile and right click (button 3)
        // to mark a tile with a flag
 
        int mouseX = e.getX() / (getWidth() / board.length);
        int mouseY = e.getY() / (getHeight() / board[0].length);
 
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            // cannot click a flagged square
            // stops taking clicks if you lose
            if (gameState == IN_PLAY && isValidSq(mouseX, mouseY)
                    && !flags[mouseX][mouseY])
                testSquare(mouseX, mouseY);
            // cheat
            else if (e.getX() < 10 && e.getY() > getHeight() - 10)
                for (int i = 0; i < board.length; i++)
                    for (int j = 0; j < board[i].length; j++)
                        flags[i][j] = bombs[i][j];
 
        } else if (e.getButton() == MouseEvent.BUTTON3)
        {
            // can only flag hidden squares
            // stops taking clicks if you lose
            if (gameState == IN_PLAY && isValidSq(mouseX, mouseY)
                    && !board[mouseX][mouseY])
                flags[mouseX][mouseY] = !flags[mouseX][mouseY];
        }
 
        this.repaint();
    }
 
    private int calcBombs(int x, int y)
    {
        int ret = 0;
        for (int i = x - 1; i < x + 2; i++)
            for (int j = y - 1; j < y + 2; j++)
            {
                if (isValidSq(i, j) && bombs[i][j])
                    ret++;
            }
        return ret;
    }
 
    private void testSquare(int x, int y)
    {
 
        if (!isValidSq(x, y))
            return;
 
        // losing
        if (bombs[x][y])
        {
            this.gameState = LOST;
            return;
        }
 
        board[x][y] = true;
 
        // winning
        if (hasWon())
            this.gameState = WON;
 
        // "spreading" behavior when choosing a safe square
        if (calcBombs(x, y) == 0)
            for (int i = x - 1; i < x + 2; i++)
                for (int j = y - 1; j < y + 2; j++)
                    if (isValidSq(i, j) && !board[i][j])
                        testSquare(i, j);
 
    }
 
    public boolean isValidSq(int i, int j)
    {
        return !(i < 0 || j < 0 || i >= board[0].length || j >= board.length);
    }
 
    private boolean hasWon()
    {
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if (!board[i][j] && !bombs[i][j])
                    return false;
        return true;
    }
 
    public void actionPerformed(ActionEvent arg0)
    {
        newGame();
    }
 
}