import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by ivan on 5/28/16.
 */
public class EraserState extends IdleState {
    private Point currentPoint;
    private DocumentModel model;
    private List<GraphicalObject> segments;

    public EraserState(DocumentModel model) {
        currentPoint = null;
        this.model = model;
        segments = new ArrayList<>();
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        currentPoint = null;

        for (GraphicalObject object: segments) {
            model.removeGraphicalObject(object);
        }

        for (GraphicalObject object: intersection()) {
            model.removeGraphicalObject(object);
        }


    }

    @Override
    public void mouseDragged(Point mousePoint) {
        if (currentPoint == null) {
            currentPoint = mousePoint;
        } else {
            LineSegment ls = new LineSegment(new Point[] { currentPoint, mousePoint});
            model.addGraphicalObject(ls);
            currentPoint = mousePoint;
            segments.add(ls);

        }

    }

    private List<GraphicalObject> intersection() {
        List<GraphicalObject> intersection = new ArrayList<>();
        for (GraphicalObject object: model.list()) {
            if (object.doesCollide(segments)) {
                intersection.add(object);
            }
        }

        return intersection;
    }


}
