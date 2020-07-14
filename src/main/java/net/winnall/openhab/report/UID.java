/* 
 * Copyright 2020 Stephen Winnall.
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
 */
package net.winnall.openhab.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Stephen Winnall
 */
public class UID implements Iterable<String> {

    private static final int SEG_BINDING_INX = 0;

    private static final int SEG_BRIDGEID_INX = 2;

    private static final int SEG_MODELID_INX = 1;

    private static final int SEG_SERIALID_INX = 3;

    private final List<String> segments;

    public UID( Iterable<String> segments ) {
        this.segments = new ArrayList<>();
        segments.forEach(
                ( segment ) -> {
                    this.segments.add( segment );
                } );
    }

    public UID( String... segments ) {
        this.segments = Arrays.asList( segments );
    }

    public UID( List<String> segments ) {
        this.segments = segments;
    }

    // not available for any use
    private UID() {
        this.segments = new ArrayList<>( 0 );
    }

    @Override
    public boolean equals( Object object ) {
        if( !( object instanceof UID ) ) {
            return false;
        }
        UID other = (UID) object;
        Iterator<String> thisIter = this.iterator();
        Iterator<String> otherIter = other.iterator();
        while( thisIter.hasNext() ) {
            if( !otherIter.hasNext() ) {
                return false;
            }
            if( !thisIter.next()
                    .equals( otherIter.next() ) ) {
                return false;
            }
        }
        return !otherIter.hasNext();
    }

    public String getBindingName() {
        return segments.get( SEG_BINDING_INX )
                .toLowerCase();
    }

    public String getDeviceId() {
        if( segments.size() == 3 ) {
            return segments.get( SEG_BRIDGEID_INX );
        } else {
            return segments.get( SEG_SERIALID_INX );
        }
    }

    public String getModelId() {
        return segments.get( SEG_MODELID_INX );
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode( this.segments );
        return hash;
    }

    @Override
    public Iterator<String> iterator() {
        return segments.iterator();
    }

}
