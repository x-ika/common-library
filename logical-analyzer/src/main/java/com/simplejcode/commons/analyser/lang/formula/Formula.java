package com.simplejcode.commons.analyser.lang.formula;

public interface Formula {
    String withBrackets();

    String withoutBrackets(int parentPriority);
}
