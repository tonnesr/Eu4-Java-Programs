/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu4positions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 *
 * @author Tonnes
 */
public class Main {
   
    String positionsString;
    String positionsLineString;
    String oldPosLineString;
    String oldPosString;
    String provinceFileString;
    String provinceFileLineString;
    
    String countryTag = "AHM";
    String fileName;
    
    boolean isComment = false;
    
    int counter = 0;
    int indexPosition;
    int index;
    
    String tab = String.format("%c", '\t');
    
    String[] splitNumbers;
    String[] newPositions;
    String[] newPositions1d;
    String[] oldPositionsFile;
    String[][] newPositions2d;
    
    String[] files;
    String[] provinceFileArray;
    File[] foundFiles;
    
    BufferedWriter buffWriter;
    
    File positionsFile;
    File file;
    File provinceFile;
    File directory;
    File filePath;    
    File saveFile;
    
    FileWriter writer;
    BufferedReader buffReader;

    static Main main;
    
    public Main() {
        file = Paths.get("C:\\Users\\Tonnes\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\mapMod3\\map\\positions.txt").toFile();
        theProcess();
    }
    
    public void theProcess() {
        getNewPositions();
        createArrayofOldPositions();
        convert2dArrayTo1d(convertTo2dArray(newPositions));
        while (counter < newPositions.length) {
            index = getIndexForOldPositions(oldPositionsFile ,newPositions1d);
            
            System.out.println(newPositions1d[counter] + "\nbefore:" + oldPositionsFile[index+2]);
            
            oldPositionsFile[index+2] = 
                    tab + tab + newPositions2d[counter][1] + " " + newPositions2d[counter][2]
                    + " " + newPositions2d[counter][1] + " " + newPositions2d[counter][2]
                    + " " + newPositions2d[counter][1] + " " + newPositions2d[counter][2]
                    + " " + newPositions2d[counter][1] + " " + newPositions2d[counter][2]
                    + " " + newPositions2d[counter][1] + " " + newPositions2d[counter][2]
                    + " " + newPositions2d[counter][1] + " " + newPositions2d[counter][2]
                    + " " + newPositions2d[counter][1] + " " + newPositions2d[counter][2]
            ;
            
            System.out.println("after:" + oldPositionsFile[index+2]);
            
            addProvincesToCountry(createArrayFromFile(getOneFilePath(newPositions1d[counter])));
            
            counter++;
        }
        writeToFile(oldPositionsFile, file);
    }
    
    public void writeToFile(String[] newFileArray, File aFile) {
        try {
            buffWriter = new BufferedWriter(writer = new FileWriter(aFile));
            for (int i = 0; i < newFileArray.length; i++) {               
                buffWriter.write(newFileArray[i]);   
                buffWriter.newLine();
            }
            buffWriter.flush();
            buffWriter.close();  
        } catch (IOException e) {
            System.out.println(e);
        }       
    }
    
    public String[] getNewPositions() {
        positionsString = "";
        positionsLineString = "";
        try {
            buffReader = new BufferedReader(new FileReader("positions\\positions.txt")); //new positions
            
            while((positionsLineString = buffReader.readLine()) != null) {
                if (!positionsLineString.contains("#")) {
                    positionsString += positionsLineString + "\r\n";
                } else {
                    positionsString = "";
                }
            }
            
            buffReader.close();
            
            newPositions = positionsString.split("\\r?\\n");
            
        } catch (IOException e) {
            System.out.println(e);
        }
        return newPositions;
    }
    
    public int getIndexForOldPositions(String[] oldPositions, String[] newPositions) {       
        //System.out.println("New Pos: " + newPositions[counter]);
        indexPosition = Arrays.asList(oldPositions).indexOf(newPositions[counter] + "={");
        //System.out.println("Index Pos: " + indexPosition);        
        return indexPosition;
    }
    
    public String[][] convertTo2dArray(String[] newPositions) {
        splitNumbers = new String[3];
        newPositions2d = new String[newPositions.length][];
        
        for (int i = 0; i < newPositions.length; i++) {
            newPositions[i] = newPositions[i].replace("null", "");
            splitNumbers = newPositions[i].split(" ");           
            //System.out.println(Arrays.toString(splitNumbers));            
            newPositions2d[i] = splitNumbers;
        }
        
        return newPositions2d;
    }
    
    public String[] convert2dArrayTo1d(String[][] newPositions2d) {        
        newPositions1d = new String[newPositions2d.length];
        
        for (int i = 0; i < newPositions2d.length; i++) {
            for (int j = 0; j < newPositions2d[i].length; j++) {
                newPositions1d[i] = newPositions2d[i][0];
            }
        }
        
        return newPositions1d;
    }
    
    public String[] createArrayofOldPositions() {
        oldPosString = "";
        oldPosLineString = "";
        try {
            buffReader = new BufferedReader(new FileReader(file));
            
            while((oldPosLineString = buffReader.readLine()) != null) {
                oldPosString += oldPosLineString + "\r\n";
            }
            buffReader.close();
            
            oldPositionsFile = oldPosString.split("\\r?\\n");
        
        } catch (IOException e) {
            System.out.println(e);
        }
        
        return oldPositionsFile;
    }
    
    public void addProvincesToCountry(String[] provinceFileArrayBanjoo) {                                
        System.out.println("Counter: " + counter);
        
        System.out.println("Lenght: " + provinceFileArrayBanjoo.length);
        
        provinceFileArrayBanjoo[1] = "";
        provinceFileArrayBanjoo[2] = "add_core = " + countryTag;
        provinceFileArrayBanjoo[3] = "owner = " + countryTag;
        provinceFileArrayBanjoo[4] = "controller = " + countryTag;
        
        writeToFile(provinceFileArrayBanjoo, saveFile);
    }

    public File getOneFilePath(String index) {        
        directory = new File("C:\\Users\\Tonnes\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\mapMod3\\history\\provinces");
        foundFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(index + " -");
            }
        });
        
        filePath = foundFiles[0];

        return filePath;
    }

    public String[] createArrayFromFile(File anotherFile) {
        saveFile = anotherFile;
        
        fileName = saveFile.getName().toLowerCase();  
        
        provinceFileString = "";
        provinceFileLineString = "";
        
        try {
            buffReader = new BufferedReader(new FileReader(saveFile));
            
            while((provinceFileLineString = buffReader.readLine()) != null) {
                provinceFileString += provinceFileLineString + "\r\n";
            }
            buffReader.close();
            
            provinceFileArray = provinceFileString.split("\\r?\\n");
            
            if (((fileName.contains("sea") || fileName.contains("coast") || fileName.contains("strait") || fileName.contains("lake") || fileName.contains("basin")) && provinceFileArray.length < 3) || provinceFileArray.length < 3) {
                provinceFileArray = new String[5];
                
                System.out.println("Sea province?");
            }
            
            provinceFileArray[0] = "# " + filePath.getName().replace(".txt", "");
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
        return provinceFileArray;
    }
    
    public static void main(String[] args) {
        main = new Main();
    }
}
