package com.baseteam.test4;

/**
 * Created by matvey on 07.10.15.
 */
public class VKaudio {
    public int id;
    public String artist;
    public String title;
    public int duration;
    public String url;
    public  VKaudio(int _id, String _artist, String _title, int _duration, String _url)
    {
        id = _id;
        artist = _artist;
        title = _title;
        duration = _duration;
        url = _url;
    }
}
