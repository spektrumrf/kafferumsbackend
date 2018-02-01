package elements;

import org.watertemplate.Template;
import org.watertemplate.TemplateMap;

/**
 *
 * @author Walter Grönholm
 */
public abstract class Page extends Template {

    @Override
    protected final Template getMasterTemplate() {
        return new Base();
    }

    private static class Base extends Template {

        public Base() {
            add("title", "SpeKR");
        }
        
        @Override
        protected void addSubTemplates(TemplateMap.SubTemplates subTemplates) {
            subTemplates.add("header", new Header());
            subTemplates.add("footer", new Footer());
        }

        @Override
        protected String getFilePath() {
            return "base.html";
        }

    }

    private static class Header extends Template {

        @Override
        protected String getFilePath() {
            return "header.html";
        }

    }

    private static class Footer extends Template {

        @Override
        protected String getFilePath() {
            return "footer.html";
        }

    }

}
