package Package;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.common.Location;
import org.jinstagram.entity.locations.LocationSearchFeed;
import org.jinstagram.entity.tags.TagInfoData;
import org.jinstagram.entity.tags.TagInfoFeed;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.tags.TagSearchFeed;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;


public class InstagramAuthenticationTest  {
	
    private static List<MediaFeedData> getUserPhotos(Instagram instagram, String userId) throws Exception {
        // Don't get all the photos, just break the page count on 5
        final int countBreaker = 5;
        MediaFeed recentMediaFeed = instagram.getRecentMediaFeed(userId);
        List<MediaFeedData> userPhotos = new ArrayList<MediaFeedData>();

        for (MediaFeedData mediaFeedData : recentMediaFeed.getData()) {
            userPhotos.add(mediaFeedData);
        }

        int count = 0;
        while (recentMediaFeed.getPagination() != null) {
            count++;

            if (count == countBreaker) {
                System.out.println("Too many photos to get!!! Breaking the loop.");
                break;
            }

            try {
                recentMediaFeed = instagram.getRecentMediaNextPage(recentMediaFeed.getPagination());
                for (MediaFeedData mediaFeedData : recentMediaFeed.getData()) {
                    userPhotos.add(mediaFeedData);
                }
            } catch (Exception ex) {
                break;
            }
        }

        return userPhotos;
    }
	
    public static void testGetUserRecentMedia(Instagram instagram) throws Exception{
        MediaFeed mf = instagram.getUserRecentMedia();

        List<MediaFeedData> mediaFeedDataList = mf.getData();

        printMediaFeedList(mediaFeedDataList);

    }
    
    public static void searchUser(Instagram instagram, String i) throws Exception {
        String query = i;
        //UserFeed userFeed = instagram.searchUser(query);
        UserInfo aw = instagram.getUserInfo(query);
            System.out.println("\n\n**************************************************");
            System.out.println("Id : " + aw.getData().getId());
            System.out.println("Username : " + aw.getData().getUsername());
            System.out.println("Name  : " + aw.getData().getFullName());
            System.out.println("Bio : " + aw.getData().getBio());
            System.out.println("Profile Picture URL : " + aw.getData().getProfilePicture());
            System.out.println("Website : " + aw.getData().getWebsite());
            System.out.println("**************************************************\n\n");
        

        }

	
    public static void searchLocation(Instagram instagram) throws Exception {

        // London  - 51.5072� N, 0.1275� W
        double latitude = 51.5072;
        double longitude = 0.1275;

        LocationSearchFeed locationSearchFeed = instagram.searchLocation(latitude, longitude);

        List<Location> locationList = locationSearchFeed.getLocationList();
        System.out.println(("Printing Location Details for Latitude " + latitude + " and longitude " + longitude));

        for (Location location : locationList) {
            System.out.println(("-------------------------------------------"));

            System.out.println(("Id : " + location.getId()));
            System.out.println(("Name : " + location.getName()));
            System.out.println(("Latitude : " + location.getLatitude()));
            System.out.println(("Longitude : " + location.getLatitude()));

            

        }
    }
	@SuppressWarnings("deprecation")
	public static void getMediaByTags(Instagram instagram, String i) throws Exception {
		long a = 10;
		TagMediaFeed b = instagram.getRecentMediaTags(i, a);
		TagMediaFeed a1 = instagram.getRecentMediaTagsByRegularIds(i, null, null);
        TagMediaFeed recentMediaTags = instagram.getRecentMediaTags(i);
        TagSearchFeed c = instagram.searchTags(i);
		
        //System.out.println("1");
		
        System.out.println(recentMediaTags);
        //System.out.println("2");
        System.out.println(recentMediaTags.getData());
        System.out.println(a1.getData());
        System.out.println(b.getData());
        System.out.println(c.toString());
        //System.out.println("3");
        printMediaFeedList(recentMediaTags.getData());
        printMediaFeedList(a1.getData());
        printMediaFeedList(b.getData());
    }
	
    private static void printMediaFeedList(List<MediaFeedData> mediaFeedDataList) {
    	//System.out.println("i'm here");
    	System.out.println(mediaFeedDataList);
        for (MediaFeedData mediaFeedData : mediaFeedDataList) {
            System.out.println(("-------------------------------------------"));
            
            System.out.println(("Id : " + mediaFeedData.getId()));
            System.out.println(("Image Filter : " + mediaFeedData.getImageFilter()));
            System.out.println(("Link : " + mediaFeedData.getLink()));
            
            }
		
    }
    

        
	public static void main(String[] args) throws Exception {

		String clientId = "3e01584498fa4a38a1a5d594d1e3d647";
		String clientSecret = "24ac297d4cf84d129fa7b9211e133bb2";

		String callbackUrl = "http://localhost";

		InstagramService service = new InstagramAuthService()
                                           .apiKey(clientId)
                                           .apiSecret(clientSecret)
				                           .callback(callbackUrl)
				                           .scope("public_content")
                                           .build();

		String authorizationUrl = service.getAuthorizationUrl();

		System.out.println("** Instagram Authorization ** \n\n");

		System.out.println("Copy & Paste the below Authorization URL in your browser...");
		System.out.println("Authorization URL : " + authorizationUrl);

		Scanner sc = new Scanner(System.in);

		String verifierCode;

		System.out.print("Your Verifier Code : ");
		verifierCode = sc.next();

		System.out.println();

		Verifier verifier = new Verifier(verifierCode);
		Token accessToken = service.getAccessToken(verifier);

		System.out.println("Access Token :: " + accessToken.getToken());
		Instagram instagram = new Instagram(accessToken);

		UserInfo userInfo = instagram.getCurrentUserInfo();
		
		System.out.println("***** User Info ******");
		System.out.println("Username : " + userInfo.getData().getUsername());
		System.out.println("First Name : " + userInfo.getData().getFirstName());
		System.out.println("Last Name : " + userInfo.getData().getLastName());
		System.out.println("Website : " + userInfo.getData().getWebsite());
		System.out.println(userInfo.getData().getBio());
		
		
		getMediaByTags( instagram, "snow");
/*  	Scanner aw = new Scanner(System.in);

		String i;

		System.out.print("Your Search Query ");
		i = sc.next();
        getMediaByTags(instagram,  i);*/
		
		//testGetUserRecentMedia(instagram);
		//getMediaByTags( instagram,  "weeknd");
		
		//String tagName = "snow";
		//TagMediaFeed mediaFeed = instagram.getRecentMediaTags(tagName);

		//List<MediaFeedData> mediaFeeds = mediaFeed.getData();
		//System.out.println(mediaFeeds.size());
		//printMediaFeedList(mediaFeeds);
		
		
		//searchLocation(instagram);
		
		
		//searchUser(instagram, "@makwasa");


		
	}

}
