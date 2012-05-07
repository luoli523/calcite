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
package org.eigenbase.rex;

import java.util.*;


/**
 * Passes over a row-expression, calling a handler method for each node,
 * appropriate to the type of the node.
 *
 * <p>Like {@link RexVisitor}, this is an instance of the {@link
 * org.eigenbase.util.Glossary#VisitorPattern Visitor Pattern}. Use <code>
 * RexShuttle</code> if you would like your methods to return a value.</p>
 *
 * @author jhyde
 * @version $Id$
 * @since Nov 26, 2003
 */
public class RexShuttle
    implements RexVisitor<RexNode>
{
    //~ Methods ----------------------------------------------------------------

    public RexNode visitOver(RexOver over)
    {
        boolean [] update = { false };
        RexNode [] clonedOperands = visitArray(over.operands, update);
        RexWindow window = visitWindow(over.getWindow());
        if (update[0] || (window != over.getWindow())) {
            // REVIEW jvs 8-Mar-2005:  This doesn't take into account
            // the fact that a rewrite may have changed the result type.
            // To do that, we would need to take a RexBuilder and
            // watch out for special operators like CAST and NEW where
            // the type is embedded in the original call.
            return new RexOver(
                over.getType(),
                over.getAggOperator(),
                clonedOperands,
                window);
        } else {
            return over;
        }
    }

    public RexWindow visitWindow(RexWindow window)
    {
        boolean [] update = { false };
        RexNode [] clonedOrderKeys = visitArray(window.orderKeys, update);
        RexNode [] clonedPartitionKeys =
            visitArray(window.partitionKeys, update);
        if (update[0]) {
            return new RexWindow(
                clonedPartitionKeys,
                clonedOrderKeys,
                window.getLowerBound(),
                window.getUpperBound(),
                window.isRows());
        } else {
            return window;
        }
    }

    public RexNode visitCall(final RexCall call)
    {
        boolean [] update = { false };
        RexNode [] clonedOperands = visitArray(call.operands, update);
        if (update[0]) {
            // REVIEW jvs 8-Mar-2005:  This doesn't take into account
            // the fact that a rewrite may have changed the result type.
            // To do that, we would need to take a RexBuilder and
            // watch out for special operators like CAST and NEW where
            // the type is embedded in the original call.
            return new RexCall(
                call.getType(),
                call.getOperator(),
                clonedOperands);
        } else {
            return call;
        }
    }

    /**
     * Visits each of an array of expressions and returns an array of the
     * results.
     *
     * @param exprs Array of expressions
     * @param update If not null, sets this to true if any of the expressions
     * was modified
     *
     * @return Array of visited expressions
     */
    protected RexNode [] visitArray(RexNode [] exprs, boolean [] update)
    {
        RexNode [] clonedOperands = new RexNode[exprs.length];
        for (int i = 0; i < exprs.length; i++) {
            RexNode operand = exprs[i];
            RexNode clonedOperand = operand.accept(this);
            if ((clonedOperand != operand) && (update != null)) {
                update[0] = true;
            }
            clonedOperands[i] = clonedOperand;
        }
        return clonedOperands;
    }

    public RexNode visitCorrelVariable(RexCorrelVariable variable)
    {
        return variable;
    }

    public RexNode visitFieldAccess(RexFieldAccess fieldAccess)
    {
        RexNode before = fieldAccess.getReferenceExpr();
        RexNode after = before.accept(this);

        if (before == after) {
            return fieldAccess;
        } else {
            return new RexFieldAccess(
                after,
                fieldAccess.getField());
        }
    }

    public RexNode visitInputRef(RexInputRef inputRef)
    {
        return inputRef;
    }

    public RexNode visitLocalRef(RexLocalRef localRef)
    {
        return localRef;
    }

    public RexNode visitLiteral(RexLiteral literal)
    {
        return literal;
    }

    public RexNode visitDynamicParam(RexDynamicParam dynamicParam)
    {
        return dynamicParam;
    }

    public RexNode visitRangeRef(RexRangeRef rangeRef)
    {
        return rangeRef;
    }

    /**
     * Applies this shuttle to each expression in a list.
     *
     * @return whether any of the expressions changed
     */
    public final <T extends RexNode> boolean apply(List<T> exprList)
    {
        int changeCount = 0;
        for (int i = 0; i < exprList.size(); i++) {
            T expr = exprList.get(i);
            T expr2 = (T) expr.accept(this);
            if (expr != expr2) {
                ++changeCount;
                exprList.set(i, expr2);
            }
        }
        return changeCount > 0;
    }

    /**
     * Applies this shuttle to an expression, or returns null if the expression
     * is null.
     */
    public final RexNode apply(RexNode expr)
    {
        return (expr == null) ? null : expr.accept(this);
    }
}

// End RexShuttle.java
