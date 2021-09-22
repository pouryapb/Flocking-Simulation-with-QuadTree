public class Rectangle {
    private double x;
    private double y;
    private double width;
    private double height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = x + width;
        this.height = y + height;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getWidth() {
        return width - x;
    }

    public void setWidth(int width) {
        this.width = x + width;
    }

    public double getHeight() {
        return height - y;
    }

    public void setHeight(int height) {
        this.height = y + height;
    }

    public boolean contains(Vector p) {
        return p.getX() >= x && p.getX() < width && p.getY() >= y && p.getY() < height;
    }

    public boolean intersects(Rectangle range) {
        return !(range.getX() > width || range.getY() > height || range.getWidth() < x || range.getHeight() < y);
    }
}
