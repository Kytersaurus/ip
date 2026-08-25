import java.util.Scanner;

public class Bob {
    public static void main(String[] args) {
        String banner = " ____     ___    ____  \n"
                + "| |_) )  / _ \\  | |_) ) \n"
                + "|  _ \\  | | | | |  _ \\ \n"
                + "| |_) | | |_| | | |_) |\n"
                + "|____/   \\___/  |____/ \n";

        String intro = "Hello! I'm B.O.B. (Best OpenAI Bot)\n";
        String start = "What can I do for you?\n";
        String exit = "Bye. Hope to see you again soon!\n";
        String divider = "____________________________________________________________\n";
        System.out.println(divider + banner + intro + start + divider);
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                String input = in.nextLine();
                if (input.trim().equalsIgnoreCase("Bye")) {
                    System.out.println(divider + exit + divider);
                    break;
                }
                System.out.println(divider + input + "\n" + divider);
            }
        }
    }
}
