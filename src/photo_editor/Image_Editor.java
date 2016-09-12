package photo_editor;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Image_Editor {
    static JFrame frame = new JFrame("Image Editor");
    static JPanel panel = new JPanel();
    static JLabel label = new JLabel();
    static JMenuBar menuBar = new JMenuBar();
    static JMenu file = new JMenu("File");
    static JMenu options = new JMenu("Options");
    static BufferedImage firstImage, newImage;
    static File picFile;

    public static void main(String[] args) {
        String[] optionList = {"Open", "Save As", "Exit", "Restore to Original", "Horizontal Flip", "Vertical Flip", "Gray Scale", "Sepia Tone", "Invert Colour", "Gaussian Blur", "Bulge Effect"};
        int[] keyStrokes = {KeyEvent.VK_O, KeyEvent.VK_S, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_H, KeyEvent.VK_V, KeyEvent.VK_G, KeyEvent.VK_P, KeyEvent.VK_I, KeyEvent.VK_U, KeyEvent.VK_B};
        JMenuItem[] commands = new JMenuItem[11];

        for (int i = 0; i < 11; i++) {
            if (i == 2) {
                file.addSeparator();
            } else if (i == 4) {
                options.addSeparator();
            }
            commands[i] = new JMenuItem(optionList[i]);
            commands[i].setActionCommand(optionList[i]);
            commands[i].setAccelerator(KeyStroke.getKeyStroke(keyStrokes[i], KeyEvent.CTRL_MASK));
            commands[i].addActionListener(new MenuItems());
            if (i < 3) {
                file.add(commands[i]);
            } else {
                options.add(commands[i]);
            }
        }
        menuBar.add(file);
        menuBar.add(options);
        frame.setJMenuBar(menuBar);
        //changing the interface to Windows from MacOS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {}

        frame.setPreferredSize(new Dimension(1000, 1000));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
    }

    public static class MenuItems implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //Open
            if (e.getActionCommand().equals("Open")) {
                JFileChooser fileChooser = new JFileChooser(".\\src\\image");
                int userInput = fileChooser.showOpenDialog(null);
                if (userInput == fileChooser.APPROVE_OPTION) {
                    picFile = fileChooser.getSelectedFile();
                    try {
                        firstImage = ImageIO.read(picFile);
                        newImage = ImageIO.read(picFile);
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(frame, "Can't open that file!");
                    }
                }
            }

            //Save As
            if (e.getActionCommand().equals("Save As")) {
                JFileChooser fileChooser = new JFileChooser();
                int locale = fileChooser.showSaveDialog(null);
                if (locale == fileChooser.APPROVE_OPTION) {
                    try {
                        File output = new File(fileChooser.getSelectedFile().getPath());
                        ImageIO.write(newImage, "jpg", output);
                    } catch (IOException err) {}
                }
            }

            //Exit
            if (e.getActionCommand().equals("Exit")) {
                System.exit(1);
            }

            //Restore to Original
            if (e.getActionCommand().equals("Restore to Original")) {
                try {
                    paintImage(firstImage);
                    newImage = ImageIO.read(picFile);
                } catch (IOException err) {}
            }

            //Horizontal Flip          
            if (e.getActionCommand().equals("Horizontal Flip")) {
                int x = (newImage.getWidth()) / 2;
                int x1 = x - 1;
                int x2 = x + 1;
                int y = 0;
                while (y < newImage.getHeight()) {
                    while (x1 > 0) {
                        int temp = newImage.getRGB(x2, y);
                        newImage.setRGB(x2, y, newImage.getRGB(x1, y));
                        newImage.setRGB(x1, y, temp);
                        x1--;
                        x2++;
                    }
                    x1 = x - 1;
                    x2 = x + 1;
                    y++;
                }
            }

            //Vertical Flip
            if (e.getActionCommand().equals("Vertical Flip")) {
                int y = (newImage.getHeight()) / 2;
                int y1 = y - 1;
                int y2 = y + 1;
                int x = 0;
                while (x < newImage.getWidth()) {
                    while (y1 > 0) {
                        int temp = newImage.getRGB(x, y2);
                        newImage.setRGB(x, y2, newImage.getRGB(x, y1));
                        newImage.setRGB(x, y1, temp);
                        y1--;
                        y2++;
                    }
                    y1 = y - 1;
                    y2 = y + 1;
                    x++;
                }
            }

            //Gray Scale
            if (e.getActionCommand().equals("Gray Scale")) {
                for (int x = 0; x < newImage.getWidth(); x++) {
                    for (int y = 0; y < newImage.getHeight(); y++) {
                        Color old = new Color(newImage.getRGB(x, y));
                        double average = (old.getRed() + old.getGreen() + old.getBlue()) / 3;
                        Color now = new Color((int) average, (int) average, (int) average);
                        newImage.setRGB(x, y, now.getRGB());
                    }
                }
            }

            //Sepia Tone
            if (e.getActionCommand().equals("Sepia Tone")) {
                for (int x = 0; x < newImage.getWidth(); x++) {
                    for (int y = 0; y < newImage.getHeight(); y++) {
                        Color old = new Color(newImage.getRGB(x, y));
                        double red = old.getRed() * 0.393 + old.getGreen() * 0.769 + old.getBlue() * 0.189;
                        double green = old.getRed() * 0.349 + old.getGreen() * 0.686 + old.getBlue() * 0.168;
                        double blue = old.getRed() * 0.272 + old.getGreen() * 0.534 + old.getBlue() * 0.131;

                        Color now = new Color((int) (red > 255 ? 255 : red), (int) (green > 255 ? 255 : green), (int) (blue > 255 ? 255 : blue));
                        newImage.setRGB(x, y, now.getRGB());
                    }
                }
            }

            //Invert Colour
            if (e.getActionCommand().equals("Invert Colour")) {
                for (int x = 0; x < newImage.getWidth(); x++) {
                    for (int y = 0; y < newImage.getHeight(); y++) {
                        Color old = new Color(newImage.getRGB(x, y));
                        double newRed = 255 - old.getRed();
                        double newGreen = 255 - old.getGreen();
                        double newBlue = 255 - old.getBlue();

                        Color now = new Color((int) newRed, (int) newGreen, (int) newBlue); 
                        newImage.setRGB(x, y, now.getRGB());
                    }
                }
            }

            //Gaussian Blur
            if (e.getActionCommand().equals("Gaussian Blur")) {
                int blurDiameter = 7;
                double sigma = 6.25;
                double matrixTotal = 0;
                double[][] distrib = new double[blurDiameter][blurDiameter];
                Color temp;

                for (int x = -3; x < 4; x++) {
                    for (int y = -3; y < 4; y++) {
                        distrib[x + 3][y + 3] = gaussianFunction(x, y, sigma);
                        matrixTotal = matrixTotal + distrib[x + 3][y + 3];
                    }
                }
                for (int x = 0; x < 7; x++) {
                    for (int y = 0; y < 7; y++) {
                        distrib[x][y] = distrib[x][y] / matrixTotal;
                    }
                }

                for (int x = 0; x < newImage.getWidth(); x++) {
                    for (int y = 0; y < newImage.getHeight(); y++) {
                        double newRed = 0;
                        double newGreen = 0;
                        double newBlue = 0;

                        for (int a = -3; a < 4; a++) {
                            for (int b = -3; b < 4; b++) {
                                if (x - a < 0 || y - a < 0 || x - a > newImage.getWidth() - 1 || y - a > newImage.getHeight() - 1) {
                                    temp = new Color(0, 0, 0);
                                } else {
                                    temp = new Color(newImage.getRGB(x - a, y - a));
                                }
                                newRed = newRed + temp.getRed() * distrib[a + 3][b + 3];
                                newGreen = newGreen + temp.getGreen() * distrib[a + 3][b + 3];
                                newBlue = newBlue + temp.getBlue() * distrib[a + 3][b + 3];
                            }
                        }
                        Color now = new Color((int) newRed, (int) newGreen, (int) newBlue);
                        newImage.setRGB(x, y, now.getRGB());
                    }
                }
            } 

            //Bulge Effect
            if (e.getActionCommand().equals("Bulge Effect")) {
                double xCentre = newImage.getWidth() / 2;
                double yCentre = newImage.getHeight() / 2;
                BufferedImage temporary = new BufferedImage(newImage.getWidth(), newImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < newImage.getWidth(); x++) {
                    for (int y = 0; y < newImage.getHeight(); y++) {
                        double radius = Math.sqrt(Math.pow(x - xCentre, 2) + Math.pow(y - yCentre, 2));
                        double angle = Math.atan2((y - yCentre), (x - xCentre));
                        double newRadius = Math.pow(radius, 2.1) / (xCentre + yCentre);
                        double xNew = newRadius * Math.cos(angle) + xCentre;
                        double yNew = newRadius * Math.sin(angle) + yCentre;
                        if (xNew >= 0 && xNew < newImage.getWidth() - 1 && yNew >= 0 && yNew < newImage.getHeight() - 1) {
                            temporary.setRGB((int)x, (int)y, newImage.getRGB((int)xNew, (int)yNew));
                        } else {}
                    }
                }
                newImage = temporary;
            }

            //Painting Image to Frame
            try {
                paintImage(newImage);
            } catch (IOException err) {}
        }
    }

    public static double gaussianFunction(int a, int b, double sigma) {
        double expon = (a * a + b * b) * (-1) / (2 * sigma * sigma);
        double value = 1 * Math.pow(Math.E, expon) / (2 * Math.PI * Math.pow(sigma, 2));
        return value;
    }

    public static void paintImage(BufferedImage image) throws IOException {
        panel.remove(label);
        label = new JLabel(new ImageIcon(image));
        panel.add(label);
        frame.repaint();
        frame.pack();
    }
}