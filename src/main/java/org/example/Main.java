package org.example;
import java.io.*;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static String curren_dir = System.getProperty("user.dir");
    public static final String home_dir = System.getProperty("user.dir");
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome , write valid commands!!");
        CommandLineInterpreter terminal = new CommandLineInterpreter();
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        while(true){

            String input= scanner.nextLine().trim();
            if(input.equals("exit")) break;
            String[] command_line = input.split("\\|");
            int current_c = 0 ;
            while (current_c<command_line.length){
                String[] command = command_line[current_c].split(" ");

                if(command[0].equals("ls")){

                    String specification="";
                    String path="";
                    if(command.length>=2) {
                        if (command[1].equalsIgnoreCase("-a") || command[1].equalsIgnoreCase("-r")) {
                            specification = command[1];
                            if (command.length == 3) path = command[2];
                        } else if (command.length == 2) path = command[1];
                    }

                    terminal.ls(specification,path);
                }else if(command[0].equalsIgnoreCase("touch")){
                    String path;
                    if(command.length==2) {
                        path= command[1];
                        terminal.touch(path);
                    }else{
                        System.out.println("Error: No file path provided for touch command.");

                    }

                }
                else if(command[0].equalsIgnoreCase("cd") ){
                    if(command.length==2){
                        System.out.println(curren_dir);
                        terminal.cd(curren_dir,command[1]);
                        System.out.println(curren_dir);
                    }
                    else{
                        System.out.println("wrong command");
                    }

                }
                current_c++;
            }
        }
    }
}