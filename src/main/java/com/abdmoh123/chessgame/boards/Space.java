package com.abdmoh123.chessgame.boards;

public class Space {
    private final int X; // a to h = 0 to 7
    private final int Y; // 1 to 8 = 0 to 7

    public Space(int x_in, int y_in) {
        this.X = x_in;
        this.Y = y_in;
    }
    public Space(String space_in) {
        /* Create a Space object using standard chess notation (e.g. a2 = Space(0, 1)) */

        if (space_in.length() != 2) {
            throw new RuntimeException("Invalid space is outside board limits!");
        }

        char x_char = Character.toLowerCase(space_in.charAt(0));
        switch (x_char) {
            case 'a':
                this.X = 0;
                break;
            case 'b':
                this.X = 1;
                break;
            case 'c':
                this.X = 2;
                break;
            case 'd':
                this.X = 3;
                break;
            case 'e':
                this.X = 4;
                break;
            case 'f':
                this.X = 5;
                break;
            case 'g':
                this.X = 6;
                break;
            case 'h':
                this.X = 7;
                break;
            default:
                throw new RuntimeException("Invalid space is outside board limits!");
        }
        this.Y = space_in.charAt(1) - '0' - 1;
    }

    @Override
    public String toString() {
        String x_axis = "";

        switch (getX()) {
            case 0:
                x_axis = "a";
                break;
            case 1:
                x_axis = "b";
                break;
            case 2:
                x_axis = "c";
                break;
            case 3:
                x_axis = "d";
                break;
            case 4:
                x_axis = "e";
                break;
            case 5:
                x_axis = "f";
                break;
            case 6:
                x_axis = "g";
                break;
            case 7:
                x_axis = "h";
                break;
            default:
                throw new RuntimeException("Invalid space is outside board limits!");
        }

        return x_axis + (getY() + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Space)) {
            return false;
        }

        Space space = (Space) obj;
        return getX() == space.getX() && getY() == space.getY();
    }

    public int getX() {
        return X;
    }
    public int getY() {
        return Y;
    }
}
