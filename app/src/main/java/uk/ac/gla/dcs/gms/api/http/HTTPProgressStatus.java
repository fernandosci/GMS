package uk.ac.gla.dcs.gms.api.http;


public class HTTPProgressStatus {
    private int processed;
    private int failed;
    private int total;

    public HTTPProgressStatus(int total) {
        if (total == 0)
            throw new IllegalArgumentException("Total must be greater than zero");
        this.processed = 0;
        this.failed = 0;
        this.total = total;
    }

    public HTTPProgressStatus(HTTPProgressStatus o) {
        this.processed = o.processed;
        this.failed = o.failed;
        this.total = o.total;
    }

    public int getPercent()
    {
        return (int) ((processed / (float) total) * 100);
    }

    public int getProcessed() {
        return processed;
    }

    public int getFailed() {
        return failed;
    }

    public int getTotal() {
        return total;
    }

    public void incFailed(){
        failed++;
        processed++;
    }

    public void incProcessed(){
        processed++;
    }



}
