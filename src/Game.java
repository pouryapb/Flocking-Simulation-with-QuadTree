import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -5571995104017886412L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = WIDTH / 12 * 9;
	private transient Thread thread;
	private boolean running = false;
	private transient QuadTree quadTree;

	public Game() {
		quadTree = new QuadTree(new Rectangle(0, 0, WIDTH, HEIGHT), 4);
		for (int i = 0; i < 20; i++) {
			quadTree.insert(new Boid(4, 0.5, this));
		}

		new Window(WIDTH, HEIGHT, "Flocking Simulation Improved", this);
	}

	public QuadTree getQuadTree() {
		return quadTree;
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}

	}

	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		var amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		var frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}
			if (running)
				render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	public void update() {
		List<Boid> boids = quadTree.getAll();
		QuadTree nextQuadTree = new QuadTree(new Rectangle(0, 0, WIDTH, HEIGHT), 4);

		for (Boid boid : boids) {
			boid.update();
			nextQuadTree.insert(boid);
		}

		quadTree = nextQuadTree;
	}

	public void render() {
		var bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		var g = bs.getDrawGraphics();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		/////////////////////////////////////

		g.setColor(Color.decode("#212121"));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		List<Boid> boids = quadTree.getAll();
		for (Boid boid : boids) {
			boid.render(g);
		}

		/////////////////////////////////////
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		new Game();
	}
}
