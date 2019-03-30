package com.aaron.Refactor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Ball {
    int x;
    int y;
    boolean connectStatus = false;

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

    public boolean isConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(boolean connectStatus) {
        this.connectStatus = connectStatus;
    }

    public List<Integer> getAdjacentBalls(){
        List data = BallsManager.data;
        byte size = (byte)data.get(0);
        short ballsPosition [][] = (short[][]) data.get(1);
        List<Integer> adjacentBall=new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int dx = Math.abs(ballsPosition[i][0]-x);
            int dy = Math.abs(ballsPosition[i][1]-y);
            if(((dx>=0&&dx<100)&&(dy>=0&&dy<100))&&(dx+dy!=0))
                adjacentBall.add(i);
        }
        return adjacentBall;
    }

    public String getPixelColor(){
        List data = BallsManager.data;
        int red[] = (int[]) data.get(2);
        int green[] = (int[]) data.get(3);
        int blue[] = (int[]) data.get(4);
        String result = null;
        int rnum,gnum,bnum,index=0,min,r=0,g=0,b=0;
        int sum[]=new int[6];        //color num

        byte m[][]={{0,0}};
        byte colorCount = (byte)m.length;
        Color color;
        try {
            Robot robot = new Robot();
            for (int i = 0; i < colorCount; i++) {
                color = robot.getPixelColor(x + m[i][0], y + m[i][1]);
                r += color.getRed();
                g += color.getGreen();
                b += color.getBlue();
            }
            for (int i = 0; i < sum.length; i++) { //計算球中心與上下左右總共五個點的顏色資訊，去比較陣列內最接近的顏色
                rnum = Math.abs(r-(red[i]*colorCount));
                gnum = Math.abs(g-(green[i]*colorCount));
                bnum = Math.abs(b-(blue[i]*colorCount));
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
        }catch (Exception ee){
            System.out.println(ee.toString());
        }

        return result;

    }


}
