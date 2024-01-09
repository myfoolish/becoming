package com.xw.util.client.exception;

public class RedisAccessException extends RedisException {
    private String connectionInfo;

    public RedisAccessException(Throwable cause) {
        super("没有写入权限\n", cause);
    }

    public String getConnectionUri() {
        return this.connectionInfo;
    }

    public void setConnectionInfo(String connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public String toString() {
        return this.getConnectionUri() + "\n" + super.toString();
    }
}
