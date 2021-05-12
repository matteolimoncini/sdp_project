package REST;

import java.util.Scanner;

public class ClientAdmin {
    public static void main(String[] args) {
        int numberMenu;
        while (true){
            System.out.println("Welcome to the admin page");
            System.out.print("<1> list of drones in the city \n" +
                            "<2> last n global statistics \n" +
                            "<3> average number of delivery between two timestamp \n"+
                            "<4> average number of kilometers between two timestamp \n"+
                            "press any other key to exit\n");
            Scanner in = new Scanner(System.in);
            numberMenu = in.nextInt();
            switch (numberMenu){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    System.out.println("exit from menu");
                    break;
            }
        }
    }
}
