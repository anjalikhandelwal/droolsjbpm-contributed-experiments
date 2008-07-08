/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.commons.jci.readers;

import java.util.Map;
import java.util.HashMap;

/**
 * A memory based reader to compile from memory
 * 
 * @author tcurdt
 */
public class MemoryResourceReader implements ResourceReader {
    
    private Map resources = null;

    public boolean isAvailable( final String pResourceName ) {
        if (resources == null) {
            return false;
        }

        return resources.containsKey(pResourceName);
    }
    
    public void add( final String pResourceName, final byte[] pContent ) {
        if (resources == null) {
            resources = new HashMap();
        }
        
        resources.put(pResourceName, pContent);
    }
    
    public void remove( final String pResourceName ) {
        if (resources != null) {
            resources.remove(pResourceName);
        }    
    }    
    

    public byte[] getBytes( final String pResourceName ) {
        return (byte[]) resources.get(pResourceName);
    }

    /**
     * @deprecated
     */
    public String[] list() {
        if (resources == null) {
            return new String[0];
        }
        return (String[]) resources.keySet().toArray(new String[resources.size()]);
    }
}
