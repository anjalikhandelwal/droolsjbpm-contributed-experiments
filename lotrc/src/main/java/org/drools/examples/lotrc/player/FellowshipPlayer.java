/*
 * Copyright 2008 Red Hat
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.drools.examples.lotrc.player;

import org.drools.examples.lotrc.model.Allegiance;

/**
 * A fellowship player implementation
 * 
 * @author etirelli
 */
public class FellowshipPlayer extends AbstractDroolsPlayer {

    public FellowshipPlayer() {
        super( "Fellowship Player", new String[]{ "players/common.drl", "fellowship/fellowship.drl" } );
    }
    
    /* (non-Javadoc)
     * @see org.drools.examples.lotrc.model.Player#getAllegiance()
     */
    public Allegiance getAllegiance() {
        return Allegiance.FELLOWSHIP;
    }

    @Override
    public String toString() {
        return "Fellowship Player";
    }
}