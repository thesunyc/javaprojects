package indi.sunyc.base.jsonrpc.model;

import indi.sunyc.base.common.model.Result;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public class JsonRpcResult extends Result {

    private String id;
    private String jsonrpc = "2.0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }
}
