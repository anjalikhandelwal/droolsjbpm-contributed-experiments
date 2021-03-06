package org.drools.objenesis;

import org.drools.objenesis.strategy.StdInstantiatorStrategy;

/**
 * Objenesis implementation using the {@link org.drools.objenesis.strategy.StdInstantiatorStrategy}.
 * 
 * @author Henri Tremblay
 */
public class ObjenesisStd extends ObjenesisBase {

    /**
     * Default constructor using the {@link org.drools.objenesis.strategy.StdInstantiatorStrategy}
     */
    public ObjenesisStd() {
        super( new StdInstantiatorStrategy() );
    }

    /**
     * Instance using the {@link org.drools.objenesis.strategy.StdInstantiatorStrategy} with or without
     * caching {@link org.drools.objenesis.instantiator.ObjectInstantiator}s
     * 
     * @param useCache If {@link org.drools.objenesis.instantiator.ObjectInstantiator}s should be cached
     */
    public ObjenesisStd(final boolean useCache) {
        super( new StdInstantiatorStrategy(),
               useCache );
    }
}
