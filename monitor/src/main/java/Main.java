import generate.Generate;
import query.Query;
import util.IpAddressUtil;
import util.QueryRecordUtil;
import util.TimeStampUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import static generate.Generate.DATE_TIME_STRING;

public class Main {

    /**  Checks for user input and runs the tool accordingly */
    private static final String GENERATE = "GENERATE";
    private static final String QUERY = "QUERY";
    private static final String EXIT = "EXIT";

    /**  Prints this message if user gives the wrong input */
    private static final String SSH_HELP = "\nTo generate the logs please use the command:" +
            "\n" + "./generate.sh <DIRECTORY_PATH> <Optional Date>" +
            "\n" + "For ex: ./generate.sh /users/foo/bar 2014-10-31" +
            "\n\n" + "To query the logs please use the command:" +
            "\n" + "./query.sh <DIRECTORY_PATH>" +
            "\n" + "For ex: ./query.sh /users/foo/bar";

    private static final String QUERY_HELP = "\n" + "To query the logs please use the command:" + " "
            + QUERY + " <IP> <CPU_ID> <YYYY-MM-DD HH:MM> <YYYY-MM-DD HH:MM>" +
            "\n" + "For ex: " + QUERY + " 192.168.1.10 1 2014-10-31 00:00 2014-10-31 00:05";

    private static final String EXIT_HELP = "\n" + "To exit use the command:" + " EXIT ";

    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            printHelp();
            return;
        }

        final String callType = args[0];
        final TimeStampUtil timeStampUtil = new TimeStampUtil();
        final QueryRecordUtil queryRecordUtil = new QueryRecordUtil(timeStampUtil);

        // If the user enters ./generate.sh and Data path then this if block is executed.
        // If the user enters date along with the Data path then logs from that particular date till 24 hrs are generated .
        // Otherwise logs for default date ie 2014-10-31 are generated.
        if (GENERATE.equals(callType) && args.length >= 2) {
            Generate generate = new Generate();
            String dir = args[1];
            if (args.length > 2) {
                String date = args[2];
                generate.generate(dir, date + " " + "00:00");
            } else {
                System.out.println("No date provided, using default date: " + DATE_TIME_STRING);
                generate.generate(dir, DATE_TIME_STRING);
            }

            // If the user enter's QUERY command then this block is executed.
        } else if (QUERY.equals(callType)) {
            final IpAddressUtil ipAddressUtil = new IpAddressUtil();
            final Query query = new Query(queryRecordUtil, timeStampUtil);
            final String directoryPath = args[1];
            final File directory = new File(directoryPath);
            try {
                if (directory.exists()) {
                    String canonicalPath = directory.getCanonicalPath();
                    query.readFilesAndBuildQueryCache(canonicalPath + "/");
                } else {
                    System.out.println("Directory not found, please enter valid directory path... ");
                    return;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(QUERY_HELP);
            System.out.println(EXIT_HELP);

            // Continue the Query until the user inputs Exit
            while (true) {
                System.out.print(">");
                String input = bufferedReader.readLine();
                String[] tokensFromQuery = queryRecordUtil.getTokensFromQuery(input);
                if (tokensFromQuery == null || tokensFromQuery.length == 0) continue;
                if (QUERY.equals(tokensFromQuery[0])) {
                    if (tokensFromQuery.length < 7) {
                        System.out.println(QUERY_HELP);
                        continue;
                    }

                    String ipAddress = tokensFromQuery[1];
                    if (!ipAddressUtil.isIPValid(ipAddress)) {
                        printIpError();
                        continue;
                    }

                    int cpuId = Integer.valueOf(tokensFromQuery[2]);
                    if (cpuId != 0 && cpuId != 1) {
                        printCpuIdError();
                        continue;
                    }

                    long startTimeStamp = -1;
                    long endTimeStamp = -1;
                    long initTimeStamp = -1;
                    try {
                        startTimeStamp = timeStampUtil.createTimeStampFromString(tokensFromQuery[3] + " " + tokensFromQuery[4]);
                        endTimeStamp = timeStampUtil.createTimeStampFromString(tokensFromQuery[5] + " " + tokensFromQuery[6]);
                        initTimeStamp = timeStampUtil.createTimeStampFromString(tokensFromQuery[3] + " " + "00:00");

                        if (endTimeStamp < startTimeStamp) {
                            printDateError();
                            continue;
                        }
                    } catch (ParseException e) {
                        printDateError();
                    }

                    if (startTimeStamp == -1 || endTimeStamp == -1 || initTimeStamp == -1) {
                        printDateError();
                        return;
                    }

                    query.queryData(ipAddress, cpuId, startTimeStamp, endTimeStamp, initTimeStamp);

                    // Exits the tool if user gives EXIT command
                } else if (EXIT.equals(tokensFromQuery[0])) {
                    return;
                }
            }
        } else {
            printHelp();
        }

    }

    /* Prints error methods */
    private static void printIpError() {
        System.out.println("Please enter valid IP Address...");
    }

    private static void printDateError() {
        System.out.println("Please verify the start/end date...");
    }

    private static void printCpuIdError() {
        System.out.println("Please enter 0/1 as Cpu Id...");
    }

    private static void printHelp() {
        System.out.println(SSH_HELP);
    }
}
