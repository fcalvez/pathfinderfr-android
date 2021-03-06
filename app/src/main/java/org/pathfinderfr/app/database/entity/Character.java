package org.pathfinderfr.app.database.entity;

import android.util.Log;

import org.pathfinderfr.app.database.DBHelper;
import org.pathfinderfr.app.util.CharacterUtil;
import org.pathfinderfr.app.util.Pair;
import org.pathfinderfr.app.util.Triplet;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Character extends DBEntity {

    public static final int ABILITY_STRENGH      = 0;
    public static final int ABILITY_DEXTERITY    = 1;
    public static final int ABILITY_CONSTITUTION = 2;
    public static final int ABILITY_INTELLIGENCE = 3;
    public static final int ABILITY_WISDOM       = 4;
    public static final int ABILITY_CHARISMA     = 5;

    public static final int SIZE_FINE        = 1;
    public static final int SIZE_DIMINUTIVE  = 2;
    public static final int SIZE_TINY        = 3;
    public static final int SIZE_SMALL       = 4;
    public static final int SIZE_MEDIUM      = 5;
    public static final int SIZE_LARGE_TALL  = 6;
    public static final int SIZE_LARGE_LONG  = 7;
    public static final int SIZE_HUGE_TALL   = 8;
    public static final int SIZE_HUGE_LONG   = 9;
    public static final int SIZE_GARG_TALL   = 10;
    public static final int SIZE_GARG_LONG   = 11;
    public static final int SIZE_COLO_TALL   = 12;
    public static final int SIZE_COLO_LONG   = 13;

    public static final int ALIGN_LG = 1;
    public static final int ALIGN_NG = 2;
    public static final int ALIGN_CG = 3;
    public static final int ALIGN_LN = 4;
    public static final int ALIGN_N = 5;
    public static final int ALIGN_CN = 6;
    public static final int ALIGN_LE = 7;
    public static final int ALIGN_NE = 8;
    public static final int ALIGN_CE = 9;

    public static final int SEX_M = 1;
    public static final int SEX_F = 2;

    public static final int SPEED_MANEUV_CLUMBSY = 1;
    public static final int SPEED_MANEUV_POOR    = 2;
    public static final int SPEED_MANEUV_AVERAGE = 3;
    public static final int SPEED_MANEUV_GOOD    = 4;
    public static final int SPEED_MANEUV_PERFECT = 5;

    public static final int MODIF_ABILITY_ALL = 1;
    public static final int MODIF_ABILITY_STR = 2;
    public static final int MODIF_ABILITY_DEX = 3;
    public static final int MODIF_ABILITY_CON = 4;
    public static final int MODIF_ABILITY_INT = 5;
    public static final int MODIF_ABILITY_WIS = 6;
    public static final int MODIF_ABILITY_CHA = 7;

    public static final int MODIF_SAVES_ALL     = 11;
    public static final int MODIF_SAVES_REF     = 12;
    public static final int MODIF_SAVES_FOR     = 13;
    public static final int MODIF_SAVES_WIL     = 14;
    public static final int MODIF_SAVES_MAG_ALL = 15;
    public static final int MODIF_SAVES_MAG_REF = 16;
    public static final int MODIF_SAVES_MAG_FOR = 17;
    public static final int MODIF_SAVES_MAG_WIL = 18;


    public static final int MODIF_COMBAT_INI = 21;
    public static final int MODIF_COMBAT_AC = 22;
    public static final int MODIF_COMBAT_MAG = 23;
    public static final int MODIF_COMBAT_HP = 24; // not supported
    public static final int MODIF_COMBAT_SPEED = 25;
    public static final int MODIF_COMBAT_MAG_LVL = 123;

    public static final int MODIF_COMBAT_AC_ARMOR = 26;
    public static final int MODIF_COMBAT_AC_SHIELD = 27;
    public static final int MODIF_COMBAT_AC_NATURAL = 28;
    public static final int MODIF_COMBAT_AC_PARADE = 29;

    public static final int MODIF_COMBAT_ATT_MELEE = 31;
    public static final int MODIF_COMBAT_ATT_RANGED = 32;
    public static final int MODIF_COMBAT_CMB = 33;
    public static final int MODIF_COMBAT_CMD = 34;
    public static final int MODIF_COMBAT_DAM_MELEE = 35;
    public static final int MODIF_COMBAT_DAM_RANGED = 36;

    public static final int MODIF_SKILL_ALL = 41;
    public static final int MODIF_SKILL_FOR = 42;
    public static final int MODIF_SKILL_DEX = 43;
    public static final int MODIF_SKILL_CON = 44; // doesn't exist
    public static final int MODIF_SKILL_INT = 45;
    public static final int MODIF_SKILL_WIS = 46;
    public static final int MODIF_SKILL_CHA = 47;

    public static final int MODIF_SKILL = 200;

    // character-specific
    private String uniqID;
    private int[] abilities;
    private Race race;
    private List<Triplet<Class,ClassArchetype,Integer>> classes;
    private Map<Long,CSkill> skills;
    private List<Feat> feats;
    private List<ClassFeature> features;
    private List<Trait> traits;
    private List<CharacterModif> modifs;
    private List<InventoryItem> invItems;
    private int hitpoints, hitpointsTemp;
    private int speed;
    private List<Spell> spells;

    // infos additionnelles
    private String player;
    private int alignment;
    private String divinity;
    private String origin;
    private int sizeType;
    private int sex;
    private int age;
    private int height;
    private int weight;
    private String hair;
    private String eyes;
    private int speedWithArmor;
    private int speedDig;
    private int speedFly;
    private int speedFlyManeuv;
    private String languages;
    private int experience;

    // money
    private int moneyCP, moneySP, moneyGP, moneyPP;

    // others
    private int maxSkillRanks;

    public Character() {
        abilities = new int[] { 10, 10, 10, 10, 10, 10 };
        classes = new ArrayList<>();
        skills = new HashMap<>();
        feats = new ArrayList<>();
        features = new ArrayList<>();
        traits = new ArrayList<>();
        modifs = new ArrayList<>();
        invItems = new ArrayList<>();
        spells = new ArrayList<>();
    }

    public static class CSkill {
        private Long skillId;         // reference to skill
        private int rank;             // current rank
        private boolean isClassSkill; // manually added class skill
        public CSkill(long skillId, int rank) { this.skillId = skillId; this.rank = rank; isClassSkill = false;}
        public Long getSkillId() { return skillId; }
        public void setSkillId(Long skillId) { this.skillId = skillId; }
        public int getRank() { return rank; }
        public void setRank(int rank) { this.rank = rank; }
        public boolean isClassSkill() { return isClassSkill; }
        public void setClassSkill(boolean classSkill) { isClassSkill = classSkill; }
    }

    // Helper to keep modifs
    public static class CharacterModif {
        private String source;
        private List<Pair<Integer,Integer>> modifs;
        private String icon;
        private int linkToWeapon;
        private boolean enabled;
        public CharacterModif(String source, List<Pair<Integer,Integer>> modifs, String icon, int linkToWeapon) {
            this(source, modifs, icon, linkToWeapon, false);
        }
        public CharacterModif(String source, List<Pair<Integer,Integer>> modifs, String icon, int linkToWeapon, boolean enabled) {
            this.source = source;
            this.modifs = modifs;
            this.icon = icon;
            this.enabled = enabled;
            this.linkToWeapon = linkToWeapon;
        }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public int getModifCount() { return modifs == null ? 0 : modifs.size(); }
        public void setModifs(List<Pair<Integer,Integer>> modifs) { this.modifs = modifs; }
        public void setLinkToWeapon(int linkToIdx) { this.linkToWeapon = linkToIdx; }
        public int getLinkToWeapon() { return this.linkToWeapon; }
        public Pair<Integer,Integer> getModif(int index) { return index < 0 || index >= modifs.size() ? null : modifs.get(index); }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled;}
        public boolean isValid() { return source != null && source.length() > 0 && modifs != null && modifs.size() > 0 && icon != null && icon.length() > 0; }
        public void update(CharacterModif modif) {
            source = modif.source;
            modifs = modif.modifs;
            icon = modif.icon;
            linkToWeapon = modif.linkToWeapon;
        }
    }

    // Helper to keep inventory
    public static class InventoryItem implements Comparable<InventoryItem> {
        public static final long IDX_WEAPONS = 0L;
        public static final long IDX_ARMORS = 1000000L;
        public static final long IDX_EQUIPMENT = 2000000L;
        public static final long IDX_MAGICITEM = 3000000L;

        private int id; // only used for linktoweapon (modifs)
        private String name;
        private int weight;
        private long price;
        private long objectId; // reference to original object
        private String infos; // additional info (ex: ammo)
        public InventoryItem(String name, int weight, long price, long objectId, String infos) {
            this.name = name;
            this.weight = weight;
            this.price = price;
            this.objectId = objectId;
            this.infos = infos;
        }
        public InventoryItem(InventoryItem copy) {
            set(copy);
        }
        public void set(InventoryItem copy) {
            this.name = copy.name;
            this.weight = copy.weight;
            this.price = copy.price;
            this.objectId = copy.objectId;
            this.infos = copy.infos;
        }
        public void setId(int id) { this.id = id; }
        public int getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getWeight() { return weight; }
        public void setWeight(int weight) { this.weight = weight; }
        public long getPrice() { return price; }
        public void setPrice(long price) { this.price = price; }
        public long getObjectId() { return this.objectId; }
        public String getInfos() { return this.infos; }
        public boolean isValid() { return name != null && name.length() >= 3 && weight >= 0; }
        public boolean isNotLinked() { return objectId <= 0; }
        public boolean isWeapon() { return objectId > IDX_WEAPONS && objectId < IDX_ARMORS; }
        public boolean isArmor() { return objectId > IDX_ARMORS && objectId < IDX_EQUIPMENT; }
        public boolean isEquipment() { return objectId > IDX_EQUIPMENT && objectId < IDX_MAGICITEM; }
        public boolean isMagicItem() { return objectId > IDX_MAGICITEM; }

        @Override
        public int compareTo(InventoryItem item) {
            return Collator.getInstance().compare(getName(),item.getName());
        }
    }

    @Override
    public String getNameLong() {
        String name = getName() == null ? "?" : getName();
        String race = getRace() == null ? "?" : getRace().getName();
        StringBuffer classes = new StringBuffer();
        for(int i = 0; i < getClassesCount(); i++) {
            Triplet<Class,ClassArchetype,Integer> cl = getClass(i);
            classes.append(", ").append(cl.first.getNameShort()).append(" ").append(cl.third);
        }
        return name + " (" + race + classes.toString() + ")";
    }

    @Override
    public DBEntityFactory getFactory() {
        return CharacterFactory.getInstance();
    }

    public int getAbilityValue(int ability, boolean withModif) {
        if(ability <0  || ability >= abilities.length) {
            return 0;
        }
        if(!withModif) {
            return abilities[ability];
        }
        // check if modif is applied
        int bonus = 0;
        bonus += getAdditionalBonus(MODIF_ABILITY_ALL);
        bonus += getAdditionalBonus(ability+2); // MODIF_ABILITY = ABILITY_ID + 2 (see above)
        return abilities[ability] + bonus;
    }

    public int getAbilityValue(int ability) {
        return getAbilityValue(ability, true);
    }

    public void setAbilityValue(int ability, int value) {
        if(ability <0  || ability >= abilities.length) {
            return;
        }
        abilities[ability] = value;
    }

    public int getAbilityModif(int ability) {
        return CharacterUtil.getAbilityBonus(getAbilityValue(ability));
    }

    public void setUniqID(String uuid) {
        if(uuid != null && uuid.length() > 0) {
            uniqID = uuid;
        }
    }

    public boolean hasUUID() {
        return uniqID != null;
    }

    public String getShortUniqID() {
        if(uniqID != null) {
            Pattern p = Pattern.compile("([\\da-f]+)-.*-([\\da-f]+)");   // the pattern to search for
            Matcher m = p.matcher(uniqID);
            if (m.find()) {
               return String.format("%s-***-%s", m.group(1).substring(0,4), m.group(2).substring(0,4));
            }
        }
        return "-";
    }

    public synchronized String getUniqID() {
        if(uniqID == null) {
            uniqID = UUID.randomUUID().toString();
        }
        return uniqID;
    }

    public int getStrength() { return getAbilityValue(ABILITY_STRENGH); }
    public int getStrengthModif() { return getAbilityModif(ABILITY_STRENGH); }
    public void setStrength(int value) { setAbilityValue(ABILITY_STRENGH, value); }
    public int getDexterity() { return getAbilityValue(ABILITY_DEXTERITY); }
    public int getDexterityModif() { return getAbilityModif(ABILITY_DEXTERITY); }
    public void setDexterity(int value) { setAbilityValue(ABILITY_DEXTERITY, value); }
    public int getConstitution() { return getAbilityValue(ABILITY_CONSTITUTION); }
    public int getConstitutionModif() { return getAbilityModif(ABILITY_CONSTITUTION); }
    public void setConstitution(int value) { setAbilityValue(ABILITY_CONSTITUTION, value); }
    public int getIntelligence() { return getAbilityValue(ABILITY_INTELLIGENCE); }
    public int getIntelligenceModif() { return getAbilityModif(ABILITY_INTELLIGENCE); }
    public void setIntelligence(int value) { setAbilityValue(ABILITY_INTELLIGENCE, value); }
    public int getWisdom() { return getAbilityValue(ABILITY_WISDOM); }
    public int getWisdomModif() { return getAbilityModif(ABILITY_WISDOM); }
    public void setWisdom(int value) { setAbilityValue(ABILITY_WISDOM, value); }
    public int getCharisma() { return getAbilityValue(ABILITY_CHARISMA); }
    public int getCharismaModif() { return getAbilityModif(ABILITY_CHARISMA); }
    public void setCharisma(int value) { setAbilityValue(ABILITY_CHARISMA, value); }

    public int getAlignment() { return alignment; }
    public void setAlignment(int alignment) { this.alignment = alignment; }
    public String getPlayer() { return player; }
    public void setPlayer(String player) { this.player = player; }
    public String getDivinity() { return divinity; }
    public void setDivinity(String divinity) { this.divinity = divinity; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public int getSizeType() { return sizeType; }
    public void setSizeType(int sizeType) { this.sizeType = sizeType; }
    public int getSex() { return sex; }
    public void setSex(int sex) { this.sex = sex; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public String getHair() { return hair; }
    public void setHair(String hair) { this.hair = hair; }
    public String getEyes() { return eyes; }
    public void setEyes(String eyes) { this.eyes = eyes; }
    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public int getMoneyCP() { return moneyCP; }
    public void setMoneyCP(int value) { this.moneyCP = value; }
    public int getMoneySP() { return moneySP; }
    public void setMoneySP(int value) { this.moneySP = value; }
    public int getMoneyGP() { return moneyGP; }
    public void setMoneyGP(int value) { this.moneyGP = value; }
    public int getMoneyPP() { return moneyPP; }
    public void setMoneyPP(int value) { this.moneyPP = value; }
    public int getMaxSkillRanks() { return maxSkillRanks; }
    public void setMaxSkillRanks(int value) { this.maxSkillRanks = value; }

    /**
     * @param abilityId ability identifier
     * @return ability modifier
     */
    private int getAbilityModif(String abilityId) {
        if(abilityId == null) {
            return 0;
        }
        // TODO: make it language-independant
        switch(abilityId) {
            case "FOR": return getStrengthModif();
            case "DEX": return getDexterityModif();
            case "CON": return getConstitutionModif();
            case "INT": return getIntelligenceModif();
            case "SAG": return getWisdomModif();
            case "CHA": return getCharismaModif();
            default: return 0;
        }
    }

    public int getAbilityBonus(String abilityId) {
        if(abilityId == null) {
            return 0;
        }
        // TODO: make it language-independant
        switch(abilityId) {
            case "FOR": return MODIF_SKILL_FOR;
            case "DEX": return MODIF_SKILL_DEX;
            case "CON": return MODIF_SKILL_CON;
            case "INT": return MODIF_SKILL_INT;
            case "SAG": return MODIF_SKILL_WIS;
            case "CHA": return MODIF_SKILL_CHA;
            default: return 0;
        }
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public int getHitpointsTemp() {
        return hitpointsTemp;
    }

    public void setHitpointsTemp(int hitpointsTemp) {
        this.hitpointsTemp = hitpointsTemp;
    }

    public float getSpeedAsMeters() {
        int bonus = getAdditionalBonus(MODIF_COMBAT_SPEED);
        return 1.5f * (speed + bonus);
    }

    public int getSpeed() {
        int bonus = getAdditionalBonus(MODIF_COMBAT_SPEED);
        return speed + bonus;
    }

    public int getBaseSpeed() {
        return speed;
    }

    public int getBaseSpeedWithArmor() {
        return speedWithArmor;
    }

    public int getBaseSpeedDig() {
        return speedDig;
    }

    public int getBaseSpeedFly() {
        return speedFly;
    }

    public int getBaseSpeedManeuverability() {
        return speedFlyManeuv;
    }

    public int getSpeedWithArmor() {
        int bonus = getAdditionalBonus(MODIF_COMBAT_SPEED);
        return speedWithArmor + bonus;
    }

    public float getSpeedSwimming() {
        return getSpeed() / 4f;
    }

    public float getSpeedClimbing() {
        return getSpeed() / 4f;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setSpeedWithArmor(int speed) {
        this.speedWithArmor = speed;
    }

    public void setSpeedDig(int speed) {
        this.speedDig = speed;
    }

    public void setSpeedFly(int speed) {
        this.speedFly = speed;
    }

    public void setSpeedManeuverability(int speed) {
        this.speedFlyManeuv = speed;
    }

    public Race getRace() {
        return race;
    }

    public String getRaceName() {
        return race == null ? "-" : race.getName();
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Triplet<Class,ClassArchetype,Integer> getClass(int idx) {
        if(idx >= classes.size()) {
            return null;
        }
        return classes.get(idx);
    }

    public String getClassNames() {
        if(classes.size() == 0) {
            return "-";
        }
        StringBuffer buf = new StringBuffer();
        for(Triplet<Class,ClassArchetype,Integer> cl : classes) {
            buf.append(cl.first.getName()).append(' ').append(cl.third).append('/');
        }
        buf.deleteCharAt(buf.length()-1);
        return buf.toString();
    }

    public int getClassesCount() {
        return classes.size();
    }

    public void addOrSetClass(Class cl, ClassArchetype arch, int level) {
        // check that this class is not already in
        for(int i=0; i<classes.size(); i++) {
            Triplet<Class,ClassArchetype,Integer> c = classes.get(i);
            if(c.first.getId() == cl.getId()) {
                classes.set(i, new Triplet<Class,ClassArchetype,Integer>(c.first, arch, level));
                Collections.sort(classes, new ClassComparator());
                return;
            }
        }
        classes.add(new Triplet<Class,ClassArchetype,Integer>(cl, arch, level));
        Collections.sort(classes, new ClassComparator());
    }

    public void removeClass(Class cl) {
        Triplet<Class,ClassArchetype,Integer> found = null;
        for(Triplet<Class,ClassArchetype,Integer> c : classes) {
            if(c.first.getId() == cl.getId()) {
                found = c;
                break;
            }
        }
        if(found != null) {
            classes.remove(found);
        }
    }

    /**
     * @param id id not to include
     * @return the list of class ids (except the provided one)
     */
    public long[] getOtherClassesIds(long id) {
        List<Long> list = new ArrayList<>();
        for(Triplet<Class,ClassArchetype,Integer> c : classes) {
            if(c.first.getId() != id) {
                list.add(c.first.getId());
            }
        }
        long[] result = new long[list.size()];
        for(int i=0; i<list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    /**
     * @param id id not to include
     * @return the total number of level (excluding the provided one)
     */
    public int getOtherClassesLevel(long id) {
        int total = 0;
        for(Triplet<Class,ClassArchetype,Integer> c : classes) {
            if(c.first.getId() != id) {
                total+=c.third;
            }
        }
        return total;
    }

    /**
     * @return character's level
     */
    public int getLevel() {
        return getOtherClassesLevel(-1);
    }

    /**
     * Sort by level (higher first) then name
     */
    private class ClassComparator implements java.util.Comparator<Triplet<Class,ClassArchetype,Integer>> {

        @Override
        public int compare(Triplet<Class,ClassArchetype,Integer> p1, Triplet<Class,ClassArchetype,Integer> p2) {
            if(p1 == null || p2 == null) {
                return 0;
            } else if(p1.third != p2.third) {
                return Long.compare(p2.third,p1.third);
            } else {
                return p1.first.getName().compareTo(p2.first.getName());
            }

        }
    }

    public int getRaceSize() {
        if(race == null) {
            return SIZE_MEDIUM;
        }
        for(Race.Trait t : race.getTraits()) {
            // TODO: improve size handling to be language independent
            if("Petite taille".equalsIgnoreCase(t.getName())) {
                return SIZE_SMALL;
            }
        }
        return SIZE_MEDIUM;
    }

    private static int getSizeModifier(int size) {
        switch(size) {
            case SIZE_FINE: return 8;
            case SIZE_DIMINUTIVE: return 4;
            case SIZE_TINY: return 2;
            case SIZE_SMALL: return 1;
            case SIZE_LARGE_TALL: return -1;
            case SIZE_LARGE_LONG: return -1;
            case SIZE_HUGE_TALL: return -2;
            case SIZE_HUGE_LONG: return -2;
            case SIZE_GARG_TALL: return -4;
            case SIZE_GARG_LONG: return -4;
            case SIZE_COLO_TALL: return -8;
            case SIZE_COLO_LONG: return -8;
            default: return 0;
        }
    }

    public int getInitiative() { return getDexterityModif() + getAdditionalBonus(MODIF_COMBAT_INI); }
    public int getArmorClass() {
        int bonus_armor = getAdditionalBonus(MODIF_COMBAT_AC_ARMOR);
        int bonus_shield = getAdditionalBonus(MODIF_COMBAT_AC_SHIELD);
        int bonus_size = getSizeModifierArmorClass();
        int bonus_natural = getAdditionalBonus(MODIF_COMBAT_AC_NATURAL);
        int bonus_parade = getAdditionalBonus(MODIF_COMBAT_AC_PARADE);
        int bonus_other = getAdditionalBonus(MODIF_COMBAT_AC);
        return 10 + bonus_armor + bonus_shield + getDexterityModif() + bonus_size + bonus_natural + bonus_parade + bonus_other;
    }

    public int getArmorClassContact() {
        int bonus_size = getSizeModifierArmorClass();
        int bonus_parade = getAdditionalBonus(MODIF_COMBAT_AC_PARADE);
        int bonus_other = getAdditionalBonus(MODIF_COMBAT_AC);
        return 10 + getDexterityModif() + bonus_size + bonus_parade + bonus_other;
    }

    public int getArmorClassFlatFooted() {
        int bonus_dex = getDexterityModif();
        int bonus_armor = getAdditionalBonus(MODIF_COMBAT_AC_ARMOR);
        int bonus_shield = getAdditionalBonus(MODIF_COMBAT_AC_SHIELD);
        int bonus_size = getSizeModifierArmorClass();
        int bonus_natural = getAdditionalBonus(MODIF_COMBAT_AC_NATURAL);
        int bonus_other = getAdditionalBonus(MODIF_COMBAT_AC);
        return 10 + bonus_armor + bonus_shield + ( bonus_dex < 0 ? bonus_dex : 0 ) + bonus_size + bonus_natural + bonus_other;
    }

    public int getSizeModifierAttack() {
        return getSizeModifier(getSizeType());
    }

    public int getSizeModifierArmorClass() {
        return getSizeModifier(getSizeType());
    }

    public int getSizeModifierManeuver() {
        return -getSizeModifier(getSizeType());
    }

    public String getArmorClassDetails() {
        StringBuffer buf = new StringBuffer();
        int bonus_armor = getAdditionalBonus(MODIF_COMBAT_AC_ARMOR);
        int bonus_shield = getAdditionalBonus(MODIF_COMBAT_AC_SHIELD);
        int bonus_dex = getDexterityModif();
        int bonus_size = getSizeModifierArmorClass();
        int bonus_natural = getAdditionalBonus(MODIF_COMBAT_AC_NATURAL);
        int bonus_parade = getAdditionalBonus(MODIF_COMBAT_AC_PARADE);
        int bonus_other = getAdditionalBonus(MODIF_COMBAT_AC);
        if(bonus_armor != 0) {
            buf.append(String.format("armure %+d, ", bonus_armor));
        }
        if(bonus_shield != 0) {
            buf.append(String.format("bouclier %+d, ", bonus_shield));
        }
        if(bonus_dex != 0) {
            buf.append(String.format("Dex %+d, ", bonus_dex));
        }
        if(bonus_size != 0) {
            buf.append(String.format("taille %+d, ", bonus_size));
        }
        if(bonus_natural != 0) {
            buf.append(String.format("naturelle %+d, ", bonus_natural));
        }
        if(bonus_parade != 0) {
            buf.append(String.format("parade %+d, ", bonus_parade));
        }
        if(bonus_other != 0) {
            buf.append(String.format("divers %+d, ", bonus_other));
        }
        if(buf.length()>0) {
            buf.delete(buf.length()-2,buf.length());
        }
        return buf.toString();
    }

    public int getMagicResistance() {
        int bonus = getAdditionalBonus(MODIF_COMBAT_MAG);
        return bonus;
    }

    public int getSavingThrowsReflexesTotal() { return getDexterityModif() + getSavingThrowsReflexes() + getSavingThrowsBonus(MODIF_SAVES_REF); }
    public int getSavingThrowsFortitudeTotal() { return getConstitutionModif() + getSavingThrowsFortitude() + getSavingThrowsBonus(MODIF_SAVES_FOR); }
    public int getSavingThrowsWillTotal() { return getWisdomModif() + getSavingThrowsWill() + getSavingThrowsBonus(MODIF_SAVES_WIL); }

    /**
     * @return saving throws based on attached classes (and levels)
     */
    public int getSavingThrowsReflexes() {
        if(classes == null || classes.size() == 0) {
            return 0;
        }
        int total = 0;
        for(Triplet<Class,ClassArchetype,Integer> cl : classes) {
            // find matching level (should be in order but ...)
            boolean found = false;
            for(Class.Level lvl: cl.first.getLevels()) {
                if(lvl.getLvl() == cl.third) {
                    total+=lvl.getReflexBonus();
                    found = true;
                    break;
                }
            }
            if(!found) {
                Log.w(Character.class.getSimpleName(), String.format("Couldn't find saving throws for %s and level %d", cl.first.getName(), cl.third));
            }
        }
        return total;
    }

    /**
     * @return saving throws based on attached classes (and levels)
     */
    public int getSavingThrowsFortitude() {
        if(classes == null || classes.size() == 0) {
            return 0;
        }
        int total = 0;
        for(Triplet<Class,ClassArchetype,Integer> cl : classes) {
            // find matching level (should be in order but ...)
            boolean found = false;
            for(Class.Level lvl: cl.first.getLevels()) {
                if(lvl.getLvl() == cl.third) {
                    total+=lvl.getFortitudeBonus();
                    found = true;
                    break;
                }
            }
            if(!found) {
                Log.w(Character.class.getSimpleName(), String.format("Couldn't find saving throws for %s and level %d", cl.first.getName(), cl.third));
            }
        }
        return total;
    }

    /**
     * @return saving throws based on attached classes (and levels)
     */
    public int getSavingThrowsWill() {
        if(classes == null || classes.size() == 0) {
            return 0;
        }
        int total = 0;
        for(Triplet<Class,ClassArchetype,Integer> cl : classes) {
            // find matching level (should be in order but ...)
            boolean found = false;
            for(Class.Level lvl: cl.first.getLevels()) {
                if(lvl.getLvl() == cl.third) {
                    total+=lvl.getWillBonus();
                    found = true;
                    break;
                }
            }
            if(!found) {
                Log.w(Character.class.getSimpleName(), String.format("Couldn't find saving throws for %s and level %d", cl.first.getName(), cl.third));
            }
        }
        return total;
    }

    /**
     * @param bonusId bonusId for corresponding SavingThrows
     * @return bonus to be applied on savingthrows
     */
    public int getSavingThrowsBonus(int bonusId) {
        // check if modif is applied
        int bonus = 0;
        bonus += getAdditionalBonus(MODIF_SAVES_ALL);
        bonus += getAdditionalBonus(MODIF_SAVES_MAG_ALL);
        bonus += getAdditionalBonus(bonusId);
        bonus += getAdditionalBonus(bonusId+4); // magic bonus
        return bonus;
    }

    /**
     * @param bonusId bonusId for corresponding SavingThrows
     * @return bonus to be applied
     */
    public int getAdditionalBonus(int bonusId) {
        return getAdditionalBonus(bonusId, 0);
    }

        /**
         * @param bonusId bonusId for corresponding SavingThrows
         * @return bonus to be applied
         */
    public int getAdditionalBonus(int bonusId, int linkToWeapon) {
        // check if modif is applied
        int bonus = 0;
        List<CharacterModif> modifs = getModifsForId(bonusId);
        // getModifsForId always returns 1 modification (matching the one being searched)
        for(CharacterModif mod : modifs) {
            // special case for combat bonuses linked to weapons
            if((bonusId == MODIF_COMBAT_ATT_MELEE || bonusId == MODIF_COMBAT_ATT_RANGED
                || bonusId == MODIF_COMBAT_DAM_MELEE || bonusId == MODIF_COMBAT_DAM_RANGED) && mod.getLinkToWeapon() > 0) {
                if(linkToWeapon == mod.getLinkToWeapon()) {
                    bonus += mod.getModif(0).second;
                }
            }
            else if(mod.isEnabled()) {
                bonus += mod.getModif(0).second;
            }
        }
        return bonus;
    }

    /**
     * @param addBonus additional bonus
     * @return attack bonus (BAB) based on attached classes (and levels)
     */
    public int[] getAttackBonus(int addBonus) {
        if(classes == null || classes.size() == 0) {
            return null;
        }
        int maxBonus = 0;
        for(Triplet<Class,ClassArchetype,Integer> cl : classes) {
            // find matching level (should be in order but ...)
            boolean found = false;
            for(Class.Level lvl: cl.first.getLevels()) {
                if(lvl.getLvl() == cl.third) {
                    int[] bonus = lvl.getBaseAttackBonus();
                    if(bonus != null && bonus.length > 0) {
                        maxBonus += bonus[0];
                    }
                    found = true;
                    break;
                }
            }
            if(!found) {
                Log.w(Character.class.getSimpleName(), String.format("Couldn't find base attack bonus (bab) for %s and level %d", cl.first.getName(), cl.third));
            }
        }
        // build list
        List<Integer> bab = new ArrayList<>();
        while(maxBonus > 0) {
            bab.add(maxBonus);
            maxBonus -= 5;
            if(bab.size()==4) {
                break;
            }
        }

        // convert to int[] and add bonus
        int[] result = new int[bab.size()];
        for(int i=0; i<bab.size(); i++) {
            result[i]=bab.get(i) + addBonus;
        }
        return result;
    }

    /**
     * @return base attack bonus (BAB) based on attached classes (and levels)
     */
    public int getBaseAttackBonusBest() {
        int[] bab = getBaseAttackBonus();
        return bab != null && bab.length > 0 ? bab[0] : 0;
    }

    /**
     * @return base attack bonus (BAB) based on attached classes (and levels)
     */
    public int[] getBaseAttackBonus() {
        return getAttackBonus(0);
    }

    /**
     * @return base attack bonus (BAB) based on attached classes (and levels), as string
     */
    public String getBaseAttackBonusAsString() {
        return CharacterUtil.getAttackBonusAsString(getBaseAttackBonus());
    }

    /**
     * @return attack bonus (melee), as string
     */
    public String getAttackBonusMeleeAsString(int weaponIdx) {
        int addBonus = getAdditionalBonus(MODIF_COMBAT_ATT_MELEE, weaponIdx);
        addBonus += getSizeModifierAttack();
        return CharacterUtil.getAttackBonusAsString(getAttackBonus(addBonus + getStrengthModif()));
    }

    public String getAttackBonusAsString(boolean melee) {
        StringBuffer buf = new StringBuffer();
        int idx = 1;
        for(Weapon w : getInventoryWeapons()) {
            if(melee != w.isRanged()) {
                buf.append(w.getName()).append(", ");
                buf.append(melee ? getAttackBonusMeleeAsString(idx) : getAttackBonusRangeAsString(idx)).append(" ");
                buf.append('(').append(getDamage(w, idx)).append(") ");
            }
            idx++;
        }
        return buf.toString();
    }

    /**
     * @return damage bonus
     */
    public int getBonusDamage(Weapon w, int weaponIdx) {
        if(w == null) {
            return 0;
        }
        int bonus = w.getDamageBonus(getStrengthModif());
        bonus += getAdditionalBonus(w.isRanged() ? MODIF_COMBAT_DAM_RANGED : MODIF_COMBAT_DAM_MELEE, weaponIdx);;
        return bonus;
    }

    /**
     * @return damage as string
     */
    public String getDamage(Weapon w, int weaponIdx) {
        if(w == null) {
            return "";
        }
        String baseDamage = w.getDamageForSize(getSizeType());
        int damBonus = getBonusDamage(w, weaponIdx);
        if(damBonus == 0) {
            return baseDamage;
        } else {
            return String.format("%s %+d", baseDamage, damBonus);
        }
    }


    /**
     * @return attack bonus (range), as string
     */
    public String getAttackBonusRangeAsString(int weaponIdx) {
        int addBonus = getAdditionalBonus(MODIF_COMBAT_ATT_RANGED, weaponIdx);
        addBonus += getSizeModifierAttack();
        return CharacterUtil.getAttackBonusAsString(getAttackBonus(addBonus + getDexterityModif()));
    }

    /**
     * @return attack bonus (melee)
     */
    public int getAttackBestBonusRange(int weaponIdx) {
        int addBonus = getAdditionalBonus(MODIF_COMBAT_ATT_RANGED, weaponIdx);
        int bestBonus = getBaseAttackBonusBest();
        return bestBonus + addBonus + getDexterityModif();
    }

    /**
     * @return combat maneuver bonus (CMB) = BAB + STR + SIZE
     */
    public int getCombatManeuverBonus() {
        int[] bab = getBaseAttackBonus();
        int bonus = 0;
        bonus += getBaseAttackBonusBest();
        int sizeModif = getSizeModifier(getSizeType());
        int addBonus = getAdditionalBonus(MODIF_COMBAT_CMB);
        return bonus + getStrengthModif() - sizeModif + addBonus;
    }

    /**
     * @return combat maneuver defense (CMD) = 10 + BAB + STR + DEX + SIZE
     */
    public int getCombatManeuverDefense() {
        int[] bab = getBaseAttackBonus();
        int bonus = 0;
        bonus += getBaseAttackBonusBest();
        int sizeModif = getSizeModifier(getSizeType());
        int addBonus = getAdditionalBonus(MODIF_COMBAT_CMD);
        return 10 + bonus + getStrengthModif() + getDexterityModif() - sizeModif + addBonus;
    }

    /**
     * @param skillId skill identifier
     * @param rank ranks for that skill
     * @return true if rank was changed, false if nothing changed
     */
    public boolean setSkillRank(long skillId, int rank) {
        if(!skills.containsKey(skillId) && rank > 0) {
            skills.put(skillId, new CSkill(skillId, rank));
            return true;
        } else if(skills.containsKey(skillId) && skills.get(skillId).getRank() != rank) {
            skills.get(skillId).setRank(rank);
            return true;
        }
        return false;
    }

    public int getSkillRank(long skillId) {
        if(skills.containsKey(skillId)) {
            return skills.get(skillId).getRank();
        } else {
            return 0;
        }
    }

    public int getSkillRanksTotal() {
        int count = 0;
        for(CSkill sk : skills.values()) {
            count += sk.getRank();
        }
        return count;
    }

    public Set<Long> getSkills() {
        return skills.keySet();
    }

    public String getSkillsAsString() {
        if(skills.size() == 0) {
            return "-";
        }
        List<DBEntity> skills = DBHelper.getInstance(null).getAllEntitiesWithAllFields(SkillFactory.getInstance());
        StringBuffer buf = new StringBuffer();
        for(DBEntity s : skills) {
            int rank = getSkillRank(s.getId());
            if(rank > 0) {
                buf.append(String.format("%s %+d", s.getName(), getSkillTotalBonus((Skill)s)));
                buf.append(", ");
            }
        }
        if(buf.length()>0) {
            buf.delete(buf.length() - 2, buf.length());
        }
        return buf.toString();
    }

    public int getSkillAbilityMod(Skill skill) {
        if(skill == null) {
            return 0;
        }
        return getAbilityModif(skill.getAbilityId());
    }

    public int getSkillTotalBonus(Skill skill) {
        if(skill == null) {
            return 0;
        }
        int rank = getSkillRank(skill.getId());
        int abilityMod = getSkillAbilityMod(skill);
        int bonus = getSkillModBonus(skill);
        return rank + abilityMod + bonus;
    }

    public int getSkillModBonus(Skill skill) {
        if(skill == null) {
            return 0;
        }
        int rank = getSkillRank(skill.getId());
        int classSkill = (rank > 0 && isClassSkill(skill)) ? 3 : 0;
        int bonus = getAdditionalBonus(MODIF_SKILL + (int)skill.getId()) +
                getAdditionalBonus(getAbilityBonus(skill.getAbilityId())) +
                getAdditionalBonus(MODIF_SKILL_ALL);
        return classSkill + bonus;
    }

    public boolean isClassSkillByDefault(Skill skill) {
        return isClassSkill(skill, true);
    }

    public boolean isClassSkill(Skill skill) {
        return isClassSkill(skill, false);
    }

    public boolean isClassSkill(Skill skill, boolean byDefault) {
        if(skill == null) {
            return false;
        }
        // forced by player
        if(!byDefault && skills.containsKey(skill.getId()) && skills.get(skill.getId()).isClassSkill()) {
            return true;
        }
        for(Triplet<Class,ClassArchetype,Integer> cl : classes) {
            if(cl.first.getSkills().contains(skill.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean setClassSkill(Skill skill, boolean value) {
        if(isClassSkillByDefault(skill)) {
            return false;
        } else {
            if(skills.containsKey(skill.getId())) {
                skills.get(skill.getId()).setClassSkill(value);
                return true;
            } else {
                CSkill sk = new CSkill(skill.getId(), 0);
                sk.setClassSkill(value);
                skills.put(skill.getId(), sk);
                return true;
            }
        }
    }

    /**
     * @return the list of traits (as a copy)
     */
    public List<Trait> getTraits() {
        Trait[] itemArray = new Trait[traits.size()];
        itemArray = traits.toArray(itemArray);
        List<Trait> traits = Arrays.asList(itemArray);
        final Collator collator = Collator.getInstance();
        Collections.sort(traits, new Comparator<Trait>() {
            @Override
            public int compare(Trait t1, Trait t2) {
                return collator.compare(t1.getName(), t1.getName());
            }
        });
        return traits;
    }

    public String getTraitsAsString() {
        List<Trait> traits = getTraits();
        if(traits.size() == 0) {
            return "-";
        }
        StringBuffer buf = new StringBuffer();
        for(Trait f : traits) {
            buf.append(f.getName()).append(", ");
        }
        buf.delete(buf.length()-2, buf.length());
        return buf.toString();
    }

    public boolean hasTrait(Trait trait) {
        for(Trait t : traits) {
            if(t.getId() == trait.getId()) {
                return true;
            }
        }
        return false;
    }

    public void addTrait(Trait trait) {
        traits.add(trait);
    }

    public boolean removeTrait(Trait trait) {
        Trait found = null;
        for(Trait t : traits) {
            if(t.getId() == trait.getId()) {
                found = t;
                break;
            }
        }
        if(found != null) {
            traits.remove(found);
            return true;
        }
        return false;
    }


    /**
     * @return the list of feats (as a copy)
     */
    public List<Feat> getFeats() {
        Feat[] itemArray = new Feat[feats.size()];
        itemArray = feats.toArray(itemArray);
        List<Feat> feats = Arrays.asList(itemArray);
        final Collator collator = Collator.getInstance();
        Collections.sort(feats, new Comparator<Feat>() {
            @Override
            public int compare(Feat f1, Feat f2) {
                return collator.compare(f1.getName(), f2.getName());
            }
        });
        return feats;
    }

    public String getFeatsAsString() {
        List<Feat> feats = getFeats();
        if(feats.size() == 0) {
            return "-";
        }
        StringBuffer buf = new StringBuffer();
        for(Feat f : feats) {
            buf.append(f.getName()).append(", ");
        }
        buf.delete(buf.length()-2, buf.length());
        return buf.toString();
    }

    public boolean hasFeat(Feat feat) {
        for(Feat f : feats) {
            if(f.getId() == feat.getId()) {
                return true;
            }
        }
        return false;
    }

    public void addFeat(Feat feat) {
        feats.add(feat);
    }

    public boolean removeFeat(Feat feat) {
        Feat found = null;
        for(Feat f : feats) {
            if(f.getId() == feat.getId()) {
                found = f;
                break;
            }
        }
        if(found != null) {
            feats.remove(found);
            return true;
        }
        return false;
    }

    /**
     * @return the list of spells (as a copy)
     */
    public List<Spell> getSpells() {
        Spell[] itemArray = new Spell[spells.size()];
        itemArray = spells.toArray(itemArray);
        List<Spell> spells = Arrays.asList(itemArray);
        return spells;
    }

    public boolean hasSpell(long spellId) {
        for(Spell f : spells) {
            if(f.getId() == spellId) {
                return true;
            }
        }
        return false;
    }

    public void addSpell(Spell spell) {
        spells.add(spell);
    }

    public boolean removeSpell(Spell spell) {
        Spell found = null;
        for(Spell s : spells) {
            if(s.getId() == spell.getId()) {
                found = s;
                break;
            }
        }
        if(found != null) {
            spells.remove(found);
            return true;
        }
        return false;
    }

    /**
     * @return the list of abilities (as a copy)
     */
    public List<ClassFeature> getClassFeatures() {
        ClassFeature[] itemArray = new ClassFeature[features.size()];
        itemArray = features.toArray(itemArray);
        List<ClassFeature> features = Arrays.asList(itemArray);
        final Collator collator = Collator.getInstance();
        Collections.sort(features, new Comparator<ClassFeature>() {
            @Override
            public int compare(ClassFeature a1, ClassFeature a2) {
                if(a1.isAuto() && !a2.isAuto()) {
                    return -1;
                } else if(!a1.isAuto() && a2.isAuto()) {
                    return 1;
                } else if(a1.getLevel() != a2.getLevel()) {
                    return Integer.compare(a1.getLevel(),a2.getLevel());
                } else if(a1.getClass_() != a2.getClass_()) {
                    return collator.compare(a1.getClass_().getNameShort(), a2.getClass_().getNameShort());
                }
                else {
                    return collator.compare(a1.getName(), a2.getName());
                }
            }
        });
        return features;
    }

    public void removeClasseFeatures(boolean autoOnly) {
        for(ClassFeature cf : getClassFeatures()) {
            if(!autoOnly || cf.isAuto()) {
                if(cf.getLinkedTo() != null) {
                    cf.getLinkedTo().setLinkedTo(null);
                    cf.setLinkedTo(null);
                }
                features.remove(cf);
            }
        }
    }

    public boolean hasClassFeature(ClassFeature feature) {
        for(ClassFeature a : features) {
            if(a.getId() == feature.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean addClassFeature(ClassFeature feature) {
        if(!features.contains(feature)) {
            features.add(feature);
            return true;
        }
        return false;
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    public boolean removeClassFeature(ClassFeature feature) {
        ClassFeature found = null;
        for(ClassFeature a : features) {
            if(a.getId() == feature.getId()) {
                found = a;
                break;
            }
        }
        if(found != null) {
            features.remove(found);
            return true;
        }
        return false;
    }

    /**
     * @param feature class feature
     * @return false if feature doesn't match class or level
     */
    public boolean isValidClassFeature(ClassFeature feature) {
        for(Triplet<Class,ClassArchetype,Integer> cl: classes) {
            if(feature.getClass_().getId() == cl.first.getId() && feature.getLevel() <= cl.third) {
                if((feature.getClassArchetype() == null) ||
                        (feature.getClassArchetype() != null && cl.second != null && feature.getClassArchetype().getId() == cl.second.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param trait trait
     * @return false if trait doesn't match race
     */
    public boolean isValidTrait(Trait trait) {
        if(race == null || trait == null) {
            return false;
        }
        return trait.getRace() == null || trait.getRace().getId() == getRace().getId();
    }

    /**
     * @param trait trait
     * @return false if trait replaces/alters a trait that was already replaced/modified
     */
    public boolean isDuplicatedTrait(Trait trait) {
        if(race == null || trait == null) {
            return false;
        }
        for(Trait t : getTraits()) {
            if(t.getId() == trait.getId()) {
                continue;
            }
            // check same replaces
            for(String repA : t.getReplaces()) {
                for(String repB : trait.getReplaces()) {
                    if(repA.equals(repB)) {
                        return true;
                    }
                }
            }
            // check same alters
            for(String repA : t.getAlters()) {
                for(String repB : trait.getAlters()) {
                    if(repA.equals(repB)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if provided trait name is replaced
     * @param name trait name
     * @return alternate trait if replaced, null otherwise
     */
    public Trait traitIsReplaced(String name) {
        for(Trait t : getTraits()) {
            if(getRace() != null && (t.getRace() == null || getRace().getId() != t.getRace().getId())) {
                continue;
            }
            for(String rep : t.getReplaces()) {
                if(rep.equals(name)) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * Checks if provided trait name is altered
     * @param name trait name
     * @return alternate trait if altered, null otherwise
     */
    public Trait traitIsAltered(String name) {
        for(Trait t : getTraits()) {
            if(getRace() != null && (t.getRace() == null || getRace().getId() != t.getRace().getId())) {
                continue;
            }
            for(String rep : t.getAlters()) {
                if(rep.equals(name)) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * @return the list of modifs
     */
    public List<CharacterModif> getModifs() {
        return modifs;
    }

    public String getModifsAsString() {
        StringBuffer buf = new StringBuffer();
        if(modifs.size() == 0) {
            return "-";
        }
        for(CharacterModif el : modifs) {
            if(el.isEnabled()) {
                buf.append(el.getSource()).append(", ");
            }
        }
        if(buf.length()>0) {
            buf.delete(buf.length() - 2, buf.length());
        }
        return buf.toString();
    }


    public List<CharacterModif> getModifsForId(Integer id) {
        List<CharacterModif> result = new ArrayList<>();
        for(CharacterModif el : modifs) {
            for(Pair<Integer,Integer> m: el.modifs) {
                if(m.first.longValue() == id.longValue()) {
                    result.add(new CharacterModif(el.source, Arrays.asList(m), el.getIcon(), el.getLinkToWeapon(), el.isEnabled()));
                    break;
                }
            }
        }
        return result;
    }

    public int getModifsCount() {
        return modifs.size();
    }

    public void addModif(CharacterModif modif) {
        modifs.add(modif);
    }

    public void deleteModif(CharacterModif modif) {
        modifs.remove(modif);
    }

    public CharacterModif getModif(int idx) {
        if(idx < 0 || idx >= modifs.size()) {
            return null;
        }
        return modifs.get(idx);
    }

    /**
     * Modif linkToWeapon is based on weapon index in list
     * Set indexes on items such it can be retrieved after inventory change
     */
    public void indexInventoryWeapons() {
        int idx = 0;
        for(InventoryItem el : invItems) {
            if(el.isWeapon()) {
                el.setId(++idx);
            }
        }
    }

    /**
     * Modifies (when needed) linkToWeapon to link to right inventory
     */
    public void reindexModifLinks() {
        for(CharacterModif modif : modifs) {
            if(modif.linkToWeapon > 0) {
                int link = modif.linkToWeapon; // reset in case reference not found
                modif.linkToWeapon = 0;
                int idx = 0;
                for(InventoryItem el : invItems) {
                    if(el.isWeapon()) {
                        idx++;
                    }
                    if(el.getId() == link) {
                        modif.linkToWeapon = idx;
                    }
                }
            }
        }
    }

    /**
     * Re-orders the list by moving one item before another (drag-and-drop)
     * @param idxToMove index of the item to move
     * @param idxBefore index of the item before which to move
     * @return true if move was done/required
     */
    public boolean moveInventoryItem(int idxToMove, int idxBefore) {
        // invalid inputs
        if(idxToMove < 0 || idxToMove >= invItems.size() || idxBefore < 0 || idxBefore >= invItems.size()) {
            return false;
        }
        // move will result with the same ordering
        if(idxToMove == idxBefore || idxBefore == idxToMove + 1) {
            return false;
        }
        indexInventoryWeapons();
        InventoryItem itemToMove = invItems.get(idxToMove);
        List<InventoryItem> newOrderedList = new ArrayList<>();
        int idx = 0;
        for(InventoryItem item : invItems) {
            if(idx == idxBefore) {
                newOrderedList.add(itemToMove);
            }
            if(idx != idxToMove) {
                newOrderedList.add(item);
            }
            idx++;
        }
        invItems = newOrderedList;
        reindexModifLinks();
        return true;
    }

    public void addInventoryItem(InventoryItem item) {
        invItems.add(item);
    }

    public void deleteInventoryItem(int idx) {
        if(idx < 0 || idx >= invItems.size()) {
            return;
        }
        indexInventoryWeapons();
        invItems.remove(idx);
        reindexModifLinks();
    }

    public void modifyInventoryItem(int idx, InventoryItem item) {
        if(idx < 0 || idx >= invItems.size() || !item.isValid()) {
            return;
        }
        indexInventoryWeapons();
        InventoryItem selItem = invItems.get(idx);
        selItem.set(item);
        Collections.sort(invItems);
        reindexModifLinks();
    }

    /**
     * @return the list of inventory items (as a copy)
     */
    public List<InventoryItem> getInventoryItems() {
        List<InventoryItem> result = new ArrayList<>();
        for(InventoryItem el : invItems) {
            result.add(new InventoryItem(el));
        }
        return result;
    }

    /**
     * @return the list of inventory items which are weapons
     */
    public List<Weapon> getInventoryWeapons() {
        List<Weapon> result = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(null);
        for(InventoryItem el : invItems) {
            if(el.isWeapon()) {
                DBEntity entity = helper.fetchObjectEntity(el);
                if(entity instanceof Weapon) {
                    Weapon w = (Weapon)entity;
                    if(!w.isAmmo()) {
                        w.setName(el.getName());
                        w.setDescription(el.getInfos());
                        result.add(w);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @return the list of inventory items which are weapons
     */
    public List<Armor> getInventoryArmors() {
        List<Armor> result = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(null);
        for(InventoryItem el : invItems) {
            if(el.isArmor()) {
                DBEntity entity = helper.fetchObjectEntity(el);
                if(entity instanceof Armor) {
                    Armor a = (Armor)entity;
                    a.setName(el.getName());
                    a.setDescription(el.getInfos());
                    result.add(a);
                }
            }
        }
        return result;
    }

    public String getInventoryAsString() {
        if(invItems.size() == 0) {
            return "-";
        }
        StringBuffer buf = new StringBuffer();
        if(moneyPP > 0) {
            buf.append(moneyPP).append(" pp, ");
        }
        if(moneyGP > 0) {
            buf.append(moneyGP).append(" po, ");
        }
        if(moneySP > 0) {
            buf.append(moneyGP).append(" pa, ");
        }
        if(moneyCP > 0) {
            buf.append(moneyGP).append(" pc, ");
        }
        for(InventoryItem el : invItems) {
            buf.append(el.getName()).append(", ");
        }
        buf.delete(buf.length()-2, buf.length());
        return buf.toString();
    }

    public int getInventoryTotalWeight() {
        int weight = 0;
        for(InventoryItem el : invItems) {
            weight += el.getWeight();
        }
        return weight;
    }
}

