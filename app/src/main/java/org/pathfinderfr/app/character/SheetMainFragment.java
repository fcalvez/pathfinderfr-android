package org.pathfinderfr.app.character;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wefika.flowlayout.FlowLayout;

import org.pathfinderfr.R;
import org.pathfinderfr.app.character.FragmentRacePicker.OnFragmentInteractionListener;
import org.pathfinderfr.app.database.DBHelper;
import org.pathfinderfr.app.database.entity.Character;
import org.pathfinderfr.app.database.entity.CharacterFactory;
import org.pathfinderfr.app.database.entity.Class;
import org.pathfinderfr.app.database.entity.ClassFactory;
import org.pathfinderfr.app.database.entity.DBEntity;
import org.pathfinderfr.app.database.entity.Race;
import org.pathfinderfr.app.database.entity.RaceFactory;
import org.pathfinderfr.app.util.CharacterUtil;
import org.pathfinderfr.app.util.FragmentUtil;
import org.pathfinderfr.app.util.Pair;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SheetMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SheetMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SheetMainFragment extends Fragment implements FragmentAbilityPicker.OnFragmentInteractionListener,
        OnFragmentInteractionListener, FragmentClassPicker.OnFragmentInteractionListener {

    private static final String ARG_CHARACTER_ID = "character_id";

    private Character character;
    private List<TextView> classPickers;
    View.OnClickListener listener;

    private long characterId;

    private OnFragmentInteractionListener mListener;

    public SheetMainFragment() {
        // Required empty public constructor
        classPickers = new ArrayList<>();
    }

    /**
     * @param characterId character id to display or 0 if new character
     * @return A new instance of fragment SheetMainFragment.
     */
    public static SheetMainFragment newInstance(long characterId) {
        SheetMainFragment fragment = new SheetMainFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CHARACTER_ID, characterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            characterId = getArguments().getLong(ARG_CHARACTER_ID);
        }
    }

    private static void showTooltip(View v, String message) {
        Toast t = Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT);
        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        t.setGravity(Gravity.TOP|Gravity.LEFT, xy[0], xy[1]);
        t.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // fetch character
        if(characterId > 0) {
            character = (Character)DBHelper.getInstance(getContext()).fetchEntity(characterId, CharacterFactory.getInstance());
        }
        if(character == null) {
            character = new Character();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sheet_main, container, false);
        listener = new ProfileListener(this);

        view.findViewById(R.id.ability_str).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_ability_strength));}
        });
        view.findViewById(R.id.ability_dex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_ability_dexterity));}
        });
        view.findViewById(R.id.ability_con).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_ability_constitution));}
        });
        view.findViewById(R.id.ability_int).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_ability_intelligence));}
        });
        view.findViewById(R.id.ability_wis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_ability_wisdom));}
        });
        view.findViewById(R.id.ability_cha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_ability_charisma));}
        });

        view.findViewById(R.id.ability_str_value).setOnClickListener(listener);
        view.findViewById(R.id.ability_dex_value).setOnClickListener(listener);
        view.findViewById(R.id.ability_con_value).setOnClickListener(listener);
        view.findViewById(R.id.ability_int_value).setOnClickListener(listener);
        view.findViewById(R.id.ability_wis_value).setOnClickListener(listener);
        view.findViewById(R.id.ability_cha_value).setOnClickListener(listener);
        view.findViewById(R.id.sheet_main_racepicker).setOnClickListener(listener);
        view.findViewById(R.id.sheet_main_classpicker).setVisibility(View.GONE);

        view.findViewById(R.id.other_ini).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_initiative));}
        });
        view.findViewById(R.id.other_ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_armorclass));}
        });
        view.findViewById(R.id.other_mag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_magicresistance));}
        });

        view.findViewById(R.id.combat_bab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_baseattackbonus));}
        });
        view.findViewById(R.id.combat_cmb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_combat_man_bonus));}
        });
        view.findViewById(R.id.combat_cmd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_combat_man_defense));}
        });

        view.findViewById(R.id.savingthrows_for).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_savingthrows_fortitude));}
        });
        view.findViewById(R.id.savingthrows_ref).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_savingthrows_reflex));}
        });
        view.findViewById(R.id.savingthrows_wil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTooltip(v, getResources().getString(R.string.sheet_savingthrows_will));}
        });

        // update abilityes
        ((TextView)view.findViewById(R.id.ability_str_value)).setText(String.valueOf(character.getStrength()));
        ((TextView)view.findViewById(R.id.ability_dex_value)).setText(String.valueOf(character.getDexterity()));
        ((TextView)view.findViewById(R.id.ability_con_value)).setText(String.valueOf(character.getConstitution()));
        ((TextView)view.findViewById(R.id.ability_int_value)).setText(String.valueOf(character.getIntelligence()));
        ((TextView)view.findViewById(R.id.ability_wis_value)).setText(String.valueOf(character.getWisdom()));
        ((TextView)view.findViewById(R.id.ability_cha_value)).setText(String.valueOf(character.getCharisma()));
        ((TextView)view.findViewById(R.id.ability_str_modif)).setText(String.valueOf(character.getStrengthModif()));
        ((TextView)view.findViewById(R.id.ability_dex_modif)).setText(String.valueOf(character.getDexterityModif()));
        ((TextView)view.findViewById(R.id.ability_con_modif)).setText(String.valueOf(character.getConstitutionModif()));
        ((TextView)view.findViewById(R.id.ability_int_modif)).setText(String.valueOf(character.getIntelligenceModif()));
        ((TextView)view.findViewById(R.id.ability_wis_modif)).setText(String.valueOf(character.getWisdomModif()));
        ((TextView)view.findViewById(R.id.ability_cha_modif)).setText(String.valueOf(character.getCharismaModif()));

        // update race
        TextView raceTv = view.findViewById(R.id.sheet_main_racepicker);
        if(character.getRace() != null) {
            raceTv.setText(character.getRace().getName());
        }

        updateClassPickers(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void updateClassPickers(View view) {
        TextView reference = view.findViewById(R.id.sheet_main_classpicker);
        FlowLayout layout = view.findViewById(R.id.sheet_main_classlayout);
        // make sure #pickers > #classes
        int toCreate = character.getClassesCount() - classPickers.size();
        for(int i = 0; i <= toCreate; i++) {
            TextView newPicker = FragmentUtil.copyExampleTextFragment(reference);
            newPicker.setOnClickListener(listener);
            classPickers.add(newPicker);
            layout.addView(newPicker);
        }
        // configure #pickers
        int idx = 0;
        int maxLevel = 20 - character.getOtherClassesLevel(-1);
        for(TextView tv : classPickers) {
            Pair<Class,Integer> cl = character.getClass(idx);
            if(cl != null) {
                tv.setText(cl.first.getName() + " " + cl.second);
                tv.setVisibility(View.VISIBLE);
            } else if(idx == character.getClassesCount() && maxLevel > 0) {
                tv.setText(reference.getText());
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
            idx++;
        }
        // update stats
        updateOtherStats(view);
    }


    private void updateOtherStats(View view) {
        TextView initiative = view.findViewById(R.id.initiative_value);
        TextView armorClass = view.findViewById(R.id.armorclass_value);
        TextView magicResis = view.findViewById(R.id.magicresistance_value);

        TextView savingFortitudeTotal = view.findViewById(R.id.savingthrows_fortitude_total);
        TextView savingReflexTotal = view.findViewById(R.id.savingthrows_reflex_total);
        TextView savingWillTotal = view.findViewById(R.id.savingthrows_will_total);

        TextView savingFortitude = view.findViewById(R.id.savingthrows_fortitude);
        TextView savingReflex = view.findViewById(R.id.savingthrows_reflex);
        TextView savingWill = view.findViewById(R.id.savingthrows_will);

        TextView savingFortitudeAbility = view.findViewById(R.id.savingthrows_fortitude_ability);
        TextView savingReflexAbility = view.findViewById(R.id.savingthrows_reflex_ability);
        TextView savingWillAbility = view.findViewById(R.id.savingthrows_will_ability);

        TextView baseAttackBonus = view.findViewById(R.id.base_attack_bonus_value);
        TextView combatManBonusTotal = view.findViewById(R.id.combat_cmb_total);
        TextView combatManBonusBab = view.findViewById(R.id.combat_cmb_bab);
        TextView combatManBonusAbility = view.findViewById(R.id.combat_cmb_ability);
        TextView combatManDefenseTotal = view.findViewById(R.id.combat_cmd_total);
        TextView combatManDefenseBab = view.findViewById(R.id.combat_cmd_bab);
        TextView combatManDefenseAbility = view.findViewById(R.id.combat_cmd_ability);

        initiative.setText(String.valueOf(character.getInitiative()));
        armorClass.setText(String.valueOf(character.getArmorClass()));
        magicResis.setText(String.valueOf(character.getMagicResistance()));

        savingFortitudeTotal.setText(String.valueOf(character.getSavingThrowsFortitudeTotal()));
        savingReflexTotal.setText(String.valueOf(character.getSavingThrowsReflexesTotal()));
        savingWillTotal.setText(String.valueOf(character.getSavingThrowsWillTotal()));

        savingFortitude.setText(String.valueOf(character.getSavingThrowsFortitude()));
        savingReflex.setText(String.valueOf(character.getSavingThrowsReflexes()));
        savingWill.setText(String.valueOf(character.getSavingThrowsWill()));

        savingFortitudeAbility.setText(String.valueOf(character.getConstitutionModif()));
        savingReflexAbility.setText(String.valueOf(character.getDexterityModif()));
        savingWillAbility.setText(String.valueOf(character.getWisdomModif()));

        int[] bab = character.getBaseAttackBonus();
        baseAttackBonus.setText(character.getBaseAttackBonusAsString());
        combatManBonusTotal.setText(String.valueOf(character.getCombatManeuverBonus()));
        combatManBonusBab.setText(String.valueOf(bab == null || bab.length == 0 ? 0: bab[0]));
        combatManBonusAbility.setText(String.valueOf(character.getStrengthModif()));
        combatManDefenseTotal.setText(String.valueOf(character.getCombatManeuverDefense()));
        combatManDefenseBab.setText(String.valueOf(bab == null || bab.length == 0 ? 0: bab[0]));
        combatManDefenseAbility.setText(String.valueOf(character.getStrengthModif()+character.getDexterityModif()));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static class ProfileListener implements View.OnClickListener {

        SheetMainFragment parent;

        public ProfileListener(SheetMainFragment fragment) {
            parent = fragment;
        }

        @Override
        public void onClick(View v) {

            if (v instanceof TextView && "ability".equals(v.getTag())) {
                TextView tv = (TextView) v;
                int value = ( tv.getText() != null ? Integer.valueOf(tv.getText().toString()) : 10);

                FragmentTransaction ft = parent.getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = parent.getActivity().getSupportFragmentManager().findFragmentByTag("ability-picker");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment newFragment = FragmentAbilityPicker.newInstance(parent);

                Bundle arguments = new Bundle();
                arguments.putInt(FragmentAbilityPicker.ARG_ABILITY_ID, tv.getId());
                arguments.putInt(FragmentAbilityPicker.ARG_ABILITY_VALUE, value);
                newFragment.setArguments(arguments);
                newFragment.show(ft, "ability-picker");
            } else if(v instanceof TextView && "race".equals(v.getTag())) {
                FragmentTransaction ft = parent.getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = parent.getActivity().getSupportFragmentManager().findFragmentByTag("race-picker");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment newFragment = FragmentRacePicker.newInstance(parent);

                Bundle arguments = new Bundle();
                Long raceId = parent.character.getRace() == null ? 0L : parent.character.getRace().getId();
                arguments.putLong(FragmentRacePicker.ARG_RACE_ID,  raceId);
                newFragment.setArguments(arguments);
                newFragment.show(ft, "race-picker");
            } else if(v instanceof TextView && "class".equals(v.getTag())) {
                FragmentTransaction ft = parent.getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = parent.getActivity().getSupportFragmentManager().findFragmentByTag("class-picker");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment newFragment = FragmentClassPicker.newInstance(parent);

                // find which class was selected
                int idx = 0;
                for(TextView tv : parent.classPickers) {
                    if(tv == v) {
                        break;
                    }
                    idx++;
                }
                // not found??
                if(idx == parent.classPickers.size()) {
                    Log.w(SheetMainFragment.class.getSimpleName(), "Class picker couldn't be found!!");
                    return;
                }
                Pair<Class,Integer> curClass = parent.character.getClass(idx);

                // prepare parameters
                long[] excluded = parent.character.getOtherClassesIds(curClass == null ? -1 : curClass.first.getId());
                int maxLevel = 20 - parent.character.getOtherClassesLevel(curClass == null ? -1 : curClass.first.getId());

                Bundle arguments = new Bundle();
                if(curClass != null) {
                    arguments.putLong(FragmentClassPicker.ARG_CLASS_ID, curClass.first.getId());
                    arguments.putInt(FragmentClassPicker.ARG_CLASS_LVL, curClass.second);
                }
                arguments.putLongArray(FragmentClassPicker.ARG_CLASS_EXCL, excluded);
                arguments.putInt(FragmentClassPicker.ARG_CLASS_MAX_LVL, maxLevel);
                newFragment.setArguments(arguments);
                newFragment.show(ft, "class-picker");
            }
        }
    }

    /**
     * Updates the data into the database for the character
     */
    private void characterDBUpdate() {
        if(character.getId() <= 0) {
            long id = DBHelper.getInstance(getContext()).insertEntity(character);
            if (id > 0) {
                character.setId(id);
            } else {
                Log.e(SheetMainFragment.class.getSimpleName(), "Couldn't persist character into database! Check logs!");
            }
        } else {
            DBHelper.getInstance(getContext()).updateEntity(character);
        }
    }

    @Override
    public void onAbilityValueChosen(int abilityId, int abilityValue) {
        View v = getView().findViewById(abilityId);
        if(v != null && v instanceof TextView) {
            TextView tv = (TextView)v;
            tv.setText(String.valueOf(abilityValue));

            switch(abilityId) {
                case R.id.ability_str_value:
                    tv = getView().findViewById(R.id.ability_str_modif);
                    character.setStrength(abilityValue);
                    break;
                case R.id.ability_dex_value:
                    tv = getView().findViewById(R.id.ability_dex_modif);
                    character.setDexterity(abilityValue);
                    break;
                case R.id.ability_con_value:
                    tv = getView().findViewById(R.id.ability_con_modif);
                    character.setConstitution(abilityValue);
                    break;
                case R.id.ability_int_value:
                    tv = getView().findViewById(R.id.ability_int_modif);
                    character.setIntelligence(abilityValue);
                    break;
                case R.id.ability_wis_value:
                    tv = getView().findViewById(R.id.ability_wis_modif);
                    character.setWisdom(abilityValue);
                    break;
                case R.id.ability_cha_value:
                    tv = getView().findViewById(R.id.ability_cha_modif);
                    character.setCharisma(abilityValue);
                    break;
            }

            if(tv != null) {
                tv.setText(String.valueOf(CharacterUtil.getAbilityBonus(abilityValue)));
            }

            // update stats
            updateOtherStats(getView());
            // store changes
            characterDBUpdate();
        }
    }

    @Override
    public void onRaceChosen(long raceId) {
        Race race = (Race)DBHelper.getInstance(getContext()).fetchEntity(raceId, RaceFactory.getInstance());
        TextView tv = getView().findViewById(R.id.sheet_main_racepicker);
        character.setRace(race);
        if(race != null) {
            tv.setText(race.getName());
        }
        // store changes
        characterDBUpdate();
    }

    @Override
    public void onClassDeleted(long classId) {
        Class cl = (Class)DBHelper.getInstance(getContext()).fetchEntity(classId, ClassFactory.getInstance());
        if(cl != null) {
            character.removeClass(cl);
            updateClassPickers(getView());
        }
        // store changes
        characterDBUpdate();
    }

    @Override
    public void onClassChosen(long classId, int level) {
        Class cl = (Class)DBHelper.getInstance(getContext()).fetchEntity(classId, ClassFactory.getInstance());
        if(cl != null) {
            character.addOrSetClass(cl, level);
            updateClassPickers(getView());
        }
        // store changes
        characterDBUpdate();
    }
}