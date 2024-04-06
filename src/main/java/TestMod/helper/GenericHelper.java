package TestMod.helper;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenericHelper {
    private static final Logger logger = LogManager.getLogger(GenericHelper.class);

    public GenericHelper() {
    }

    public static void loop(int times, Consumer<Integer> consumer) {
        for(int i = 0; i < times; ++i) {
            consumer.accept(i);
        }

    }

    public static <T> ArrayList<T> getRandom(ArrayList<T> list, int amt, Random rnd, Predicate<T> filter) {
        ArrayList<T> tmp = new ArrayList();
        int val = list.size();
        if (list.size() <= amt) {
            return list;
        } else {
            while(tmp.size() < amt && val > 0) {
                T item = list.get(rnd.random(list.size() - 1));
                if (!filter.test(item)) {
                    --val;
                } else if (!tmp.stream().anyMatch((temp) -> {
                    return temp.equals(item);
                })) {
                    tmp.add(item);
                }
            }

            return tmp;
        }
    }

    public static <T> T getRandom(ArrayList<T> list, Random rnd, Predicate<T> filter) {
        if (list.size() == 0) {
            return null;
        } else {
            for(int amt = list.size(); amt > 0; --amt) {
                T item = list.get(rnd.random(list.size() - 1));
                if (filter.test(item)) {
                    return item;
                }
            }

            return null;
        }
    }

    public static AbstractMonster getRandomMonsterSafe() {
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        return m != null && !m.isDeadOrEscaped() && !m.isDead ? m : null;
    }

    public static AbstractMonster getLowestMonster() {
        AbstractMonster m = null;
        if (AbstractDungeon.getMonsters() != null) {
            Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster mo = (AbstractMonster)var1.next();
                if (isAlive(mo)) {
                    if (m == null) {
                        m = mo;
                    } else if (mo.currentHealth < m.currentHealth) {
                        m = mo;
                    }
                }
            }
        }

        return m;
    }

    public static boolean isInBattle() {
        return CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT;
    }

    public static ArrayList<AbstractMonster> monsters() {
        return AbstractDungeon.getMonsters().monsters;
    }

    public static boolean isAlive(AbstractCreature c) {
        return c != null && !c.isDeadOrEscaped() && !c.isDead;
    }

    public static int aliveMonstersAmount() {
        int i = 0;
        Iterator var1 = monsters().iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if (!m.isDeadOrEscaped() && !m.isDead) {
                ++i;
            }
        }

        return i;
    }

    public static AbstractMonster getFrontMonster() {
        ArrayList<AbstractMonster> list = (ArrayList)monsters().stream().filter((mx) -> {
            return isAlive(mx);
        }).collect(Collectors.toList());
        info(list.toString());
        if (list.size() > 0) {
            AbstractMonster target = (AbstractMonster)list.get(0);
            Iterator var2 = list.iterator();

            while(var2.hasNext()) {
                AbstractMonster m = (AbstractMonster)var2.next();
                if (m.hb.cX < target.hb.cX) {
                    target = m;
                }
            }

            return target;
        } else {
            return null;
        }
    }

    public static void MoveMonster(AbstractMonster m, float x, float y) {
        m.drawX = x;
        m.drawY = y;
        m.animX = 0.0F;
        m.animY = 0.0F;
        m.hb.move(m.drawX + m.hb_x, m.drawY + m.hb_y + m.hb_h / 2.0F);
        m.healthHb.move(m.hb.cX, m.hb.cY - m.hb_h / 2.0F - m.healthHb.height / 2.0F);
        m.refreshIntentHbLocation();
    }

    public static void addToNext(AbstractGameAction action) {
        if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            int index = Math.min(AbstractDungeon.actionManager.actions.size(), 1);
            AbstractDungeon.actionManager.actions.add(index, action);
        }

    }

    public static void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static void addToBotAbstract(final VoidSupplier func) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                func.get();
                this.isDone = true;
            }
        });
    }

    public static void applyPowerToSelf(AbstractCreature source, Function<AbstractCreature, AbstractPower> fac) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(source, source, (AbstractPower)fac.apply(source)));
    }

    public static void applyPowerTo(AbstractCreature source, Function<AbstractCreature, AbstractPower> fac, AbstractCreature... targets) {
        AbstractCreature[] var3 = targets;
        int var4 = targets.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            AbstractCreature t = var3[var5];
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(source, t, (AbstractPower)fac.apply(t)));
        }

    }

    public static void addEffect(AbstractGameEffect effect) {
        AbstractDungeon.effectList.add(effect);
    }

    public static void GainRelic(AbstractRelic r) {
        AbstractDungeon.player.relics.add(r);
        r.onEquip();
        AbstractDungeon.player.reorganizeRelics();
    }

    public static void info(String s) {
        logger.info(s);
    }

    public static AbstractCard makeStatEquivalentCopy(AbstractCard c) {
        AbstractCard card = c.makeStatEquivalentCopy();
        card.retain = c.retain;
        card.selfRetain = c.selfRetain;
        card.purgeOnUse = c.purgeOnUse;
        card.isEthereal = c.isEthereal;
        card.exhaust = c.exhaust;
        card.glowColor = c.glowColor;
        card.rawDescription = c.rawDescription;
        card.cardsToPreview = c.cardsToPreview;
        card.initializeDescription();
        return card;
    }

    public static void foreachCardNotExhausted(Function<AbstractCard, Boolean> func) {
        Iterator var1 = AbstractDungeon.player.drawPile.group.iterator();

        AbstractCard c;
        do {
            if (!var1.hasNext()) {
                var1 = AbstractDungeon.player.hand.group.iterator();

                do {
                    if (!var1.hasNext()) {
                        var1 = AbstractDungeon.player.discardPile.group.iterator();

                        do {
                            if (!var1.hasNext()) {
                                return;
                            }

                            c = (AbstractCard)var1.next();
                        } while(!(Boolean)func.apply(c));

                        return;
                    }

                    c = (AbstractCard)var1.next();
                } while(!(Boolean)func.apply(c));

                return;
            }

            c = (AbstractCard)var1.next();
        } while(!(Boolean)func.apply(c));

    }

    public static void foreachCardNotExhaustedNotHand(Function<AbstractCard, Boolean> func) {
        Iterator var1 = AbstractDungeon.player.drawPile.group.iterator();

        AbstractCard c;
        do {
            if (!var1.hasNext()) {
                var1 = AbstractDungeon.player.discardPile.group.iterator();

                do {
                    if (!var1.hasNext()) {
                        return;
                    }

                    c = (AbstractCard)var1.next();
                } while(!(Boolean)func.apply(c));

                return;
            }

            c = (AbstractCard)var1.next();
        } while(!(Boolean)func.apply(c));

    }

    public static void foreachPowerHeroAndMonstersHave(Function<AbstractPower, Boolean> func) {
        Iterator var1 = AbstractDungeon.player.powers.iterator();

        while(var1.hasNext()) {
            AbstractPower p = (AbstractPower)var1.next();
            if ((Boolean)func.apply(p)) {
                return;
            }
        }

        var1 = monsters().iterator();

        while(true) {
            AbstractMonster m;
            do {
                if (!var1.hasNext()) {
                    return;
                }

                m = (AbstractMonster)var1.next();
            } while(!isAlive(m));

            Iterator var3 = m.powers.iterator();

            while(var3.hasNext()) {
                AbstractPower p = (AbstractPower)var3.next();
                if ((Boolean)func.apply(p)) {
                    return;
                }
            }
        }
    }

    public static void foreachAliveMonster(Function<AbstractMonster, Boolean> func) {
        Iterator var1 = monsters().iterator();

        AbstractMonster m;
        do {
            if (!var1.hasNext()) {
                return;
            }

            m = (AbstractMonster)var1.next();
        } while(!isAlive(m) || !(Boolean)func.apply(m));

    }

    public static void tempLoseStrength(AbstractCreature mo, AbstractCreature p, int amt) {
        addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -amt), -amt, true, AttackEffect.NONE));
        if (!mo.hasPower("Artifact")) {
            addToBot(new ApplyPowerAction(mo, p, new GainStrengthPower(mo, amt), amt, true, AttackEffect.NONE));
        }

    }

    public static void queueExtraCard(AbstractCard card, AbstractMonster m) {
        AbstractCard tmp = card.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(tmp);
        tmp.current_x = card.current_x;
        tmp.current_y = card.current_y;
        tmp.target_x = MathUtils.random((float)Settings.WIDTH * 0.2F, (float)Settings.WIDTH * 0.8F);
        tmp.target_y = MathUtils.random((float)Settings.HEIGHT * 0.3F, (float)Settings.HEIGHT * 0.7F);

        for(int i = AbstractDungeon.actionManager.cardQueue.size() - 1; i > -1; --i) {
            CardQueueItem item = (CardQueueItem)AbstractDungeon.actionManager.cardQueue.get(i);
            if (item.card == null) {
                AbstractDungeon.actionManager.cardQueue.remove(item);
            }
        }

        if (m != null) {
            tmp.calculateCardDamage(m);
        }

        tmp.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
    }

    public interface VoidSupplier {
        void get();
    }
}

