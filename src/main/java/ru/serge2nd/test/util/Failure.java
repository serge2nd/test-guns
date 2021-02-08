package ru.serge2nd.test.util;

import static java.lang.String.join;
import static java.util.Collections.nCopies;
import static ru.serge2nd.test.Env.EOL;

@SuppressWarnings("StringBufferReplaceableByString")
public class Failure {
    private final int idx;
    private final Throwable error;
    public Failure(int idx, Throwable error) { this.idx = idx; this.error = error; }

    public Integer   getIdx()      { return idx; }
    public Throwable getError()    { return error; }
    public String    getTypeName() { return error.getClass().getName(); }
    public String    getMessage()  { return error.getMessage(); }

    @Override
    public String toString() {
        String idxAndType = "[" + getIdx() + "] " + getTypeName() + ": ";
        String msg = padMsg(idxAndType.length());
        return new StringBuilder(idxAndType.length() + msg.length() + EOL.length())
                .append(idxAndType).append(msg)
                .append(EOL).toString();
    }

    String padMsg(int pad) {
        String msg = getMessage();
        msg = msg != null ? msg.trim() : "";

        return msg.isEmpty() ? "<no message>" : msg.replaceAll(
                "\r\n?|\n", R_SELF + join("", nCopies(pad, " ")));
    }

    static final String R_SELF = "$0";
}
