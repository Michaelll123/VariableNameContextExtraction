import utils.CSVUtils;
import utils.FileHelper;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class preprcess {
    public static void main(String [] args) throws IOException {
        String s = "C:\\Users\\25359\\Desktop\\fsdownload\\context.txt";
        ArrayList<String> lines = FileHelper.readFileByLines(s);
        String csvFile = "C:\\Users\\25359\\Desktop\\fsdownload\\context.csv";
//        String resultFile = "C:\\Users\\25359\\Desktop\\fsdownload\\name.csv";
        FileWriter writer = new FileWriter(csvFile);
//        FileWriter writer1 = new FileWriter(resultFile);
        for(String line:lines){
            String [] splitArray = line.split("###");
            String firstColumn = splitArray[0];
            String secondColumn = splitArray[1];
            String thirdColumn = splitArray[2];
            CSVUtils.writeLine(writer, Arrays.asList(firstColumn,secondColumn,thirdColumn),'#');
        }
    }
}
