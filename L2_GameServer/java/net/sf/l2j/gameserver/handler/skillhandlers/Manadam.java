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
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 * Class handling the Mana damage skill
 *
 * @author slyce
 */
public class Manadam implements ISkillHandler
{
private static final SkillType[] SKILL_IDS = { SkillType.MANADAM };

public void useSkill(@SuppressWarnings("unused")
L2Character activeChar, L2Skill skill, L2Object[] targets)
{
	L2Character target = null;

	if (activeChar.isAlikeDead())
		return;

	boolean ss = false;
	boolean bss = false;

	L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();

	if (weaponInst != null)
	{
		if (weaponInst.getChargedSpiritshot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
		{
			bss = true;
			weaponInst.setChargedSpiritshot(L2ItemInstance.CHARGED_NONE);
		} else if (weaponInst.getChargedSpiritshot() == L2ItemInstance.CHARGED_SPIRITSHOT)
		{
			ss = true;
			weaponInst.setChargedSpiritshot(L2ItemInstance.CHARGED_NONE);
		}
	}
	// If there is no weapon equipped, check for an active summon.
	else if (activeChar instanceof L2Summon)
	{
		L2Summon activeSummon = (L2Summon) activeChar;
			
		if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
		{
			bss = true;
			activeSummon.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);
		}
		else if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
		{
			ss = true;
			activeSummon.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);
		}
	}
	else if (activeChar instanceof L2NpcInstance)
	{
		bss = ((L2NpcInstance)activeChar).isUsingShot(false);
		ss = ((L2NpcInstance)activeChar).isUsingShot(true);
	}
	for (int index = 0; index < targets.length; index++)
	{
		target = (L2Character) targets[index];

		if (target.reflectSkill(skill))
			target = activeChar;

		boolean acted = Formulas.getInstance().calcMagicAffected(activeChar, target, skill);
		if (target.isInvul() || !acted)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.MISSED_TARGET));
		}
		else
		{
			double damage = Formulas.getInstance().calcManaDam(activeChar, target, skill, ss, bss);

			double mp = (damage > target.getCurrentMp() ? target.getCurrentMp() : damage);
			target.reduceCurrentMp(mp);
			if (damage > 0)
			{
				if (target.isSleeping())
				{
					target.stopSleeping(null);
				}
				else if (target.isImmobileUntilAttacked())
				{
					target.stopImmobileUntilAttacked(null);
				}
			}

			if (target instanceof L2PcInstance)
			{
				StatusUpdate sump = new StatusUpdate(target.getObjectId());
				sump.addAttribute(StatusUpdate.CUR_MP, (int) target.getCurrentMp());
				// [L2J_JP EDIT START - TSL]
				target.sendPacket(sump);

				SystemMessage sm = new SystemMessage(SystemMessageId.S2_MP_HAS_BEEN_DRAINED_BY_S1);
				sm.addCharName(activeChar);
				sm.addNumber((int) mp);
				target.sendPacket(sm);
			}

			if (activeChar instanceof L2PcInstance)
			{
				SystemMessage sm2 = new SystemMessage(SystemMessageId.YOUR_OPPONENTS_MP_WAS_REDUCED_BY_S1);
				sm2.addNumber((int) mp);
				activeChar.sendPacket(sm2);
			}
			// [L2J_JP EDIT END - TSL]
		}
	}
}

public SkillType[] getSkillIds()
{
	return SKILL_IDS;
}
}
