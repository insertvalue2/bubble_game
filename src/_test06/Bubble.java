package _test06;

import lombok.Getter;

import javax.swing.*;

@Getter
public class Bubble extends JLabel implements Moveable {

    private int x;
    private int y;
    private Player player;
    private ImageIcon bubbleIcon;
    private ImageIcon bombIcon;
    private BackgroundBubbleService backgroundBubbleService;

    // 버블 이동 상태 플래그
    private static final int HORIZONTAL_DISTANCE = 400; // 버블의 수평 이동 거리
    private static final int BUBBLE_SPEED_MS = 1; //  이동 간격 (ms)
    private static final int SCREEN_TOP = 0; //  화면 상단 경계 (y 값)

    private boolean leftMoving;
    private boolean rightMoving;
    private boolean upMoving;


    public Bubble(Player player) {
        this.player = player;
        this.backgroundBubbleService = new BackgroundBubbleService(this);
        initData();
        setInitLayout();

        // 만약 이 시점에 플레이의 방향 상태를 내가 알고 있다면
        // if  문을 사용해서 left(), right() 메서드를 분기하면 될 수 있을것 같아...
        new Thread(() -> {
            if (player.getPlayerWay() == PlayerWay.LEFT) {
                left();
            }  else if (player.getPlayerWay() == PlayerWay.RIGHT) {
                right();
            }
        }).start();
    }

    private void initData() {
        bubbleIcon = new ImageIcon("images/bubble.png");
        bombIcon = new ImageIcon("images/bomb.png");
    }

    private void setInitLayout() {
        x = player.getX();
        y = player.getY();
        setLocation(x, y);
        setSize(50, 50);
        setIcon(bubbleIcon);
    }

    @Override
    public void left() {
        leftMoving = true;
        for (int i = 0; i < HORIZONTAL_DISTANCE; i++) {
            if(backgroundBubbleService.leftWall()) break;
            x--;
            setLocation(x, y);
            try {
                Thread.sleep(BUBBLE_SPEED_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        leftMoving = false;
        up();
    }

    @Override
    public void right() {
        rightMoving = true;
        for (int i = 0; i < HORIZONTAL_DISTANCE; i++) {
            if(backgroundBubbleService.rightWall()) {
                // true 넘어온다면 오른쪽 부딪침
                break;
            }
            x++;
            setLocation(x, y);
            try {
                Thread.sleep(BUBBLE_SPEED_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        rightMoving = false;
        up();
    }

    @Override
    public void up() {
        upMoving = true;
        while (true) {
            if (backgroundBubbleService.topWall()) break;
            y--;
            setLocation(x, y);
            try {
                Thread.sleep(BUBBLE_SPEED_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        upMoving = false;
        explode();
    }

    private void explode() {
        try {
            Thread.sleep(3000);
            setIcon(bombIcon);
            Thread.sleep(1000);
            // 부모 컴포넌트에서 제거
            if (getParent() != null) {
                this.setVisible(false);
                getParent().remove(this); // 메모리에 해제
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
