package my_test.ch02;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BubbleFrame extends JFrame {

    private JLabel backgroundMap;
    private Player player;

    public BubbleFrame() {
        initData();
        setInitLayout();
        addEventListener();
    }

    private void initData() {
        setTitle("버블버블");
        setSize(1000, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backgroundMap = new JLabel(new ImageIcon("images/backgroundMap.png"));
        setContentPane(backgroundMap); // 루트 패널에 JLabel 설정
        player = new Player();
    }

    private void setInitLayout() {
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null); // JFrame 화면 가운데 배치

        add(player);
        setVisible(true);
    }

    private void addEventListener() {
        // 프레임에 키보드 리스너 등록
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyCode : " + e.getKeyCode());

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT :
                        player.left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        player.right();
                        break;
                    case KeyEvent.VK_UP:
                        player.up();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("KeyReleased : " + e.getKeyCode());
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT :
                        // 왼쪽으로 가고 있다가 방향키를 떼면 -- while 멈추는 동작이 필요하다.
                        player.setLeft(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        // 오른쪽으로 가고 있다가 방향키를 떼면 -- while 멈추는 동작이 필요하다.
                        player.setRight(false); // 돌아가고 있던 while 문이 false 되어서 멈추게 된다.
                        break;

                    case KeyEvent.VK_UP:

                        break;
                }
            }
        });

    }

    // 테스트 코드 작성
    public static void main(String[] args) {
        new BubbleFrame();
    }

}
