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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class gui {
    static boolean initialized = false;

    static JFrame window;
    static JLabel mousePositionLabel;
    static JButton fishButton;
    static JTextField xPosField;
    static JTextField yPosField;
    static JSlider holdMsSlider;
    static JSlider meanDiffThresholdSlider;
    static JLabel meanDifferenceValueLabel;
    static JLabel capturedImageLabel;

    public static void launchGUI(String windowName, String version) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                window = new JFrame(windowName);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setMenuBar(null);
                window.setMinimumSize(new Dimension(460, 300));
                window.setResizable(false);
                window.setLayout(null);
                window.setAlwaysOnTop(true);

                // mouse position labels and fields
                JLabel xAxisFieldLabel = new JLabel("x:");
                xAxisFieldLabel.setBounds(20, 20, 50, 20);
                window.add(xAxisFieldLabel);
                xAxisFieldLabel.requestFocus();

                xPosField = new JTextField("0", 1);
                xPosField.setBounds(80, 20, 110, 20);
                xPosField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyPressed(KeyEvent key) {
                        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
                            // switch focus to the other field
                            yPosField.requestFocus();
                        }
                    }
                    @Override
                    public void keyReleased(KeyEvent arg0) {}
                    @Override
                    public void keyTyped(KeyEvent arg0) {}
                });
                window.add(xPosField);


                JLabel yAxisFieldLabel = new JLabel("y:");
                yAxisFieldLabel.setBounds(20, 50, 50, 20);
                window.add(yAxisFieldLabel);

                yPosField = new JTextField("0", 1);
                yPosField.setBounds(80, 50, 110, 20);
                yPosField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyPressed(KeyEvent key) {
                        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
                            // switch focus to the other field
                            xPosField.requestFocus();
                        }
                    }
                    @Override
                    public void keyReleased(KeyEvent arg0) {}
                    @Override
                    public void keyTyped(KeyEvent arg0) {}
                });
                window.add(yPosField);
                

                // label and delay slider
                JLabel holdSliderLabel = new JLabel("hold (ms)");
                holdSliderLabel.setBounds(20, 90, 70, 20);
                window.add(holdSliderLabel);

                holdMsSlider = new JSlider(JSlider.HORIZONTAL, 0, 1500, 250);
                holdMsSlider.setValue(250);
                holdMsSlider.setMajorTickSpacing(500);
                holdMsSlider.setMinorTickSpacing(250);
                holdMsSlider.setPaintTicks(true);
                holdMsSlider.setPaintLabels(true);
                holdMsSlider.setBounds(150, 90, 170, 45);
                window.add(holdMsSlider);

                // label and threshold slider
                JLabel thresholdSliderLabel = new JLabel("mean diff threshold");
                thresholdSliderLabel.setBounds(20, 135, 130, 20);
                window.add(thresholdSliderLabel);

                meanDiffThresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
                meanDiffThresholdSlider.setValue(3);
                meanDiffThresholdSlider.setMajorTickSpacing(5);
                meanDiffThresholdSlider.setMinorTickSpacing(1);
                meanDiffThresholdSlider.setPaintTicks(true);
                meanDiffThresholdSlider.setPaintLabels(true);
                meanDiffThresholdSlider.setBounds(150, 135, 170, 45);
                window.add(meanDiffThresholdSlider);


                // captured image and label
                JLabel capturedImageLabelLabel = new JLabel("captured image");
                capturedImageLabelLabel.setBounds(340, 20, 110, 20);
                window.add(capturedImageLabelLabel);

                capturedImageLabel = new JLabel();
                capturedImageLabel.setBounds(340, 40, 60, 60);
                window.add(capturedImageLabel);


                // current mean value and label
                JLabel meanDifferenceValueLabelLabel = new JLabel("mean difference");  
                meanDifferenceValueLabelLabel.setBounds(340, 120, 110, 20);
                window.add(meanDifferenceValueLabelLabel);

                meanDifferenceValueLabel = new JLabel();
                meanDifferenceValueLabel.setBounds(340, 140, 70, 20);
                window.add(meanDifferenceValueLabel);

                // launch button
                fishButton = new JButton("fish");
                fishButton.setBounds(20, 180, 150, 75);
                window.add(fishButton);

                // mouse position label
                mousePositionLabel = new JLabel("move mouse cursor");
                mousePositionLabel.setBounds(190, 230, 100, 30);
                window.add(mousePositionLabel);

                // version label
                JLabel versionLabel = new JLabel(version);
                versionLabel.setBounds(290, 230, 40, 30);
                window.add(versionLabel);

                // copyright
                JLabel copyrightLabel = new JLabel("(c) Kasyanov N. A.");
                copyrightLabel.setBounds(335, 230, 110, 30);
                window.add(copyrightLabel);

                // show window
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);

                initialized = true;
            }
        });
    }
}
