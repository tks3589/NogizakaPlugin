package com.aaron.old;


import java.awt.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {

        testPhoto();
       // go();

    }

    private static void function(){

        if(!BallsManager.excstatus)
            BallsManager.go();

    }
    private static void go(){
        Robot robot;
        int color=0,r,g,b,count=0;
        try {
            robot = new Robot();
            BallsManager.mode=true;
            while (true) {
                Thread.sleep(10);
                r = robot.getPixelColor(960,100).getRed();
                g = robot.getPixelColor(960,100).getGreen();
                b = robot.getPixelColor(960,100).getBlue();
                color += (r<<16)|(g<<8)|(b);
                if(count==10){
                    color/=10;
                    if(color>13000000&&color<16000000){
                        System.out.println("遊戲內 (普通)_____"+color);
                        function();
                    }
                    else if(color>6000000&&color<8000000){
                        System.out.println("遊戲內 (fever time)_____"+color);
                        BallsManager.fevertime=true;
                        function();
                    }
                    else{
                        System.out.println("遊戲外_____"+color);
                    }
                    count=0;
                    color=0;
                }else {
                    count++;

                }


            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private static void testPhoto() {
        String path = "testP2/";
        //BallsManager.testPicPath=path+"17.png";
        //BallsManager.showConnectBalls(path+"17.png",path+"output.png");
        //BallsManager.fevertimeORnot(path+"17.png",path+"ooo.png");


        //BallsManager.mode=true;
        File file = new File(path);
        int length = file.list().length;
        String status;
        for (int i = 0; i < length; i++) {
            String picpath = BallsManager.testPicPath = path+file.list()[i];
            String picedpath = path+"ED_"+file.list()[i];
            BallsManager.showConnectBalls(picpath,picedpath);
            int color = BallsManager.fevertimeORnot(picpath); //測試這部分讀整張圖 實際的話用robot類測幾個點就好 加速
            color = Math.abs(color);
            if(color>2000000&&color<4000000){
                status = "遊戲內 (普通)";
            }else if(color > 10000000){
                status = "遊戲內 (fever time)";
            }else{
                status = "遊戲外";
            }
            System.out.println(picpath+" ----- > "+status);
            System.out.println(":r"+((color>>16)&0xff)+"_g:"+((color>>8)&0xff)+"_b:"+((color)&0xff));
        }
    }
}
