package jenkins.plugins.edabogd;

import java.io.IOException;
import java.util.HashMap;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import hudson.Extension;
import hudson.model.BuildableItem;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.Job;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;


public class MultipleConfiguration extends ListViewColumn{
    
    @DataBoundConstructor
    public MultipleConfiguration(String viewName) {
        super();
    }
    
    // contains all current jobs with their corresponding checkbox status
    private static HashMap<Job, Boolean> map = new HashMap<Job, Boolean>();
    
    // enables access to Job object via job name
    private static HashMap<String, Job> jobMap = new HashMap<String, Job>();
    
    // used to keep track if view has changed or not
    private static String url = "";
    
    
    public static HashMap<String, Job> getJobMap() {
        return jobMap;
    }
    
    public static HashMap<Job, Boolean> getMap() {
        return map;
    }
    
    /**
     * @param job
     * @return - returns the checkbox status of corresponding job
     */
    public boolean getStatus(Job job) {
        if (map.containsKey(job)) return map.get(job);
        else  return false;  
    }
    
 
    // function returns the text of the column header displayed in the view page. 
    // It may not be ideal to let this method check for changes and call clearMap() 
    public String checkMap(String url) {
        
        // if view url is different from the current one, clear maps
        if (!this.url.equals(url)) {
            this.url = url;
            clearMap();
        }
        return "Select job";
    }
    
    public void fillMap(Job job) {
        
        // add job to map if it's a new one
        if (!map.containsKey(job)) {
            map.put(job, false);
            jobMap.put(job.getName(), job);
            
        }   
    }
    
    public static void clearMap() {
        map.clear();
        jobMap.clear();
    }
    
    @JavaScriptMethod
    public void buildAll() {
        
        if (map.size() != 0) { 
            for (Job job : map.keySet()) {
                
                if (job.hasPermission(job.BUILD)) ((AbstractProject<?,?>) job).scheduleBuild(new Cause.UserCause());
            }
        }
    }
    
    
    @JavaScriptMethod
    public void buildSelected() {
        
        for (Job job : map.keySet()) {
            
            if (map.get(job)) {
                if (job.hasPermission(job.BUILD)) ((AbstractProject<?,?>) job).scheduleBuild(new Cause.UserCause());
            }  
        }
    }
    
    @JavaScriptMethod
    public void mark(String job) {
       map.put(jobMap.get(job), !map.get(jobMap.get(job)));
    }
    
    
    @JavaScriptMethod
    public void SelectAll() {
       
        if (map.size() != 0) { 
            for (Job job : map.keySet()) {
                map.put(job, true);
            }
        }
    }
    
    @JavaScriptMethod
    public void DeselectAll() {
       
        if (map.size() != 0) { 
            for (Job job : map.keySet()) {
                map.put(job, false);
            }
        }
    }
    
    @JavaScriptMethod
    public void disableJobs() throws IOException {
        
        for (Job job : map.keySet()){
            
            if (map.get(job)) {
                
               AbstractProject<?,?> abstractItem = ((AbstractProject<?, ?>) job);
       
                // disable/enable depending on current state
                if  (abstractItem.isDisabled()) {
                    abstractItem.enable();
                }
                else if (!abstractItem.isDisabled()) {
                    abstractItem.disable();
                }
                else {
                    // any other state which may be present
                }   
            }
        }
        
       
    }
    
    @JavaScriptMethod
    public void disableAll() throws IOException { 
    
        for (Job job : map.keySet()){
            ((AbstractProject<?, ?>) job).disable();
        }
    }
    
    @JavaScriptMethod
    public void enableAll() throws IOException { 
   
        for (Job job : map.keySet()){
            ((AbstractProject<?, ?>) job).enable();
        }
    }
    
    
    
    @Extension
    public static class DescriptorImpl extends ListViewColumnDescriptor {

        @Override
        public String getDisplayName() {
            return "Multiple Configuration box";
        }
        
        @Override 
        public boolean shownByDefault() {
            return false;
        }
        
    }
}
