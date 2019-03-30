package com.aaron.Refactor;

import java.awt.*;

public class Robot extends java.awt.Robot {

    public Robot() throws AWTException {
    }


    public synchronized void mouseMove(int x, int y , int time) throws InterruptedException {
        Thread.sleep(time);
        super.mouseMove(x, y);
    }


    public synchronized void mousePress(int buttons , int time) throws InterruptedException {
        Thread.sleep(time);
        super.mousePress(buttons);
    }


    public synchronized void mouseRelease(int buttons , int time) throws InterruptedException {
        Thread.sleep(time);
        super.mouseRelease(buttons);
    }
}
