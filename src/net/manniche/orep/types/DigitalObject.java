/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.orep.types;

/**
 *
 * @author stm
 */
public interface DigitalObject {

    /**
     * Writes out the DigitalObject as a byte[]. The binary representation is
     * determined by the implementors.
     * @return
     */
    public byte[] getBytes();
}
