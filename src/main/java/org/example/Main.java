package org.example;
import java.io.*;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome , write valid commands!!");
        CommandLineInterpreter terminal = new CommandLineInterpreter();
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        while(true){

            String input= scanner.nextLine().trim();
            if(input.equals("exit")) break;
            String[] command = input.split("\\s+");
            if(command[0].equals("ls")){
                /*for(String x:command){
                    System.out.println(x);
                    System.out.println("%n");
                }*/
                String specification="";
                String path="";
                if(command.length>=2) {
                    if (command[1].equals("-a") || command[1].equals("-r")) {
                        specification = command[1];
                        if (command.length == 3) path = command[2];
                    } else if (command.length == 2) path = command[1];
                }

                terminal.ls(specification,path);
            }
        }
    }
}