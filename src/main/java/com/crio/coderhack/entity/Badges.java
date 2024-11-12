package com.crio.coderhack.entity;

import java.util.HashSet;

public class Badges {
    private static final String Code_Ninja = "Code Ninja";
    private static final String Code_Champ = "Code Champ";
    private static final String Code_Master = "Code Master";

    public static HashSet<String> getBadge(int score) {
        HashSet<String> badgeSet = new HashSet<>();
        if (score < 30) {
            badgeSet.add(Code_Ninja);
        } else if (score >= 60) {
            badgeSet.add(Code_Ninja);
            badgeSet.add(Code_Champ);
            badgeSet.add(Code_Master);
        } else {
            badgeSet.add(Code_Ninja);
            badgeSet.add(Code_Champ);
        }
        return badgeSet;
    }
}
