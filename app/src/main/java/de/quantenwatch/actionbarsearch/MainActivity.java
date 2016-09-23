package de.quantenwatch.actionbarsearch;

import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    private FeedAdapter feedAdapter;
    private FeedAdapter resultFeedAdapter;
    private ListView feedList;

    public static final String FEED_URL = "http://www.uni-duesseldorf.de/home/startseite/rss.xml";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        super.onCreateOptionsMenu(menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                makeToast("Suche " + query);
                resultFeedAdapter.filter(query, feedAdapter);
                feedList.setAdapter(resultFeedAdapter);
                resultFeedAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { return false;}
        });

        MenuItemCompat.setOnActionExpandListener(
        searchItem,
        new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                makeToast("Suche verlassen");
                feedList.setAdapter(feedAdapter);
                feedAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                makeToast("Suche betreten");
                resultFeedAdapter.clear();
                feedList.setAdapter(resultFeedAdapter);
                resultFeedAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedAdapter = new FeedAdapter(this);
        feedList = (ListView) findViewById(R.id.feedList);
        feedList.setAdapter(feedAdapter);

        resultFeedAdapter = new FeedAdapter(this);
        new RetrieveFeedTask().execute(FEED_URL);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... urlsStr) {
            try {
                URL url = new URL(urlsStr[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("item");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Feed feed = new Feed();
                    Node node = nodeList.item(i);

                    Element fstElmnt = (Element) node;
                    NodeList nameList = fstElmnt.getElementsByTagName("title");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    feed.title = ((Node) nameList.item(0)).getNodeValue();

                    NodeList websiteList = fstElmnt.getElementsByTagName("description");
                    Element websiteElement = (Element) websiteList.item(0);
                    websiteList = websiteElement.getChildNodes();
                    feed.body = feed.removeHtml(((Node) websiteList.item(0)).getNodeValue());
                    feedAdapter.addItem(feed);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            feedAdapter.notifyDataSetChanged();
        }
    }

    public void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
