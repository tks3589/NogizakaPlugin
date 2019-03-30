package com.aaron.old;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class BallsManager {
    protected static int [][] ballsPosition = new int[37][2]; //37點座標
    static List<Ball> ballsList= new ArrayList<>(ballsPosition.length);
    private static List<Integer> ballTmp= new ArrayList<>();
    static String [] ballsColor = new String[ballsPosition.length]; //先將球顏色存進陣列，為了加速
    private static int counter;
    static boolean mode = false; //分辨ball比較顏色的對象是螢幕or測試影像   螢幕:true
    static boolean excstatus = false; //go()執行狀態
    static boolean fevertime = false;
    static String testPicPath=""; //測試要給path


    private BallsManager() {
    }

    static int fevertimeORnot(String inputPath){
        int color=0;
        try {
            BufferedImage bi = ImageIO.read(new File(inputPath));
            color = bi.getRGB(960,100);
//            if(inputPath.equals("testP2/9.png")){
//                Graphics2D g2d = bi.createGraphics();
//                g2d.setColor(Color.RED);
//                g2d.drawString("@",960,100);
//                ImageIO.write(bi,"png",new File("testP2/TTTT.png"));
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return color;
    }
    static void go(){
        try {
            ///
            excstatus=true;
            Robot robot = new Robot();
            ///
            init();
            /////////////////////////////////////
            for (int i = 0; i < BallsManager.ballsPosition.length; i++) {
                ballsList.add(new Ball(ballsPosition[i][0], ballsPosition[i][1]));
            }
            /////////////////////////////////////
            for (int i = 0; i < ballsColor.length; i++) {
                ballsColor[i] = ballsList.get(i).getPixelColor();
            }
            /////////////////////////////////////
            /////////////////////////////////////
            int num = 0;
            int stayTime=50;
            for (int i = 0; i < ballsList.size(); i++) {
                if (!ballsList.get(i).getConnectStatus()) {  //連過的不能再連
                    connect(i);
                    if (counter > 1) {
                        System.out.println("NO : " + num);
                        for (int j = 0; j < ballTmp.size(); j += 2) {
                            int index = ballTmp.get(j);
                            int next = ballTmp.get(j + 1);
                            Ball currentBall = ballsList.get(index);
                            Ball adjacentBall = ballsList.get(next);
                            // System.out.println(currentBall.getX()+"_"+currentBall.getY()+"_"+adjacentBall.getX()+"_"+adjacentBall.getY());
                            System.out.print(index + "------>" + next + " , ");
                            //g2d.drawLine(currentBall.getX(), currentBall.getY(), adjacentBall.getX(), adjacentBall.getY());
                            if(fevertime){
                                Thread.sleep(stayTime);
                                robot.mouseMove(currentBall.getX(),currentBall.getY());
                                Thread.sleep(stayTime);
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                                Thread.sleep(stayTime);
                                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                                fevertime=false;
                                break;
                            }else{
                                Thread.sleep(stayTime);
                                robot.mouseMove(currentBall.getX(),currentBall.getY());
                                Thread.sleep(stayTime);
                                if(j==0)
                                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                                Thread.sleep(stayTime);
                                robot.mouseMove(adjacentBall.getX(),adjacentBall.getY());
                                if(j+2>=ballTmp.size()){
                                    Thread.sleep(stayTime);
                                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                                }
                            }

                        }
                        System.out.println();
                        num++;
                        counter = 0;
                        ballTmp.clear();
                        excstatus=false;
                        break;
                    } else { //少於三顆的
                        counter = 0;
                        for (int j = 0; j < ballTmp.size(); j++) {
                            Ball tmpball = ballsList.get(ballTmp.get(j));
                            tmpball.setConnectStatus(false);
                        }
                        ballTmp.clear();
                    }
                }
            }


            ballsList.clear();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    static void showConnectBalls(String inputPath,String outputPath){
        try {
            BufferedImage bi = ImageIO.read(new File(inputPath));
            Graphics2D g2d = bi.createGraphics();
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setColor(Color.RED);
            init();
            /////////////////////////////////////
            for (int i = 0; i < BallsManager.ballsPosition.length; i++) {
                g2d.drawString(i+"",ballsPosition[i][0],ballsPosition[i][1]);
                ballsList.add(new Ball(ballsPosition[i][0],ballsPosition[i][1]));
            }
            /////////////////////////////////////
            for (int i = 0; i < ballsColor.length; i++) {
                ballsColor[i] = ballsList.get(i).getPixelColor();
            }
            /////////////////////////////////////
            int num=0;
            for (int i = 0; i < ballsList.size(); i++) {
                if(!ballsList.get(i).getConnectStatus()) {  //連過的不能再連
                    connect(i);
                    if (counter > 1) {
                        System.out.println("NO : " + num);
                        for (int j = 0; j < ballTmp.size(); j += 2) {
                            int index = ballTmp.get(j);
                            int next = ballTmp.get(j + 1);
                            Ball currentBall = ballsList.get(index);
                            Ball adjacentBall = ballsList.get(next);
                            // System.out.println(currentBall.getX()+"_"+currentBall.getY()+"_"+adjacentBall.getX()+"_"+adjacentBall.getY());
                            System.out.print(index + "------>" + next + " , ");
                            g2d.drawLine(currentBall.getX(), currentBall.getY(), adjacentBall.getX(), adjacentBall.getY());
                        }
                        System.out.println();
                        num++;
                        counter = 0;
                        ballTmp.clear();
                    } else { //少於三顆的
                        counter = 0;
                        for (int j = 0; j < ballTmp.size(); j++) {
                            Ball tmpball =ballsList.get(ballTmp.get(j));
                            tmpball.setConnectStatus(false);
                        }
                        ballTmp.clear();
                    }
                }
            }

            ImageIO.write(bi,"png",new File(outputPath));
//            for (int i = 0; i < ballsList.size(); i++) {
//                System.out.println(i+"----->"+ballsList.get(i).getConnectStatus());
//            }
            ballsList.clear();
        }catch (Exception ee){
            System.out.println(ee);
        }
    }
    private static void connect(int index){
        Ball currentBall = ballsList.get(index);
        List<Integer> a = currentBall.getAdjacentBall();
        //String color = currentBall.getPixelColor();
        String color = ballsColor[index];

        for (int i = 0; i < a.size(); i++) {
            int adjacentIndex = a.get(i);
            Ball adjacentBall = ballsList.get(adjacentIndex);
            if (color.equals(ballsColor[adjacentIndex]) && !adjacentBall.getConnectStatus()) {
                counter++;
                currentBall.setConnectStatus(true);
                adjacentBall.setConnectStatus(true);
                ballTmp.add(index);
                ballTmp.add(adjacentIndex);
                /*g2d.drawLine(currentBall.getX(),currentBall.getY(),adjacentBall.getX(),adjacentBall.getY());*/
                connect(adjacentIndex);
                break;
            }
        }
    }
    private static void init(){
        if(ballsPosition[0][0]==0){ //初始化只做第一次
            byte row=13,column=7;
            int [][] allBallsPosition = new int [row*column][2];
            int x=755,y=450,count=0;
            for (int i = 0; i < column; i++) { //直
                System.out.println(i+" x : "+x);
                for (int j = 0; j < row; j++) { //橫
                    System.out.println("   , y : "+y);
                    allBallsPosition[count][0]=x;
                    allBallsPosition[count][1]=y;
                    count++;
                    y+=40;
                }
                x+=68;
                y=450;
            }
            ////////////////////////////////////////////////////////////
            int remove[]={11,13,25,65,77,79,89};
            int index=0;
            for (int i = 3; i <91; i+=2) {
                if(i==remove[0]||i==remove[1]||i==remove[2]||i==remove[3]||i==remove[4]||i==remove[5]||i==remove[6])
                    continue;
                ballsPosition[index][0]=allBallsPosition[i][0];
                ballsPosition[index][1]=allBallsPosition[i][1];
                index++;
            }
            ////////////////////////////////////////////////////////////
            int tmpx = 0, tmpy = 0;
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



        }

    }
}
