import java.util.Locale;
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
        String[] tasks = new String[100];
        int taskCount = 0;
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                String input = in.nextLine();
                if (input.trim().equalsIgnoreCase("bye")) {
                    System.out.println(divider + exit + divider);
                    break;
                }
                else if (input.trim().equalsIgnoreCase("list")) {
                    System.out.println(divider);
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + ". " + tasks[i]);
                    }
                    System.out.println(divider);
                }
                else {
                    System.out.println(divider + "added: " + input.trim() + '\n' + divider);
                    tasks[taskCount] = input.trim();
                    taskCount++;
                }
            }
        }
    }
}
