import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ivan on 5/27/16.
 */
public class DocumentModel {

    public final static double SELECTION_PROXIMITY = 10;
    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            if (go.isSelected() && !selectedObjects.contains(go)) {
                selectedObjects.add(go);
            }
            notifyListeners();
        }
    };

    private List<GraphicalObject> objects;
    private List<GraphicalObject> roObjects;
    private List<DocumentModelListener> listeners;
    private List<GraphicalObject> selectedObjects;
    private List<GraphicalObject> roSelectedObjects;

    public DocumentModel() {
        objects = new ArrayList<>();
        listeners = new ArrayList<>();
        selectedObjects = new ArrayList<>();
        roObjects = Collections.unmodifiableList(objects);
        roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    }

    public void clear() {
        for (GraphicalObject obj: objects) {
            removeGraphicalObject(obj);
        }

        notifyListeners();
    }

    public void deSelectAll() {
        for (GraphicalObject object: selectedObjects) {
            object.setSelected(false);
            for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
                object.setHotPointSelected(i, false);
            }
        }
        selectedObjects.clear();
    }

    public void addGraphicalObject(GraphicalObject obj) {
        objects.add(obj);
        obj.addGraphicalObjectListener(goListener);
        notifyListeners();
    }

    public void removeGraphicalObject(GraphicalObject obj) {
        objects.remove(obj);
        obj.removeGraphicalObjectListener(goListener);
        notifyListeners();
    }

    public List<GraphicalObject> list() {
        return roObjects;
    }

    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        for (DocumentModelListener listener: listeners) {
            listener.documentChange();
        }
    }

    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index == objects.size() - 1) {
            return;
        }

        Collections.swap(objects, index, index + 1);
        notifyListeners();
    }

    public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index == 0) {
            return;
        }

        Collections.swap(objects, index, index - 1);
        notifyListeners();

    }


    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        GraphicalObject object = objects.stream().sorted((o1, o2) ->
                Double.compare(o1.selectionDistance(mousePoint), o2.selectionDistance(mousePoint))).findFirst().get();

        if (object.selectionDistance(mousePoint) <= SELECTION_PROXIMITY) {
            return object;
        }
        return null;
    }

    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int index = 0;

        if (object.getNumberOfHotPoints() == 0) {
            return -1;
        }

        double distance = GeometryUtil.distanceFromPoint(object.getHotPoint(0), mousePoint);

        for (int i = 1; i < object.getNumberOfHotPoints(); i++) {
            double objectDist = object.getHotPointDistance(i, mousePoint);
            if (distance > objectDist) {
                distance = objectDist;
                index = i;
            }
        }

        if (distance <= SELECTION_PROXIMITY) {
            return index;
        }
        return -1;
    }

    public void deselectHotPoints(GraphicalObject object) {
        for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
            object.setHotPointSelected(i, false);
        }
    }
}
