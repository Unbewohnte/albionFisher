/*
albionFisher - albion online fishing bot.
Copyright (C) 2022 Kasyanov Nikolay Alexeyevich

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;


public class fisher {
    static String VERSION = "v0.1.2";

    static RGB BOBBER_COLOR = new RGB(255, 180, 31);

    static boolean fishing = false; 

    static Robot bot;

    gui GUI;

    static int mousePos_x;
    static int mousePos_y;
    static double lastMean;
    static double currentMean;
    static double meanDifference;


    public static void updateGUI() {
        // react to input and update mouse position
        try{
        
        // wait until gui initializes
        while (!gui.initialized) {
            Thread.sleep(50);
        }

        gui.fishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent action) {
                if (action.getID() == ActionEvent.ACTION_FIRST) {                            
                    if (fishing) {
                        gui.fishButton.setText("stop");
                        fishing = false;
                    } else {
                        gui.fishButton.setText("fish");
                        fishing = true;
                    }
                }
            }
        });

        while (true) {
            // update mouse position
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            mousePos_x = mousePos.x;
            mousePos_y = mousePos.y;
            if (gui.mousePositionLabel != null) {
                gui.mousePositionLabel.setText(String.format("x: %d; y: %d", mousePos_x, mousePos_y));
            }
            
            // update fish button
            if (fishing) {
                gui.fishButton.setText("stop");
            } else {
                gui.fishButton.setText("fish");
            }

            if (currentMean != 0.0f) {
                gui.meanDifferenceValueLabel.setText(String.format("%3.3f", meanDifference));
            }

            Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        try {
            // initialize mouse, keyboard, etc.
            bot = new Robot();

            // GUI
            gui.launchGUI("fisher", VERSION);

            // update gui in the separate thread
            Thread updateGUIThread = new Thread() {
                @Override
                public void run() {
                    updateGUI();
                }
            };
            updateGUIThread.start();

            // fishing logic
            boolean windowActivated = false;
            while (true) {
                Thread.sleep(50);

                if (!fishing) {
                    windowActivated = false;
                    continue;
                }

                // retrieve information from GUI
                int xpos;
                int ypos;
                int holdms;
                int meanDiffThreshold;
                try {
                    xpos = Integer.parseInt(gui.xPosField.getText());
                    ypos = Integer.parseInt(gui.yPosField.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                    gui.xPosField.setText("0");
                    gui.yPosField.setText("0");
                    continue;
                }
                holdms = gui.holdMsSlider.getValue();
                meanDiffThreshold = gui.meanDiffThresholdSlider.getValue();

                
                Thread.sleep(500);

                // activate window if needed
                if (!windowActivated) {
                    windowActivated = true;
                    bot.mouseMove(xpos, ypos);
                    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    bot.delay(50);
                    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }

                // move the mouse to the position

                // give the game window needed vector
                bot.delay(300);
                bot.mouseMove(xpos-5, ypos-5);
                bot.mouseMove(xpos, ypos);

                // now throw the bobber !
                bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                bot.delay(holdms);
                bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                // sleep for some time while the bobber lands
                Thread.sleep(3500);

                // wait for fish as long as the user does not move the mouse
                while (true) {
                    if (mousePos_x != xpos && mousePos_y != ypos) {
                        fishing = false;
                        break; // break out of loop with fishing flag set to FALSE
                    }

                    // take screenshot of the specified positions
                    BufferedImage screenshot = bot.createScreenCapture(new Rectangle(mousePos_x-25, mousePos_y-25, 45, 45));
                    
                    // update UI
                    gui.capturedImageLabel.setIcon(new ImageIcon(screenshot));

                    // calculate image mean
                    lastMean = currentMean;
                    currentMean = imgutils.calculateImageMean(screenshot);
                    if (lastMean == 0) {
                        // it's the first screenshot
                        lastMean = currentMean;
                    }
                    meanDifference = Math.abs(currentMean - lastMean);

                    // check if severe changes has occured in the image
                    if (meanDifference > meanDiffThreshold) {
                        // catch !
                        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        break; // break out of loop still fishing so the next stage starts
                    }

                    Thread.sleep(50);
                }

                // play minigame if still fishing
                lastMean = 0.0f;
                currentMean = 0.0f;
                if (!fishing) {
                    continue;
                };

                // wait for the game to pop up
                Thread.sleep(500);

                Point initialBobberPosition = new Point(-1, -1);
                while (true) {
                    // play the game if the user hasn't taken control
                    if (mousePos_x != xpos && mousePos_y != ypos) {
                        fishing = false;
                        break;
                    }

                    // screenshot
                    BufferedImage screenshot = bot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                    // find mini-game bobber by pixel color
                    Point bobberCoords = imgutils.getPixelCoordinates(screenshot, BOBBER_COLOR);
                    if (bobberCoords.x != -1 && bobberCoords.y != -1) {
                        // found one !
                        if (initialBobberPosition.x == -1 && initialBobberPosition.y == -1) {
                            // it's the first time
                            initialBobberPosition = bobberCoords;
                            continue;
                        }

                        if (bobberCoords.x < initialBobberPosition.x) {
                            // hold
                            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        }

                        if (bobberCoords.x > initialBobberPosition.x) {
                            // release
                            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        }
                    }

                    if (bobberCoords.x == -1 && bobberCoords.y == -1 && initialBobberPosition.x != -1 && initialBobberPosition.y != -1) {
                        // the game has been finished
                        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(6000);
                        break;
                    }

                    Thread.sleep(5);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}