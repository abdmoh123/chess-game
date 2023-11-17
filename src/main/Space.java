package main;

public class Space {
    private final int X; // a to h = 0 to 7
    private final int Y; // 1 to 8 = 0 to 7

    public Space(int x_in, int y_in) {
        this.X = x_in;
        this.Y = y_in;
    }

    public int getX() {
        return X;
    }
    public int getY() {
        return Y;
    }
}
