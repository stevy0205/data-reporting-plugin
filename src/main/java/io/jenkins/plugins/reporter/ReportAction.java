package io.jenkins.plugins.reporter;

import hudson.model.Run;
import io.jenkins.plugins.reporter.model.Item;
import io.jenkins.plugins.reporter.model.Report;
import io.jenkins.plugins.util.BuildAction;
import io.jenkins.plugins.util.JobAction;
import org.kohsuke.stapler.StaplerProxy;

public class ReportAction extends BuildAction<Report> implements StaplerProxy {

    private final Report report;
    
    protected ReportAction(Run<?, ?> owner, Report report) {
        this(owner, report, true);
    }

    public ReportAction(Run<?, ?> owner, Report report, boolean canSerialize) {
        super(owner, report, canSerialize);
        this.report = report;
    }

    public Report getReport() {
        return report;
    }

    @Override
    protected ReportXmlStream createXmlStream() {
        return new ReportXmlStream();
    }

    @Override
    protected JobAction<? extends BuildAction<Report>> createProjectAction() {
        return new ReportJobAction(getOwner().getParent());
    }

    @Override
    protected String getBuildResultBaseName() {
        return "data-report.xml";
    }

    @Override
    public String getIconFileName() {
        return ReportJobAction.BIG_ICON;
    }

    @Override
    public String getDisplayName() {
        return report.getLabel();
    }

    @Override
    public String getUrlName() {
        return ReportJobAction.ID;
    }

    @Override
    public Object getTarget() {
        Item item = new Item();
        item.setId("report");
        item.setResult(report.getResult().aggregate());
        item.setItems(report.getResult().getComponents());
        
        return new ReportViewModel(getOwner(), ReportJobAction.ID, item, "Report Overview", report.getResult().getColors());
    }
}
