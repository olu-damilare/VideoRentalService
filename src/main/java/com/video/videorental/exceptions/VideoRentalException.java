package com.video.videorental.exceptions;

public class VideoRentalException extends RuntimeException{

    public VideoRentalException(String message){
        super(message);
    }
    public VideoRentalException(String message, Throwable e){
        super(message, e);
    }
    public VideoRentalException(){
        super();
    }
    public VideoRentalException(Throwable e){
        super(e);
    }
}
