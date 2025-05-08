package Package;

import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Location;
import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class twitter {

	public static void main(String[] args) throws TwitterException {
		
		ConfigurationBuilder cf = new ConfigurationBuilder();
		cf.setDebugEnabled(true).setOAuthConsumerKey("JObJORbxO1zczrjNNIwt20hyt").setOAuthConsumerSecret("vcqZxIHPqcN4d0p7zjbQa4jhvccduWgESXSTJYMqpSaVj1o86c").setOAuthAccessToken("761983970-sjfdLxgV9meVyciBJ3C4R9iU5HFp89THOvwdxIYh").setOAuthAccessTokenSecret("ZRPqEDjFwdOKSqseLX72Mq7IQWN4UhSXLTXyPoHg47hcf");
		
		
		TwitterFactory tf	= new TwitterFactory(cf.build());
		
		twitter4j.Twitter twitter = tf.getInstance();
		
		List<Status> status = twitter.getHomeTimeline();
		
		User aw = twitter.showUser("Makwasay");
		System.out.println(aw.toString());
		for (Status st : status){
			System.out.println(st.getUser().getName() + "   -------   " + st.getText());
		}
		
		Trends trends = twitter.getPlaceTrends(1);
		
		Trend[] a = trends.getTrends();
		for (Trend b: a){
			
			System.out.println(b.getName());
		}
		
		//System.out.println(trends.getTrends().toString());
		
		
/*		java.util.List<Location> abc =  twitter.getAvailableTrends();
		 assertNotNull(TwitterObjectFactory.getRawJSON(abc));
	        assertEquals(abc.get(0), TwitterObjectFactory.createLocation(TwitterObjectFactory.getRawJSON(abc.get(0))));
	        assertTrue(abc.size() > 0);
		for (Location location : abc) {
            System.out.println(location.getName() + " (woeid:" + location.getWoeid() + ")");
	}*/
		
		
        /*Query query = new Query("#weeknd");
        QueryResult result = twitter.search(query);
        for (Status status1 : result.getTweets()) {
            System.out.println("@" + status1.getUser().getScreenName() + " : " + status1.getText() + " : " + status1.getGeoLocation());
        }*/
        
        Query query1 = new Query("");
        query1.setCount(100);// sets the number of tweets to return per page, up to a max of 100
        QueryResult  result1 = twitter.search(query1);
         
     
        for (Status xyz : result1.getTweets()){
        	MediaEntity [] media = xyz.getMediaEntities();
			System.out.println(xyz.getUser().getName() + "   -------   " + xyz.getText());
			for(MediaEntity m : media){
				System.out.println(m.getMediaURL()); //get your url!
			}
				
			}
        
}

        
        
        
		
	
}
