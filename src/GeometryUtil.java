/**
 * Created by ivan on 5/26/16.
 */
public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        int dx = point1.getX() - point2.getX();
        int dy = point1.getY() - point2.getY();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        int dx = e.getX() - s.getX();
        int dy = e.getY() - s.getY();
        double numerator = Math.abs(dy * p.getX() - dx * p.getY() + e.getX() * s.getY() - e.getY() * s.getX());
        double denominator = distanceFromPoint(s, e);

        return numerator / denominator;
    }
}
