package mars.util;

import java.util.Observable;
import java.util.Observer;

public class DelegatingObservable extends Observable implements Observer {

    private final Observable delegate;

    public DelegatingObservable(Observable delegate) {
        this.delegate = delegate;
        delegate.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(!o.equals(delegate))
            return;

        setChanged();
        notifyObservers(arg);
    }
}
