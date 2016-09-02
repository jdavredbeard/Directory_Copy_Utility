package com.company;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        while (true) {
            if (enterDirectories() == -1) {
                enterDirectories();
            }
            else {
                break;
            }
        }
    }

    public static int enterDirectories() throws Exception {
        Scanner kb = new Scanner(System.in);
        File[] initialPaths = null;
        String source, destination = null;

        System.out.print("Enter the directory to be copied: ");
        source = kb.nextLine();
        initialPaths = readDirectoryList(source);

        if (initialPaths == null) {
            return -1;
        }

        System.out.print("Enter the destination directory: ");
        destination = kb.nextLine();


        if (readDirectoryList(destination) == null) {
            return -1;
        }

        System.out.println("Attempting to copy...");
        File sourceDir = new File(source);
        File[] sourceDirArray = new File[1];
        sourceDirArray[0] = sourceDir;
        copyFilesAndDirs(sourceDirArray, destination);
        return 1;
    }

    public static File[] readDirectoryList(String directoryName) {
        File[] entries = null;      // an array for the entries in the target directory

        // Create a File class object linked to the target
        File target = new File(directoryName);

        // echo target name and skip a line
        System.out.println("You selected " + directoryName + "\n");

        // if the identified item exists
        if (target.exists()) {

            // echo target and show absolute path
            System.out.println(directoryName + " exists.");
            System.out.println("Directory of " + target.getAbsolutePath());

            // if the target is a directory
            if (target.isDirectory()) {

                // get the data and load the array
                entries = target.listFiles();

                // for each name in the path array
                // (using an alternate  for stament)
                for (File entry : entries) {

                    // print the file or directory name
                    System.out.printf("%-32S", entry.getName());

                    // print the file size, or label if it is a directory
                    // allowing 32 spaces for the name(left justified)
                    if (entry.isFile())
                        System.out.printf("%,d \n", entry.length());
                    else
                        System.out.println("<DIR>");

                } // end for

            } // end if ( target.isDirectory() )

            else if (target.isFile()) {
                System.out.println("You have indentified a file at:");
                System.out.println(target.getAbsolutePath());
            } // end if ( target.isDirectory() )
        } // end if ( target.exists() )

        else {
            System.out.println("The item you asked about - " + target + " - does not exist.");
        }
        // add a next line at the end of the printout
        System.out.println();
        return entries;
    }

    public static void copyFilesAndDirs(File[] sourcePaths, String destination) throws Exception{

        //the main event
        for (int i = 0; i <= sourcePaths.length - 1; i++) {
            //create a new File object pointing at the destination path
            File destPath = new File(destination + "/" + sourcePaths[i].getName());
            //create String variables holding the absolute file paths of the source and destination
            String absSourceString = sourcePaths[i].getAbsolutePath();
            String absDestString = destPath.getAbsolutePath();
            //if the source path is a directory, copy the directory and recursively call CopyFilesAndDirs with the paths from the copied directory
            if (sourcePaths[i].isDirectory()) {

                System.out.println("Beginning folder copy:");
                System.out.println("\tcopying " + absSourceString);
                System.out.println("\tto      " + absDestString);
                destPath.mkdir();
                File[] newPaths = sourcePaths[i].listFiles();
                copyFilesAndDirs(newPaths, destPath.getAbsolutePath());
            }
            //if the path leads to a File, call the copyFile method
            else if (sourcePaths[i].isFile()) {
                copyFile(sourcePaths[i].getAbsolutePath(), destPath.getAbsolutePath());
            }
            else {
                System.out.println("This path does not lead to a directory or a file and will not be copied: " + sourcePaths[i].getAbsolutePath());
            }

        }
    }


    public static void copyFile(String source, String destination) throws Exception {

        // declare File
        File sourceFile = null;
        File destFile = null;

        // declare stream variables
        FileInputStream sourceStream = null;
        FileOutputStream destStream = null;

        // declare buffering variables
        BufferedInputStream bufferedSource = null;
        BufferedOutputStream bufferedDestination = null;

        try {

            // Create file objects for the source and destination files
            sourceFile = new File(source);
            destFile = new File(destination);

            // create file streams for the source and destination files
            sourceStream = new FileInputStream(sourceFile);
            destStream = new FileOutputStream(destFile);

            // buffer the file streams -- set the buffer sizes to 8K
            bufferedSource = new BufferedInputStream(sourceStream, 8192);
            bufferedDestination = new BufferedOutputStream(destStream, 8192);

            // use an integer to transfer data between files
            int transfer;

            // tell the user what is happening
            System.out.println("Beginning file copy:");
            System.out.println("\tcopying " + source);
            System.out.println("\tto      " + destination);

            // read a byte, checking for end of file (-1 is returned by read at EOF)
            while ((transfer = bufferedSource.read()) != -1) {

                // write a byte
                bufferedDestination.write(transfer);


            } // end while

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println(" An unexpected I/O error occurred.");

        } finally {

            // close file streams
            if (bufferedSource != null)
                bufferedSource.close();

            if (bufferedDestination != null)
                bufferedDestination.close();

            System.out.println("Files closed. Copy complete.");

        } // end finally

    } // end copyFile
}
