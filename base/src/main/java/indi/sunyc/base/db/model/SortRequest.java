package indi.sunyc.base.db.model;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public class SortRequest {

    public static enum DIRECT {
        ASC("asc"),DESC("desc");
        private String value;
        DIRECT(String value){this.value = value;}
        public String getValue(){return this.value;}
    }

    private String sortKey;
    private String sortDirection;

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
