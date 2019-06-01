package com.simplejcode.commons.misc._pattern.observable;

@SuppressWarnings("unchecked")
public interface IModelListener<T, D> {

    void modelChanged(T model, D delta);

    default void subscribeTo(ObservableModel... models) {
        for (ObservableModel model : models) {
            model.addListener(this);
        }
    }

    default void unsubscribeTo(ObservableModel... models) {
        for (ObservableModel model : models) {
            model.removeListener(this);
        }
    }

}
