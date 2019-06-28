package generate;

import util.IpAddressUtil;
import util.TimeStampUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Generate {

    public static final String DATE_TIME_STRING = "2014-10-31 00:00";

    /**
     * This method creates the directory or otherwise returns error if any
     * @param directoryPath the directory path in which the log files are generated
     * @param date The start date
     */
    public void generate(String directoryPath, String date) {
        final String canonicalPath = createDirectoryOrReturnError(directoryPath);
        if (canonicalPath == null) return;
        generateLogs(canonicalPath + "/", date);
    }


    /**
     * This method is used to generate logs for one day
     * @param directoryPath the directory path in which the log files are generated
     * @param date The start date
     */
    public void generateLogs(String directoryPath, String date) {
        try {
            System.out.println("Generating logs...");
            TimeStampUtil timeStampUtil = new TimeStampUtil();
            String[] ipForServers = new IpAddressUtil().generateIPForServers(10, 100);
            List<Long> timeStampsForFullDay = timeStampUtil.createTimeStampsEveryMin(date, 1440);

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

            for (int i = 1, j = 0; i <= 10; j = j + 100, i++) {
                GenerateTask task = new GenerateTask(directoryPath, timeStampsForFullDay, ipForServers, j, j + 99);
                executor.execute(task);
            }

            executor.shutdown();

            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                System.out.println("Logs generated...");
            } catch (InterruptedException e) {
            }

        } catch (ParseException e) {
            System.out.println("Unable to create time stamps, please check date format is valid... ");
        } catch (IpAddressUtil.InvalidInputException e) {
            System.out.println("Unable to create IP Addresses for servers, please try again... ");
        }
    }

    /* This method creates directory otherwise returns error */
    private String createDirectoryOrReturnError(String directoryPath) {
        final File directory = new File(directoryPath);
        final boolean isCreated = directory.mkdirs();
        String canonicalPath = null;

        try {
            if (isCreated) {
                canonicalPath = directory.getCanonicalPath();
                System.out.println("Successfully created directory: " + canonicalPath);
            } else if (directory.exists()) {
                canonicalPath = directory.getCanonicalPath();
                System.out.println("Directory path already exist, overwriting logs...");
            } else {
                System.out.println("Unable to create directory, please verify directory path...");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return canonicalPath;
    }

}
