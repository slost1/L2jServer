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

import javolution.util.FastMap;
import net.sf.l2j.gameserver.GameServer;
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
    
    // track the times that players got disconnected. Players are allowed
    // to log back into the zone as long as their log-out was within _timeInvade
    // time...
    private int _timeInvade;
    
    // <player objectId, expiration time in milliseconds>
    private FastMap<Integer, Long> _playerAllowedEntryExpirationTimes;
    
    public L2BossZone(int id)
    {
        super(id);
        _playerAllowedEntryExpirationTimes = new FastMap<Integer, Long>();
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
    /**
     * Boss zones have special behaviors for player characters. Players are
     * automatically teleported out when the attempt to enter these zones,
     * except if the time at which they enter the zone is prior to the entry
     * expiration time set for that player. Entry expiration times are set by
     * any one of the following: 1) A player logs out while in a zone
     * (Expiration gets set to logoutTime + _timeInvade) 2) An external source
     * (such as a quest or AI of NPC) set up the player for entry.
     * 
     * There exists one more case in which the player will be allowed to enter.
     * That is if the server recently rebooted (boot-up time more recent than
     * currentTime - _timeInvade) AND the player's last access is not too old
     * (to prevent players camping until reboot for easy entry). 4*_timeInvade
     * should be plenty of time to allow for some server downtime. Ideally it
     * should be (lastServerShutDownTime - _timeInvade) but we do not track the
     * time for the last shutdown, so just (bootup -4*_timeInvade) will be a
     * good compromise.
     */
    protected void onEnter(L2Character character)
    {
        if (character instanceof L2PcInstance)
        {
            if (((L2PcInstance) character).isGM())
            {
                ((L2PcInstance) character).sendMessage("You entered "
                        + _zoneName);
            }
            // Get the information about this player's last logout-exit from
            // this zone.
            Long expirationTime = _playerAllowedEntryExpirationTimes.get(character.getObjectId());
            
            // with legal entries, do nothing.
            if (expirationTime == null) // legal null expirationTime entries
            {
                long serverStartTime = GameServer.dateTimeServerStarted.getTimeInMillis();
                long playerLastAccess = ((L2PcInstance) character).getLastAccess();
                if ((serverStartTime > (System.currentTimeMillis() - _timeInvade))
                        && (playerLastAccess < serverStartTime)
                        && (playerLastAccess > (serverStartTime - 4 * _timeInvade)))
                    return;
            }
            else
            // legal non-null logoutTime entries
            {
                _playerAllowedEntryExpirationTimes.remove(character.getObjectId());
                if (expirationTime.longValue() > System.currentTimeMillis())
                    return;
            }
            // teleport out all players who attempt "illegal" (re-)entry
            ((L2PcInstance) character).teleToLocation(MapRegionTable.TeleportWhereType.Town);
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
            else
            {
                // if the player just got disconnected/logged out, store the dc
                // time so that
                // decisions can be made later about allowing or not the player
                // to log into the zone
                if (((L2PcInstance) character).isOnline() == 0)
                {
                    // mark the time that the player left the zone
                    _playerAllowedEntryExpirationTimes.put(character.getObjectId(), System.currentTimeMillis()
                            + _timeInvade);
                }
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
    
    /**
     * occasionally, all players need to be sent out of the zone (for example,
     * if the players are just running around without fighting for too long, or
     * if all players die, etc). This call sends all online players to town and
     * marks offline players to be teleported (by clearing their relog
     * expiration times) when they log back in (no real need for off-line
     * teleport).
     */
    public void oustAllPlayers()
    {
        L2Character[] charList = (L2Character[]) _characterList.values().toArray();
        for (L2Character character : charList)
        {
            if (character instanceof L2PcInstance)
            {
                L2PcInstance player = (L2PcInstance) character;
                if (player.isOnline() == 1)
                    player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
            }
        }
        charList = null;
        _playerAllowedEntryExpirationTimes.clear();
    }
    
    /**
     * This function is to be used by external sources, such as quests and AI
     * in order to allow a player for entry into the zone for some time.  Naturally
     * if the player does not enter within the allowed time, he/she will be
     * teleported out again... 
     * @param player: reference to the player we wish to allow 
     * @param durationInSec: amount of time in seconds during which entry is valid.
     */
    public void allowPlayerEntry(L2PcInstance player, int durationInSec)
    {
        _playerAllowedEntryExpirationTimes.put(player.getObjectId(), System.currentTimeMillis() + durationInSec*1000);
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