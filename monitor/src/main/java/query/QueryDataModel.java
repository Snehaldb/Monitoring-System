package query;

import java.util.Objects;

public class QueryDataModel {
    private long timeStamp;
    private String date;
    private int cpuId;
    private int cpuUsage;

    public QueryDataModel(String date, long timeStamp, int cpuId, int cpuUsage) {
        this.date = date;
        this.timeStamp = timeStamp;
        this.cpuId = cpuId;
        this.cpuUsage = cpuUsage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getDate() {
        return date;
    }

    public int getCpuId() {
        return cpuId;
    }

    public int getCpuUsage() {
        return cpuUsage;
    }

    @Override
    public String toString() {
        return "(" + date + ',' + cpuUsage + "%" + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryDataModel that = (QueryDataModel) o;
        return timeStamp == that.timeStamp &&
                cpuId == that.cpuId &&
                cpuUsage == that.cpuUsage &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp, date, cpuId, cpuUsage);
    }
}
