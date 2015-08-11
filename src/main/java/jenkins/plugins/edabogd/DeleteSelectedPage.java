package jenkins.plugins.edabogd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.RootAction;


@Extension
public class DeleteSelectedPage implements RootAction {

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return "ConfirmDeleteSelected";
    }
    
    public ArrayList<Job> getCurrentJobs() {
       
        ArrayList<Job> list = new ArrayList<Job>();
        
        for (Job job : MultipleConfiguration.getMap().keySet()) {
            
            if (MultipleConfiguration.getMap().get(job)) {
                
                list.add(job);
            }
        }
        
        if (list.size() == 0) {
            return null;
        }
        
        else return list;
    }
    
    public boolean isListEmpty() {
        ArrayList<Job> list = new ArrayList<Job>();
        
        for (Job job : MultipleConfiguration.getMap().keySet()) {
            
            if (MultipleConfiguration.getMap().get(job)) {
                
                list.add(job);
            }
        }
        
        if (list.size() != 0) return false;
        else return true;
    }
    
    
    
    public void doConfigSubmit(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException, InterruptedException {
        
        
        // return only
        if (isListEmpty()) {
            rsp.sendRedirect("../");
        }
        
        // delete the selected jobs and return
        else {
            for (Job job : MultipleConfiguration.getMap().keySet()) {
                
                if (job.hasPermission(job.DELETE)) if (MultipleConfiguration.getMap().get(job)) job.delete();
                
            }
            
            MultipleConfiguration.clearMap();
            rsp.sendRedirect("../");
        }
    }
}
