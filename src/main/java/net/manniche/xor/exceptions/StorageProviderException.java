/*
 *  This file is part of xor
 *  Copyright © 2009, Steen Manniche.
 *
 *  xor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  xor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with xor.  If not, see <http://www.gnu.org/licenses/>.
 */


package net.manniche.xor.exceptions;

/**
 * The general exception for the storage providers, defining all types of
 * exception states that storage providers can throw.
 * 
 * @author Steen Manniche
 */
public class StorageProviderException extends Exception {
    static final long serialVersionUID = -2848357502538662396L;

    /**
     * Creates a new instance of <code>StorageProviderException</code> without detail message.
     */
    public StorageProviderException() {
    }


    /**
     * Constructs an instance of <code>StorageProviderException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public StorageProviderException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>StorageProviderException</code> with the specified detail message.
     * @param msg the detail message.
     * @param cause the {@link Throwable} that caused this exception.
     */
    public StorageProviderException( String msg, Throwable cause )
    {
        super( msg, cause );
    }
}
