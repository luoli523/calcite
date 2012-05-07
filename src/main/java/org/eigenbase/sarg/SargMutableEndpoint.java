/*
// Licensed to DynamoBI Corporation (DynamoBI) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  DynamoBI licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at

//   http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
*/
package org.eigenbase.sarg;

import org.eigenbase.reltype.*;
import org.eigenbase.rex.*;


/**
 * SargMutableEndpoint exposes methods for modifying a {@link SargEndpoint}.
 *
 * @author John V. Sichi
 * @version $Id$
 */
public class SargMutableEndpoint
    extends SargEndpoint
{
    //~ Constructors -----------------------------------------------------------

    /**
     * @see SargFactory#newEndpoint
     */
    SargMutableEndpoint(SargFactory factory, RelDataType dataType)
    {
        super(factory, dataType);
    }

    //~ Methods ----------------------------------------------------------------

    // publicize SargEndpoint
    public void setInfinity(int infinitude)
    {
        super.setInfinity(infinitude);
    }

    // publicize SargEndpoint
    public void setFinite(
        SargBoundType boundType,
        SargStrictness strictness,
        RexNode coordinate)
    {
        super.setFinite(boundType, strictness, coordinate);
    }
}

// End SargMutableEndpoint.java
