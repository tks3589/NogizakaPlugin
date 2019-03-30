package com.aaron.old;



import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Temp {
    static Robot robot;
    static int [][] ballPosition = new int[7*13][2];
    static int [][] finalPosition = new int[37][2]; //37點座標
    static List<Ball> ballList= new ArrayList<>();
    static BufferedImage bi;
    static Graphics2D g2d;
    static int counter=0;
    static List<Integer> ballTmp= new ArrayList<>();

    public static void main(String[] args) {

        first();

        //getPictures();


    }

    public static void getPictures(){
        int x=0,y=0,width=1920,height=1080;
        int count=0;
        BufferedImage bfImage = null;
         try {
                robot = new Robot();
                while (true){
                    Thread.sleep(3000);
                    bfImage = robot.createScreenCapture(new Rectangle(x, y, width, height));
                    ImageIO.write(bfImage,"png",new File("testP/"+count+".png"));
                    count++;
                }

         } catch (Exception e) {
                 e.printStackTrace();
         }

    }



    private static void first() {

        drawLine();
        showArray();
        drawPoint2();
        rebuild();
        drawPoint3();
        showAdjacentBall2();
    }


    private static void showAdjacentBall2() { //連到底
        try{
            bi = ImageIO.read(new File("point3.png"));
            g2d = bi.createGraphics();
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setColor(Color.RED);
            int num=0;
            for (int i = 0; i < ballList.size(); i++) {
                if(!ballList.get(i).getConnectStatus()) {  //連過的不能再連
                    connectBall2(i);
                    if (counter > 1) {
                        System.out.println("NO : " + num);
                        for (int j = 0; j < ballTmp.size(); j += 2) {
                            int index = ballTmp.get(j);
                            int next = ballTmp.get(j + 1);
                            Ball currentBall = ballList.get(index);
                            Ball adjacentBall = ballList.get(next);
                            // System.out.println(currentBall.getX()+"_"+currentBall.getY()+"_"+adjacentBall.getX()+"_"+adjacentBall.getY());
                            System.out.print(index + "------>" + next + " , ");
                            g2d.drawLine(currentBall.getX(), currentBall.getY(), adjacentBall.getX(), adjacentBall.getY());
                        }
                        System.out.println();
                        num++;
                        counter = 0;
                        ballTmp.clear();
                    } else {
                        counter = 0;
                        ballTmp.clear();
                    }
                }
            }


            ImageIO.write(bi,"png",new File("connectLine.png"));
        }catch (Exception ee){
            System.out.println(ee+"");
        }
    }
    private static void connectBall2(int index) {  //好一點的方法應該是要遞迴每個可能都跑過一次，之後選一個球最多的路徑，目前只有三個就可。  bug : fever time 判斷 & ball color error
        Ball currentBall = ballList.get(index);
        List<Integer> a = currentBall.getAdjacentBall();
        String color = currentBall.getPixelColor();

        for (int i = 0; i < a.size(); i++) {
            int adjacentIndex = a.get(i);
            Ball adjacentBall = ballList.get(adjacentIndex);
            if (color.equals(adjacentBall.getPixelColor()) && !adjacentBall.getConnectStatus()) {
                counter++;
                currentBall.setConnectStatus(true);
                adjacentBall.setConnectStatus(true);
                ballTmp.add(index);
                ballTmp.add(adjacentIndex);
                /*g2d.drawLine(currentBall.getX(),currentBall.getY(),adjacentBall.getX(),adjacentBall.getY());*/
                connectBall2(adjacentIndex);
                break;
            }
        }
    }
    private static void connectBall() {
        //int connectCount=0;
        for(int j=0;j<ballList.size();j++) {

            Ball currentBall = ballList.get(j);
            List<Integer> a = currentBall.getAdjacentBall();
            String color = currentBall.getPixelColor();

            for (int i = 0; i < a.size(); i++) {
                int adjacentIndex = a.get(i);
                Ball adjacentBall = ballList.get(adjacentIndex);

                if(color.equals(adjacentBall.getPixelColor())&&!adjacentBall.getConnectStatus()){
                    //g2d.drawLine(currentBall.getX(),currentBall.getY(),adjacentBall.getX(),adjacentBall.getY());
                    //g2d.drawRect(currentBall.getX(),currentBall.getY(),1,1);
                    //g2d.drawRect(adjacentBall.getX(),adjacentBall.getY(),1,1);
                    currentBall.setConnectStatus(true);
                    adjacentBall.setConnectStatus(true);
                    //connectBall(adjacentIndex);
                }

            }

        }
    }

    private static void showAdjacentBall() { //先將附近的連起來
        try{
            BufferedImage bi = ImageIO.read(new File("point3.png"));
            Graphics2D g2d = bi.createGraphics();
            g2d.setColor(Color.RED);
            for(int j=0;j<ballList.size();j++) {
                Ball currentBall = ballList.get(j);
                List<Integer> a = currentBall.getAdjacentBall();
                String color = currentBall.getPixelColor();
                System.out.print("No "+j+" ball :  "+color);

                for (int i = 0; i < a.size(); i++) {
                    //if (a[0]==0||a[i] != 0)
                    int adjacentIndex = a.get(i);
                    Ball adjacentBall = ballList.get(adjacentIndex);
                    System.out.print(", "+adjacentIndex);

                    if(color.equals(adjacentBall.getPixelColor())){
                        g2d.drawLine(currentBall.getX(),currentBall.getY(),adjacentBall.getX(),adjacentBall.getY());
                    }

                }
                System.out.println();
            }
            ImageIO.write(bi,"png",new File("connectLine.png"));
        }catch (Exception ee){
            System.out.println(ee+"");
        }
    }



    private static void showArray() {
        for (int i = 0; i < 7*13; i++) {

            System.out.print(i+" ( ");
            System.out.print(ballPosition[i][0]);
            System.out.print("，");
            System.out.print(ballPosition[i][1]);
            System.out.print(" )");
            System.out.println();
        }
    }

    public static void rebuild() {
        int tmpx = 0, tmpy = 0;
        for (int j = 0; j < finalPosition.length - 1; j++) {
            for (int i = 0; i < finalPosition.length - 1; i++) {
                if (finalPosition[i][1] > finalPosition[i + 1][1]) {
                    tmpy = finalPosition[i + 1][1];
                    finalPosition[i + 1][1] = finalPosition[i][1];
                    finalPosition[i][1] = tmpy;
                    tmpx = finalPosition[i + 1][0];
                    finalPosition[i + 1][0] = finalPosition[i][0];
                    finalPosition[i][0] = tmpx;
                }
            }
        }
    }

    public static void drawPoint3(){
        try {
            BufferedImage bi = ImageIO.read(new File("27.png"));
            Graphics2D g2d = bi.createGraphics();
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setColor(Color.RED);
            for (int i = 0; i < finalPosition.length; i++) {
                g2d.drawString(i+"",finalPosition[i][0],finalPosition[i][1]);
                System.out.println(i+"  : ( "+finalPosition[i][0]+" , "+finalPosition[i][1]+" )");
                ballList.add(new Ball(finalPosition[i][0],finalPosition[i][1]));
            }

            ImageIO.write(bi,"png",new File("point3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void drawPoint2(){
        try {
            BufferedImage bi = ImageIO.read(new File("first.png"));
            Graphics2D g2d = bi.createGraphics();
            g2d.setColor(Color.RED);
            int remove[]={11,13,25,65,77,79,89};
            int index=0;
            for (int i = 3; i <91; i+=2) {
                g2d.drawRect(ballPosition[i][0],ballPosition[i][1],1,1);
                if(i==remove[0]||i==remove[1]||i==remove[2]||i==remove[3]||i==remove[4]||i==remove[5]||i==remove[6])
                    continue;
                //g2d.drawString(i+"",ballPosition[i][0],ballPosition[i][1]);
                finalPosition[index][0]=ballPosition[i][0];
                finalPosition[index][1]=ballPosition[i][1];
                index++;
            }
            ImageIO.write(bi,"png",new File("point2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void drawPoint(){
        try {
            BufferedImage bi = ImageIO.read(new File("first.png"));
            Graphics2D g2d = bi.createGraphics();
            g2d.setColor(Color.RED);
            for (int i = 0; i <91; i+=1) {
                g2d.drawRect(ballPosition[i][0],ballPosition[i][1],1,1);
            }
            ImageIO.write(bi,"png",new File("point.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void drawLine(){
        try {
            BufferedImage bi = ImageIO.read(new File("first.png"));
            Graphics2D g2d = bi.createGraphics();
            g2d.setColor(Color.RED);
            int x=755,y=450,count=0;

            for (int i = 0; i < 7; i++) {//7
                g2d.drawLine(x,0,x,1080);
                System.out.println(i+" x : "+x);
                for (int j = 0; j < 13; j++) {//13
                    g2d.drawLine(0,y,1920,y);
                    System.out.println("   , y : "+y);
                    ballPosition[count][0]=x;
                    ballPosition[count][1]=y;
                    count++;
                    y+=40;
                }
                x+=68;
                y=450;
            }


            ImageIO.write(bi,"png",new File("line.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }














    public static  void click(){
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            robot.keyRelease(KeyEvent.VK_D);
            Thread.sleep(500);
            robot.mouseMove(112,235);
            robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
            robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(60000);
            robot.mouseMove(977,378);
            robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void testPoint(){
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
            System.out.println("x: " + point.x + " y: " + point.y);
        }
    }
}
