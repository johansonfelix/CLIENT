package com.clients;

import com.sun.security.ntlm.Server;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.json.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Scanner;

/**
 * Main class.
 *
 */
public class Main {
    private static String RESTPath = "http://localhost:8080/myapp/album/";
    private static String servletPath = "http://localhost:8081/myapp/artist/"; // RENAME THIS
    public enum ServerType{
        REST,
        Servlet
    }
    public enum RequestType{
        GET,
        POST,
        PUT,
        DELETE
    }

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

    private static String httpRequest(ServerType type, RequestType requestType, String contentType, String path, String data){

        String beginningPath = "";
        switch(type){
            case REST:
                beginningPath = RESTPath;
                break;
            case Servlet:
                beginningPath = servletPath;
                break;
        }
        HttpResponse response = null;
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        String fullPath = beginningPath + path;
        HttpUriRequest request = null;
        switch(requestType){
            case POST:
                request = new HttpPost(fullPath);
                break;
            case GET:
                request = new HttpGet(fullPath);
                break;
            case PUT:
                request = new HttpPut(fullPath);
                break;
            case DELETE:
                request = new HttpDelete(fullPath);
                break;
        }
        StringEntity input = null;
        try {
            if(request instanceof HttpPost || request instanceof HttpPut){
                input = new StringEntity(data);
                input.setContentType(contentType);
                if(request instanceof HttpPost){
                    ((HttpPost) request).setEntity(input);
                    System.out.println("Is a post request");
                }
                else if(request instanceof HttpPut){
                    ((HttpPut) request).setEntity(input);
                    System.out.println("Is a put request");
                }
            }
            try {
                response = httpClient.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity = response.getEntity();
            String responseString = null;
            responseString = EntityUtils.toString(entity, "UTF-8");
            return responseString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
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
        System.out.println(httpRequest(ServerType.REST, RequestType.GET, null, "get-details/" + ISRC, null));
        manageAlbumsMenu();
    }
    private static String promptUpdateSingleAlbumAttribute(String ISRC, String attribute){
        String result = null;
        String newValue = getInputFor(attribute);
        /*
        API call, get the string. Use a switch statement to determine which attribute to change.
         */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("attribute",attribute);
        jsonObject.put("newValue",newValue);
        String data = jsonObject.toString();
        System.out.println(httpRequest(ServerType.REST, RequestType.PUT, MediaType.APPLICATION_JSON, "update-single-attribute/" + ISRC, data));
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("newTitle",newTitle);
        jsonObject.put("newDescription",newDescription);
        jsonObject.put("newReleaseYear",newReleaseYear);
        jsonObject.put("newArtistNickname",newArtistNickname);
        String jsonString = jsonObject.toString();
        System.out.println(httpRequest(ServerType.REST, RequestType.PUT, MediaType.APPLICATION_JSON, "update-all-attributes/" + ISRC, jsonString));
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
                promptUpdateSingleAlbumAttribute(ISRC, "title");
                break;
            case 2:
                promptUpdateSingleAlbumAttribute(ISRC, "description");
                break;
            case 3:
                promptUpdateSingleAlbumAttribute(ISRC, "releaseYear");
                break;
            case 4:
                promptUpdateSingleAlbumAttribute(ISRC, "artistNickname");
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
        System.out.println(httpRequest(ServerType.REST, RequestType.GET, null, "get-details-all", null));
    }
    private static void promptDeleteAlbum(){
        String ISRC = getInputFor("ISRC");
        /*
        API call to delete the album.
         */
        System.out.println(httpRequest(ServerType.REST, RequestType.DELETE, null, "delete/" + ISRC, null));
    }
    private static String addTag(String name, String type){
        if(type.equals("open")){
            return "<" + name + ">";

        }
        else return "</" + name + ">";
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
        String xml = "";
        xml += addTag("album", "open");
        xml += addTag("ISRC", "open");
        xml += ISRC;
        xml += addTag("ISRC", "closed");
        xml += addTag("title","open");
        xml += title;
        xml += addTag("title","closed");
        xml += addTag("description","open");
        xml += description;
        xml += addTag("description", "closed");
        xml += addTag("releaseYear", "open");
        xml += releaseYear;
        xml += addTag("releaseYear","closed");
        xml += addTag("artist","open");
        xml += artistNickname;
        xml += addTag("artist","closed");
        xml += addTag("album","closed");
        System.out.println(httpRequest(ServerType.REST,RequestType.POST,MediaType.TEXT_XML, "add",xml));
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

