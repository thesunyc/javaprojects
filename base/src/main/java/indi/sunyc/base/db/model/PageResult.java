package indi.sunyc.base.db.model;

import java.util.List;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public class PageResult {

    private Integer pageIndex;
    private Integer pageSize;
    private Long recordCount;
    private Long pageCount;
    private List records;

    public PageResult(PageRequest pageRequest,Long recordCount,List records){
        setPageIndex(pageRequest.getPageIndex());
        setPageSize(pageRequest.getPageSize());
        setRecordCount(recordCount);
        setRecords(records);
        calPageCount();
    }

    private void calPageCount(){
        long divisor = this.getRecordCount() / this.getPageSize();
        long remainder = this.getRecordCount() % this.getPageSize();
        this.setPageCount(remainder == 0 ? divisor == 0 ? 1 : divisor : divisor + 1);
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public List getRecords() {
        return records;
    }

    public void setRecords(List records) {
        this.records = records;
    }
}
