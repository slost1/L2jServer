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
package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;

/**
 * @author DaRkRaGe
 */
public class L2BossZone extends L2ZoneType
{
    private String _zoneName;
    private int _timeInvade;

    public L2BossZone(int id)
    {
	super(id);
    }

    @Override
    public void setParameter(String name, String value)
    {
	if (name.equals("name"))
	{
	    _zoneName = value;
	}

	if (name.equals("InvadeTime"))
	{
	    _timeInvade = Integer.parseInt(value);
	}
        else
	{
	    super.setParameter(name, value);
	}
    }

    @Override
    protected void onEnter(L2Character character)
    {
	if (character instanceof L2PcInstance)
	{
	    if (((L2PcInstance) character).isGM())
	    {
		   ((L2PcInstance) character).sendMessage("You entered " + _zoneName);
	    }
	    if (!((L2PcInstance) character).isGM() && (System.currentTimeMillis() - ((L2PcInstance) character).getLastAccess() >= _timeInvade))
	    {
		   ((L2PcInstance) character).teleToLocation(MapRegionTable.TeleportWhereType.Town);
	    }
      }
    }

    @Override
    protected void onExit(L2Character character)
    {
	if (character instanceof L2PcInstance)
	{
	    if (((L2PcInstance) character).isGM())
	    {
		((L2PcInstance) character).sendMessage("You left " + _zoneName);
	    }
	}
    }

    public String getZoneName()
    {
	return _zoneName;
    }

    public int getTimeInvade()
    {
	return _timeInvade;
    }

    @Override
    protected void onDieInside(L2Character character)
    {
    }

    @Override
    protected void onReviveInside(L2Character character)
    {
    }
}