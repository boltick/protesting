package com.gramant.jtr.log;

/**
 * Screen-Shot object
 * User: AB83625
 * Date: 24/05/11
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class ScreenShot {

    private String title;
    private String path;

    public ScreenShot(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "SCREEN_SHOT{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
