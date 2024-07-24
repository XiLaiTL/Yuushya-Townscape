package com.yuushya.utils;

import org.jetbrains.annotations.NotNull;

public record Version(int release, int main, int vice) implements Comparable<Version>{

    public static final Version V1_20_6 = new Version(1,20,6);
    public static final Version V1_21 = new Version(1,21,0);
    @Override
    public int compareTo(@NotNull Version o) {
        int c1 = release - o.release;
        if(c1 != 0) return c1;
        int c2 = main - o.main;
        if(c2 != 0) return c2;
        return vice - o.vice;
    }
    public static Version parse(String version){
        String[] list = version.split("\\.");
        if(list.length == 3){
            int c1 = Integer.parseInt(list[0]);
            int c2 = Integer.parseInt(list[1]);
            int c3 = Integer.parseInt(list[2]);
            return new Version(c1,c2,c3);
        }
        return new Version(0,0,0);
    }
}
