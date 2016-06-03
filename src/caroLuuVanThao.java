import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Bai tạp Caro
// Copy rights 2016 by Luu Van Thao-D8CNPM
//@Student    Luu Van Thao
//@ID  1381310069

public class caroLuuVanThao {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Copyright 2016 by Luu Van Thao( 1381310069) - D8CNPM", "Game Caro", JOptionPane.INFORMATION_MESSAGE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // this variable is used to set the size of matrix
        int size = -1;
        while (true) {
            String result = JOptionPane.showInputDialog("Vui lòng chọn kích thước bàn cờ\nTối thiểu: 5, Tối đa: 20", "16");
            if (result != null)
                try {
                    size = Integer.parseInt(result);
                    if (size < 5 || size > 20) {
                        JOptionPane.showMessageDialog(null, "Xin lỗi! Vui lòng nhập lại(tối thiểu 5, tối đa 20)");
                    } else break;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Định dạng bạn vừa nhập không chính xác, vui lòng nhập lại");
                }
            else System.exit(0);
        }
        cuaSoTroChoi game = cuaSoTroChoi.create(size);
        if (game == null) {
            // Failed
            System.out.println("Xin lỗi nhưng không thể bắt đầu game");
            System.exit(-1);
        } else {
            // Successful
            game.setVisible(true);
        }
    }

}
// NguoiChoi class

class NguoiChoi {

    /**
     * Set a name of the player
     */
    private String name;

    /**
     * character of player
     */
    private char c;
    private int score;
    private JLabel label;

    /**
     * Constructor
     * @param name  a nem of player
     * @param c    Character of player ('X' or 'O')
     */
    public NguoiChoi(String name, char c) {
        this.name = name;
        this.c = c;
        score = 0;
        label = new JLabel(name, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 32));
        label.setPreferredSize(new Dimension(0, 100));
    }

    /**
     * Get a name of player
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * get character of player
     *
     * @return character of player
     */
    public char getChar() {
        return c;
    }

    /**
     * return player's score
     *
     *
     * @return score
     */
    private int getScore() {
        return score;
    }

    /**
     * return the label which is attached with this player
     *
     * @return
     */
    public JComponent getUI () {
        return label;
    }

    /**
     * Icreasing player's score after wining
     * set value of lable again
     */
    public void won() {
        score++;
        label.setText(name + " - " + score);
    }

    /**
     * Event occcurs when someone get a turn
     * Change border of a lable
     */
    public void gotTurn() {
        label.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
    }

    /**
     * SU kien khi nguoi choi mat luot
     * Thay doi vien cua label
     */
    public void lostTurn() {
        label.setBorder(null);
    }
}

/**
 * Lop Ke thua JButton
 * @author thanbaiks
 */
class nutTroChoi extends JButton {
    /**
     * Nguoi choi cua Button nay
     */
    private NguoiChoi p;

    /**
     * Chi so hang va cot cua button tren ban co
     */
    private int x,y;

    /**
     * Constructor
     * @param listener catch event Click
     * @param x x coordinate
     * @param y coordinate
     */
    public nutTroChoi(ActionListener listener, int x, int y) {
        super();
        this.x = x;
        this.y = y;
        setSize(100,100);
        setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        setPreferredSize(new Dimension(42, 42));
        addActionListener(listener);
    }


    /**
     * excute n this cell
     *
     * @param p player excute
     */
    public void play(NguoiChoi p) {
        this.p = p;
        setText(String.valueOf(p.getChar()));
        if (p.getChar() == 'O')
            setForeground(Color.red);
        else
            setForeground(Color.BLACK);
    }

    /**
     * Checking whether or not Someone click on this button
     * @return boolean
     */
    public boolean isPlayable() {
        return p == null;
    }

    public void reset() {
        setText("");
        setForeground(Color.black);
        p = null;
    }

    /**
     * Getting x coordinate of this cell
     *
     * @return x coordinate
     */
    public int getPosX() {
        return x;
    }

    /**
     * Getting y coordinate of this cell
     * @return Y coordinate
     */
    public int getPosY() {
        return y;
    }

    /**
     * returning player whom played on this cell
     * returning player or null if cell is empty
     */
    public NguoiChoi getPlayer() {
        return p;
    }
}

class cuaSoTroChoi extends JFrame {
    private static final int[][] DIRECTIONS = {{0,1},{1,1},{1,0},{-1,1}};

    private final int size;

    private final NguoiChoi nguoiChoi1 = new NguoiChoi("Luu Thao 01", 'X');
    private final NguoiChoi nguoiChoi2 = new NguoiChoi("Luu Thao 02", 'O');
    private NguoiChoi mCurrentNguoiChoi = null;
    private nutTroChoi[][] buttons;
    private int countEmpty;

