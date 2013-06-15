package com.model.dao;

import java.util.List;

import com.model.entity.News;

public interface NewsDao {
	public News getNew(int id);

	public List<News> getNews();

	public boolean update(News n);

	public boolean insert(News n);

	public boolean delete(int id);

}