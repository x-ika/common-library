package com.simplejcode.commons.analyser.calc;

import java.util.Collection;

public interface Tactics {
    Code getCodeFor(Sequent sequent);

    Collection<Code> getAllCodesFor(Sequent sequent);
}
