import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

public class Boid {

    private Vector position;
    private Vector velocity;
    private Vector acceleration = new Vector();
    private double maxSpeed;
    private double maxForce;
    private Game engine;
    private double alingNeighborRadius = 150;
    private double cohisionNeighborRadius = 100;
    private double seperationNeighborRadius = 50;

    public Boid(double maxSpeed, double maxForce, Game engine) {
        this.maxSpeed = maxSpeed;
        this.maxForce = maxForce;
        this.engine = engine;

        var w = Math.random() * Game.WIDTH;
        var h = Math.random() * Game.HEIGHT;
        position = new Vector(w, h);
        velocity = Vector.random2D();
    }

    public Boid(double x, double y, double maxSpeed, double maxForce, Game engine) {
        this.maxSpeed = maxSpeed;
        this.maxForce = maxForce;
        this.engine = engine;

        position = new Vector(x, y);
        velocity = Vector.random2D();
    }

    public void update() {
        Vector seperation = seperation();
        Vector align = align();
        Vector cohesion = cohesion();

        seperation.mult(1f);
        align.mult(1f);
        cohesion.mult(0.9f);

        acceleration.add(seperation);
        acceleration.add(align);
        acceleration.add(cohesion);

        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        position.add(velocity);
        acceleration.mult(0);

        border();
    }

    private void border() {
        var x = position.getX();
        var y = position.getY();

        if (x > Game.WIDTH)
            position.setX(0);
        else if (x < 0)
            position.setX(Game.WIDTH - 1d);

        if (y > Game.HEIGHT)
            position.setY(0);
        else if (y < 0)
            position.setY(Game.HEIGHT - 1d);

    }

    public void render(Graphics g) {
        g.setColor(Color.white);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform transform = g2d.getTransform();

        double angle = Math.atan2(velocity.getY(), velocity.getX()) + Math.PI / 2;
        g2d.rotate(angle, position.getX(), position.getY());

        g2d.fillPolygon(
                new int[] { (int) position.getX(), (int) (position.getX() - 3), (int) position.getX(),
                        (int) (position.getX() + 3) },
                new int[] { (int) position.getY(), (int) (position.getY() + 12), (int) (position.getY() + 9),
                        (int) (position.getY() + 12) },
                4);

        g2d.setTransform(transform);
    }

    private Vector align() {
        var steering = new Vector();
        var count = 0;

        var quadTree = engine.getQuadTree();
        List<Boid> neighbors = quadTree.query(new Rectangle(position.getX() - alingNeighborRadius / 2,
                position.getY() - alingNeighborRadius / 2, alingNeighborRadius, alingNeighborRadius));

        for (Boid boid : neighbors) {
            if (boid != this) {
                steering.add(boid.getVelocity());
                count++;
            }
        }

        if (count > 0) {
            steering.div(count);
            steering.setMag(maxSpeed);
            steering.sub(velocity);
            steering.limit(maxForce);
        }

        return steering;
    }

    private Vector cohesion() {
        var steering = new Vector();
        var count = 0;

        var quadTree = engine.getQuadTree();
        List<Boid> neighbors = quadTree.query(new Rectangle(position.getX() - cohisionNeighborRadius / 2,
                position.getY() - cohisionNeighborRadius / 2, cohisionNeighborRadius, cohisionNeighborRadius));

        for (Boid boid : neighbors) {
            if (boid != this) {
                steering.add(boid.getPosition());
                count++;
            }
        }

        if (count > 0) {
            steering.div(count);
            steering.sub(position);
            steering.setMag(maxSpeed);
            steering.sub(velocity);
            steering.limit(maxForce);
        }

        return steering;
    }

    private Vector seperation() {
        var steering = new Vector();
        var count = 0;

        var quadTree = engine.getQuadTree();
        List<Boid> neighbors = quadTree.query(new Rectangle(position.getX() - seperationNeighborRadius / 2,
                position.getY() - seperationNeighborRadius / 2, seperationNeighborRadius, seperationNeighborRadius));

        for (Boid boid : neighbors) {
            if (boid != this) {
                double d = position.dist(boid.getPosition());
                var diff = Vector.sub(position, boid.getPosition());
                diff.mult(1 / d);
                steering.add(diff);
                count++;
            }
        }

        if (count > 0) {
            steering.div(count);
            steering.setMag(maxSpeed);
            steering.sub(velocity);
            steering.limit(maxForce);
        }

        return steering;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }
}
