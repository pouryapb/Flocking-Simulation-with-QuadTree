import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.List;

public class Sketch extends Engine {
    private transient QuadTree quadTree;

    public QuadTree getQuadTree() {
        return quadTree;
    }

    public static void main(String[] args) {
        new Sketch().start();
    }

    @Override
    protected void setup() {
        setTitle("Flocking Simulation Improved");
        var w = 800;
        var h = 800;
        setPreferredSize(new Dimension(w, h));
        setSize(w, h);
        quadTree = new QuadTree(new Rectangle(0, 0, getWidth(), getHeight()), 4);
        for (int i = 0; i < 1000; i++) {
            quadTree.insert(new Boid(1.5, 0.09, this));
        }
    }

    private void debug(Graphics g) {
        List<Rectangle> zoneList = quadTree.getAllZones();

        g.setColor(Color.red);
        for (Rectangle z : zoneList) {
            g.drawRect((int) z.getX(), (int) z.getY(), (int) (z.getWidth()), (int) (z.getHeight()));
        }

        var x = 50;
        var y = 50;
        var w = 250;
        var h = 250;

        g.setColor(Color.green);
        g.drawRect(x, y, w, h);

        List<Boid> test = quadTree.query(new Rectangle(x, y, w, h));

        for (Boid boid : test) {
            g.fillRect((int) boid.getPosition().getX(), (int) boid.getPosition().getY(), 8, 8);
        }
    }

    @Override
    protected void loop(Graphics g) {
        List<Boid> boids = quadTree.getAll();
        QuadTree nextQuadTree = new QuadTree(new Rectangle(0, 0, getWidth(), getHeight()), 4);

        for (Boid boid : boids) {
            boid.update();
            boid.show(g);
            nextQuadTree.insert(boid);
        }

        debug(g);

        quadTree = nextQuadTree;
    }
}
