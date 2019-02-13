package org.pathfinderfr.app.util;

import org.pathfinderfr.app.database.entity.Spell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellUtil {

    /**
     * This function cleans the "school" data to avoid duplicates
     * @param school original value (from import)
     * @return clean value
     */
    public static String cleanSchool(String school) {
        // only take first word
        Pattern pattern = Pattern.compile("([A-zÀ-ú]+).*");
        Matcher matcher = pattern.matcher(school.toLowerCase());
        if (matcher.find())
        {
            String clean = matcher.group(1);
            // capitalize first letter only
            return clean.substring(0, 1).toUpperCase() + clean.substring(1);
        }
        return null;
    }

    /**
     * @return the class and minimum level for given classes and spell
     * Ex: if classes = {Bar, Mag} and spell is available for "Bar 2, Mag 1", the result will be Mag 1
     */
    public static Pair<String,Integer> getLevel(List<String> classes, Spell spell, boolean clean) {
        List<Pair<String,Integer>> levels = cleanClasses(spell.getLevel());

        Set<String> classNames = new HashSet<>();
        if(clean) {
            for (String name : classes) {
                classNames.add(cleanClass(name));
            }
        } else {
            classNames.addAll(classes);
        }

        // find lowest level for given classes
        Pair<String, Integer> min = null;
        for(Pair<String,Integer> pair : levels) {
            if(classNames.contains(pair.first) && (min == null || min.second > pair.second)) {
                min = pair;
            }
        }

        return min;
    }

    public static Pair<String,Integer> getLevel(List<String> classes, Spell spell) {
        return getLevel(classes, spell, true);
    }


    /**
     * Returns a clean text representation of the class (3 chars)
     * @param cl class name
     * @return clean class (3 chars, first capitalized)
     */
    public static String cleanClass(String cl) {
        Pattern pattern = Pattern.compile("([A-zÀ-ú]+)");
        Matcher matcher = pattern.matcher(cl.toLowerCase());
        if (matcher.find())
        {
            String cleanClass = matcher.group(1);
            if(cleanClass.length() > 3) {
                cleanClass = cleanClass.substring(0,3);
            }
            // capitalize first letter only
            cleanClass = cleanClass.substring(0, 1).toUpperCase() + cleanClass.substring(1);
            return cleanClass;
        }
        return null;
    }

    /**
     * This function cleans the "class" data to avoid duplicates
     *
     * Examples:
     *   Alch 3,  Conj 3, Ens/Mag 3, Magus 3, Sor 3
     *   ensorceleur/magicien 3, sorcière 3
     *
     * @param cl (class) original value (from import)
     * @return clean value (list of <class,level>)
     */
    public static List<Pair<String,Integer>> cleanClasses(String cl) {
        List<String> classes = new ArrayList<>();
        // Split by comma ',' and eventually by slash '/'
        String[] regex = cl.split(",");
        for(String s : regex) {
            s = s.toLowerCase().trim();
            if(s.indexOf('/') > 0) {
                // extract level
                String level = s.substring(s.length()-2);
                for(String el: s.split("/")) {
                    if(el.endsWith(level)) {
                        classes.add(el);
                    } else {
                        classes.add(el + level);
                    }
                }
            } else {
                classes.add(s);
            }
        }
        // clean data by removing level and spaces, and reduce to 3-letters acronym
        List<Pair<String,Integer>> clean = new ArrayList<>();
        for(String c: classes) {
            Pattern pattern = Pattern.compile("([A-zÀ-ú]+).*([0-9])");
            Matcher matcher = pattern.matcher(c.toLowerCase());
            if (matcher.find())
            {
                String cleanClass = matcher.group(1);
                Integer cleanLevel = Integer.parseInt(matcher.group(2));
                if(cleanClass.length() > 3) {
                    cleanClass = cleanClass.substring(0,3);
                }
                // capitalize first letter only
                cleanClass = cleanClass.substring(0, 1).toUpperCase() + cleanClass.substring(1);

                clean.add(new Pair<String,Integer>(cleanClass,cleanLevel));
            }
        }

        return clean;
    }

}