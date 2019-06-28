package util;

import query.QueryDataModel;

public class QueryRecordUtil {

    TimeStampUtil timeStampUtil;

    public QueryRecordUtil(TimeStampUtil timeStampUtil) {
        this.timeStampUtil = timeStampUtil;
    }

    public String[] getTokensFromRecord(String record) {
        if (record == null || record.length() == 0) return new String[0];
        return record.split(",");
    }

    public QueryDataModel createQueryDataModelFromTokens(String record) {
        String[] tokens = getTokensFromRecord(record);
        return createQueryDataModelFromTokens(tokens);
    }

    public QueryDataModel createQueryDataModelFromTokens(String[] tokens) {
        if (tokens.length == 0 && tokens.length < 4) return null;
        final long timeStamp = Long.valueOf(tokens[0]);
        final String date = timeStampUtil.timeStampToString(timeStamp);
        final int cpuId = Integer.valueOf(tokens[2]);
        final int cpuUsage = Integer.valueOf(tokens[3]);
        return new QueryDataModel(date, timeStamp, cpuId, cpuUsage);
    }

    public String[] getTokensFromQuery(String query) {
        if (query == null || query.length() == 0) return new String[0];
        return query.split(" ");
    }
}
