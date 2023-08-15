package yanagawa.websocket.http;

public enum ContentTypeDef {
	HTML,JAVASCRIPT,JSON,VIDEOMP4,IMGPNG;
	public String getContentType() {
		switch(this){
		case HTML:
			return "text/html; charset=UTF-8";
		case JAVASCRIPT:
			return "text/javascript; charset=UTF-8";
		case JSON:
			return "application/json; charset=UTF-8";
		case VIDEOMP4:
			return "video/mp4";
		case IMGPNG:
			return "img/png";
		}
		return "text/plain; charset=UTF-8";
	}
}
