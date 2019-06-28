package query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueryCache {
    private Map<String, Map<Integer, List<QueryDataModel>>> queryMap;

    public QueryCache() {
        queryMap = new ConcurrentHashMap<>();
    }

    public synchronized void setQueryMapValues(String key, Map<Integer, List<QueryDataModel>> value) {
        queryMap.put(key, value);
    }

    public Map<String, Map<Integer, List<QueryDataModel>>> getQueryMap() {
        return queryMap;
    }
}
