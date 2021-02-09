package me.soongjamm;

public class Holoman {

    String name;

    int howLong;

    public void setName(String name) {
        this.name = name;
    }

    public void setHowLong(int howLong) {
        this.howLong = howLong;
    }

    public String getName() {
        return name;
    }

    public int getHowLong() {
        return howLong;
    }

    @Override
    public String toString() {
        return "Holoman{" +
                "name='" + name + '\'' +
                ", howLong=" + howLong +
                '}';
    }
}
