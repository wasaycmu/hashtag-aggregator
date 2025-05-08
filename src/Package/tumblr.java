package Package;


import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.request.RequestBuilder;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;
import org.scribe.model.Token;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class tumblr {

    private RequestBuilder requestBuilder;
    private String apiKey;
    
    public static void main(String[] args){
    	JumblrClient client = new JumblrClient("8S8k1LWVXXv41pEZz5VjqWGsgYZzRbFE4GkYYd3oLaSJT9nY0M", 
    			"AZULojHohuo35u1aO4SptNcgnKINtVyLvnPjYsQbXj7CP6o99E");
    	client.setToken(
    			  "iyKGrBB28cDXNHfZvVvKAqkAK9h6jbJz2VJPinLnMSWuDXNSbg",
    			  "TncrPYXtYxeKBXAd5o4q3VvgrksRKy91ko1JyuLY8J0GxMj7Lq"
    			);
    	
    	
		ConfigurationBuilder cf = new ConfigurationBuilder();
		cf.setDebugEnabled(true).setOAuthConsumerKey("JObJORbxO1zczrjNNIwt20hyt")
					.setOAuthConsumerSecret("vcqZxIHPqcN4d0p7zjbQa4jhvccduWgESXSTJYMqpSaVj1o86c")
					.setOAuthAccessToken("761983970-sjfdLxgV9meVyciBJ3C4R9iU5HFp89THOvwdxIYh")
					.setOAuthAccessTokenSecret("ZRPqEDjFwdOKSqseLX72Mq7IQWN4UhSXLTXyPoHg47hcf");
		TwitterFactory tf	= new TwitterFactory(cf.build());
		
		twitter4j.Twitter twitter = tf.getInstance();
		
		Trends trends;
		try {
			trends = twitter.getPlaceTrends(1);
			Trend[] a = trends.getTrends();
			System.out.println("These are the current worldwide trends:");
			for (Trend b: a){
				System.out.println(b.getName());
			}
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		Scanner aw = new Scanner(System.in);
		String i;
		System.out.println("    ---------------------------------      ");
		System.out.print("Enter your Search Query: ");
		i = aw.next();
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("filter", "text");
    	List<Post> posts = client.tagged(i, params);
    	for (Post x: posts){
			System.out.println("Blog Name: " + x.getBlogName() +  "   -------   "+ "BlogPost: " + x.getSlug());
		}
    	
    	Query query1 = new Query(i);
        query1.setCount(20);// sets the number of tweets to return per page, up to a max of 100
        QueryResult result1;
        
		try {
			result1 = twitter.search(query1);
			for (Status xyz : result1.getTweets()){
	        	MediaEntity [] media = xyz.getMediaEntities();
				System.out.println("Twitter User: " + xyz.getUser().getName() + "   -------   " +"Tweet: "+ xyz.getText());
				for(MediaEntity m : media){
					System.out.println(m.getMediaURL()); 
				}
	        }
		} catch (TwitterException e) {
			e.printStackTrace();
		}

    }

    
    public List<Post> blogPosts(String blogName, Map<String, ?> options) {
        if (options == null) {
            options = Collections.emptyMap();
        }
        Map<String, Object> soptions = tumblr.safeOptionMap(options);
        soptions.put("api_key", apiKey);

        String path = "/posts";
        if (soptions.containsKey("type")) {
            path += "/" + soptions.get("type").toString();
            soptions.remove("type");
        }
        return requestBuilder.get(tumblr.blogPath(blogName, path), soptions).getPosts();
    }

    public List<Post> blogPosts(String blogName) {
        return this.blogPosts(blogName, null);
    }

    
    private static String blogPath(String blogName, String extPath) {
        return "/blog/" + blogUrl(blogName) + extPath;
    }

    private static String blogUrl(String blogName) {
        return blogName.contains(".") ? blogName : blogName + ".tumblr.com";
    }

    public void setRequestBuilder(RequestBuilder builder) {
        this.requestBuilder = builder;
    }

    public RequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

 

}