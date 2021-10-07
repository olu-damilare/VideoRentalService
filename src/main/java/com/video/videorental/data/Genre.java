package com.video.videorental.data;

public enum Genre {
    ACTION, DRAMA, ROMANCE, COMEDY, HORROR;

    @Override
    public String toString() {
        String value;
        switch (this){
            case ACTION:
                value = "Action";
                break;
            case DRAMA:
                value = "Drama";
                break;
            case COMEDY:
                value = "Comedy";
                break;
            case HORROR:
                value = "Horror";
                break;
            case ROMANCE:
                value = "Romance";
                break;
            default:
                value = null;
        }
        return value;
    }
}
