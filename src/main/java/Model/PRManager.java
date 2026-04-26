
import java.io.*;
import java.util.*;

/**
 * 
 */
public class PRManager extends Manager {
    private List<String> managedChannels = new ArrayList<>();

    public PRManager() {
    }
    /**
     * @return
     */
    public List<String> getManagedChannels() {
        return managedChannels;
    }

    /**
     * @param value
     */
    public void setManagedChannels(List<String> value) {
        this.managedChannels=value;
    }

}