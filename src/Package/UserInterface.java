package Package;

import java.awt.*;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.*;

import javax.swing.JFrame;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Post;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class UserInterface extends java.applet.Applet implements ActionListener {
	
	public String trending;
	public static String trendz;
	public TextArea ta;
	public TextArea ta2;
	
	public void init() {
		
		trendz = "";
		trending = "";
		
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
			for (Trend b: a) {
				trendz = trendz + "" + b.getName() + System.getProperty("line.separator");
			}
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		trending = "These are the current worldwide trends:" + System.getProperty("line.separator") + trendz;
		
		ta = new TextArea(trending);
		ta2 = new TextArea("", 1, 40);
		Button button = new Button("Search");
		button.addActionListener(this);

		Panel p;
		setLayout(new FlowLayout());
		p = new Panel();
		p.add(ta);
		add("Center", p);
		p = new Panel();
		p.add(new TextField("Enter your search query here and press the search button: "));
		add("Center", p);
		p = new Panel();
		p.add(ta2);
		add("Middle", p);
		p = new Panel();
		p.add(button);
		add("South", p);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		String results = "";
		
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
		
		String search = ta2.getText();
		Map<String, Object> params = new HashMap<String, Object>();
    	params.put("filter", "text");
    	List<Post> posts = client.tagged(search, params);
    	for (Post x: posts){
			results = results + "Blog Name: " + x.getBlogName() +  "   -------   "+ "BlogPost: " + x.getSlug() + System.getProperty("line.separator");
		}
    	
    	Query query1 = new Query(search);
        query1.setCount(20);// sets the number of tweets to return per page, up to a max of 100
        QueryResult result1;
        
		try {
			result1 = twitter.search(query1);
			for (Status xyz : result1.getTweets()){
	        	MediaEntity [] media = xyz.getMediaEntities();
				results = results + "Twitter User: " + xyz.getUser().getName() + "   -------   " +"Tweet: "+ xyz.getText() + System.getProperty("line.separator");
				for(MediaEntity m : media){
					System.out.println(m.getMediaURL()); 
				}
	        }
		} catch (TwitterException ex) {
			ex.printStackTrace();
		}
		
		Panel p;
		p = new Panel();
		p.add(new TextArea(results));
		add("South", p);
		p.validate();
		p.repaint();
	}
	
	public static void main(String [] args) {
		JFrame f = new JFrame("# Aggregator");
		UserInterface ui = new UserInterface();
		f.add("Center", ui);
		ui.init();
		
		f.pack();
		f.show();
	}
}

