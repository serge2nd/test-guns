package ru.serge2nd.test;

import static java.lang.Integer.getInteger;
import static java.lang.invoke.MethodHandles.lookup;
import static ru.serge2nd.test.Assist.errNotInstantiable;

public class Env {
    private Env() { throw errNotInstantiable(lookup()); }

    //region Property names
    /** The number of MultipleFailuresError stacktrace frames to add to the message. */
    public static final String P_STK_TRC_TOP     = "stkTrcTop";
    /** The number of single failure stacktrace frames to add to the message. */
    public static final String P_SUB_STK_TRC_TOP = "subStkTrcTop";
    //endregion

    //region Property values
    public static final String EOL  = System.lineSeparator();
    public static final String EOL2 = EOL + EOL;

    /** The {@link #P_STK_TRC_TOP} value. */
    public static final int STK_TRC_TOP     = getInteger(P_STK_TRC_TOP, 8);
    /** The {@link #P_SUB_STK_TRC_TOP} value. */
    public static final int SUB_STK_TRC_TOP = getInteger(P_SUB_STK_TRC_TOP, 16);
    //endregion
}
