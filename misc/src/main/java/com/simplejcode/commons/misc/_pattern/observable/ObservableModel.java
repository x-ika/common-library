package com.simplejcode.commons.misc._pattern.observable;

import org.apache.logging.log4j.*;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class ObservableModel<T extends ObservableModel<T, D>, D extends ObservableDelta> {

    private static final Logger logger = LogManager.getLogger(ObservableModel.class);


    protected final String name;

    protected int version;

    protected final List<IModelListener<T, D>> eventListeners;


    @Deprecated
    protected ObservableModel() {
        this("ND");
    }

    protected ObservableModel(String name) {
        this.name = name;
        eventListeners = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    //-----------------------------------------------------------------------------------
    /*
    Public API
     */

    public void update(D delta) {

        synchronized (this) {
            if (delta == null) {
                clear();
            } else {
                updateInternal(delta);
                delta.version = version++;
            }
        }

        notifyListeners(delta);

    }

    public void addListener(IModelListener<T, D> listener) {
        Iterable<D> fullState;
        synchronized (this) {
            fullState = getFullState();
        }
        synchronized (eventListeners) {
            for (D delta : fullState) {
                notifyListener(listener, delta);
            }
            eventListeners.add(listener);
        }
    }

    public void removeListener(IModelListener listener) {
        notifyListener(listener, null);
        synchronized (eventListeners) {
            eventListeners.remove(listener);
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    Protected
     */

    protected void notifyListeners(D delta) {
        synchronized (eventListeners) {
            for (IModelListener listener : eventListeners) {
                if (listener == null) {
                    break;
                }
                notifyListener(listener, delta);
            }
        }
    }

    protected void notifyListener(IModelListener listener, D delta) {
        try {
            listener.modelChanged(this, delta);
        } catch (Exception e) {
            logger.error("Error occurred while handling the model change event", e);
        }
    }

    protected abstract void clear();

    protected abstract void updateInternal(D delta);

    protected abstract Iterable<D> getFullState();

}
