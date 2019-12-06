package com.youdao.util;

import java.util.HashMap;

public class Constant {
    public final static HashMap<String, String> sAbbrProviderMap;
    static {
        sAbbrProviderMap = new HashMap<>();
        sAbbrProviderMap.put("English", "en");
        sAbbrProviderMap.put("Arabic", "ar");
        sAbbrProviderMap.put("Bangla", "bn");
        sAbbrProviderMap.put("Spanish", "es");
        sAbbrProviderMap.put("Hindi", "hi");
        sAbbrProviderMap.put("Indonesian", "in");
        sAbbrProviderMap.put("Japanese", "ja");
        sAbbrProviderMap.put("Marathi", "mr");
        sAbbrProviderMap.put("Portuguese", "pt");
        sAbbrProviderMap.put("Tamil", "ta");
        sAbbrProviderMap.put("Telugu", "te");
        sAbbrProviderMap.put("Thai", "th");
        sAbbrProviderMap.put("Filipino", "tl");
        sAbbrProviderMap.put("Urdu", "ur");
        sAbbrProviderMap.put("Vietnamese", "vi");

    }
}
