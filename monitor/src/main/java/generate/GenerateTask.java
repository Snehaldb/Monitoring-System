package generate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class GenerateTask implements Runnable {

    private String directoryPath;
    private List<Long> timeStamps;
    private String[] ipAddresses;
    private int startIndex;
    private int endIndex;

    /**
     * This method generates the log file for each server and for each Cpu id
     * @param directoryPath the directory path in which the log files are generated
     * @param timestamps for one day ie. total 1440
     * @param ipAddresses for each server. ie. total 1000
     * @param startIndex for start of ip address array ie from 0
     * @param endIndex for end of ip address array ie till 99
     */
    public GenerateTask(String directoryPath, List<Long> timestamps, String[] ipAddresses, int startIndex, int endIndex) {
        this.directoryPath = directoryPath;
        this.timeStamps = timestamps;
        this.ipAddresses = ipAddresses;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }


    public void run() {
        for (int i = startIndex; i <= endIndex; i++) {
            String ipAddress = ipAddresses[i];
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(directoryPath + i + ".txt"));
                for (long timeStamp : timeStamps) {
                    bufferedWriter.write(timeStamp + "," + ipAddress + ",0," + new Random().nextInt(101) + "\n");
                    bufferedWriter.write(timeStamp + "," + ipAddress + ",1," + new Random().nextInt(101) + "\n");
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
