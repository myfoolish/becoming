package com.xw.util.client.exception;

import com.xw.util.client.inter.IExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/21
 */
public enum DefaultExceptionHandler implements IExceptionHandler {
    INSTANCE;

    private Logger logger = Logger.getLogger("RedisException");

    private DefaultExceptionHandler() {
    }

    public Object dealException(RedisException ex) {
        PrintWriter pw = null;

        try {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            this.logger.warning(sw.toString());
        } finally {
            if (pw != null) {
                pw.close();
            }

        }

        return null;
    }

    public Object dealConnectionException(RedisException ex) {
        return this.dealException(ex);
    }
}
