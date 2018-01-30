package elements;

import org.watertemplate.Template;

/**
 *
 * @author Walter Grönholm
 */
public class StatisticsPage extends Template {
    
    private StatisticsGraph graph;

    public StatisticsPage(StatisticsGraph initialGraph) {
        this.graph = initialGraph;
    }
    
    @Override
    protected String getFilePath() {
        return "statistics";
    }
    
}
