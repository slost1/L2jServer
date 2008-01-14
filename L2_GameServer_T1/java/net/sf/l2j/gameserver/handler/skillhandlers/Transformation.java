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
package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.instancemanager.TransformationManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 *
 * @author nBd
 */
public class Transformation implements ISkillHandler
{
    private static final SkillType[] SKILL_IDS = {SkillType.TRANSFORM};

    public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
    {
        if (activeChar.isAlikeDead())
            return;
        
        for (L2Object target : targets)
        {
            if (!(target instanceof L2PcInstance))
                continue;
            
            L2PcInstance trg = (L2PcInstance)target;
            
            if (trg.isAlikeDead() || trg.isCursedWeaponEquiped())
                continue;
            
            int transformId = skill.getTransformId();
            int duration = skill.getTransformDuration();
            
            if (duration < -1)
                duration = -1;
            
            if (!trg.isTransformed())
            {
                switch (duration)
                {
                    case 0:
                        TransformationManager.getInstance().transformPlayer(transformId, trg);
                        break;
                    case -1:
                        TransformationManager.getInstance().transformPlayer(transformId, trg, Integer.MAX_VALUE);
                        break;
                    default:
                        TransformationManager.getInstance().transformPlayer(transformId, trg, duration);
                        break;
                }
            }
            else
            {
                trg.untransform();
            }
        }
    }

    public SkillType[] getSkillIds()
    {
        return SKILL_IDS;
    }
}
