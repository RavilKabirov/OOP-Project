
import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class NewsService {
    private NewsRepository newsRepository;
    private UserRepository userRepository;

    public NewsService(NewsRepository newsRepository, UserRepository userRepository) {
    	this.newsRepository = newsRepository;
    	this.userRepository = userRepository;
    }
    
    public News publishNews(Long authorId, String title, String content) {
        userRepository.findById(authorId)
        		.orElseThrow(() -> new RuntimeException(
        				"Author not found with id: " + authorId
        				));
        if (title == null || title.isEmpty()) {
        	throw new RuntimeException("Title can not be empty");
        }
        if (content == null || content.isEmpty()) {
        	throw new RuntimeException("Content can not be empty");
        }
        News news = new News(null, title, content, authorId);
        return newsRepository.save(news);
    }

    public News editNews(Long newsId, Long editorId, String newContent) {
        userRepository.findById(editorId)
        		.orElseThrow(() -> new RuntimeException(
        				"Editor not found with id: " + editorId
        				));
        News news = newsRepository.findById(newsId)
        		.orElseThrow(() -> new RuntimeException(
        				"News not found with id: " + newsId
        				));
        if (news.isArchived()) {
        	throw new RuntimeException("Can not edit archived news");
        }
        if (newContent == null || newContent.isEmpty()) {
        	throw new RuntimeException("Content can not be empty");
        }
        news.setContent(newContent);
        return newsRepository.save(news);
    }

    public void archiveNews(Long newsId, Long archivedBy) {
        userRepository.findById(archivedBy)
        		.orElseThrow(() -> new RuntimeException(
        				"User not found with id: " + archivedBy
        				));
        News news = newsRepository.findById(newsId)
        		.orElseThrow(() -> new RuntimeException(
        				"News not found with id: " + newsId
        				));
        if (news.isArchived()) {
        	throw new RuntimeException("News is already archived");
        }
        
        news.setArchived(true);
        newsRepository.save(news);
    }

    public void deleteNews(Long newsId) {
        newsRepository.findById(newsId)
        	.orElseThrow(() -> new RuntimeException(
        			"News not found with id: " + newsId
        			));
        
        newsRepository.deleteById(newsId);
    }

    public List<News> getLatestNews(int limit) {
        List<News> all = newsRepository.findAll();
        
        all.sort((a,b) -> b.getPublishedAt().compareTo(a.getPublishedAt()));
        
        List<News> result = new ArrayList<>();
        for (News n : all) {
        	if (!n.isArchived()) {
        		result.add(n);
        		if (result.size() == limit) break;
        	}
        }
        return result;
    }

    public Optional<News> getNewsById(Long newsId) {
        return newsRepository.findById(newsId);
    }

    public List<News> searchNews(String keyword, LocalDate fromDate, LocalDate toDate) {
        List<News> all = newsRepository.findAll();
        List<News> result = new ArrayList<>();
        
        for (News news : all) {
        	boolean matches = true;
        	if (keyword != null && !keyword.isEmpty()) {
        		String lower = keyword.toLowerCase();
        		boolean inTitle = news.getTitle() != null && 
        				news.getTitle().toLowerCase().contains(lower);
        		boolean inContent = news.getContent() != null &&
        				news.getContent().toLowerCase().contains(lower);
        		if (!inTitle && !inContent) {
        			matches = false;
        		}
        	}
        	if (fromDate != null) {
                LocalDate publishedDate = news.getPublishedAt().toLocalDate();
                if (publishedDate.isBefore(fromDate)) {
                    matches = false;
                }
            }

            if (toDate != null) {
                LocalDate publishedDate = news.getPublishedAt().toLocalDate();
                if (publishedDate.isAfter(toDate)) {
                    matches = false;
                }
            }

            if (matches) {
                result.add(news);
            }
        	
        }
        return result;
    }

    public List<News> getNewsByAuthor(Long authorId) {
        userRepository.findById(authorId)
        		.orElseThrow(() -> new RuntimeException(
        				"Author not found with id: " + authorId
        				));
        return newsRepository.findByAuthorId(authorId);
    }

}