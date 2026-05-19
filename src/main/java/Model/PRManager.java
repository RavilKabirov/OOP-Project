
import java.io.*;
import java.util.*;

/**
 * 
 */
public class PRManager extends Manager {
    private List<String> managedChannels = new ArrayList<>();
    private String campaignFocus;

    public PRManager() {
    }

    public PRManager(String email, String firstName, String lastName) {
        super(email, firstName, lastName);
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
        this.managedChannels = value != null ? new ArrayList<>(value) : new ArrayList<>();
    }

    public void addManagedChannel(String channel) {
        if (channel != null && !channel.isBlank() && !managedChannels.contains(channel)) {
            managedChannels.add(channel);
        }
    }

    public void removeManagedChannel(String channel) {
        managedChannels.remove(channel);
    }

    public boolean managesChannel(String channel) {
        return channel != null && managedChannels.contains(channel);
    }

    public String getCampaignFocus() {
        return campaignFocus;
    }

    public void setCampaignFocus(String value) {
        this.campaignFocus = value;
    }

}
