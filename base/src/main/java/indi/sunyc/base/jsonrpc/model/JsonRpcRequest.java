package indi.sunyc.base.jsonrpc.model;

import java.util.Map;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public class JsonRpcRequest {

    private String id;
    private String jsonrpc = "2.0";
    private String method;
    private Map<String,Object> params;

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
