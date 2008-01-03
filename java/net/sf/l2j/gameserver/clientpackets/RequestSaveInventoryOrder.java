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
package net.sf.l2j.gameserver.clientpackets;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.Inventory;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ItemInstance.ItemLocation;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * Format:(ch) d[dd]
 * 
 * @author -Wooden-
 */
public final class RequestSaveInventoryOrder extends L2GameClientPacket
{
    private List<InventoryOrder> _order;
    
    /**
     * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#readImpl()
     */
    @Override
    protected void readImpl()
    {
        int sz = readD();
        sz = Math.min(sz, this.getClient().getActiveChar().GetInventoryLimit());
        _order = new ArrayList<InventoryOrder>(sz);
        for (int i = 0; i < sz; i++)
        {
            int objectId = readD();
            int order = readD();
            _order.add(new InventoryOrder(objectId, order));
        }
    }
    
    /**
     * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#runImpl()
     */
    @Override
    protected void runImpl()
    {
        L2PcInstance player = this.getClient().getActiveChar();
        if (player != null)
        {
            Inventory inventory = player.getInventory();
            for (InventoryOrder order : _order)
            {
                L2ItemInstance item = inventory.getItemByObjectId(order.objectID);
                if (item != null && item.getLocation() == ItemLocation.INVENTORY)
                {
                    item.setLocation(ItemLocation.INVENTORY, order.order);
                }
            }
        }
    }
    
    /**
     * @see net.sf.l2j.gameserver.BasePacket#getType()
     */
    @Override
    public String getType()
    {
        return "[C] D0:49 RequestSaveInventoryOrder";
    }
    
    private class InventoryOrder
    {
        int order;
        
        int objectID;
        
        /**
         * 
         */
        public InventoryOrder(int id, int ord)
        {
            objectID = id;
            order = ord;
        }
    }
}
