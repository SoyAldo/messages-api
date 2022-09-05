package org.kayteam.messagesapi;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class MessagesAPI {

    public static void sendMessage(Player player , Object message ) {

        sendMessage( player , message , new String[][]{} );

    }

    public static void sendMessage( Player player , Object message , String[][] replacements ) {

        String type = message.getClass().getSimpleName();

        if ( type.equals( "ArrayList" ) ) {

            sendMessage( player , (List<String>) message , replacements );

        } else if ( type.equals( "String" ) ) {

            sendMessage( player , (String) message , replacements );

        }

    }

    public static void sendMessage(Player player , List<String> messages , String[][] replacements ) {

        for ( String message : messages )   sendMessage( player , message , replacements );

    }

    public static void sendMessage(Player player , List<String> messages ) {

        for ( String message : messages )   sendMessage( player , message , new String[][]{} );

    }

    public static void sendMessage( Player player , String message ) {

        sendMessage( player , message , new String[][]{} );

    }

    public static void sendMessage( Player player , String message , String[][] replacements ) {

        message = applyReplacements( message , replacements );

        if ( Bukkit.getServer().getPluginManager().getPlugin( "PlaceholderAPI" ) != null ) {
            message = PlaceholderAPI.setPlaceholders( player , message );
        }

        message = ChatColor.translateAlternateColorCodes( '&' , message );

        if ( message.startsWith( "[sound]" ) ) {
            playSound(player, message);
            return;
        }

        if ( message.startsWith( "[title]" ) ) {
            playTitle(player, message);
            return;
        }

        player.sendMessage( message );

    }

    public static void playSound( Player player , String format ) {
        format = format.replaceFirst( "[sound] " , "" );

        String[] split = format.split( " " );

        Sound sound;

        try {
            sound = Sound.valueOf( split[ 0 ] );
        } catch ( IllegalArgumentException exception ) {
            exception.printStackTrace();
            return;
        }

        float yaw = 1;

        float pitch = 1;

        for ( int i = 0 ; i < split.length ; i++ ) {
            try {
                if ( i == 1 )   yaw = Float.parseFloat( split[ i ] );
                if ( i == 2 )   pitch = Float.parseFloat( split[ i ] );
            } catch ( NumberFormatException exception ) {
                exception.printStackTrace();
            }
        }

        Location location = player.getLocation();

        player.playSound( location , sound , yaw , pitch );

    }

    public static void playTitle(Player player, String format) {

        format = format.replaceFirst( "[title] " , "" );

        String[] split = format.split("\\|");

        String title = "";
        String subTitle = "";

        for ( int i = 0 ; i < split.length ; i++ ) {
            if ( i == 0 )   title = split[ i ];
            if ( i == 1 )   subTitle = split[ i ];
        }

        player.sendTitle( title , subTitle );

    }

    public static List<String> applyReplacements( List<String> texts , String[][] replacements ) {

        List<String> result = new ArrayList<>();

        for ( String text : texts )   result.add( applyReplacements( text, replacements ) );

        return result;

    }

    public static String applyReplacements( String text , String[][] replacements ) {

        for ( String[] replacement : replacements ) {

            String key = replacement[0];

            String value = replacement[1];

            text = text.replaceAll(key, value);

        }

        return text;

    }

}