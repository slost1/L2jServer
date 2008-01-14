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
package net.sf.l2j.gameserver.serverpackets;

public class Ride extends L2GameServerPacket
{
    private static final String _S__8c_Ride = "[S] 8c Ride";
    public static final int ACTION_MOUNT = 1;
    public static final int ACTION_DISMOUNT = 0;
    private int _id;
    private int _bRide;
    private int _rideType;
    private int _rideClassID;

    public Ride(int id, int action, int rideClassId)
    {
        _id = id; // charobjectID
        _bRide = action; // 1 for mount ; 2 for dismount
        _rideClassID = rideClassId + 1000000; // npcID

        switch(rideClassId)
        {
            case 12526: // Wind
            case 12527: // Star
            case 12528: // Twilight
                _rideType = 1; break;
            case 12621: // Wyvern
                _rideType = 2; break;
            case 16030: // Great Wolf
                _rideType = 3; break;
        }
    }

    @Override
    public void runImpl()
    {

    }

    public int getMountType()
    {
        return _rideType;
    }

    @Override
	protected final void writeImpl()
    {

        writeC(0x8c);
        writeD(_id);
        writeD(_bRide);
        writeD(_rideType);
        writeD(_rideClassID);
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
     */
    @Override
	public String getType()
    {
        return _S__8c_Ride;
    }
}
