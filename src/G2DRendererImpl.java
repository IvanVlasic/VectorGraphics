import java.awt.*;

/**
 * Created by ivan on 5/27/16.
 */
public class G2DRendererImpl implements Renderer {
    private Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {
        Color color = new Color(25, 3,255);
        g2d.setColor(color);
        g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
    }

    @Override
    public void fillPolygon(Point[] points) {
        int[] xpoints = new int[points.length];
        int[] ypoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            xpoints[i] = points[i].getX();
            ypoints[i] = points[i].getY();
        }

        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(xpoints, ypoints, points.length);
        g2d.setColor(Color.RED);
        g2d.drawPolygon(xpoints, ypoints, points.length);
    }
}
