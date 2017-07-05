package own.framework.game.support;

import java.awt.*;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by 挨踢狗 on 2017/6/23.
 */
public class Utility {
    public static Matrix3x3f createReverseViewport(float worldWidth, float worldHeight, float screenWidth, float screenHeight){
        float sx = worldWidth / (screenWidth - 1.0f);
        float sy = worldHeight / (screenHeight - 1.0f);
        float tx = (screenWidth - 1.0f) / 2.0f;
        float ty = (screenHeight - 1.0f) / 2.0f;
        Matrix3x3f viewport = Matrix3x3f.translate(-tx, -ty);
        viewport = viewport.mul(Matrix3x3f.scale(sx,-sy));
        return viewport;
    }
    public static Matrix3x3f createViewport(float worldWidth, float worldHeight, float screenWidth, float screenHeight){
        float sx = (screenWidth - 1.0f) / worldWidth;
        float sy = (screenHeight - 1.0f) / worldHeight;
        float tx = (screenWidth - 1.0f) / 2.0f;
        float ty = (screenHeight - 1.0f) / 2.0f;
        Matrix3x3f viewport = Matrix3x3f.scale(sx,-sy);
        viewport = viewport.mul(Matrix3x3f.translate(tx,ty));
        return viewport;
    }

    public static void drawPolygon(Graphics g, Vector2f[] vector2fs){
        Vector2f S = null;
        Vector2f E = vector2fs[vector2fs.length - 1];
        for (int i = 0;i < vector2fs.length;i++){
            S = vector2fs[i];
            g.drawLine((int)S.x,(int)S.y,(int)E.x,(int) E.y);
            E = S;
        }
    }

    public static void drawPolygon(Graphics g, java.util.List<Vector2f> vector2fList){
        Vector2f S = null;
        Vector2f E = vector2fList.get(vector2fList.size() - 1);
        for (Vector2f v : vector2fList){
            S = v;
            g.drawLine((int)S.x,(int)S.y,(int)E.x,(int) E.y);
            E = S;
        }
    }

    public static void drawFill(Graphics g,Vector2f[] polygon){
        Graphics2D g2d = (Graphics2D)g;
        Polygon polygons = new Polygon();
        for (Vector2f v : polygon){
            polygons.addPoint((int)v.x,(int)v.y);
        }
        g2d.fill(polygons);
    }

    public static void drawFill(Graphics g, java.util.List<Vector2f> polygon){
        Graphics2D g2d = (Graphics2D)g;
        Polygon polygons = new Polygon();
        for (Vector2f v : polygon){
            polygons.addPoint((int)v.x,(int)v.y);
        }
        g2d.fill(polygons);
    }

    public static int drawString(Graphics g, int x, int y, String str){
        return drawString(g,x,y,new String[]{str});
    }
    public static int drawString(Graphics g, int x, int y, java.util.List<String> str){
        return drawString(g,x,y,str.toArray(new String[0]));
    }
    public static int drawString(Graphics g, int x, int y, String... str){
        FontMetrics fm =  g.getFontMetrics();
        int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
        for (String s : str){
            g.drawString(s,x,y+fm.getAscent());
            y += height;
        }
        return y;
    }

    public static int drawCenteredString(Graphics g, int w, int h, String str){
        return drawCenteredString(g, w, h, new String[]{str});
    }

    public static int drawCenteredString(Graphics g, int w, int y, List<String> str){
        return drawCenteredString(g, w, y, str);
    }

    public static int drawCenteredString(Graphics g, int w, int y, String... str){
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
        for (String s : str){
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
            int x = (w - (int)bounds.getWidth()) / 2;
            g.drawString(s, x, y + fm.getAscent());
            y += height;
        }
        return y;
    }

    public static BufferedImage scaleImage(BufferedImage toScale, int targetWidth, int targetHeight){
        int width = toScale.getWidth();
        int height = toScale.getHeight();
        if (targetWidth < width || targetHeight < height){
            return scaleDownImage(toScale, targetWidth, targetHeight);
        }else {
            return scaleUpImage(toScale, targetWidth, targetHeight);
        }
    }

    private static BufferedImage scaleUpImage(BufferedImage toScale, int targetWidth, int targetHeight){
        BufferedImage image = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(toScale, 0, 0, image.getWidth(), image.getHeight(), null);
        g2d.dispose();
        return image;
    }

    private static BufferedImage scaleDownImage(BufferedImage toScale, int targetWidth, int targetHeight){
        int w = toScale.getHeight();
        int h = toScale.getHeight();
        do {
            w = w/2;
            if (w < targetWidth){
                w = targetWidth;
            }
            h = h/2;
            if (h < targetHeight){
                h = targetHeight;
            }
            BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = tmp.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(toScale, 0, 0, w, h, null);
            g2d.dispose();
            toScale = tmp;
        }while (w != targetWidth || h != targetHeight);
        return toScale;
    }
}
