package org.example;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class CommandLineInterpreter {
    public void cd(String current_dir, String argu) throws IOException {
        String Dircectory = "";
        if (argu.equals("..")) {
            int index = current_dir.lastIndexOf('\\');
            if (index > 0) {
                Dircectory = current_dir.substring(0, index);

            } else {
                System.out.println("there is something wrong");
            }
            Main.curren_dir = Dircectory;
        } else {
            if (argu.contains("/")) {
                argu = argu.replace('/', '\\');
            }
            Dircectory = current_dir + '\\' + argu;
            File dir = new File(Dircectory);
            if (dir.isDirectory()) {
                Main.curren_dir = Dircectory;
            } else {
                System.out.println("NO dirctoy with this name");
            }

        }

    }


    public void appendRedirect(String s) {
        String[] splited_command = s.split(" ");
        String[] result = new String[0];
        boolean flag = false;
        if (splited_command.length < 3) {
            System.out.println("write : <command> >> <file>");

        } else if (splited_command[0].equals("cat")) {
            System.out.println("write : <command> >> <file>");
        } else if (splited_command[0].equals("ls")) {
            result = ls("", "");
        } else if (splited_command[0].equals("echo")) {
            if (splited_command.length != 4) {
                flag = true;
            }
            String text = splited_command[1];
            if (!text.equals(">>") && splited_command[2].equals(">>")) {
                result = new String[]{text};
            } else {
                System.out.println("write : echo text >> <file>");
            }

        }
        if (result.length > 0) {
            File f = new File(splited_command[splited_command.length - 1]);
            if (!f.exists()) {
                if (flag == true) {
                    System.out.println("write : echo text >> <file>");
                } else {
                    System.out.println("No file with this name exists");
                }
            } else {
                FileWriter fw = null;
                try {
                    fw = new FileWriter(splited_command[splited_command.length - 1], true);

                    for (String string : result) {
                        fw.write(string);
                        fw.write('\n');
                    }
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("wrong command");
        }
    }

    public void redirect(String s) {
        String[] splited_command = s.split(" ");
        String[] result = new String[0];
        boolean flag = false;
        if (splited_command.length < 3) {
            System.out.println("write : <command> > <file>");

        } else if (splited_command[0].equals("cat")) {
            System.out.println("write : <command> > <file>");
        } else if (splited_command[0].equals("ls")) {
            result = ls("", "");
        } else if (splited_command[0].equals("echo")) {
            if (splited_command.length != 4) {
                flag = true;
            }
            String text = splited_command[1];
            if (!text.equals(">>") && splited_command[2].equals(">")) {
                result = new String[]{text};
            } else {
                System.out.println("write : echo text > <file>");
            }

        }
        if (result.length > 0) {
            File f = new File(splited_command[splited_command.length - 1]);
            if (flag == true) {
                System.out.println("write : echo text >> <file>");
            } else {
                FileWriter fw = null;
                try {
                    fw = new FileWriter(splited_command[splited_command.length - 1], true);

                    for (String string : result) {
                        fw.write(string);
                        fw.write('\n');
                    }
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("wrong command");
        }
    }

    public String[] sort(String name) {
        String[] conntent = ls("", name);
        Arrays.sort(conntent);
        return conntent;
    }

    public String[] uniq(String[] arr) {
        String[] uniq_array = Arrays.stream(arr)
                .distinct()
                .toArray(String[]::new);

        return uniq_array;
    }

    public String[] grep(String[] arr, String key) {
        List<String> result = new ArrayList<>();
        for (String elemnt : arr) {
            if (elemnt.contains(key)) {
                result.add(elemnt);
            }
        }
        return result.toArray(new String[0]);
    }


    public String[] ls(String specification, String path) {
        if (path.isEmpty()) {
            path = Main.curren_dir;
        }
        File currentDirectory = new File(path);
        if (!currentDirectory.exists()) {
            System.out.println("current directory not exist");
        }
        if (!currentDirectory.isDirectory()) {
            System.out.println("current path is not directory");
        }
        File[] files = null;
        List<String> visibleFiles = new ArrayList<>();
        if (currentDirectory.exists() && currentDirectory.isDirectory()) {
            //System.out.println("the content of the current directory: "+currentDirectory);
            files = currentDirectory.listFiles();
            if (files != null) {
                if (specification.equals("-r")) {
                    for (int i = files.length - 1; i >= 0; i--) {
                        if (files[i].getName().startsWith(".")) continue;
                        // System.out.println(files[i].getName());
                        visibleFiles.add(files[i].getName());
                    }
                } else {
                    for (File x : files) {
                        if (x.getName().startsWith(".") && !specification.equals("-a")) continue;
                        //  System.out.println(x.getName());
                        visibleFiles.add(x.getName());
                    }
                }

            }
        }
        return visibleFiles.toArray(new String[0]);
    }

    public String touch(String path) {
        File file;
        if (Paths.get(path).getParent() == null) {
            file = new File(Main.curren_dir, path);
        } else {
            file = new File(path);
        }
        try {
            Path parentPath = file.toPath().getParent();
            if (parentPath != null || Files.notExists(parentPath)) {
                Files.createDirectories(parentPath);
                //System.out.println("Parent directory exists: " + parentPath);
            }
            if (file.exists()) {
                file.setLastModified(System.currentTimeMillis());
                System.out.println("file exists");
            } else {
                Files.createFile(file.toPath());
                System.out.println("file created");
            }

        } catch (IOException x) {
            System.out.println("error happening while creating new file " + x);
        }
        return file.getName();
    }

    //////////////////////////
    public void rm(String... filenames) {
        for (String filename : filenames) {
            File file = new File(filename);
            if (!file.isAbsolute()) {
                file = new File(Main.curren_dir, filename);
            }

            if (file.exists()) {
                if (file.isDirectory()) {
                    System.out.println("Cannot remove '" + filename + "': Is a directory");
                } else if (file.isFile()) {
                    if (file.delete()) {
                        System.out.println("Deleted file: " + filename);
                    } else {
                        System.out.println("Failed to delete file: " + filename);
                    }
                }
            } else {
                System.out.println("File not found or not a file: " + filename);
            }
        }
    }

    public List<String> cat(String... filenames) {
        List<String> content = new ArrayList<>();

        if (filenames.length == 0) {
            System.out.println("Enter text... (type 'done' to finish the input process):");
            Scanner scanner = new Scanner(System.in);
            try {
                String line;
                while (scanner.hasNextLine() && !(line = scanner.nextLine()).equalsIgnoreCase("done")) {
                    System.out.println(line);
                    content.add(line);
                }
            } catch (NoSuchElementException e) {
                System.out.println("There is no line founded,input process ended");
            }
        } else {
            for (String filename : filenames) {
                File file = new File(filename);
                if (!file.isAbsolute()) {
                    file = new File(Main.curren_dir, filename);
                }

                if (file.exists() && file.isFile()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                            content.add(line);
                        }
                    } catch (IOException e) {
                        System.out.println("Error reading file");
                    }
                } else {
                    System.out.println("File not found or it is not a file");
                }
            }
        }
        return content;
    }

    public void rmdir(String... dirs) {
        for (String dirPath : dirs) {

            File dir = new File(dirPath);
            if (!dir.isAbsolute()) {
                dir = new File(Main.curren_dir, dirPath);
            }

            if (dir.exists() && dir.isDirectory()) {
                File[] contents = dir.listFiles();
                if (contents != null && contents.length == 0) {
                    if (dir.delete()) {
                        System.out.println("Deleted directory: " + dir.getPath());
                    } else {
                        System.out.println("Failed to delete directory: " + dir.getPath());
                    }
                } else {
                    System.out.println("Directory is not empty or cannot be deleted: " + dir.getPath());
                }
            } else {
                System.out.println("Directory not found or not a directory: " + dir.getPath());
            }
        }
    }


    public void help() {
        System.out.println("Available commands & Their usage:");
        System.out.println("  pwd             -> Prints the working directory");
        System.out.println("  cd <dir>        -> Changes the current directory to <dir>");
        System.out.println("  ls              -> List files in the current or specified directory");
        System.out.println("  ls -a           -> Display all contents, even entries starting with .");
        System.out.println("  ls -r           -> List contents in reverse order");
        System.out.println("  mkdir <dir>     -> Create a new directory named <dir>");
        System.out.println("  rmdir <dir>     -> Remove an empty directory named <dir>");
        System.out.println("  touch <file>    -> Create a new file or update the timestamp of <file>");
        System.out.println("  mv <src> <dest> -> Move or rename a file or directory from <src> to <dest>");
        System.out.println("  rm <file>       -> Remove one or more files");
        System.out.println("  cat <file>      -> Display the contents of <file>");
        System.out.println("  > <file>        -> Redirect output to <file> (overwrites)");
        System.out.println("  >> <file>       -> Append output to <file>");
        System.out.println("  | <command>     -> Pipe the output of one command to another");
        System.out.println("  help            -> Show available commands and usage");
        System.out.println("  exit            -> Exit the CLI");
    }

}

