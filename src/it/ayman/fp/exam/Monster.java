package it.ayman.fp.exam;

import it.ayman.fp.lib.RandomDraws;

public class Monster {
    private int hp;
    private final int attack;
    private final String name;

    private static final String[] monsterNames = {
            "Burrascoso",
            "Vampirico",
            "Tenebroso",
            "Fulminante",
            "Spettrale",
            "Oscuro",
            "Infernale",
            "Malefico",
            "Divoratore",
            "Morsiccante",
            "Schiacciante",
            "Sanguevoro",
            "Orrorevolto",
            "Agghiacciante",
            "Spinoso",
            "Abominevole",
            "Ululante",
            "Fiammeggiante",
            "Brontolante",
            "Malefico",
            "Vorace",
            "Squarciante",
            "Inquietante",
            "Sanguinario",
            "Sfuggente",
            "Istigante",
            "Insidioso",
            "Oscuroso",
            "Ferino",
            "Abissale",
            "Affamato",
            "Sussurrante",
            "Ruggente",
            "Infuocato",
            "Immondo",
            "Selvaggio",
            "Maledetto",
            "Assordante",
            "Voragine",
            "Morsofiero",
            "Velenoso",
            "Incubo"
    };

    public Monster() {
        this.hp = 12 + RandomDraws.drawInteger(-5, 5);
        this.attack = 3 + RandomDraws.drawInteger(-2, 2);
        this.name = monsterNames[RandomDraws.drawInteger(0, monsterNames.length - 1)];
    }

    public Monster(String name) {
        this.hp = 18 + RandomDraws.drawInteger(-5, 5);
        this.attack = 4 + RandomDraws.drawInteger(-2, 2);
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public String getName() {
        return name;
    }
}
