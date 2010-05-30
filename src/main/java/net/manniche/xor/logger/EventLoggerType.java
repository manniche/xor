/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.xor.logger;

import net.manniche.xor.types.ObjectRepositoryService;
import net.manniche.xor.types.ObjectRepositoryServiceType;


/**
 *
 * @author stm
 */
public enum EventLoggerType  implements ObjectRepositoryServiceType<ObjectRepositoryService>{

    /**
     * Filebased handler of repository event logs
     */
    FileEventLogger( FileBasedLogMessageHandler.class );
    
    private Class<LogMessageHandler> eventlogger_type;

    @SuppressWarnings( "unchecked" )
    EventLoggerType( Class<? extends LogMessageHandler> klass )
    {
        this.eventlogger_type = (Class<LogMessageHandler>) klass;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Class<ObjectRepositoryService> getClassofService()
    {
        return (Class<ObjectRepositoryService>) (Object) this.eventlogger_type;
    }

}
