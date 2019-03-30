package com.aaron.Refactor;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

public class BallsManager {

    final byte ballsCount = 37;
    private short [][] ballsPosition = new short [ballsCount][2];
    static List data = new ArrayList();

    private List<Ball> ballsList = new ArrayList<>(ballsCount);
    private List<Integer> ballsTmp = new ArrayList<>();
    private String [] ballsColor = new String[ballsCount];
    byte counter;
    private Robot robot;


    public BallsManager() {
        init();
    }

//bug : 如果球偵測顏色錯誤 沒消掉 就要主動跑下一個
    protected void go (int status){  //設計一個thread 主要負責偵測版面所有可能排法，然後回傳其中一種給go跑 ，跑完消掉後再去偵測球有沒有消失，沒有的話立即回傳另一種
        try {
            dyInit();
            int num = 0;
            int stayTime = 50;

            for (int i = 0; i < ballsCount; i++) {
                if (!ballsList.get(i).isConnectStatus()) {
                    connect(i);
                    if (counter > 1) {
                        System.out.println("NO : " + num);
                        for (int j = 0; j < ballsTmp.size(); j += 2) {
                            int index = ballsTmp.get(j);
                            int next = ballsTmp.get(j + 1);
                            Ball currentBall = ballsList.get(index);
                            Ball adjacentBall = ballsList.get(next);
                            System.out.print(index + "------>" + next + " , ");
                            if (status == 2) {
                                robot.mouseMove(currentBall.getX(), currentBall.getY(), stayTime);
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK, stayTime);
                                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK, stayTime);
                                break;
                            }else{
                                robot.mouseMove(currentBall.getX(),currentBall.getY(),stayTime);
                                //Thread.sleep(stayTime);
                                if(j == 0)
                                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                                robot.mouseMove(adjacentBall.getX(),adjacentBall.getY(),stayTime);
                                if(j+2 >= ballsTmp.size())
                                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK,stayTime);
                            }
                        }

                        System.out.println();
                        num++;
                        counter = 0;
                        ballsTmp.clear();
                        break;
                    } else {
                        counter = 0;
                        for (int j = 0; j < ballsTmp.size(); j++) {
                            Ball tmpball = ballsList.get(ballsTmp.get(j));
                            tmpball.setConnectStatus(false);
                        }
                        ballsTmp.clear();
                    }
                }
            }

            ballsList.clear();
        }catch (Exception ee){
            System.out.println(ee.toString());
        }
    }

    private void dyInit(){      //每次有消掉就要做 動態
        for (int i = 0; i < ballsCount; i++) {
            ballsList.add(new Ball(ballsPosition[i][0],ballsPosition[i][1]));
            ballsColor[i] = ballsList.get(i).getPixelColor();
        }
    }

    private void connect(int index){
        Ball currentBall = ballsList.get(index);
        List<Integer> a = currentBall.getAdjacentBalls();
        String color = ballsColor[index];

        for (int i = 0; i < a.size(); i++) {
            int adjacentIndex = a.get(i);
            Ball adjacentBall = ballsList.get(adjacentIndex);
            if (color.equals(ballsColor[adjacentIndex]) && !adjacentBall.isConnectStatus()) {
                counter++;
                currentBall.setConnectStatus(true);
                adjacentBall.setConnectStatus(true);
                ballsTmp.add(index);
                ballsTmp.add(adjacentIndex);

                connect(adjacentIndex);
                break;
            }
        }
    }









    private void init() {   //只做一次
        try {
            byte row = 13, column = 7;
            short[][] allBallsPosition = new short[row * column][2];
            short x = 755, y = 450, count = 0;
            for (int i = 0; i < column; i++) {      //直
                System.out.println(i+" x : "+x);
                for (int j = 0; j < row; j++) {     //橫
                    System.out.println("   , y : "+y);
                    allBallsPosition[count][0] = x;
                    allBallsPosition[count][1] = y;
                    count++;
                    y += 40;
                }
                x += 68;
                y = 450;
            }

            //刪除多餘的位置
            int remove[] = {11, 13, 25, 65, 77, 79, 89};
            int index = 0;
            for (int i = 3; i < 91; i += 2) {
                if (i == remove[0] || i == remove[1] || i == remove[2] || i == remove[3] || i == remove[4] || i == remove[5] || i == remove[6])
                    continue;
                ballsPosition[index][0] = allBallsPosition[i][0];
                ballsPosition[index][1] = allBallsPosition[i][1];
                index++;
            }

            // 由上往下編號，因為初始是由左至右 (之後再改吧)
            short tmpx, tmpy;
            for (int j = 0; j < ballsPosition.length - 1; j++) {
                for (int i = 0; i < ballsPosition.length - 1; i++) {
                    if (ballsPosition[i][1] > ballsPosition[i + 1][1]) {
                        tmpy = ballsPosition[i + 1][1];
                        ballsPosition[i + 1][1] = ballsPosition[i][1];
                        ballsPosition[i][1] = tmpy;
                        tmpx = ballsPosition[i + 1][0];
                        ballsPosition[i + 1][0] = ballsPosition[i][0];
                        ballsPosition[i][0] = tmpx;
                    }
                }
            }

            data.add(ballsCount);
            data.add(ballsPosition);

            //0:purple:(195,140,255) 1:yellow:(255,240,105) 2:blue:(80,185,250) 3:green:(20,255,140) 4:pink:(240,145,190) 5:skill:(70,60,55)
            int red[] = {195, 255, 80, 20, 240, 70};
            int green[] = {140, 240, 185, 255, 145, 60};
            int blue[] = {255, 105, 250, 140, 190, 55};
            data.add(red);
            data.add(green);
            data.add(blue);

            robot = new Robot();

        }catch (Exception ee){
            System.out.println(ee.toString());
        }

    }

}
