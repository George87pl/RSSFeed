package com.gmail.gpolomicz.rssfeed;

//import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseString {
    private static final String TAG = "GPDEB";

    private ArrayList<FeedEntry> informationArrayList;

    public ParseString() {
        this.informationArrayList = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getInformationArrayList() {
        return informationArrayList;
    }

    public String getLink(int position) {
        return informationArrayList.get(position).getLink();
    }

    public boolean parseXML(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inItem = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: Starting tag for  " + tagName);
                        if ("item".equalsIgnoreCase(tagName)) {
                            inItem = true;
                            currentRecord = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if (inItem) {
                            if ("item".equalsIgnoreCase(tagName)) {
                                informationArrayList.add(currentRecord);
                                inItem = false;
                            } else if ("title".equalsIgnoreCase(tagName)) {
                                currentRecord.setTitle(textValue);
                            } else if ("link".equalsIgnoreCase(tagName)) {
                                currentRecord.setLink(textValue);
                            } else if ("description".equalsIgnoreCase(tagName)) {
                                String[] image = textValue.split("src=\"", 2);
                                image = image[1].split("\"",2);
                                currentRecord.setImage(image[0]);

                            } else if ("pubDate".equalsIgnoreCase(tagName)) {
                                String date= textValue.substring(0, textValue.length() - 9);
                                currentRecord.setPubDate(date);
                            }
                        }
                        break;
                    default:
                        // Nothing else to do
                }
                eventType = xpp.next();
            }

//            for (FeedEntry info : informationArrayList) {
//                Log.d(TAG, "*************");
//                Log.d(TAG, info.toString());
//            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}
