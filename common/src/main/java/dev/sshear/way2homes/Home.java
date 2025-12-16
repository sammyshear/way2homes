package dev.sshear.way2homes;

public class Home {
    private int x, y, z;
    private String name;
    public Home(int x, int y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public void setCoords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

}