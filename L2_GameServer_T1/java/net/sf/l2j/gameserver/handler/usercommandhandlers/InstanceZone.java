/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * 
 * @author nille02
 */
public class InstanceZone implements IUserCommandHandler
{
    
    private static final int[] COMMAND_IDS = { 114 };
    
    /**
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#getUserCommandList()
     */
    public int[] getUserCommandList()
    {
        
        return COMMAND_IDS;
    }
    
    /**
     * @see net.sf.l2j.gameserver.handler.IUserCommandHandler#useUserCommand(int,
     *      net.sf.l2j.gameserver.model.actor.instance.L2PcInstance)
     */
    public boolean useUserCommand(int id, L2PcInstance activeChar)
    {
        if (id != COMMAND_IDS[0])
            return false;
        
        activeChar.sendPacket(new SystemMessage(SystemMessageId.NO_INSTANCEZONE_TIME_LIMIT));
        
        return true;
    }
}
