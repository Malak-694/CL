package org.example;
import java.io.*;
import java.util.*;
import java.nio.file.*;

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
    public File touch(String path){
        File file = new File(path);
    try {
        //get the path of the parent as string
        String parentPathString= file.getParent();
        if (parentPathString != null) {
            Path parentPath = Paths.get(parentPathString);
            if (!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
                System.out.println("Created directories: " + parentPath);
            } else {
                System.out.println("Parent directories already exist: " + parentPath);
            }
        }
        if(file.exists()){
            file.setLastModified(System.currentTimeMillis());
            System.out.println("file exist");
        }else{
            if(file.createNewFile()) {
                System.out.println("file created");
            }
        }

    }catch(IOException x){
        System.out.println("error happening while creating new file "+ x);
    }
        return file;}
}