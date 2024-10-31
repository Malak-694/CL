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

        while (true) {
            System.out.print(curren_dir + '>');
            String input = scanner.nextLine().trim();
            if (input.equals("exit")) break;

            String[] result = new String[0];
            int current_c = 0;

            String[] command = input.split(" ");
            if(input.contains("|")){
                result=terminal.pipe(input);
            }
            else if (input.contains(">>")) {
                // System.out.println(current_c);
                terminal.appendRedirect(input);
            } else if (input.contains(">")) {
                terminal.redirect(input);
            } else if (command[0].equals("ls")) {

                String specification = "";
                String path = "";
                if (command.length >= 2) {
                    if (command[1].equalsIgnoreCase("-a") || command[1].equalsIgnoreCase("-r")) {
                        specification = command[1];
                        if (command.length == 3) path = command[2];
                    } else if (command.length == 2) path = command[1];
                }

                result = terminal.ls(specification, path);
            } else if (command[0].equalsIgnoreCase("touch")) {
                String path;
                if (command.length == 2) {
                    path = command[1];
                    terminal.touch(path);
                } else {
                    System.out.println("Error: No file path provided for touch command.");

                }

            } else if (command[0].equalsIgnoreCase("cd")) {
                if (command.length == 2) {
                    // System.out.println(curren_dir);
                    try {
                        terminal.cd(curren_dir, command[1]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // System.out.println(curren_dir);
                } else {
                    System.out.println("wrong command");
                }

            }
            else if (command[0].equals("rm")) {
                if (command.length < 2) {
                    System.out.println("ERROR:there is not file to remove");
                } else {
                    for (int i = 1; i < command.length; i++) {
                        terminal.rm(command[i]);
                    }
                }
            } else if (command[0].equals("cat")) {
                if (command.length == 1) {
                    result = terminal.cat().toArray(String[]::new);
                } else {
                    String[] filenames = Arrays.copyOfRange(command, 1, command.length);
                    result = terminal.cat(filenames).toArray(String[]::new);
                }

            } else if (command[0].equals("rmdir")) {
                if (command.length < 2) {
                    System.out.println("ERROR:there is not dir to remove");
                } else {
                    for (int i = 1; i < command.length; i++) {
                        terminal.rmdir(command[i]);
                    }
                }
            } else if (command[0].equals("help")) {
                terminal.help();
            } else if (command[0].equalsIgnoreCase("pwd")) {
                System.out.println("Current Directory: " + terminal.pwd());

            } else if (command[0].equalsIgnoreCase("mkdir")) {
                if (command.length > 1) {
                    for (int i = 1; i < command.length; i++) {
                        terminal.mkdir(command[i]);
                    }
                } else {
                    System.out.println("error: directory name isn't provided for mkdir command");
                }
            } else if (command[0].equals("mv")) {
                if (command.length < 3) {
                    System.out.println("error, mv command requires at least two arguments");
                } else {
                    String[] paths = Arrays.copyOfRange(command, 1, command.length);
                    terminal.mv(paths);
                }


            } else {
                System.out.println("wrong command");
            }


            for (String line : result) {
                System.out.println(line);
            }
        }
    }}
