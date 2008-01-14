/*
+* This program is free software: you can redistribute it and/or modify it under
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
package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.effects.EffectBattleForce;
import net.sf.l2j.gameserver.skills.effects.EffectSpellForce;


/**
 * @author kombat
 *
 */
public class ConditionForceBuff extends Condition
{
	private static int BATTLE_FORCE = 5104;
	private static int SPELL_FORCE = 5105;

	private int _battleForces;
	private int _spellForces;

	public ConditionForceBuff(int[] forces)
	{
		_battleForces = forces[0];
		_spellForces = forces[1];
	}

	public ConditionForceBuff(int battle, int spell)
	{
		_battleForces = battle;
		_spellForces = spell;
	}

	@Override
	public boolean testImpl(Env env)
	{
		int neededBattle = _battleForces;
		if (neededBattle > 0)
		{
			L2Effect battleForce = env.player.getFirstEffect(BATTLE_FORCE);
			if (!(battleForce instanceof EffectBattleForce) ||
			  ((EffectBattleForce)battleForce).forces < neededBattle)
				return false;
		}
		int neededSpell = _spellForces;
		if (neededSpell > 0)
		{
			L2Effect spellForce = env.player.getFirstEffect(SPELL_FORCE);
			if (!(spellForce instanceof EffectSpellForce) ||
			  ((EffectSpellForce)spellForce).forces < neededSpell)
				return false;
		}
		return true;
	}
}