    private cuaSoTroChoi(int size) throws HeadlessException {
        super("Caro - Luu Van Thao - 1381310069 ");
        this.size = size;
    }
    public static cuaSoTroChoi create(int size){
        try {
            cuaSoTroChoi game = new cuaSoTroChoi(size);
            game.init();
            return game;
        } catch (HeadlessException e) {
            return null;
        }
    }

    private void init() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(size,size));
        gamePanel.setBackground(Color.red);
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nutTroChoi btn = (nutTroChoi)e.getSource();
                if (btn.isPlayable()){
                    btn.play(currentPlayer());
                    if (!onPlayerPlayed(currentPlayer() ,btn.getPosX(), btn.getPosY())) {
                        countEmpty--;
                        if (countEmpty <= 0) {

                            gameEnded(null);
                        }else {

                            nextPlayer();
                        }
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }


        };
        buttons = new nutTroChoi[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0;j<size;j++){
                buttons[i][j] = new nutTroChoi(listener, i, j);
                gamePanel.add(buttons[i][j]);
            }
        }
        JButton resignButton = new JButton("Bỏ cuộc");
        resignButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPlayer().won();
                gameEnded(currentPlayer());
            }
        });
        resignButton.setFont(new Font("SansSerif", Font.BOLD, 32));
        resignButton.setForeground(Color.BLUE);
        JPanel scoreBoard = new JPanel(new GridLayout(1, 3));
        scoreBoard.add(nguoiChoi1.getUI());
        scoreBoard.add(resignButton);
        scoreBoard.add(nguoiChoi2.getUI());
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(gamePanel, BorderLayout.CENTER);
        container.add(scoreBoard, BorderLayout.SOUTH);
        getContentPane().add(container);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuItem;
        menuItem = new JMenuItem("Luật chơi");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(cuaSoTroChoi.this,
                        "Mỗi người chơi đánh lần lượt, trong mỗi lượt được đánh vào 1 ô\n" +
                                "Người chơi nào đạt được 5 ô liên tục trước sẽ chiến thắng\n" +
                                "Sau khi đánh hết các ô mà không có người chiến thắng sẽ HÒA",
                        "Luật chơi",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Giới thiệu");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(cuaSoTroChoi.this,
                        "Game cờ caro\n" +
                                "Đây là Game Caro làm bài tập về nhà\n"+
                                "Tác giả: Lưu Văn Thảo - Mã số SV: 1381310069",
                        "Giới thiệu",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        menu.add(menuItem);
        menu.add(new JSeparator());

        menuItem = new JMenuItem("Thoát");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(cuaSoTroChoi.this, "Thoát khỏi trò chơi?","Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        menu.add(menuItem);

        menubar.add(menu);
        setJMenuBar(menubar);
        newGame();
    }
    private void newGame() {
        for (nutTroChoi[] gbs : buttons)
            for (nutTroChoi gb : gbs)
                gb.reset();
        countEmpty = size*size;
        mCurrentNguoiChoi = nguoiChoi1;
        notifyTurnChanged();
    }
    private NguoiChoi nextPlayer() {
        mCurrentNguoiChoi =  mCurrentNguoiChoi == nguoiChoi1 ? nguoiChoi2 : nguoiChoi1;
        notifyTurnChanged();
        return mCurrentNguoiChoi;
    }
    private NguoiChoi currentPlayer() {
        return mCurrentNguoiChoi;
    }
    private boolean onPlayerPlayed(NguoiChoi nguoiChoi, int x, int y) {
        int i = 0;
        for (int[] dir : DIRECTIONS) {
            i =  Math.max(calc(x, y, nguoiChoi, dir[0], dir[1]) + calc(x, y, nguoiChoi, -dir[0], -dir[1]) - 1, i);
        }
        if (i >= 5) {
            nguoiChoi.won();
            gameEnded(nguoiChoi);
            return true;
        }
        return false;
    }
    private int calc(int x, int y, NguoiChoi who, int ax, int ay) {
        if (x < 0 || y < 0 || x >= size || y >= size || buttons[x][y].getPlayer() != who) return 0; // Nope.
        else return 1 + calc(x+ax, y+ay, who, ax, ay);
    }
    private void notifyTurnChanged() {
        NguoiChoi now = currentPlayer();
        NguoiChoi next = now == nguoiChoi1 ? nguoiChoi2 : nguoiChoi1;
        next.lostTurn();
        now.gotTurn();
    }
    private void gameEnded(NguoiChoi nguoiChoi) {
        String message = nguoiChoi == null? "Hòa!" : nguoiChoi.getName() + " Chiến thắng!";
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        newGame();
    }
}