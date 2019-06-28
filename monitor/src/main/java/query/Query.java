package query;

import util.QueryRecordUtil;
import util.TimeStampUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Query {

    private TimeStampUtil timeStampUtil;
    private QueryRecordUtil queryRecordUtil;
    private QueryCache queryCache;

    public Query(QueryRecordUtil queryRecordUtil, TimeStampUtil timeStampUtil) {
        this.timeStampUtil = timeStampUtil;
        this.queryRecordUtil = queryRecordUtil;
        queryCache = new QueryCache();
    }

    /**
     * This method read's file and build the query cache which uses Map that keeps ipaddress and its related data
     * @param directoryPath the path os directory
     */
    public void readFilesAndBuildQueryCache(String directoryPath) {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        for (int i = 1, j = 0; i <= 10; j = j + 100, i++) {
            QueryTask task = new QueryTask(directoryPath, queryRecordUtil, queryCache, j, j + 99);
            executor.execute(task);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
    }

    /**
     * This method outputs the data requested by the user for the given Query
     * @param ipAddress is user specified ip
     * @param cpuId is user specified Cpu Id
     * @param startTimeStamp is user specified state timestamp
     * @param endTimeStamp is user specified end timestamp
     * @param initTimeStamp is the start timestamp
     */
    public void queryData(String ipAddress, int cpuId, long startTimeStamp, long endTimeStamp, long initTimeStamp) {
        Instant start = Instant.now();

        String startDate = timeStampUtil.timeStampToString(startTimeStamp);
        String endDate = timeStampUtil.timeStampToString(endTimeStamp);

        List<QueryDataModel> list = queryCache.getQueryMap().get(ipAddress).get(cpuId);
        int endIndex = (int) ((endTimeStamp / 60) - (initTimeStamp / 60));
        int startIndex = (int) ((startTimeStamp / 60) - (initTimeStamp / 60));

        if (startIndex < 0 || endIndex > 1439) {
            System.out.println("Start and End Date should be within 24 hours range...");
            return;
        }

        QueryDataModel startQueryModel = list.get(startIndex);
        QueryDataModel endQueryModel = list.get(endIndex);
        if (!startDate.equals(startQueryModel.getDate()) && !(endDate.equals(endQueryModel.getDate()))) {
            System.out.println("No record for start date: " + startDate + " and end date: " + endDate);
            return;
        }

        System.out.println("CPU" + cpuId + " usage on " + ipAddress + ":");
        System.out.println(list.subList(startIndex, endIndex + 1));
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Query Time: " + timeElapsed.toMillis() + " milliseconds");
    }
}
