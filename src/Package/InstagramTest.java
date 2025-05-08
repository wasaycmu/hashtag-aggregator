
package Package;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jinstagram.Instagram;
import org.jinstagram.InstagramClient;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.common.Location;
import org.jinstagram.entity.common.Meta;
import org.jinstagram.entity.locations.LocationSearchFeed;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramBadRequestException;
import org.jinstagram.exceptions.InstagramException;
import org.jinstagram.exceptions.InstagramRateLimitException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class InstagramTest {

    private final Logger logger = LoggerFactory.getLogger(InstagramTest.class);

    private static final String IG_TOKEN_SYSTEM_PROPERTY = "IG_ACCESS_TOKEN";

    private InstagramClient instagram = null;

    @Before
    public void beforeMethod() {
        org.junit.Assume.assumeTrue(isAccessTokenAvailable());
    }

    private boolean isAccessTokenAvailable() {

        if (System.getenv(IG_TOKEN_SYSTEM_PROPERTY) != null) {
            logger.info("Access token found...");
            String ACCESS_TOKEN = System.getenv(IG_TOKEN_SYSTEM_PROPERTY);
            Token token = new Token(ACCESS_TOKEN, null);
            instagram = new Instagram(token);
            return true;
        } else {
            logger.error("No access token found...");
        }
        return false;
    }

    @Test(expected = InstagramBadRequestException.class)
    public void testInvalidAccessToken() throws Exception {
        InstagramClient instagram = new Instagram(new Token("InvalidAccessToken", null));
        instagram.getPopularMedia();
    }

    @Test
    @Ignore
    public void testInvalidRequest() throws Exception {
        instagram.getRecentMediaTags("test/u10191");
    }

    @Test
    public void testCurrentUserInfo() throws Exception {

        UserInfo userInfo = instagram.getCurrentUserInfo();
        UserInfoData userInfoData = userInfo.getData();

        logger.info("Username : " + userInfoData.getUsername());
        logger.info("Full name : " + userInfoData.getFullName());
        logger.info("Bio : " + userInfoData.getBio());

    }


    @Test
    @Ignore //cause getPopularMedia is deprecated
    public void testPopularMedia() throws Exception {

        logger.info("Printing a list of popular media...");
        MediaFeed popularMedia = instagram.getPopularMedia();

        printMediaFeedList(popularMedia.getData());

    }

    @Test
    public void searchLocation() throws Exception {

        // London  - 51.5072� N, 0.1275� W
        double latitude = 51.5072;
        double longitude = 0.1275;

        LocationSearchFeed locationSearchFeed = instagram.searchLocation(latitude, longitude);

        List<Location> locationList = locationSearchFeed.getLocationList();
        logger.info("Printing Location Details for Latitude " + latitude + " and longitude " + longitude);

        for (Location location : locationList) {
            logger.info("-------------------------------------------");

            logger.info("Id : " + location.getId());
            logger.info("Name : " + location.getName());
            logger.info("Latitude : " + location.getLatitude());
            logger.info("Longitude : " + location.getLatitude());

            logger.info("-------------------------------------------");

        }
    }

    @Test
    public void searchLocationWithDistance() throws Exception {
    	
    	// London  - 51.5072� N, 0.1275� W
    	double latitude = 51.5072;
    	double longitude = 0.1275;
    	int distance = 1000;
    	
		LocationSearchFeed locationSearchFeed = instagram.searchLocation(latitude, longitude, distance);
    	
    	List<Location> locationList = locationSearchFeed.getLocationList();
    	logger.info("Printing Location Details for Latitude " + latitude + " and longitude " + longitude);
    	
    	for (Location location : locationList) {
    		logger.info("-------------------------------------------");
    		
    		logger.info("Id : " + location.getId());
    		logger.info("Name : " + location.getName());
    		logger.info("Latitude : " + location.getLatitude());
    		logger.info("Longitude : " + location.getLatitude());
    		
    		logger.info("-------------------------------------------");
    		
    	}
    }

    @Test
    @Ignore // failing due to user not found
    public void userFollowedBy() throws Exception {
        // instagram user id
        String userId = "25025320";

        UserFeed feed1 = instagram.getUserFollowedByList(userId);
        assertEquals(50, feed1.getUserList().size());

        UserFeed feed2 = instagram.getUserFollowedByListNextPage(userId, feed1.getPagination().getNextCursor());
        assertEquals(50, feed2.getUserList().size());

        assertNotEquals(feed1.getUserList().get(0).getId(), feed2.getUserList().get(1).getId());
    }

    @Test
    @Ignore // failing due to user not found
    public void userFollower() throws Exception {
        // instagram user id
        String userId = "25025320";

        UserFeed feed1 = instagram.getUserFollowList(userId);
        assertEquals(50, feed1.getUserList().size());

        UserFeed feed2 = instagram.getUserFollowListNextPage(userId, feed1.getPagination().getNextCursor());
        assertEquals(50, feed2.getUserList().size());

        assertNotEquals(feed1.getUserList().get(0).getId(), feed2.getUserList().get(1).getId());
    }

    @Test
    public void searchUser() throws Exception {
        String query = "sachin";
        UserFeed userFeed = instagram.searchUser(query);

        for (UserFeedData userFeedData : userFeed.getUserList()) {
            System.out.println("\n\n**************************************************");
            System.out.println("Id : " + userFeedData.getId());
            System.out.println("Username : " + userFeedData.getUserName());
            System.out.println("Name  : " + userFeedData.getFullName());
            System.out.println("Bio : " + userFeedData.getBio());
            System.out.println("Profile Picture URL : " + userFeedData.getProfilePictureUrl());
            System.out.println("Website : " + userFeedData.getWebsite());
            System.out.println("**************************************************\n\n");


        }


    }

    @Test
    public void getMediaByTags() throws Exception {
        String tagName = "london";
        TagMediaFeed recentMediaTags = instagram.getRecentMediaTags(tagName);
        printMediaFeedList(recentMediaTags.getData());
    }

    @Test
    public void getMediaByTagsSpecialCharacters() throws Exception {
        String tagName = "#london";
        TagMediaFeed recentMediaTags = instagram.getRecentMediaTags(tagName);
        printMediaFeedList(recentMediaTags.getData());
    }

    @Test
    @Ignore // failing due to user not found
    public void testGetAllUserPhotos() throws Exception {
        getUserPhotos("18428658");
    }

    private List<MediaFeedData> getUserPhotos(String userId) throws Exception {
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


    private void printMediaFeedList(List<MediaFeedData> mediaFeedDataList) {

        for (MediaFeedData mediaFeedData : mediaFeedDataList) {
            logger.info("-------------------------------------------");

            logger.info("Id : " + mediaFeedData.getId());
            logger.info("Image Filter : " + mediaFeedData.getImageFilter());
            logger.info("Link : " + mediaFeedData.getLink());

            logger.info("-------------------------------------------");

        }
    }

    @Test(expected = InstagramRateLimitException.class)
    public void testCheckRateLimitException() throws InstagramException {
        int responseCode = 429; //according to API Docs https://instagram.com/developer/limits/
        String responseBody = createRateLimitMeta(429);

        Instagram instagram2=(Instagram) instagram;
        
        //nstagram2.handleInstagramError(responseCode, responseBody, null);
    }

    private String createRateLimitMeta(int code) {
        Meta meta = new Meta();
        meta.setCode(code);
        meta.setErrorMessage("message");
        meta.setErrorType("type");

        return new Gson().toJson(meta);
    }

    @Test
    public void testGetUserRecentMedia() throws Exception{
        MediaFeed mf = instagram.getUserRecentMedia();

        List<MediaFeedData> mediaFeedDataList = mf.getData();

        printMediaFeedList(mediaFeedDataList);

    }

    @Test
    @Ignore // Will fix later on
    public void testGetUserRecentMediaWithParams() throws  Exception{

        MediaFeed mf = instagram.getUserRecentMedia(2, null, null);

        List<MediaFeedData> mediaFeedDataList = mf.getData();
        Assert.assertEquals(mediaFeedDataList.size(), 2);

        printMediaFeedList(mediaFeedDataList);

    }
    
    
    public static void main(String[] args) throws Exception {

		String clientId = "3e01584498fa4a38a1a5d594d1e3d647";
		String clientSecret = "24ac297d4cf84d129fa7b9211e133bb2";

		String callbackUrl = "http://localhost";

		InstagramService service = new InstagramAuthService()
                                           .apiKey(clientId)
                                           .apiSecret(clientSecret)
				                           .callback(callbackUrl)
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
		
		
	    
	}

}