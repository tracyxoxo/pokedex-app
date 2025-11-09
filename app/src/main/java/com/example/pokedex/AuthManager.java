package com.example.pokedex;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String USERS = "Users";
    private static final String SESSION = "Session";
    private static final String KEY_CURRENT = "current_user";

    // Register: false if already exists
    public static boolean register(Context ctx, String username, String password) {
        SharedPreferences prefs = ctx.getSharedPreferences(USERS, Context.MODE_PRIVATE);
        if (prefs.contains(username)) return false;
        prefs.edit().putString(username, password).apply();
        return true;
    }

    // Validate username/password
    public static boolean validate(Context ctx, String username, String password) {
        SharedPreferences prefs = ctx.getSharedPreferences(USERS, Context.MODE_PRIVATE);
        String saved = prefs.getString(username, null);
        return saved != null && saved.equals(password);
    }

    // Session helpers
    public static void setCurrentUser(Context ctx, String username) {
        ctx.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
                .edit().putString(KEY_CURRENT, username).apply();
    }

    public static String getCurrentUser(Context ctx) {
        return ctx.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
                .getString(KEY_CURRENT, null);
    }

    public static void logout(Context ctx) {
        ctx.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
                .edit().remove(KEY_CURRENT).apply();
    }
}
