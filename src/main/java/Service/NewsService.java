
import java.io.*;
import java.util.*;

/**
 * 
 */
public class NewsService {

    /**
     * Default constructor
     */
    public NewsService() {
    }

    /**
     * 
     */
    private NewsRepository newsRepository;

    /**
     * 
     */
    private UserRepository userRepository;

    /**
     * @param authorId 
     * @param title 
     * @param content 
     * @return
     */
    public News publishNews(Long authorId, String title, String content) {
        // TODO implement here
        return null;
    }

    /**
     * @param newsId 
     * @param editorId 
     * @param newContent 
     * @return
     */
    public News editNews(Long newsId, Long editorId, String newContent) {
        // TODO implement here
        return null;
    }

    /**
     * @param newsId 
     * @param archivedBy 
     * @return
     */
    public void archiveNews(Long newsId, Long archivedBy) {
        // TODO implement here
        return null;
    }

    /**
     * @param newsId 
     * @return
     */
    public void deleteNews(Long newsId) {
        // TODO implement here
        return null;
    }

    /**
     * @param limit 
     * @return
     */
    public List<News> getLatestNews(int limit) {
        // TODO implement here
        return null;
    }

    /**
     * @param newsId 
     * @return
     */
    public Optional<News> getNewsById(Long newsId) {
        // TODO implement here
        return null;
    }

    /**
     * @param keyword 
     * @param fromDate 
     * @param toDate 
     * @return
     */
    public List<News> searchNews(String keyword, LocalDate fromDate, LocalDate toDate) {
        // TODO implement here
        return null;
    }

    /**
     * @param authorId 
     * @return
     */
    public List<News> getNewsByAuthor(Long authorId) {
        // TODO implement here
        return null;
    }

}