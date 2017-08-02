package com.myAndroidServer.entity;

//杩斿洖post淇℃伅
public class PostBean {
      private int p_id;
      private int userId;
	  private String userNameStr;
	    private String userHeadStr;
	    private String textContent;
	    private String imageContent;
	    private String time;
	    private boolean relationship;             //是否关注
	    private boolean isPrised;
	    private int priseNum;
	    private int commentNum;
	    private int t_id;
	  
         //首页帖子
		public PostBean(int p_id,int userId, String userNameStr,String userHeadStr,String textContent, 
	    		String imageContent,String time,boolean relationship,boolean isPrised,int priseNum,int commentNum){
	    	this.p_id = p_id;
	    	this.userId = userId;
	        this.userNameStr = userNameStr;
	        this.userHeadStr = userHeadStr;
	        this.textContent = textContent;
	        this.imageContent = imageContent;
	        this.time = time;
	        this.relationship = relationship;
	        this.isPrised = isPrised;
	        this.priseNum = priseNum;
	        this.commentNum = commentNum;
	    }
	    //话题下的帖子
		public PostBean(int p_id,int t_id,int userId, String userNameStr,String userHeadStr,String textContent, 
	    		String imageContent,String time){
	    	this.p_id = p_id;
	    	this.t_id = t_id;
	    	this.userId = userId;
	        this.userNameStr = userNameStr;
	        this.userHeadStr = userHeadStr;
	        this.textContent = textContent;
	        this.imageContent = imageContent;
	        this.time = time;

	    }
	    
	    public PostBean(int userId, String userNameStr,String userHeadStr,String textContent, String imageContent){
	    	this.userId = userId;
	        this.userNameStr = userNameStr;
	        this.userHeadStr = userHeadStr;
	        this.textContent = textContent;
	        this.imageContent = imageContent;
	    }
	    
	    public PostBean(int t_id,int userId, String userNameStr,String userHeadStr,String textContent, String imageContent){
	    	this.t_id = t_id;
	    	this.userId = userId;
	        this.userNameStr = userNameStr;
	        this.userHeadStr = userHeadStr;
	        this.textContent = textContent;
	        this.imageContent = imageContent;
	    }
	    
	    public PostBean(int userId,String userNameStr,String userHeadStr,String textContent, String imageContent,String time){
	    	this.userId = userId;
	    	this.userNameStr = userNameStr;
	        this.userHeadStr = userHeadStr;
	        this.textContent = textContent;
	        this.imageContent = imageContent;
	        this.time = time;
	    }
	    
	    public int getT_id() {
			return t_id;
		}

		public void setT_id(int t_id) {
			this.t_id = t_id;
		}
		
	    public int getP_id(){
	    	return p_id;
	    }
        public void setP_id(int id){
        	this.p_id = id;
        }
        
        public int getUserId(){
        	return userId;
        }
        
        public void setUserId(int userId){
        	this.userId = userId;
        }
        
	    public String getUserNameStr() {
	        return userNameStr;
	    }

	    public void setUserNameStr(String userNameStr) {
	        this.userNameStr = userNameStr;
	    }

	    public String getUserHeadStr() {
	        return userHeadStr;
	    }

	    public void setUserHeadStr(String userHeadStr) {
	        this.userHeadStr = userHeadStr;
	    }

	    public String getTextContent() {
	        return textContent;
	    }

	    public void setTextContent(String textContent) {
	        this.textContent = textContent;
	    }

	    public String getImageContent() {
	        return imageContent;
	    }

	    public void setImageContent(String imageContent) {
	        this.imageContent = imageContent;
	    }

	    public String getTime() {
	        return time;
	    }

	    public void setTime(String time) {
	        this.time = time;
	    }
	    
	    public boolean getRelationship(){
	    	return relationship;
	    }
	    
	    public void setRelationship(boolean relationship){
	    	this.relationship = relationship;
	    }
	    
	    public boolean getIsPrised(){
	    	return isPrised;
	    }
	    
	    public void setIsPrised(boolean isPrised){
	    	this.isPrised = isPrised;
	    }

		public int getPriseNum() {
			return priseNum;
		}

		public void setPriseNum(int priseNum) {
			this.priseNum = priseNum;
		}
		
		public void setPrised(boolean isPrised) {
			this.isPrised = isPrised;
		}

		public int getCommentNum() {
			return commentNum;
		}

		public void setCommentNum(int commentNum) {
			this.commentNum = commentNum;
		}

}
