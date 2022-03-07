package com.saf.Dan;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.planner.R;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {
    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_third, container, false);

        models = new ArrayList<>();
        models.add(new Model(R.drawable.impala, "Impala", "The impala is a medium-sized antelope found in eastern and southern Africa. The sole member of the genus Aepyceros, it was first described to European audiences by German zoologist Hinrich Lichtenstein in 1812."));
        models.add(new Model(R.drawable.butterfly, "Butterflys", "Butterflies are insects in the macrolepidopteran clade Rhopalocera from the order Lepidoptera, which also includes moths. Adult butterflies have large, often brightly coloured wings, and conspicuous, fluttering flight."));
        models.add(new Model(R.drawable.dfly, "Dragon Flies", "A dragonfly is an insect belonging to the order Odonata, infraorder Anisoptera. Adult dragonflies are characterized by large, multifaceted eyes, two pairs of strong, transparent wings, sometimes with coloured patches, and an elongated body."));
        models.add(new Model(R.drawable.gparrot, " grey parrots", "The grey parrot, also known as the Congo grey parrot, Congo African grey parrot or African grey parrot, is an Old World parrot in the family Psittacidae. The Timneh parrot (Psittacus timneh) once was identified as a subspecies of the grey parrot, but has since been elevated to a full species.\n" +
                "\n"));


        models.add(new Model(R.drawable.hippo, "Hippos", "The hippopotamus, also called the hippo, common hippopotamus or river hippopotamus, is a large, mostly herbivorous, semiaquatic mammal and ungulate native to sub-Saharan Africa. It is one of only two extant species"));
        models.add(new Model(R.drawable.ostrich, "Ostrich", "ostrich, is a species of large flightless bird native to certain large areas of Africa. It is one of two extant species"));
        models.add(new Model(R.drawable.sjackal, " Side-striped jackals", "The side-striped jackal is a canine native to central and southern Africa. Unlike the smaller black-backed jackal which dwells in open plains, the side-striped jackal primarily dwells in woodland and scrub areas.\n" +
                "\n"));
        models.add(new Model(R.drawable.vmonkey, " Vervet monkeys", "The vervet monkey, or simply vervet, is an Old World monkey of the family Cercopithecidae native to Africa. The term \"vervet\" is also used to refer to all the members of the genus Chlorocebus."));

        models.add(new Model(R.drawable.lions, "Lions", "The lion is a species in the family Felidae and a member of the genus Panthera. It has a muscular, deep-chested body, short, rounded head, round ears, and a hairy tuft at the end of its tail. It is sexually dimorphic; adult male lions have a prominent mane. "));
        models.add(new Model(R.drawable.olivebabboon, "Olivebabboon", "The olive baboon, also called the Anubis baboon, is a member of the family Cercopithecidae. The species is the most wide-ranging of all baboons, being found in 25 countries throughout Africa, extending from Mali eastward to Ethiopia and Tanzania."));
        models.add(new Model(R.drawable.mlizard, "Monitor Lizard", "Monitor lizards are large lizards in the genus Varanus. They are native to Africa, Asia, and Oceania, but are now found also in the Americas as an invasive species. "));
        models.add(new Model(R.drawable.ltotoise, " Leopard Tortoises", "The leopard tortoise is a large and attractively marked tortoise found in the savannas of eastern and southern Africa, from Sudan to the southern Cape. It is the only member of the genus Stigmochelys"));

        models.add(new Model(R.drawable.girrafe, "Giraffe", "The giraffe is an African artiodactyl mammal, the tallest living terrestrial animal and the largest ruminant. "));
        models.add(new Model(R.drawable.gfowl, "Guinnea Fowls", "Guineafowl are birds of the family Numididae in the order Galliformes. They are endemic to Africa and rank among the oldest of the gallinaceous birds. Phylogenetically, they branched off from the core Galliformes after the Cracidae and before the odontophoridae"));
        models.add(new Model(R.drawable.cheeter, "Cheetah", "The Southeast African cheetah is the nominate cheetah subspecies native to East and Southern Africa. The Southern African cheetah lives mainly in the lowland areas and deserts of the Kalahari"));
        models.add(new Model(R.drawable.hyena, "Hyena", "Hyenas or hyaenas are feliform carnivoran mammals of the family Hyaenidae. With only four extant species, it is the fifth-smallest biological family in the Carnivora, and one of the smallest in the class Mammalia. Despite their low diversity, hyenas are unique and vital"));

        models.add(new Model(R.drawable.buffalo, "Buffalo", "The African buffalo or Cape buffalo is a large sub-Saharan African bovine. Syncerus caffer caffer, the Cape buffalo, is the typical subspecies, and the largest one, found in Southern and East Africa. S. c. nanus is the smallest subspecies, common in forest areas"));
        models.add(new Model(R.drawable.sitaantelope, " Sitatunga Antelope ", "The sitatunga is a rare swamp-dwelling antelope. It is distinguished by its long, splayed hooves. These hooves make them clumsy and vulnerable on firm terrain but well-adapted for walking through muddy, vegetated swamplands"));


        adapter = new Adapter(models, getActivity());

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
               getResources().getColor(R.color.color1),
              getResources().getColor(R.color.color2),
               getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                  viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

}
