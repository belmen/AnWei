package com.belmen.anwei.weibo;

import java.io.File;
import java.util.List;

import com.belmen.anwei.data.Comment;
import com.belmen.anwei.data.Status;
import com.belmen.anwei.data.User;
import com.belmen.anwei.http.HttpException;

public interface WeiboApi {

	/**
	 * 验证当前授权的用户，获取用户名
	 * @return 授权用户的用户名
	 * @throws HttpException
	 */
	public String verify() throws HttpException;
	
	/**
	 * 获取当前授权用户的相关信息
	 * @return 当前授权用户的User对象
	 * @throws HttpException
	 */
	public User showUser() throws HttpException;
	
	/**
	 * 获取某个ID的用户的相关信息
	 * @param id 用户ID
	 * @return 该ID用户的User对象
	 * @throws HttpException
	 */
	public User showUser(String id) throws HttpException;
	
	/**
	 * 获取公共时间线
	 * @return 公共时间线的Status列表
	 * @throws HttpException
	 */
	public List<Status> getPublicTimeline() throws HttpException;
	
	/**
	 * 获取前20条主页时间线
	 * @return 主页时间线的Status列表
	 * @throws HttpException
	 */
	public List<Status> getHomeTimeline() throws HttpException;
	
	/**
	 * 获取主页时间线，获取范围由paging指定
	 * @param paging 获取时间线的范围
	 * @return 主页时间线的Status列表
	 * @throws HttpException
	 */
	public List<Status> getHomeTimeline(Paging paging) throws HttpException;

	/**
	 * 获取评论列表
	 * @param id 微博ID
	 * @return 该条微博消息的Comment列表
	 * @throws HttpException
	 */
	public List<Comment> getComments(String id) throws HttpException;
	
	/**
	 * 转发一条消息
	 * @param status 被转发的消息
	 * @param repost 转发理由
	 * @throws HttpException
	 */
	public void repostStatus(Status status, String repost) throws HttpException;
	
	/**
	 * 评论一条消息
	 * @param status 被评论的消息
	 * @param comment 评论内容
	 * @throws HttpException
	 */
	public void commentStatus(Status status, String comment) throws HttpException;
	
	/**
	 * 回复一条评论
	 * @param id 要回复的微博ID
	 * @param cid 要回复的评论ID
	 * @param text 回复内容
	 * @throws HttpException
	 */
	public void replyComment(String id, String cid, String comment) throws HttpException;
	
	/**
	 * 发送一条不带图片的新消息
	 * @param status 消息内容
	 * @throws HttpException
	 */
	public void updateStatus(String status) throws HttpException;
	
	/**
	 * 发送一条带图片的消息
	 * @param status 消息内容
	 * @param photo 图片文件
	 * @throws HttpException
	 */
	public void uploadPhoto(String status, File photo) throws HttpException;

	/**
	 * 收藏一条微博
	 * @param id 微博id
	 * @throws HttpException
	 */
	public void addFavorite(String id) throws HttpException;
	
	/**
	 * 取消收藏的微博
	 * @param id 微博id
	 * @throws HttpException
	 */
	public void removeFavorite(String id) throws HttpException;
}
