/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.clientpackets;

import java.util.logging.Logger;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * Format (ch) ddd
 * c: (id) 0xD0
 * h: (subid) 0x31
 * d: skill id
 * d: skill lvl ?
 * d: ?
 * @author -Wooden-
 *
 */
public final class RequestExEnchantSkillInfoDetail extends L2GameClientPacket
{
    protected static final Logger _log = Logger.getLogger(RequestExEnchantSkillInfoDetail.class.getName());
	private int _skillId;
	private int _skillLvl;
	private int _unk;
	
	protected void readImpl()
	{
		_skillId = readD();
		_skillLvl = readD();
		_unk = readD();
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#runImpl()
	 */
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
        
        if (activeChar == null) 
            return;
        //TODO this
        
        _log.info("[T1:RequestExEnchantSkillInfoDetail] skill id?:"+_skillId);
        _log.info("[T1:RequestExEnchantSkillInfoDetail] skill lvl?:"+_skillLvl);
        _log.info("[T1:RequestExEnchantSkillInfoDetail] unk:"+_unk);
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return "[C] D0:31 RequestExEnchantSkillInfo";
	}
	
}