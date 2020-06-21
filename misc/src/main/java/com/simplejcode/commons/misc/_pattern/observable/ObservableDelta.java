package com.simplejcode.commons.misc._pattern.observable;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public abstract class ObservableDelta {

    protected Object source;

    protected int version;

    public ObservableDelta(Object source) {
        this.source = source;
    }

}
