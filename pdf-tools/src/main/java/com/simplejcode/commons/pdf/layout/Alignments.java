package com.simplejcode.commons.pdf.layout;

/**
 * @author I. Merabishvili | HighPots
 */
public enum Alignments {

    TOP_LEFT(AlignmentV.TOP, AlignmentH.LEFT),
    TOP_CENTER(AlignmentV.TOP, AlignmentH.CENTER),
    TOP_RIGHT(AlignmentV.TOP, AlignmentH.RIGHT),

    MID_LEFT(AlignmentV.CENTER, AlignmentH.LEFT),
    MID_CENTER(AlignmentV.CENTER, AlignmentH.CENTER),
    MID_RIGHT(AlignmentV.CENTER, AlignmentH.RIGHT),

    BOTTOM_LEFT(AlignmentV.BOTTOM, AlignmentH.LEFT),
    BOTTOM_CENTER(AlignmentV.BOTTOM, AlignmentH.CENTER),
    BOTTOM_RIGHT(AlignmentV.BOTTOM, AlignmentH.RIGHT);

    public final AlignmentV v;
    public final AlignmentH h;

    Alignments(AlignmentV v, AlignmentH h) {
        this.h = h;
        this.v = v;
    }

}
