package com.clients;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

/**
 * Main class.
 *
 */
public class Main {


    private static void printWelcomeMessage(){
        System.out.println("Welcome to the A1 RESTful web service.");
    }
    private static int loopMenu(String choices, int lowerValue, int upperValue){
        int choice = -1;
        while(true){
            System.out.println(choices);
            Scanner scan = new Scanner(System.in);
             choice = scan.nextInt();
            if(choice >=  lowerValue && choice <= upperValue){
                break;
            }
            else{
                System.out.println("Invalid input. Please enter a number between " + lowerValue + " and " + upperValue);
            }
        }
        return choice;
    }
    private static void mainMenu(){

        Scanner scan = new Scanner(System.in);
        String choices =
                "[MAIN MENU]\nWhat would you like to manage?\n"+
                        "\t 1. Albums\n"+
                        "\t 2. Artists\n"+
                        "\t 3. EXIT";
        int choice = loopMenu(choices, 1,3);
        switch(choice){
            case 1:
                manageAlbumsMenu();
                break;
            case 2:
                manageArtistsMenu();
                break;
            case 3:
            {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            break;
        }
    }

    private static void promptGetAlbumDetails(){
        System.out.println("[GET ALBUM DETAILS]\n Please enter the ISRC of the album you want to get details for:");
        Scanner scan = new Scanner(System.in);
        String ISRC = scan.nextLine();
        /*
        Use ISRC input to get the album details, send HTTP request.
         */
        manageAlbumsMenu();
    }
    private static String promptUpdateSingleAlbumAttribute(String ISRC, String attribute){
        String result = null;
        String newValue = getInputFor(attribute);
        /*
        API call, get the string. Use a switch statement to determine which attribute to change.
         */
        return result;
    }
    private static String promptUpdateAllAlbumAttributes(String ISRC){
        String result = null;
        String newTitle = getInputFor("Title");
        String newDescription = getInputFor("Description");
        String newReleaseYear = getInputFor("Release year");
        String newArtistNickname = getInputFor("Artist nickname");
        /*
        API call, get the string.
         */
        return result;
    }
    private static void promptUpdateAlbum(){
        System.out.println("[UPDATE ALBUM]\n Please enter the ISRC of the album you want to update.");
        String ISRC = getInputFor("ISRC");
        String choices = "";
        choices += ("Which attribute do you want to update?");
        choices += ("\t 1. Title\n");
        choices += ("\t 2. Description\n");
        choices += ("\t 3. Release year\n");
        choices += ("\t 4. Artist (Nickname)\n");
        choices += ("\t 5. All attributes\n");
        choices += ("\t 6. ↵ ALBUMS menu");
        int choice = loopMenu(choices, 1, 6);
        switch(choice){
            case 1:
                promptUpdateSingleAlbumAttribute(ISRC, "Title");
                break;
            case 2:
                promptUpdateSingleAlbumAttribute(ISRC, "Description");
                break;
            case 3:
                promptUpdateSingleAlbumAttribute(ISRC, "Release year");
                break;
            case 4:
                promptUpdateSingleAlbumAttribute(ISRC, "Artist nickname");
                break;
            case 5:
                promptUpdateAllAlbumAttributes(ISRC);
                break;
            case 6:
                manageAlbumsMenu();
                break;
        }
    }
    private static String getInputFor(String thing){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter " + thing + "> ");
        return scan.nextLine();
    }
    private static void listAlbums(){
        /*
        API call to list all albums.
         */
    }
    private static void promptDeleteAlbum(){
        String ISRC = getInputFor("ISRC");
        /*
        API call to delete the album.
         */
    }
    private static void promptAddAlbum(){
        String ISRC = getInputFor("ISRC");
        String title = getInputFor("Title");
        String description = getInputFor("Description");
        String releaseYear = getInputFor("Release year");
        String artistNickname = getInputFor("Artist nickname");
        /*
        API call to add an album.
         */
    }
    private static void manageAlbumsMenu(){

        String choices = "";
        choices+=("[ALBUMS]\n Select an option below:\n");
        choices+=("\t 1. Add album\n");
        choices+=("\t 2. Delete album\n");
        choices+=("\t 3. List all albums\n");
        choices+=("\t 4. Update an album\n");
        choices+=("\t 5. Get album details\n");
        choices+=("\t 6. ↵ Main menu\n");
        int choice = loopMenu(choices, 1,6);
        switch (choice) {
            case 1:
                promptAddAlbum();
                manageAlbumsMenu();
                break;
            case 2:
                promptDeleteAlbum();
                manageAlbumsMenu();
                break;
            case 3:
                listAlbums();
                manageAlbumsMenu();
                break;
            case 4:
                promptUpdateAlbum();
                manageAlbumsMenu();
                break;
            case 5:
                promptGetAlbumDetails();
                manageAlbumsMenu();
                break;
            case 6:
                mainMenu();
                break;
        }
    }
    private static void promptAddArtist(){
        String nickname = getInputFor("Artist nickname");
        String firstName = getInputFor("Artist first name");
        String lastName = getInputFor("Artist last name");
        String bio = getInputFor("Artist bio");
        /*
        API CALL FOR ADD ARTIST
         */
    }
    private static void promptDeleteArtist(){
        String nickname = getInputFor("nickname of artist to delete");
        /*
        API CALL FOR DELETE ARTIST
         */
    }
    private static void listAllArtists(){
        /*
        API CALL FOR LIST ALL ARTISTS
         */
    }
    private static void promptUpdateSingleArtistAttribute(String nickname, String attribute){
        String newValue = getInputFor("new " + attribute.toLowerCase());
        /*
        API CALL FOR UPDATING A SINGLE ATTRIBUTE func(nickname, attribute, new value)
         */
    }
    private static void promptUpdateAllArtistAttributes(String nickname){
        String newFirstName = getInputFor("new first name");
        String newLastName = getInputFor("new last name");
        String newBio = getInputFor("new bio");
        /*
        API CALL FOR UPDATING ALL ATTRIBUTES (nickname, firstName, lastName, bio)
         */
    }
    private static void promptUpdateArtist(){
        System.out.println("[UPDATE ARTIST]\n Please enter the nickname of the artist you want to update.");

        String nickname = getInputFor("nickname");
        String choices = "";
        choices += ("Which attribute do you want to update for " + nickname + "?");
        choices += ("\t 1. First name\n");
        choices += ("\t 2. Last name\n");
        choices += ("\t 3. Bio\n");
        choices += ("\t 4. All attributes\n");
        choices += ("\t 5. ↵ ARTISTS menu");
        int choice = loopMenu(choices, 1,5);
        switch(choice){
            case 1:
                promptUpdateSingleArtistAttribute(nickname, "First name");
                break;
            case 2:
                promptUpdateSingleArtistAttribute(nickname, "Last name");
                break;
            case 3:
                promptUpdateSingleArtistAttribute(nickname, "Bio");
                break;
            case 4:
                promptUpdateAllArtistAttributes(nickname);
                break;
            case 5:
                manageArtistsMenu();
                break;
        }
    }
    private static void promptGetArtistDetails(){
        String nickname = getInputFor("artist nickname");
        /*
        API CALL FOR GETTING ARTIST DETAILS OF "nickname"
         */

    }
    private static void manageArtistsMenu(){
        String choices = "";
        choices += ("[ARTISTS] Select an option.\n");
        choices+=("\t 1. Add artist\n");
        choices+=("\t 2. Delete artist\n");
        choices+=("\t 3. List all artists\n");
        choices+=("\t 4. Update an artist\n");
        choices+=("\t 5. Get artist details\n");
        choices+=("\t 6. ↵ Main menu\n");
        int choice = loopMenu(choices, 1,6);
        switch(choice){
            case 1:
                promptAddArtist();
                manageArtistsMenu();
                break;
            case 2:
                promptDeleteArtist();
                manageArtistsMenu();
                break;
            case 3:
                listAllArtists();
                manageArtistsMenu();
                break;
            case 4:
                promptUpdateArtist();
                manageArtistsMenu();
                break;
            case 5:
                promptGetArtistDetails();
                manageArtistsMenu();
                break;
            case 6:
                mainMenu();
                break;
        }
    }
    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        printWelcomeMessage();
        mainMenu();
    }
}

