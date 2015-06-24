package com.sysu.yibaosysu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 评论的实体类
 * 
 * @author kuxinwei
 */
public class Comment {

	public static final String CONTENT = "content";
	public static final String TIME = "time";
	public static final String AUTHOR_NAME = "authorName";
	public static final String AUTHOR_ID = "authorId";

	private String content;
	private String time;
	private String authorName;
	private int authorId;

	public static List<Comment> parseList(List<Map<String, Object>> data) {
		List<Comment> commentList = new ArrayList<Comment>();
		for (Map<String, Object> tMap : data) {
			Comment tComment = new Comment();
			tComment.authorId = (Integer) tMap.get(Comment.AUTHOR_ID);
			tComment.authorName = (String) tMap.get(Comment.AUTHOR_NAME);
			tComment.time = (String) tMap.get(Comment.TIME);
			tComment.content = (String) tMap.get(Comment.CONTENT);
			commentList.add(tComment);
		}

		return commentList;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

}
