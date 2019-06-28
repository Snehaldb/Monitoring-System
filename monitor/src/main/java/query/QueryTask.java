package query;

import util.QueryRecordUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class QueryTask implements Runnable {

    private String directoryPath;
    private QueryRecordUtil queryRecordUtil;
    private QueryCache queryCache;
    private int startIndex;
    private int endIndex;

    public QueryTask(String directoryPath, QueryRecordUtil queryRecordUtil, QueryCache queryCache, int startIndex, int endIndex) {
        this.directoryPath = directoryPath;
        this.queryRecordUtil = queryRecordUtil;
        this.queryCache = queryCache;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public void run() {
        for (int i = startIndex; i <= endIndex; i++) {
            Map<Integer, List<QueryDataModel>> map = new HashMap<>();
            List<QueryDataModel> queryDataModelsForCpu0 = new ArrayList<>();
            List<QueryDataModel> queryDataModelsForCpu1 = new ArrayList<>();
            String ipAddress = "";
            try {
                Scanner scanner = new Scanner(new File(directoryPath + i + ".txt"));
                while (scanner.hasNextLine()) {

                    String record = scanner.nextLine();
                    String[] tokens = queryRecordUtil.getTokensFromRecord(record);
                    ipAddress = tokens[1];
                    QueryDataModel queryDataModel = queryRecordUtil.createQueryDataModelFromTokens(tokens);
                    if (queryDataModel != null) {
                        if (queryDataModel.getCpuId() == 0) {
                            queryDataModelsForCpu0.add(queryDataModel);
                        } else {
                            queryDataModelsForCpu1.add(queryDataModel);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            map.put(0, queryDataModelsForCpu0);
            map.put(1, queryDataModelsForCpu1);
            queryCache.setQueryMapValues(ipAddress, map);
        }
    }
}
