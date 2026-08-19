package _test07;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// 클래스 역할 : 플레이어의 벽 충돌 감시 서비스 (백그라운드에서 계속 돌아감)
// 메인 쓰레드는 너무 바쁘다.
public class BackgroundPlayerService implements Runnable {

    // Image / ImageIcon : 좌표 값으로 현재 픽셀 값 추출 할 수 없다.
    // 메모리에 픽셀 배열로 저장된 이미지
    // getRGB(x, y)로 특정 좌표에 색상값을 직접 읽을 수 있다.
    private BufferedImage image;
    private Player player;

    // 생성자 주입(DI Dependency Injection)
    public BackgroundPlayerService(Player player) {
        this.player = player;
        try {
            image = ImageIO.read(new File("images/backgroundMapservice.png"));
        } catch (IOException e) {
            System.out.println("이미지 경로 및 파일명을 확인하세요");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        // 게임이 종료 될 때까지 계속 실행 예정
        while (true) {

            Color leftColor = new Color(image.getRGB(player.getX() + 5, player.getY() + 25));
            Color rightColor = new Color(image.getRGB(player.getX() + 50 + 10, player.getY() + 25));

            // 플레이어 바닥 감지 좌표
            int bottomLeft = image.getRGB(player.getX() + 10 ,player.getY() + 55);
            int bottomRight = image.getRGB(player.getX() + 50, player.getY() + 55);

            // -1 이 아니면 바닥 멈춤 --> -1 + -1 = -2 가 아니라면 바닥 멈춤
            if(bottomLeft + bottomRight == -2) {
                // 발 아래가 허공 --> 아직 점프/낙하 중이 아닐 때만 낙하 시작
                if (!player.isUp() && !player.isDown()) {
                    player.down(); // isDown() 아님
                }
            } else {
                // 발 아래가 바닥이거나 / 층이라면 --> 낙하 즉시 중단
                player.setDown(false); // 플레이어의 down() 메서드의 while 문 즉시 종료
            }

            if(isRed(leftColor)) {
                // 현재 플레이어 왼쪽 벽에 충돌된 상태
                player.setLeftWallCrash(true);
                player.setLeft(false); // 움직임 해제
            } else {
                player.setLeftWallCrash(false);
            }

            // 오른쪽벽 감지
            if(isRed(rightColor)) {
                // 현재 플레이어 오른쪽 벽에 충돌된 상태
                player.setRightWallCrash(true);
                player.setRight(false); // 움직임 해제
            } else {
                player.setRightWallCrash(false);
            }


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private boolean isRed(Color color) {
        return color.getRed() == 255 && color.getGreen() == 0 && color.getBlue() == 0;
    }

}
