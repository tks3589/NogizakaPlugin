package com.aaron.old;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Ball {
    int x,y;
    boolean connectStatus=false;
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(boolean connectStatus) {
        this.connectStatus = connectStatus;
    }

    public List<Integer>  getAdjacentBall(){ //list可動態配置array
        int size=BallsManager.ballsPosition.length;
        //int []adjacentBall = new int[6];
        List<Integer> adjacentBall=new ArrayList<>();
        int index=0;
        for (int i = 0; i < size; i++) {
            int dx=abs(BallsManager.ballsPosition[i][0]-x);
            int dy=abs(BallsManager.ballsPosition[i][1]-y);
            if(((dx>=0&&dx<100)&&(dy>=0&&dy<100))&&(dx+dy!=0)){
                adjacentBall.add(index,i);
                index++;
            }
        }
        return adjacentBall;
    }



    public String getPixelColor(){ //與桌面螢幕比較顏色
        //0:purple:(195,140,255) 1:yellow:(255,240,105) 2:blue:(80,185,250) 3:green:(20,255,140) 4:pink:(240,145,190) 5:skill:(70,60,55)
        int red[] = {195,255,80,20,240,70};
        int green[] = {140,240,185,255,145,60};
        int blue[] = {255,105,250,140,190,55};
        String result = null;
        int rnum,gnum,bnum,index=0,min,r=0,g=0,b=0;
        int sum[]=new int[6]; //color num

//        byte m[][]={{0,0},{-10,0},{10,0},{0,-10},{0,10}}; //中左右上下5個位置 (速度太慢)
//        byte colorCount = (byte)m.length;

        byte m[][]={{0,0}};
        byte colorCount = (byte)m.length;
        try {
            if(BallsManager.mode){
                Robot robot = new Robot();
                Color color;
                for (int i = 0; i <colorCount; i++) {
                    color = robot.getPixelColor(x+m[i][0],y+m[i][1]);
                    r += color.getRed();
                    g += color.getGreen();
                    b += color.getBlue();
                }
            }
            else{
                int color;
                for (int i = 0; i < colorCount; i++) {
                    color = ImageIO.read(new File(BallsManager.testPicPath)).getRGB(x+m[i][0],y+m[i][1]);
                    r += (color>>16)&0xff;
                    g += (color>>8)&0xff;
                    b += (color)&0xff;
                }
            }

            for (int i = 0; i < sum.length; i++) { //計算球中心與上下左右總共五個點的顏色資訊，去比較陣列內最接近的顏色
                rnum = abs(r-(red[i]*colorCount));
                gnum = abs(g-(green[i]*colorCount));
                bnum = abs(b-(blue[i]*colorCount));
                sum[i] = rnum+gnum+bnum;
            }
            min=sum[0];
            for (int i = 0; i < sum.length; i++) {
                if(sum[i]<min) {
                    min = sum[i];
                    index = i;
                }
            }
            switch (index){
                case 0 :
                    result = "Purple";
                    break;
                case 1 :
                    result = "Yellow";
                    break;
                case 2 :
                    result = "Blue";
                    break;
                case 3 :
                    result = "Green";
                    break;
                case 4:
                    result = "Pink";
                    break;
                default:
                    result = "Skill";
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
