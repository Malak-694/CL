package org.example;
import java.io.*;
import java.util.*;
public class CommandLineInterpreter {
    public File[] ls(String  specification, String path){
        if(path.isEmpty()) {
            path = System.getProperty("user.dir");
        }
        File currentDirectory = new File(path);
        if(!currentDirectory.exists()){
            System.out.println("current directory not exist");
        } if(!currentDirectory.isDirectory()){
            System.out.println("current path is not directory");
        }
        File[] files =null;
        if(currentDirectory.exists()&&currentDirectory.isDirectory()){
            System.out.println("the content of the current directory: "+currentDirectory);
            files=currentDirectory.listFiles();
            if(files!= null){
                if(specification.equals("-r")){
                    for(int i= files.length-1; i>=0 ;i--) {
                        if(files[i].getName().startsWith(".")) continue;
                        System.out.println(files[i].getName());
                    }
                }
                else{
                    for(File  x: files){
                        if(x.getName().startsWith(".")&&!specification.equals("-a")) continue;
                        System.out.println(x.getName());
                    }
                }

            }
        }
        return files;
    }
}
