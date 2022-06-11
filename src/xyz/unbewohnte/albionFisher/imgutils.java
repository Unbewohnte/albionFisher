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

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.*;

public class imgutils {
    public static double calculateImageMean(BufferedImage img) {
        WritableRaster raster = img.getRaster();
        double sum = 0;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                sum += raster.getSampleDouble(x, y, 0);
            }
        }

        return sum / (img.getWidth() * img.getHeight());
    }

    public static Point getPixelCoordinates(BufferedImage img, RGB color) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int imgColor = img.getRGB(x, y);
                int imgR = (imgColor & 0xff0000) >> 16;
                int imgG = (imgColor & 0xff00) >> 8;
                int imgB = (imgColor & 0xff);
                
                if (imgR == color.r && imgG == color.g && imgB == color.b) {
                    return new Point(x, y);
                }
            }
        }

        return new Point(-1, -1); 
    }

}
