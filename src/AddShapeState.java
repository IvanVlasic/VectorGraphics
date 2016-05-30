/**
 * Created by ivan on 5/27/16.
 */
public class AddShapeState extends IdleState {
    private GraphicalObject prototype;
    private DocumentModel model;

    public AddShapeState(GraphicalObject prototype, DocumentModel model) {
        this.prototype = prototype;
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

        GraphicalObject go = prototype.duplicate();
        go.translate(mousePoint);
        model.addGraphicalObject(go);
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        System.out.println("lala");
    }
}
