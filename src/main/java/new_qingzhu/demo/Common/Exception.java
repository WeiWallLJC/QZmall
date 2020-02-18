package new_qingzhu.demo.Common;

public class Exception extends RuntimeException {

    public Exception() {
    }

    public Exception(String message) {
        super(message);
    }


    public static void fail(String message) {
        throw new Exception(message);
    }

}
