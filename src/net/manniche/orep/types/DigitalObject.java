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
     * The identifier of any DigitalObject in the Object Repository is a
     * (universally) unique value
     *
     * @return the identifier of the DigitalObject
     */
//    public ObjectIdentifier getIdentifier();


    /**
     * Writes out the DigitalObject as a byte[]. The binary representation is
     * determined by the implementors.
     * @return
     */
    public byte[] getBytes();
}
