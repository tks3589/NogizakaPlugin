package com.aaron.Refactor;


public class Gaming extends Thread{
    private byte status;
    private short [] dsp = {960,100};  //偵測螢幕點位置
    private int r,g,b,color,count;     //偵測螢幕點的RGB,總和,計算次數
    private Robot robot;
    BallsManager bm;

    @Override
    public void run() {
        try {
            robot = new Robot();
            bm = new BallsManager();
            while (true) {
                if (getStatus() == 1){ //遊戲內_普通
                    System.out.println("遊戲內_普通");
                    bm.go(1);
                }else if(getStatus() == 2){ //遊戲內_fever time
                    System.out.println("遊戲內_fever time");
                    bm.go(2);
                }else{
                    System.out.println("遊戲外");
                }
            }
        }catch (Exception ee){
            System.out.println(ee.toString());
        }
    }

    private byte getStatus(){
       try {
           while (true) {
               r = robot.getPixelColor(dsp[0], dsp[1]).getRed();
               g = robot.getPixelColor(dsp[0], dsp[1]).getGreen();
               b = robot.getPixelColor(dsp[0], dsp[1]).getBlue();
               color += (r << 16) | (g << 8) | (b);

               if (count == 10) {
                   color /= 10;
                   if (color > 13000000 && color < 16000000)
                       status = 1;
                   else if (color > 6000000 && color < 8000000)
                       status = 2;
                   else
                       status = 0;
                   count = 0;
                   color = 0;
                   break;
               } else
                   count++;
           }

       }catch (Exception ee){
           System.out.println(ee.toString());
       }

        return status;
    }

}
