import java.io.*;
import java.util.*;

public class NewsRepository {
    public List<News> newsList;
    private long nextId = 1;
    
    public NewsRepository() {
    	this.newsList = new ArrayList();
    }

    public News save(News news) {
        if (news.getId() == null) {
        	news.setId(nextId++);
        	newsList.add(news);
        } else {
        	for (int i=0; i < newsList.size(); i++) {
        		if (newsList.get(i).getId().equals(news.getId())) {
        			newsList.set(i, news);
        			return news;
        		}
        	}
        	newsList.add(news);
        }
        return news;
    }

    public Optional<News> findById(Long id) {
        return newsList.stream()
        		.filter(n -> n.getId().equals(id))
        		.findFirst();
    }
    
    public List<News> findAll(){
    	return new ArrayList<>(newsList);
    }

    public List<News> findByAuthorId(Long authorId){
    	List<News> result = new ArrayList();
    	for (News n : newsList) {
    		if (n.getAuthorId() != null && n.getAuthorId().equals(authorId)) {
    			result.add(n);
    		}
    	}
    	return result;
    }
    
    public void deleteById(Long id) {
        newsList.removeIf(n -> n.getId().equals(id));
    }

}