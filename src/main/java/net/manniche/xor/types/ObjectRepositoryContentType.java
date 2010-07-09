/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.xor.types;

/**
 * Defines the content types that can be handled by the server. The basic
 * content type(s) must be implemented by the core server 
 * {@link net.manniche.xor.server.RepositoryServer.RepositoryServerContentType}
 * @author stm
 */
public interface ObjectRepositoryContentType {

    /**
     * Returns a String representing a way to generate the exact same content
     * type using
     * {@link ObjectRepositoryContentType#getContentType(java.lang.String) }
     * @return a String representation of the content type
     */
    @Override
    public String toString();

    /**
     * @param contentType a String representing a content type
     * @throws TypeNotPresentException if the supplied String does not resolve to a {@link ObjectRepositoryContentType}
     * @return an ObjectRepositoryContentType from the String supplied
     */
    //public ObjectRepositoryContentType getContentType( String contentType ) throws TypeNotPresentException;
}
