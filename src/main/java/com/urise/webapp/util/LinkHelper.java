package com.urise.webapp.util;

public class LinkHelper {
    public static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }
}