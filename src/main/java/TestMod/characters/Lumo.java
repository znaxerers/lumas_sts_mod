package TestMod.characters;

import TestMod.characters.TheLuma.Enums;
import TestMod.helper.GenericHelper;
//import KaltsitMod.modcore.KaltsitModCore;
import TestMod.patches.LumoPatch;
//import KaltsitMod.powers.CantAttackPower;
//import KaltsitMod.powers.NonDamagingRestructuringPower;
//import KaltsitMod.powers.PerceptualCoexistencePower;
//import KaltsitMod.ui.SkinSelectScreen;
//import KaltsitMod.vfx.MeltDownEffect;
import TestMod.TestMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Lumo extends AbstractMonster {
    public static final String ID = TestMod.makeID(Lumo.class.getSimpleName());
    private static final MonsterStrings STRINGS;
    private static final String NAME;
    private static final String[] MOVES;
    private static final String[] DIALOG;
    private static final int MAX_HP = 40;
    private static final float HB_X = 0.0F;
    private static final float HB_Y = 0.0F;
    private static final float HB_W = 200.0F;
    private static final float HB_H = 250.0F;
    private static final float OFFSET_X = 0.0F;
    private static final float OFFSET_Y = 0.0F;
    private static final int SELF_DAMAGE = 3;
    public boolean meltdownNextTurn = false;
    public boolean gainBlockNextTurn = false;
    public boolean damageToAllNextTurn = false;
    public boolean damageToLowestNextTurn = false;
    public int damageTimes = 1;
    protected ArrayList<AnimationInfo> animationList = new ArrayList();
    protected float animationTimer = 0.0F;
    protected AnimationInfo currentAnim;

    public Lumo() {
        super(NAME, ID, 40, 0.0F, 0.0F, 200.0F, 250.0F, (String)null, 0.0F, 0.0F);
        this.isPlayer = true;
        this.damage.add(new DamageInfo(this, 3));
        //SkinSelectScreen.Skin skin = SkinSelectScreen.getSkin();
        //this.loadAnimation(skin.monsterPath + ".atlas", skin.monsterPath + ".json", 1.8F);
        this.loadAnimation("TestModResources/images/char/token_10002_kalts_mon3tr_2" + ".atlas", "TestModResources/images/char/token_10002_kalts_mon3tr_2" + ".json", 1.8F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void spawn() {
        if (AbstractDungeon.player.chosenClass == Enums.THE_LUMA) {
            if (MathUtils.randomBoolean()) {
                //CardCrawlGame.sound.playA(KaltsitModCore.MakePath("Summon_1"), 0.0F);
            } else {
                //CardCrawlGame.sound.playA(KaltsitModCore.MakePath("Summon_2"), 0.0F);
            }
        }

        this.changeState("Start");
        this.init();
        this.showHealthBar();
        this.createIntent();
        GenericHelper.MoveMonster(this, AbstractDungeon.player.drawX + 200.0F * Settings.scale, AbstractDungeon.player.drawY + 100.0F * Settings.scale);
    }

    public void addTip() {
        this.tips.add(new PowerTip(DIALOG[0], DIALOG[1]));
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
    }

    public void changeState(String stateName) {
        switch (stateName) {
            case "Start":
            case "Attack":
            case "Skill":
            case "Skill_2":
                if (this.meltdownNextTurn) {
                    this.addAnimation("Skill_2");
                } else {
                    this.addAnimation(stateName);
                }
                break;
            case "Die":
                this.state.setAnimation(0, stateName, false);
        }

    }

    protected void getMove(int num) {
        this.setMove((byte)0, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
    }

    public int calculateDmg(AbstractMonster mo) {
        float tmp = (float)((DamageInfo)this.damage.get(0)).base;
        DamageInfo.DamageType type = ((DamageInfo)this.damage.get(0)).type;

        Iterator var4;
        AbstractPower p;
        for(var4 = this.powers.iterator(); var4.hasNext(); tmp = p.atDamageGive(tmp, type)) {
            p = (AbstractPower)var4.next();
        }

        if (mo != null) {
            for(var4 = mo.powers.iterator(); var4.hasNext(); tmp = p.atDamageReceive(tmp, type)) {
                p = (AbstractPower)var4.next();
            }
        }

        for(var4 = this.powers.iterator(); var4.hasNext(); tmp = p.atDamageFinalGive(tmp, type)) {
            p = (AbstractPower)var4.next();
        }

        if (mo != null) {
            for(var4 = mo.powers.iterator(); var4.hasNext(); tmp = p.atDamageFinalReceive(tmp, type)) {
                p = (AbstractPower)var4.next();
            }
        }

        return (int)Math.floor((double)tmp);
    }

    public void takeTurn() {
        if (this.gainBlockNextTurn) {
            for(int i = 0; i < this.damageTimes; ++i) {
                GenericHelper.addToBotAbstract(() -> {
                    ((DamageInfo)this.damage.get(0)).output = this.calculateDmg((AbstractMonster)null);
                    this.addToTop(new GainBlockAction(this, ((DamageInfo)this.damage.get(0)).output));
                });
            }
        } else {
//            if (this.hasPower(CantAttackPower.id)) {
//                this.getPower(CantAttackPower.id).flash();
//                return;
//            }

            DamageInfo.DamageType type = this.meltdownNextTurn ? DamageType.HP_LOSS : DamageType.NORMAL;
            ((DamageInfo)this.damage.get(0)).type = type;
            AbstractGameAction.AttackEffect effect = this.meltdownNextTurn ? AttackEffect.FIRE : (((DamageInfo)this.damage.get(0)).output > 20 ? AttackEffect.SLASH_HORIZONTAL : AttackEffect.SLASH_HEAVY);
            int i;
            if (this.damageToAllNextTurn) {
                this.addToBot(new ChangeStateAction(this, "Skill"));

                for(i = 0; i < this.damageTimes; ++i) {
                    if (this.meltdownNextTurn) {
                        GenericHelper.foreachAliveMonster((mo) -> {
                            //GenericHelper.addEffect(new MeltDownEffect(mo));
                            return false;
                        });
                    }

                    GenericHelper.addToBotAbstract(() -> {
                        int dmg = this.calculateDmg((AbstractMonster)null);
                        int[] retVal = new int[AbstractDungeon.getMonsters().monsters.size()];
                        Arrays.fill(retVal, dmg);
                        this.addToBot(new DamageAllEnemiesAction(this, retVal, type, effect));
                    });
                }
            } else if (this.damageToLowestNextTurn) {
                this.addToBot(new ChangeStateAction(this, "Attack"));

                for(i = 0; i < this.damageTimes; ++i) {
                    GenericHelper.addToBotAbstract(() -> {
                        AbstractMonster m = GenericHelper.getLowestMonster();
                        if (m != null) {
                            if (this.meltdownNextTurn) {
                                //GenericHelper.addEffect(new MeltDownEffect(m));
                            }

                            ((DamageInfo)this.damage.get(0)).output = this.calculateDmg(m);
                            GenericHelper.addToNext(new DamageAction(m, (DamageInfo)this.damage.get(0), effect, true));
                        }

                    });
                }
            } else {
                this.addToBot(new ChangeStateAction(this, "Attack"));

                for(i = 0; i < this.damageTimes; ++i) {
                    GenericHelper.addToBotAbstract(() -> {
                        AbstractMonster m = GenericHelper.getRandomMonsterSafe();
                        if (m != null) {
                            if (this.meltdownNextTurn) {
                                //GenericHelper.addEffect(new MeltDownEffect(m));
                            }

                            ((DamageInfo)this.damage.get(0)).output = this.calculateDmg(m);
                            GenericHelper.addToNext(new DamageAction(m, (DamageInfo)this.damage.get(0), effect, true));
                        }

                    });
                }
            }
        }

    }

    public void die() {
        this.die(false);
        //MonsterPatch.ReviveTimer = 5;
    }

    public void die(boolean triggerRelic) {
        this.powers.forEach(AbstractPower::onDeath);
        this.changeState("Die");
        this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, 3, DamageType.THORNS)));
//        if (AbstractDungeon.player.hasPower(NonDamagingRestructuringPower.id)) {
//            int intentDmg = (Integer)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentDmg");
//            this.addToBot(new DamageAllEnemiesAction((AbstractCreature)null, DamageInfo.createDamageMatrix(intentDmg), DamageType.THORNS, AttackEffect.FIRE));
//        }

//        if (AbstractDungeon.player.hasPower(PerceptualCoexistencePower.id)) {
//            this.powers.forEach((p) -> {
//                if (p.type != PowerType.DEBUFF) {
//                    p.owner = AbstractDungeon.player;
//                    if (p.amount / 2 > 0) {
//                        p.amount /= 2;
//                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, p));
//                    }
//
//                }
//            });
//        }

        if (!this.isDying) {
            this.isDying = true;
            if (this.currentHealth < 0) {
                this.currentHealth = 0;
            }

            ++this.deathTimer;
        }

    }

    @SpireOverride
    protected void updateIntentTip() {
        int intentDmg = (Integer)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentDmg");
        PowerTip tip = new PowerTip();
        if (this.damageToAllNextTurn) {
            tip.header = MOVES[4];
            tip.body = MOVES[5].replace("D", String.valueOf(intentDmg));
        } else if (this.damageToLowestNextTurn) {
            tip.header = MOVES[2];
            tip.body = MOVES[3].replace("D", String.valueOf(intentDmg));
        } else if (this.gainBlockNextTurn) {
            tip.header = MOVES[6];
            tip.body = MOVES[7].replace("D", String.valueOf(intentDmg));
        } else {
            tip.header = MOVES[0];
            tip.body = MOVES[1].replace("D", String.valueOf(intentDmg));
        }

        tip.img = this.getAttackIntent();
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentTip", tip);
    }

    @SpireOverride
    protected void calculateDamage(int dmg) {
        float tmp = (float)dmg;
        GenericHelper.info("raw damage:" + tmp);

        Iterator var3;
        AbstractPower po;
        for(var3 = this.powers.iterator(); var3.hasNext(); tmp = po.atDamageGive(tmp, DamageType.NORMAL)) {
            po = (AbstractPower)var3.next();
        }

        for(var3 = this.powers.iterator(); var3.hasNext(); tmp = po.atDamageFinalGive(tmp, DamageType.NORMAL)) {
            po = (AbstractPower)var3.next();
        }

        dmg = MathUtils.floor(tmp);
        if (dmg < 0) {
            dmg = 0;
        }

        GenericHelper.info("final damage:" + dmg);
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", dmg);
    }

    public void damage(DamageInfo info) {
        float damageAmount = (float)info.output;
        if (!this.isDying) {
            if (damageAmount < 0.0F) {
                damageAmount = 0.0F;
            }

            boolean hadBlock = this.currentBlock != 0;
            boolean weakenedToZero = damageAmount == 0.0F;
            damageAmount = (float)this.decrementBlock(info, (int)damageAmount);

            Iterator var5;
            AbstractPower po;
            for(var5 = this.powers.iterator(); var5.hasNext(); damageAmount = po.atDamageFinalReceive(damageAmount, info.type)) {
                po = (AbstractPower)var5.next();
            }

            if (info.owner != null) {
                for(var5 = info.owner.powers.iterator(); var5.hasNext(); damageAmount = po.atDamageFinalGive(damageAmount, info.type)) {
                    po = (AbstractPower)var5.next();
                }

                for(var5 = info.owner.powers.iterator(); var5.hasNext(); damageAmount = (float)po.onAttackToChangeDamage(info, (int)damageAmount)) {
                    po = (AbstractPower)var5.next();
                }
            }

            GenericHelper.info(this.name + "受到的伤害：" + damageAmount);

            for(var5 = this.powers.iterator(); var5.hasNext(); damageAmount = (float)po.onAttackedToChangeDamage(info, (int)damageAmount)) {
                po = (AbstractPower)var5.next();
            }

            var5 = this.powers.iterator();

            while(var5.hasNext()) {
                po = (AbstractPower)var5.next();
                po.wasHPLost(info, (int)damageAmount);
            }

            if (info.owner != null) {
                var5 = info.owner.powers.iterator();

                while(var5.hasNext()) {
                    po = (AbstractPower)var5.next();
                    po.onAttack(info, (int)damageAmount, this);
                }
            }

            for(var5 = this.powers.iterator(); var5.hasNext(); damageAmount = (float)po.onAttacked(info, (int)damageAmount)) {
                po = (AbstractPower)var5.next();
            }

            this.lastDamageTaken = Math.min((int)damageAmount, this.currentHealth);
            boolean probablyInstantKill = this.currentHealth == 0;
            if (damageAmount > 0.0F) {
                if (info.owner != null && info.owner != this) {
                    this.useStaggerAnimation();
                }

                this.currentHealth = (int)((float)this.currentHealth - damageAmount);
                if (!probablyInstantKill) {
                    AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, (int)damageAmount));
                }

                if (this.currentHealth < 0) {
                    this.currentHealth = 0;
                }

                this.healthBarUpdatedEvent();
            } else if (!probablyInstantKill) {
                if (weakenedToZero && this.currentBlock == 0) {
                    if (hadBlock) {
                        AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
                    } else {
                        AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, 0));
                    }
                } else if (Settings.SHOW_DMG_BLOCK) {
                    AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
                }
            }

            if (this.currentHealth <= 0) {
                this.die();
                if (this.currentBlock > 0) {
                    this.loseBlock();
                    AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
                }
            }
        }

    }

    public void applyPowers() {
        EnemyMoveInfo move = (EnemyMoveInfo)ReflectionHacks.getPrivate(this, AbstractMonster.class, "move");
        if (move.baseDamage > -1) {
            this.calculateDamage(move.baseDamage);
        }

        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentImg", this.getIntentImg());
        this.updateIntentTip();
    }

    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
        this.powers.forEach((p) -> {
            p.atEndOfTurn(true);
            p.atEndOfTurnPreEndTurnCards(true);
            p.atEndOfRound();
        });
    }

    public void addAnimation(String anim) {
        this.animationList.add(new AnimationInfo(anim, this.stateData.getSkeletonData().findAnimation(anim).getDuration()));
    }

    public void update() {
        super.update();
        this.updatePowers();
        this.hb.update();
        this.intentHb.update();
        this.healthHb.update();
        if (AbstractDungeon.player != null) {
            this.flipHorizontal = AbstractDungeon.player.flipHorizontal;
        }

        if (this.animationList.size() > 0 || this.currentAnim != null) {
            this.animationTimer -= Gdx.graphics.getDeltaTime();
            if (this.animationTimer <= 0.0F) {
                if (this.currentAnim != null) {
                    this.animationList.remove(0);
                }

                if (this.animationList.size() == 0) {
                    this.state.addAnimation(0, "Idle", true, 0.0F);
                    this.currentAnim = null;
                } else {
                    this.currentAnim = (AnimationInfo)this.animationList.get(0);
                    this.animationTimer = this.currentAnim.time;
                    this.state.setAnimation(0, this.currentAnim.anim, false);
                }
            }
        }

    }

    @SpireOverride
    protected void renderDamageRange(SpriteBatch sb) {
        int intentDmg = (Integer)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentDmg");
        BobEffect bobEffect = (BobEffect)ReflectionHacks.getPrivate(this, AbstractMonster.class, "bobEffect");
        Color intentColor = (Color)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentColor");
        String label = this.damageToAllNextTurn ? "->ALL" : (this.damageToLowestNextTurn ? "->LOW" : "");
        if (this.damageTimes > 1) {
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, intentDmg + "x" + this.damageTimes + label, this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + bobEffect.y - 12.0F * Settings.scale, intentColor);
        } else {
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, intentDmg + label, this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + bobEffect.y - 12.0F * Settings.scale, intentColor);
        }

    }

    public void render(SpriteBatch sb) {
        super.render(sb);
        if ((this.hb.hovered || this.intentHb.hovered) && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase != RoomPhase.COMPLETE) {
            this.renderTip(sb);
        }

    }

    @SpireOverride
    protected Texture getIntentImg() {
        this.intent = this.gainBlockNextTurn ? Intent.DEFEND : Intent.ATTACK;
        return (Texture)SpireSuper.call(new Object[0]);
    }

    static {
        STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = STRINGS.NAME;
        MOVES = STRINGS.MOVES;
        DIALOG = STRINGS.DIALOG;
    }

    public static class AnimationInfo {
        public String anim;
        public float time;

        public AnimationInfo(String anim, float time) {
            this.anim = anim;
            this.time = time;
        }
    }
}

