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

        if (isBetween(s, e, p)) {
            int dx = e.getX() - s.getX();
            int dy = e.getY() - s.getY();
            double numerator = Math.abs(dy * p.getX() - dx * p.getY() + e.getX() * s.getY() - e.getY() * s.getX());
            double denominator = distanceFromPoint(s, e);

            return numerator / denominator;
        } else {
            return Math.min(distanceFromPoint(s, p), distanceFromPoint(e, p));
        }

    }
    // check if point is between two points that make a line
    private static boolean isBetween(Point s, Point e, Point p) {
        int minx = Math.min(s.getX(), e.getX());
        int maxx = Math.max(s.getX(), e.getX());

        int miny = Math.min(s.getY(), e.getY());
        int maxy = Math.max(s.getY(), e.getY());

        return (p.getX() >= minx && p.getX() <= maxx) || (p.getY() >= miny && p.getY() <= maxy);
    }


}
