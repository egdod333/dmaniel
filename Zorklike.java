package zorklike;

import zorklike.Room;
import java.util.Scanner;

public class Zorklike {
	static Scanner scan;
	static enum Type{
        KEY,
        CD,
        WEAPON
    };
	public static void main(String[] args) {
		scan = new Scanner(System.in);
		System.out.println("Hello World");
	}
}
