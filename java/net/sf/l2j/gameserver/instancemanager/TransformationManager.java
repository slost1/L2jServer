/* This program is free software; you can redistribute it and/or modify
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
package net.sf.l2j.gameserver.instancemanager;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastMap;

import net.sf.l2j.gameserver.model.L2Transformation;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 *
 * @author  KenM
 */
public class TransformationManager
{
    private static final Logger _log = Logger.getLogger(TransformationManager.class.getName());
    
    private static final TransformationManager INSTANCE = new TransformationManager();
    
    public static TransformationManager getInstance()
    {
        return INSTANCE;
    }
    
    private Map<Integer, L2Transformation> _transforamtions;
    
    private TransformationManager()
    {
        _transforamtions = new FastMap<Integer, L2Transformation>();
    }
    
    public void report()
    {
        _log.info("Loaded: "+this.getAllTransformations().size()+" transformations.");
    }
    
    public boolean transformPlayer(int id, L2PcInstance player)
    {
        L2Transformation template = this.getTransformationById(id);
        if (template != null)
        {
            L2Transformation trans = template.createTransformationForPlayer(player);
            trans.start();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean transformPlayer(int id, L2PcInstance player, long forceDuration)
    {
        L2Transformation template = this.getTransformationById(id);
        if (template != null)
        {
            L2Transformation trans = template.createTransformationForPlayer(player);
            trans.setDuration(forceDuration);
            trans.start();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public L2Transformation getTransformationById(int id)
    {
        return _transforamtions.get(id);
    }
    
    public L2Transformation registerTransformation(L2Transformation transformation)
    {
        return _transforamtions.put(transformation.getId() , transformation);
    }
    
    public Collection<L2Transformation> getAllTransformations()
    {
        return _transforamtions.values();
    }
}
