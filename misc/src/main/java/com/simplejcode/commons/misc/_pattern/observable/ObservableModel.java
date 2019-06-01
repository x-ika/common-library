package com.simplejcode.commons.misc._pattern.observable;

import org.apache.log4j.Logger;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class ObservableModel<T extends ObservableModel<T, D>, D extends ObservableDelta> {

    private static Logger logger = Logger.getLogger(ObservableModel.class);


    protected int version;

    protected List<IModelListener<T, D>> eventListeners;

    private IModelListener[] _copy;


    protected ObservableModel() {
        eventListeners = new Vector<>();
        _copy = new IModelListener[9];
    }

    //-----------------------------------------------------------------------------------
    /*
    Public API
     */

    public synchronized void update(D delta) {

        if (delta == null) {
            clear();
        } else {
            updateInternal(delta);
            delta.version = version++;
        }

        notifyListeners(delta);

    }

    public synchronized void addListener(IModelListener listener) {
        eventListeners.add(listener);
        for (D delta : getFullState()) {
            delta.version = version++;
            notifyListeners(delta);
        }
    }

    public synchronized void removeListener(IModelListener listener) {
        eventListeners.remove(listener);
    }

    //-----------------------------------------------------------------------------------
    /*
    Protected
     */

    protected void notifyListeners(D delta) {

        eventListeners.toArray(_copy);

        for (IModelListener listener : _copy) {
            if (listener == null) {
                break;
            }
            notifyListener(listener, delta);
        }

    }

    protected void notifyListener(IModelListener listener, D delta) {
        try {
            listener.modelChanged(this, delta);
        } catch (Exception e) {
            logger.fatal("Error occurred while handling the model change event", e);
        }
    }

    protected abstract void clear();

    protected abstract void updateInternal(D delta);

    protected abstract Iterable<D> getFullState();

}
