package cn.com.cnpc.cpoa.core.exception;

/**
 * 应用异常
 *
 * @author scchenyong@189.cn
 * @create 2018-12-24
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    public AppException(String message) {
        super();
        this.message = message;
    }

    /**
     * @return message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
