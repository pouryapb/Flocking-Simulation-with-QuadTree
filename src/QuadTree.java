import java.util.ArrayList;
import java.util.List;

public class QuadTree {
    private Rectangle boundary;
    private int capacity;
    private List<Boid> boids = new ArrayList<>();
    private QuadTree nw;
    private QuadTree ne;
    private QuadTree se;
    private QuadTree sw;

    public QuadTree(Rectangle boundary, int capacity) {
        this.boundary = boundary;
        this.capacity = capacity;
    }

    public void insert(Boid boid) {
        if (!boundary.contains(boid.getPosition()))
            return;

        if (boids.size() < capacity)
            boids.add(boid);
        else {
            if (nw == null)
                subdivide();
            nw.insert(boid);
            ne.insert(boid);
            se.insert(boid);
            sw.insert(boid);
        }
    }

    public List<Boid> query(Rectangle range) {
        if (!boundary.intersects(range))
            return List.of();

        List<Boid> res = new ArrayList<>();
        for (Boid boid : boids) {
            if (range.contains(boid.getPosition()))
                res.add(boid);
        }
        if (nw != null) {
            res.addAll(nw.query(range));
            res.addAll(ne.query(range));
            res.addAll(se.query(range));
            res.addAll(sw.query(range));
        }
        return res;
    }

    public List<Boid> getAll() {
        return query(boundary);
    }

    public List<Rectangle> getAllZones() {
        List<Rectangle> res = new ArrayList<>();
        res.add(boundary);
        if (nw != null) {
            res.addAll(nw.getAllZones());
            res.addAll(ne.getAllZones());
            res.addAll(se.getAllZones());
            res.addAll(sw.getAllZones());
        }
        return res;
    }

    private void subdivide() {
        var w = boundary.getWidth() / 2;
        var h = boundary.getHeight() / 2;

        var nwb = new Rectangle(boundary.getX(), boundary.getY(), w, h);
        var neb = new Rectangle(boundary.getX() + w, boundary.getY(), w, h);
        var seb = new Rectangle(boundary.getX() + w, boundary.getY() + h, w, h);
        var swb = new Rectangle(boundary.getX(), boundary.getY() + h, w, h);

        nw = new QuadTree(nwb, capacity);
        ne = new QuadTree(neb, capacity);
        se = new QuadTree(seb, capacity);
        sw = new QuadTree(swb, capacity);
    }
}
