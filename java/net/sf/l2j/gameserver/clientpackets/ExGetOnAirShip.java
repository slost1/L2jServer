/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.clientpackets;

/**
 * Format: (c) dddd
 * d: dx
 * d: dy
 * d: dz
 * d: AirShip id ??
 * @author  -Wooden-
 * 
 */
public class ExGetOnAirShip extends L2GameClientPacket
{
	private int _x;
	private int _y;
	private int _z;
	private int _shipId;

    protected void readImpl()
    {
    	_x = readD();
    	_y = readD();
    	_z = readD();
    	_shipId = readD();
    }

    protected void runImpl()
    {
        System.out.println("[T1:ExGetOnAirShip] x: "+_x);
        System.out.println("[T1:ExGetOnAirShip] y: "+_y);
        System.out.println("[T1:ExGetOnAirShip] z: "+_z);
        System.out.println("[T1:ExGetOnAirShip] ship ID: "+_shipId);
    }

    public String getType()
    {
        return "[C] 0xD0:0x35 ExGetOnAirShip";
    }

}
