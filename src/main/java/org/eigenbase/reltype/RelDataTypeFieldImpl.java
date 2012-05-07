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
package org.eigenbase.reltype;

import java.io.*;


/**
 * Default implementation of {@link RelDataTypeField}.
 *
 * @author jhyde
 * @version $Id$
 */
public class RelDataTypeFieldImpl
    implements RelDataTypeField,
        Serializable
{
    //~ Instance fields --------------------------------------------------------

    private final RelDataType type;
    private final String name;
    private final int index;

    //~ Constructors -----------------------------------------------------------

    /**
     * @pre name != null
     * @pre type != null
     */
    public RelDataTypeFieldImpl(
        String name,
        int index,
        RelDataType type)
    {
        assert (name != null);
        assert (type != null);
        this.name = name;
        this.index = index;
        this.type = type;
    }

    //~ Methods ----------------------------------------------------------------

    // implement RelDataTypeField
    public String getName()
    {
        return name;
    }

    // implement RelDataTypeField
    public int getIndex()
    {
        return index;
    }

    // implement RelDataTypeField
    public RelDataType getType()
    {
        return type;
    }

    // for debugging
    public String toString()
    {
        return "#" + index + ": " + name + " " + type;
    }
}

// End RelDataTypeFieldImpl.java
