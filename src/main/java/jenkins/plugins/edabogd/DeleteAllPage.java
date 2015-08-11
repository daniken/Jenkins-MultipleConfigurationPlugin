package jenkins.plugins.edabogd;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;




import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.RootAction;


@Extension
public class DeleteAllPage implements RootAction {

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return "ConfirmDeleteAll";
    }
    
    public Collection<Job> getCurrentJobs() {
        return MultipleConfiguration.getJobMap().values();
    }
    
    public void doConfigSubmit(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException, InterruptedException {
        
        
        for (Job job : MultipleConfiguration.getJobMap().values()) {   
            if (job.hasPermission(job.DELETE)) job.delete();
        }
        
        MultipleConfiguration.clearMap();
        rsp.sendRedirect("../");
    }
    
  
    
    

}
