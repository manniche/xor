/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.xor.services.search;

/**
 * Defines allowed operators for queries
 *
 * @see {@link Query}
 * @author stm
 */
public enum Operator {

    AND( true ),
    OR( true ),
    NOT( false);

    private final boolean isBinary;
    Operator( boolean binary )
    {
        this.isBinary = binary;
    }
}
